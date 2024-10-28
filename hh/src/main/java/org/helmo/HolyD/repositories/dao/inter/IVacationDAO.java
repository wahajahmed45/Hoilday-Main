package org.helmo.HolyD.repositories.dao.inter;

import org.helmo.HolyD.domains.models.Vacation;

import java.util.List;

public interface IVacationDAO {
    Vacation getVacation(Long id);

    Vacation saveVacation(Vacation vacation);

    List<Vacation> getAllVacations(Long id_user);

    Vacation findVacationByIdAndUserId(Long idVacation, Long id);


}
