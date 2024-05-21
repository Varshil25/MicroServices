package com.lcwd.user.service.services;

import com.lcwd.user.service.entities.User;

import java.util.List;


public interface UserService {
    // user operations

    // save user
    User saveUser(User user) ;

    // get user by id
    User getUser(String userId);

    // get all users
    List<User> getAllUsers();

    // update user
    User updateUser(User user);

    // delete user
    void deleteUser(String userId);


}
