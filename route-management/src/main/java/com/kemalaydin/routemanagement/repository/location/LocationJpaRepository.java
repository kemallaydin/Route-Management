package com.kemalaydin.routemanagement.repository.location;

import com.kemalaydin.routemanagement.repository.location.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationJpaRepository extends JpaRepository<LocationEntity, String> {
    Optional<LocationEntity> findFirstByCode(String code);

    List<LocationEntity> findAllByCode(String code);
}
