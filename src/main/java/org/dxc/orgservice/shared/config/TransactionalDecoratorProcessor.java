package org.dxc.orgservice.shared.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.dxc.orgservice.shared.application.ports.in.IBulkCommandHandler;
import org.dxc.orgservice.shared.application.ports.in.IEventDrivenHandler;
import org.dxc.orgservice.shared.application.ports.in.IReturnCommandHandler;
import org.dxc.orgservice.shared.application.ports.in.IVoidCommandHandler;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class TransactionalDecoratorProcessor implements BeanPostProcessor, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        boolean isVoid = bean instanceof IVoidCommandHandler<?>;
        boolean isReturn = bean instanceof IReturnCommandHandler<?, ?>;

        if (!isVoid && !isReturn) {
            return bean;
        }

        // IBulkCommandHandler and IEventDrivenHandler must NOT be transaction-wrapped
        if (bean instanceof IBulkCommandHandler<?, ?> || bean instanceof IEventDrivenHandler<?>) {
            return bean;
        }

        PlatformTransactionManager txManager = beanFactory.getBean(PlatformTransactionManager.class);
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);

        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.setProxyTargetClass(false);

        proxyFactory.addAdvice((MethodInterceptor) invocation -> {
            if ("handle".equals(invocation.getMethod().getName())) {
                return txTemplate.execute(status -> {
                    try {
                        return invocation.proceed();
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            return invocation.proceed();
        });

        return proxyFactory.getProxy();
    }
}
