package org.helmo.HolyD.domains.mapper.daoMapper;

import org.helmo.HolyD.domains.entities.DocumentEntity;
import org.helmo.HolyD.domains.entities.ScheduleEntity;
import org.helmo.HolyD.domains.models.Document;
import org.helmo.HolyD.domains.models.Schedule;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface IDocumentMapper {

    IDocumentMapper INSTANCE = Mappers.getMapper(IDocumentMapper.class);

    @Mapping(target = "userEntity", source = "user")
    @Mapping(target = "vacationEntity", source = "vacation")
    DocumentEntity toDocumentEntity(Document document);

    @InheritInverseConfiguration
    Document toDocument(DocumentEntity documentEntity);
}
