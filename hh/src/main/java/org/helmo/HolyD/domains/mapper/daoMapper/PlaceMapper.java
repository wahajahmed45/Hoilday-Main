package org.helmo.HolyD.domains.mapper.daoMapper;

import org.helmo.HolyD.domains.entities.PlaceEntity;
import org.helmo.HolyD.domains.models.Place;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
;

@Mapper
public interface PlaceMapper {
    PlaceMapper INSTANCE = Mappers.getMapper(PlaceMapper.class);

    @Mapping(target = "vacationEntities", source = "vacations")
    @Mapping(target = "activityEntities", source = "activities")
    @Mapping(target = "longitude", expression = "java(new java.math.BigDecimal(place.getLongitude()))")
    @Mapping(target = "latitude", expression = "java(new java.math.BigDecimal(place.getLatitude()))")
    @Mapping(target = "street", source = "rue")
    @Mapping(target = "number", source = "rueNumero")
    @Mapping(target = "postalCode", source = "codePostal")
    @Mapping(target = "city", source = "ville")
    @Mapping(target = "country", source = "pays")
    PlaceEntity toPlaceEntity(Place place);

    @InheritInverseConfiguration
    @Mapping(target = "vacations", source = "vacationEntities")
    @Mapping(target = "activities", source = "activityEntities")
    @Mapping(target = "longitude", expression = "java(placeEntity.getLongitude().doubleValue())")
    @Mapping(target = "latitude", expression = "java(placeEntity.getLatitude().doubleValue())")
    @Mapping(target = "rue", source = "street")
    @Mapping(target = "rueNumero", source = "number")
    @Mapping(target = "codePostal", source = "postalCode")
    @Mapping(target = "ville", source = "city")
    @Mapping(target = "pays", source = "country")
    Place toPlace(PlaceEntity placeEntity);
}

