package com.joaquinogallar.personalblog.user.controller;

import com.joaquinogallar.personalblog.user.service.IUserSerivce;
import com.joaquinogallar.personalblog.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/api/v1/users")
public class UserController {

    private final IUserSerivce userEntityService;

    public UserController(UserService userEntityService) {
        this.userEntityService = userEntityService;
    }


}
