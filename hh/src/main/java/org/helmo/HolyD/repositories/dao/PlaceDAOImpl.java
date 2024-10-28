package org.helmo.HolyD.repositories.dao;

import jakarta.transaction.Transactional;
import org.helmo.HolyD.domains.entities.PlaceEntity;
import org.helmo.HolyD.domains.mapper.daoMapper.PlaceMapper;
import org.helmo.HolyD.domains.models.Place;
import org.helmo.HolyD.repositories.PlaceRepository;
import org.helmo.HolyD.repositories.dao.inter.IPlaceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class PlaceDAOImpl implements IPlaceDAO {
    @Autowired
    private PlaceRepository placeRepository;
    private final static PlaceMapper I_Place_MAPPER = PlaceMapper.INSTANCE;

    @Override
    public Place getPlace(Long id) {
        Optional<PlaceEntity> placeEntity = placeRepository.findById(id);
        if (placeEntity.isPresent()) {
            PlaceEntity entity = placeEntity.get();
            Place place = I_Place_MAPPER.toPlace(entity);
            return place;
        }
        return null;
    }

    @Override
    public Place savePlace(Place Place) {
        PlaceEntity placeEntity = I_Place_MAPPER.toPlaceEntity(Place);
        PlaceEntity savedEntity = placeRepository.save(placeEntity);
        Place savedPlace = I_Place_MAPPER.toPlace(savedEntity);
        return savedPlace;
    }

    @Override
    public List<Place> getAllPlaces() {
        List<PlaceEntity> placeEntities = placeRepository.findAll();
        List<Place> places = placeEntities.stream().map(I_Place_MAPPER::toPlace).collect(Collectors.toList());
        return places;
    }

    @Override
    public Place createPlaceIfNotExist(Place place) {
        Optional<PlaceEntity> placeEntity = placeRepository.findByCityAndCountryAndPostalCode(place.getVille(),
                place.getPays(), place.getCodePostal());
        if (placeEntity.isEmpty()) {
            return savePlace(place);
        }
        return I_Place_MAPPER.toPlace(placeEntity.get());
    }

}
