package org.helmo.HolyD.repositories.dao.inter;

import org.helmo.HolyD.domains.models.Activity;

import java.util.List;

public interface IActivityDAO {
    Activity getActivity(Long id);

    Activity saveActivity(Activity activity);

    List<Activity> getAllActivitiesForUser(Long id_user);

    Activity findActivityByIdAndUserId(Long idActivity, Long id);

    void deleteActivityById(Long idActivity);

    void deleteActivity(Activity activity);
}
