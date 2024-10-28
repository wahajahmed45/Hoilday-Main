package org.helmo.HolyD.domains.mapper.dtoMapper;

import org.helmo.HolyD.domains.models.User;
import org.helmo.HolyD.domains.requests.SignupDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface IUserMapper {
    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "firstname", source = "firstname")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "passwd")
    User toUser(SignupDTO signupDTO);

    @InheritInverseConfiguration
    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "firstname", source = "firstname")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "passwd", source = "password")
    SignupDTO toSignupRequest(User user);
}
