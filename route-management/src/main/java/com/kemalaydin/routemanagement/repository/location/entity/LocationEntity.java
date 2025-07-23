package com.kemalaydin.routemanagement.repository.location.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "locations")
@Getter
@Setter
public class LocationEntity {
    @Id
    @UuidGenerator
    private String id;
    @Column(unique = true, nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String city;
}
