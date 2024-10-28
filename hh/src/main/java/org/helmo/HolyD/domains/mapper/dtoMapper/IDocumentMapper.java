package org.helmo.HolyD.domains.mapper.dtoMapper;

import org.helmo.HolyD.domains.models.Document;
import org.helmo.HolyD.domains.requests.DocumentDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface IDocumentMapper {

    IDocumentMapper INSTANCE = Mappers.getMapper(IDocumentMapper.class);

    DocumentDTO toDocumentDTO(Document document);

    @InheritInverseConfiguration
    Document toDocument(DocumentDTO documentDTO);

}
