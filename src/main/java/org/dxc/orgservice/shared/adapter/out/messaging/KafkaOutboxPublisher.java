package org.dxc.orgservice.shared.adapter.out.messaging;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.dxc.orgservice.shared.application.ports.out.IOutboxRepository;
import org.dxc.orgservice.shared.application.ports.out.OutboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class KafkaOutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaOutboxPublisher.class);

    private final IOutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${outbox.batch-size:50}")
    private int batchSize;

    public KafkaOutboxPublisher(IOutboxRepository outboxRepository,
                                KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelayString = "${outbox.polling-interval-ms:5000}")
    @SchedulerLock(name = "KafkaOutboxPublisher", lockAtMostFor = "120s", lockAtLeastFor = "5s")
    public void publishPending() {
        outboxRepository.findUnpublished(batchSize).forEach(this::publishEvent);
    }

    private void publishEvent(OutboxEvent event) {
        String topic = event.topic();
        try {
            kafkaTemplate.send(topic, event.aggregateId(), event.payload())
                    .get(2, TimeUnit.SECONDS);
            outboxRepository.markPublished(event.id());
            log.debug("Published event [{}] to topic [{}]", event.id(), topic);
        } catch (TimeoutException e) {
            log.warn("Kafka send timed out for event [{}] on topic [{}]", event.id(), topic);
        } catch (ExecutionException e) {
            log.error("Kafka send failed for event [{}]: {}", event.id(), e.getCause().getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Kafka send interrupted for event [{}]", event.id());
        }
    }
}
