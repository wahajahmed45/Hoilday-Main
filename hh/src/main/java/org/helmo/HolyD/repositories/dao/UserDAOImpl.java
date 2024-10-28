package org.helmo.HolyD.repositories.dao;


import org.helmo.HolyD.domains.entities.UserEntity;
import org.helmo.HolyD.domains.mapper.daoMapper.IUserMapper;
import org.helmo.HolyD.domains.models.User;
import org.helmo.HolyD.repositories.RoleRepository;
import org.helmo.HolyD.repositories.UserRepository;
import org.helmo.HolyD.repositories.dao.inter.IUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDAOImpl implements IUserDAO {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private final IUserMapper USER_MAPPER = IUserMapper.INSTANCE;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        UserEntity userEntity = USER_MAPPER.toUserEntity(user);

        UserEntity userEntitySave = userRepository.save(userEntity);

        // List<RoleEntity> roleEntities =
        userEntitySave.getRoleEntity().forEach(roleEntity -> {
            roleEntity.setUserEntity(userEntitySave);
        });
        // return roleRepository.save(roleEntity);
        //}).collect(Collectors.toList());
        //userEntitySave.setRoleEntity(roleEntities);userRepository.save(userEntitySave)

        User userMapperUser = USER_MAPPER.toUser(userEntitySave);
        return userMapperUser;
    }

    @Override
    public User findUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));
        return USER_MAPPER.toUser(userEntity);
    }

  /*  private UserEntity toUserEntity(User user) {
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        userEntity.setRoleEntity(toRoleEntities(user.getRoles()));
        return userEntity;
    }

    private User toUser(UserEntity userSave) {
        User user = modelMapper.map(userSave, User.class);
        user.setRoles(toRoles(userSave.getRoleEntity()));
        return user;
    }

    private List<Role> toRoles(List<RoleEntity> roleEntities) {
        return roleEntities.stream().map(this::toRole).collect(Collectors.toList());
    }

    private Role toRole(RoleEntity roleEntity) {
        return modelMapper.map(roleEntity, Role.class);
    }

    private List<RoleEntity> toRoleEntities(List<Role> roles) {
        return roles.stream().map(this::toRoleEntity).collect(Collectors.toList());
    }

    private RoleEntity toRoleEntity(Role role) {
        return modelMapper.map(role, RoleEntity.class);
    }
*/

}
