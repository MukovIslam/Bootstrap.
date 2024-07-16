package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;



import org.json.JSONException;
import org.json.JSONObject;
import  java.util.HashMap;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserResource {

    private PasswordEncoder passwordEncoder;

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public String findById(@PathVariable int userId) {

        JSONObject userInfo = new JSONObject();
        userInfo.put("id",userService.getById(userId).getId());
        userInfo.put("firstName",userService.getById(userId).getUsername());
        userInfo.put("lastName",userService.getById(userId).getLastName());
        userInfo.put("Age",userService.getById(userId).getAge());
        userInfo.put("Email",userService.getById(userId).getEmail());
        userInfo.put("password",userService.getById(userId).getPassword());


        ArrayList userRoles = new ArrayList();
        for(Role role :userService.getById(userId).getRoles()){
           userRoles.add(role.getName());
        }
        userInfo.put("roles",userRoles);
        return userInfo.toString();
    }



}