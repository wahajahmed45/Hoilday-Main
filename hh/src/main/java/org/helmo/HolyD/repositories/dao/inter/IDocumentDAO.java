package org.helmo.HolyD.repositories.dao.inter;

import org.helmo.HolyD.domains.models.Document;
import org.helmo.HolyD.domains.models.Schedule;

import java.util.List;

public interface IDocumentDAO {
    void create(Document document);

    // Schedule createPlaceIfNotExist(Schedule schedule);
}
