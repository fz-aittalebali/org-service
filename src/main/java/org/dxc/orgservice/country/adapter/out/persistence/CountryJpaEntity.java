package org.dxc.orgservice.country.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dxc.orgservice.shared.adapter.out.persistence.jpa.entities.BaseJpaEntity;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "countries",
        uniqueConstraints = @UniqueConstraint(name = "uq_countries_name", columnNames = {"name"}))
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class CountryJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "domain_created_at", nullable = false, updatable = false)
    private Instant domainCreatedAt;
}
