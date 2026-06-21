package org.dxc.orgservice.city.adapter.out.persistence;

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
@Table(name = "cities",
        uniqueConstraints = @UniqueConstraint(name = "uq_cities_name_country", columnNames = {"name", "country_id"}))
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class CityJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "zip_code", nullable = false, length = 20)
    private String zipCode;

    @Column(name = "country_id", nullable = false, updatable = false)
    private UUID countryId;

    @Column(name = "domain_created_at", nullable = false, updatable = false)
    private Instant domainCreatedAt;
}
