package org.helmo.HolyD.domains.mapper.dtoMapper;

import org.helmo.HolyD.domains.models.Place;
import org.helmo.HolyD.domains.requests.PlaceDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

;

@Mapper
public interface IPlaceMapper {
    IPlaceMapper INSTANCE = Mappers.getMapper(IPlaceMapper.class);

    @Mapping(target = "rue", source = "rue")
    @Mapping(target = "rueNumero", source = "rueNumero")
    @Mapping(target = "codePostal", source = "codePostal")
    @Mapping(target = "ville", source = "ville")
    @Mapping(target = "pays", source = "pays")
    Place toPlace(PlaceDTO placeDTO);

    @InheritInverseConfiguration
    @Mapping(target = "rue", source = "rue")
    @Mapping(target = "rueNumero", source = "rueNumero")
    @Mapping(target = "codePostal", source = "codePostal")
    @Mapping(target = "ville", source = "ville")
    @Mapping(target = "pays", source = "pays")
    PlaceDTO toPlaceRequest(Place place);
}

