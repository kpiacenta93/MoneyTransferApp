package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.RegisterUserDto;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    private UserDao userDao;
    private AccountDao accountDao;

    public UserController(UserDao userDao, AccountDao accountDao){
        this.userDao = userDao;
        this.accountDao = accountDao;
    }
    @RequestMapping(path = "tenmo_users", method = RequestMethod.GET)
    public List<User> getAllUsers(){
        return userDao.getUsers();
    }

    @RequestMapping(path = "tenmo_users/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable int id){
        return userDao.getUserById(id);
    }
    @RequestMapping(path = "tenmo_users/username/{username}")
    public User getUsersByUsername(@PathVariable String username){
        return userDao.getUserByUsername(username);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "tenmo_users", method = RequestMethod.POST)
    public User createUser(@Valid @RequestBody RegisterUserDto user){
        return userDao.createUser(user);
    }

}
