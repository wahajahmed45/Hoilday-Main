package org.helmo.HolyD.domains.mapper.dtoMapper;



import org.helmo.HolyD.domains.models.Vacation;
import org.helmo.HolyD.domains.requests.VacationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {IPlaceMapper.class, IParticipantMapper.class, IActivityMapper.class})
public interface IVacationMapper {
    IVacationMapper INSTANCE = Mappers.getMapper(IVacationMapper.class);

    @Mapping(target = "place", source = "placeDTO")
    Vacation toVacation(VacationDTO vacationDTO);

    @Mapping(target = "placeDTO", source = "place")
    VacationDTO toVacationRequest(Vacation vacation);
}
