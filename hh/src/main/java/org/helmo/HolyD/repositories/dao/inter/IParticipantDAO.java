package org.helmo.HolyD.repositories.dao.inter;

import org.helmo.HolyD.domains.models.Participant;
import org.helmo.HolyD.domains.models.User;

import java.util.List;

public interface IParticipantDAO {
    Participant getParticipant(long id);

    Participant saveParticipant(Participant participant);

    List<Participant> getAllParticipants();

    Participant findParticipantByEmailAndNameAndFirstname(String email, String lastname, String firstname);

    Participant createParticipantIfNoExist(String email, String lastname, String firstname);

    void deleteAllParticipants();

    void deleteParticipant(Long id_participant);
}
