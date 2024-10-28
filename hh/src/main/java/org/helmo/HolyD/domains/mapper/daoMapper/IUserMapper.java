package org.helmo.HolyD.domains.mapper.daoMapper;

import org.helmo.HolyD.domains.entities.UserEntity;
import org.helmo.HolyD.domains.models.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses ={IVacationMapper.class})
public interface IUserMapper {
    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

     @Mapping(target = "lastname", source = "lastname")
     @Mapping(target = "firstname", source = "firstname")
     @Mapping(target = "email", source = "email")
     @Mapping(target = "passwd", source = "password")
     @Mapping(target = "roleEntity", source = "roles")
     @Mapping(target = "vacationEntities", source = "vacations")
    UserEntity toUserEntity(User user);

    @InheritInverseConfiguration
    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "firstname", source = "firstname")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "passwd" )
    @Mapping(target = "roles", source = "roleEntity")
    @Mapping(target ="vacations", source = "vacationEntities")
    User toUser(UserEntity userEntity);
}
