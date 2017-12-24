package com.cstu.service;

import com.cstu.dao.UserDao;
import com.cstu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserDao dao;

    public User createUser(String firstName, String lastName, String email) {
        User user = new User();
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setEmail(email);
        user.setPass(generatePass());
        dao.save(user);
        return user;
    }

    public List<User> getUsers() {
            return dao.getUsers();
    }

    public User getUser(String email) {
        return dao.getUser(email);
    }

    private String generatePass() {
        return "myGeneratedPass"; //TODO generate random email!!!!
    }
}
