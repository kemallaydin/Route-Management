package com.kemalaydin.routemanagement.repository.transportation;

import com.kemalaydin.routemanagement.repository.transportation.entity.TransportationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportationJpaRepository extends JpaRepository<TransportationEntity, String> {
    List<TransportationEntity> findAllByOriginLocation_CodeAndDestinationLocation_CodeAndType(String originLocationCode, String destinationLocationCode, String type);

    List<TransportationEntity> findAllByOriginLocation_Code(String originLocationCode);

    boolean existsByOriginLocation_CodeOrDestinationLocation_Code(String originLocationCode, String destinationLocationCode);
}
