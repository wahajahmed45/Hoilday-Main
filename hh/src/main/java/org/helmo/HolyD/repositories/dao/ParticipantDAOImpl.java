package org.helmo.HolyD.repositories.dao;

import org.helmo.HolyD.domains.entities.ParticipantEntity;
import org.helmo.HolyD.domains.mapper.daoMapper.IParticipantMapper;
import org.helmo.HolyD.domains.models.Participant;
import org.helmo.HolyD.repositories.ParticipantRepository;
import org.helmo.HolyD.repositories.dao.inter.IParticipantDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ParticipantDAOImpl implements IParticipantDAO {
    @Autowired
    private ParticipantRepository participantRepository;
    private final static IParticipantMapper I_PARTICIPANT_MAPPER = IParticipantMapper.INSTANCE;

    @Override
    public Participant getParticipant(long id) {
        Optional<ParticipantEntity> participantEntity = participantRepository.findById(id);
        if (participantEntity.isPresent()) {
            ParticipantEntity participantEntity1 = participantEntity.get();
            return I_PARTICIPANT_MAPPER.toParticipant(participantEntity1);
        }
        return null;
    }

    @Override
    public Participant saveParticipant(Participant participant) {
        ParticipantEntity participantEntity = I_PARTICIPANT_MAPPER.toParticipantEntity(participant);
        ParticipantEntity savedEntity = participantRepository.save(participantEntity);
        return I_PARTICIPANT_MAPPER.toParticipant(savedEntity);
    }

    @Override
    public List<Participant> getAllParticipants() {
        List<ParticipantEntity> participantEntityList = participantRepository.findAll();
        return participantEntityList.stream().map(I_PARTICIPANT_MAPPER::toParticipant).collect(Collectors.toList());
    }

    @Override
    public Participant findParticipantByEmailAndNameAndFirstname(String email, String lastname, String firstname) {
        Optional<ParticipantEntity> ParticipantEntity = participantRepository.findByEmail(email);
        if (ParticipantEntity.isPresent()) {
            ParticipantEntity ParticipantEntity1 = ParticipantEntity.get();
            return I_PARTICIPANT_MAPPER.toParticipant(ParticipantEntity1);
        }
        return null;
    }

    @Override
    public Participant createParticipantIfNoExist(String email, String lastname, String firstname) {
        Optional<ParticipantEntity> ParticipantEntity = participantRepository.findByEmail(email);
        if (ParticipantEntity.isEmpty()) {
            Participant participant = Participant.builder()
                    .email(email)
                    .nom(lastname)
                    .prenom(firstname).build();
            return saveParticipant(participant);
        }
        return I_PARTICIPANT_MAPPER.toParticipant(ParticipantEntity.get());
    }

    @Override
    public void deleteAllParticipants() {
        participantRepository.deleteAll();
    }

    @Override
    public void deleteParticipant(Long id_participant) {
        participantRepository.deleteById(id_participant);
    }
}
