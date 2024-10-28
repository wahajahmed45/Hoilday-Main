package org.helmo.HolyD.repositories.dao;

import org.helmo.HolyD.domains.entities.VacationEntity;
import org.helmo.HolyD.domains.mapper.daoMapper.IVacationMapper;
import org.helmo.HolyD.domains.models.Vacation;
import org.helmo.HolyD.repositories.VacationRepository;
import org.helmo.HolyD.repositories.dao.inter.IVacationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class VacationDAOImpl implements IVacationDAO {
    @Autowired
    private VacationRepository vacationRepository;
    private final static IVacationMapper I_VACATION_MAPPER = IVacationMapper.INSTANCE;

    @Override
    public Vacation getVacation(Long id) {
        Optional<VacationEntity> vacationEntity = vacationRepository.findById(id);
        if (vacationEntity.isPresent()) {
            VacationEntity vacationEntity1 = vacationEntity.get();
            Vacation vacation = I_VACATION_MAPPER.toVacation(vacationEntity1);
            vacation.setVacationStatus();
            vacationEntity1 = I_VACATION_MAPPER.toVacationEntity(vacation);
            vacationEntity1 = vacationRepository.save(vacationEntity1);
            vacation = I_VACATION_MAPPER.toVacation(vacationEntity1);
            return vacation;
        }
        return null;
    }

    @Override
    public Vacation saveVacation(Vacation vacation) {
        VacationEntity vacationEntity = I_VACATION_MAPPER.toVacationEntity(vacation);
        VacationEntity savedEntity = vacationRepository.save(vacationEntity);

        Vacation savedVacation = I_VACATION_MAPPER.toVacation(savedEntity);
        return savedVacation;
    }

    @Override
    public List<Vacation> getAllVacations(Long id_user) {
        List<VacationEntity> vacationEntities = vacationRepository.findByUserEntity_Id(id_user);
        List<Vacation> vacations = vacationEntities.stream().map(I_VACATION_MAPPER::toVacation).collect(Collectors.toList());
        vacations.forEach(Vacation::setVacationStatus);
        vacationEntities = vacations.stream().map(I_VACATION_MAPPER::toVacationEntity).collect(Collectors.toList());
        vacationEntities = vacationRepository.saveAll(vacationEntities);
        vacations = vacationEntities.stream().map(I_VACATION_MAPPER::toVacation).collect(Collectors.toList());
        return vacations;
    }

    public Vacation findVacationByIdAndUserId(Long id, Long userId) {
        Optional<VacationEntity> vacationEntity = vacationRepository.findByIdAndUserEntity_Id(id, userId);
        if (vacationEntity.isPresent()) {
            VacationEntity vacationEntity1 = vacationEntity.get();
            Vacation vacation = I_VACATION_MAPPER.toVacation(vacationEntity1);
            return vacation;
        }
        return null;
    }

}
