package com.kemalaydin.routemanagement.repository.location;

import com.kemalaydin.routemanagement.converter.LocationConverter;
import com.kemalaydin.routemanagement.model.location.Location;
import com.kemalaydin.routemanagement.repository.location.entity.LocationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {
    private final LocationJpaRepository locationJpaRepository;
    private final LocationConverter locationConverter;

    @Override
    public Optional<Location> findById(String id) {
        return locationJpaRepository.findById(id).map(locationConverter::entityToModel);
    }

    @Override
    public Optional<Location> findFirstByCode(String code) {
        return locationJpaRepository.findFirstByCode(code).map(locationConverter::entityToModel);
    }

    @Override
    public List<Location> findAllByCode(String code) {
        return locationJpaRepository.findAllByCode(code).stream().map(locationConverter::entityToModel).collect(Collectors.toList());
    }

    @Override
    public Location save(Location location) {
        LocationEntity locationEntity = locationConverter.modelToEntity(location);

        LocationEntity savedLocationEntity = locationJpaRepository.save(locationEntity);

        return locationConverter.entityToModel(savedLocationEntity);
    }

    @Override
    public void deleteById(String id) {
        locationJpaRepository.deleteById(id);
    }

    @Override
    public Page<Location> findAll(Pageable pageable) {
        return locationJpaRepository.findAll(pageable).map(locationConverter::entityToModel);
    }
}
