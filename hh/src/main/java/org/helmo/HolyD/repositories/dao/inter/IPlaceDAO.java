package org.helmo.HolyD.repositories.dao.inter;

import org.helmo.HolyD.domains.models.Place;

import java.util.List;

public interface IPlaceDAO {
    Place getPlace(Long id);
    Place savePlace(Place place);
    List<Place> getAllPlaces();

    Place createPlaceIfNotExist(Place place);
}
