package org.bashirov.cashdeposit.controller;

import org.bashirov.cashdeposit.entity.Profiles;
import org.bashirov.cashdeposit.entity.Users;
import org.bashirov.cashdeposit.service.ProfilesService;
import org.bashirov.cashdeposit.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/profiles")
public class ProfilesController {

    @Autowired
    private UsersService usersDao;

    @Autowired
    private ProfilesService profilesDao;

    @PostMapping(value = "/create/{userId}", produces = "application/json")
    public String create(@RequestBody @Valid Profiles newProfile, BindingResult bindingResult, @PathVariable long userId){
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors().toString();
        }

        Users user = usersDao.readUserById(userId);
        newProfile.setUsers(user);
        profilesDao.saveProfile(newProfile);
        profilesDao.increaseDepositOnPercentInSeparateThread(newProfile);
        return "success";
    }
}
