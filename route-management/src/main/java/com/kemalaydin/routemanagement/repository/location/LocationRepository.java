package com.kemalaydin.routemanagement.repository.location;

import com.kemalaydin.routemanagement.model.location.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LocationRepository {
    Optional<Location> findById(String id);

    Optional<Location> findFirstByCode(String code);

    List<Location> findAllByCode(String code);

    Location save(Location location);

    void deleteById(String id);

    Page<Location> findAll(Pageable pageable);
}
