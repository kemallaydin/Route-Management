package com.kemalaydin.routemanagement.repository.transportation;

import com.kemalaydin.routemanagement.converter.TransportationConverter;
import com.kemalaydin.routemanagement.model.location.Location;
import com.kemalaydin.routemanagement.model.transportation.Transportation;
import com.kemalaydin.routemanagement.model.transportation.TransportationType;
import com.kemalaydin.routemanagement.repository.transportation.entity.TransportationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransportationRepositoryImpl implements TransportationRepository {
    private final TransportationJpaRepository transportationJpaRepository;
    private final TransportationConverter transportationConverter;

    @Override
    public Optional<Transportation> findById(String id) {
        return transportationJpaRepository.findById(id).map(transportationConverter::entityToModel);
    }

    @Override
    public Transportation save(Transportation transportation) {
        TransportationEntity transportationEntity = transportationConverter.modelToEntity(transportation);

        TransportationEntity savedTransportationEntity = transportationJpaRepository.save(transportationEntity);

        return transportationConverter.entityToModel(savedTransportationEntity);
    }

    @Override
    public List<Transportation> findAllByOriginLocationAndDestinationLocationAndType(Location originLocation, Location destinationLocation, TransportationType transportationType) {
        String originLocationCode = originLocation.getCode();
        String destinationLocationCode = destinationLocation.getCode();
        String type = transportationType.name();

        return transportationJpaRepository.findAllByOriginLocation_CodeAndDestinationLocation_CodeAndType(originLocationCode, destinationLocationCode, type)
                .stream().map(transportationConverter::entityToModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transportation> findAllByOriginLocation(Location originLocation) {
        String originLocationCode = originLocation.getCode();

        return transportationJpaRepository.findAllByOriginLocation_Code(originLocationCode)
                .stream().map(transportationConverter::entityToModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        transportationJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByOriginLocationOrDestinationLocation(Location originLocation, Location destinationLocation) {
        String originLocationCode = originLocation.getCode();
        String destinationLocationCode = destinationLocation.getCode();

        return transportationJpaRepository.existsByOriginLocation_CodeOrDestinationLocation_Code(originLocationCode, destinationLocationCode);
    }

    @Override
    public Page<Transportation> findAll(Pageable pageable) {
        return transportationJpaRepository.findAll(pageable).map(transportationConverter::entityToModel);
    }
}
