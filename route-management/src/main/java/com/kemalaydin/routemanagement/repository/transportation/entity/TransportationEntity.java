package com.kemalaydin.routemanagement.repository.transportation.entity;

import com.kemalaydin.routemanagement.repository.location.entity.LocationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;

@Entity
@Table(name = "transportations")
@Getter
@Setter
public class TransportationEntity {
    @Id
    @UuidGenerator
    private String id;
    @ManyToOne
    @JoinColumn(name = "origin_location_id", nullable = false)
    private LocationEntity originLocation;
    @ManyToOne
    @JoinColumn(name = "destination_location_id", nullable = false)
    private LocationEntity destinationLocation;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private Set<Integer> operatingDays;
}
