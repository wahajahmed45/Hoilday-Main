package org.helmo.HolyD.repositories.dao;

import org.helmo.HolyD.domains.entities.DocumentEntity;
import org.helmo.HolyD.domains.mapper.daoMapper.IDocumentMapper;
import org.helmo.HolyD.domains.models.Document;
import org.helmo.HolyD.repositories.DocumentRepository;
import org.helmo.HolyD.repositories.dao.inter.IDocumentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentDAOImpl implements IDocumentDAO {
    @Autowired
    private DocumentRepository documentRepository;

    private final static IDocumentMapper I_DOCUMENT_MAPPER = IDocumentMapper.INSTANCE;

    @Override
    public void create(Document document) {
        DocumentEntity documentEntity = I_DOCUMENT_MAPPER.toDocumentEntity(document);
        documentRepository.save(documentEntity);
    }
}
