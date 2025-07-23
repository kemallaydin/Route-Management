package com.kemalaydin.routemanagement.security.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class RoleEntity {
    @Id
    @UuidGenerator
    private String id;
    @Column(nullable = false)
    private String name;
}
