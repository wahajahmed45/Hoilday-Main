package org.helmo.HolyD.repositories.dao.inter;


import org.helmo.HolyD.domains.models.User;

public interface IUserDAO {
    boolean existsByEmail(String email);

    User saveUser(User user);

    User findUserByEmail(String email);
}
