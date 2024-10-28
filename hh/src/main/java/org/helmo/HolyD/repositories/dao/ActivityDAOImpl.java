package org.helmo.HolyD.repositories.dao;

import org.helmo.HolyD.domains.entities.ActivityEntity;
import org.helmo.HolyD.domains.mapper.daoMapper.IActivityMapper;
import org.helmo.HolyD.domains.models.Activity;
import org.helmo.HolyD.repositories.ActivityRepository;
import org.helmo.HolyD.repositories.dao.inter.IActivityDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ActivityDAOImpl implements IActivityDAO {
    @Autowired
    private ActivityRepository activityRepository;
    private final static IActivityMapper I_ACTIVITY_MAPPER = IActivityMapper.INSTANCE;

    @Override
    public Activity getActivity(Long id) {
        Optional<ActivityEntity> activityEntity = activityRepository.findById(id);
        if (activityEntity.isPresent()) {
            ActivityEntity activityEntity1 = activityEntity.get();
            Activity activity = I_ACTIVITY_MAPPER.toActivity(activityEntity1);
            activity.setActivityStatus();
            activityEntity1 = I_ACTIVITY_MAPPER.toActivityEntity(activity);
            activityEntity1 = activityRepository.save(activityEntity1);
            activity = I_ACTIVITY_MAPPER.toActivity(activityEntity1);
            return activity;
        }
        return null;
    }

    @Override
    public Activity saveActivity(Activity activity) {
        ActivityEntity activityEntity = I_ACTIVITY_MAPPER.toActivityEntity(activity);
        ActivityEntity savedEntity = activityRepository.save(activityEntity);
        Activity savedActivity = I_ACTIVITY_MAPPER.toActivity(savedEntity);
        return savedActivity;
    }

    @Override
    public List<Activity> getAllActivitiesForUser(Long id_user) {
        List<ActivityEntity> activityEntities = activityRepository.findByUserEntity(id_user);
        List<Activity> activities = activityEntities.stream().map(I_ACTIVITY_MAPPER::toActivity).collect(Collectors.toList());
        List<Activity> updatedActivities = new ArrayList<>();
        for (Activity activity : activities) {
            activity.setActivityStatus();
            updatedActivities.add(activity);
        }
        activityEntities = updatedActivities.stream().map(I_ACTIVITY_MAPPER::toActivityEntity).collect(Collectors.toList());
        activityEntities = activityRepository.saveAll(activityEntities);
        activities = activityEntities.stream().map(I_ACTIVITY_MAPPER::toActivity).collect(Collectors.toList());
        return activities;
    }

    @Override
    public Activity findActivityByIdAndUserId(Long id_activity, Long id_user) {
        Optional<ActivityEntity> vacationEntity = activityRepository.findByIdAndUserEntity(id_activity, id_user);
        if (vacationEntity.isPresent()) {
            ActivityEntity activity = vacationEntity.get();
            Activity vacation = I_ACTIVITY_MAPPER.toActivity(activity);
            return vacation;
        }
        return null;
    }

    @Override
    public void deleteActivityById(Long idActivity) {
        ActivityEntity activityEntity = activityRepository.findById(idActivity).orElse(null);
        assert activityEntity != null;
        activityEntity.removeAllParticipant();
        activityRepository.delete(activityEntity);
    }

    @Override
    public void deleteActivity(Activity activity) {
        ActivityEntity activityEntity = I_ACTIVITY_MAPPER.toActivityEntity(activity);
        activityEntity.getPlaceEntity().removerActivity(activityEntity.getId());
        activityRepository.deleteActivityEntitiesById(activityEntity.getId());
    //    ActivityEntity test = activityRepository.findById(activityEntity.getId()).get();
     //   activityRepository.delete(activityEntity);
       // ActivityEntity test1 = activityRepository.findById(activityEntity.getId()).get();
    //    activityRepository.delete(test);
    }
}
