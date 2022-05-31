package org.bashirov.cashdeposit.service;

import org.bashirov.cashdeposit.entity.Phones;
import org.bashirov.cashdeposit.entity.Profiles;
import org.bashirov.cashdeposit.entity.Users;

import java.util.List;


public interface UsersService {
    List<Users> readAllUsers(int page);
    Users readUserById(long userId);
    List<Users> readUsersByName(String name, int page);
    List<Users> readUsersByAgeBetween(int fromAge,int toAge, int page);
    Users readUsersByEmail(String email);
    List<Users> readUsersByPhoneValue(String email, int page);

    void saveUser(Users user);
    void savePhone(Phones phone);

    void update(long userId, Users updatedUser);
    void delete(long userId);
}
