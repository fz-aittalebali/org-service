package org.dxc.orgservice.team.adapter.out.persistence;

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
@Table(name = "teams",
        uniqueConstraints = @UniqueConstraint(name = "uq_teams_name_department", columnNames = {"name", "department_id"}))
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class TeamJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "department_id", nullable = false, updatable = false)
    private UUID departmentId;

    @Column(name = "domain_created_at", nullable = false, updatable = false)
    private Instant domainCreatedAt;
}
