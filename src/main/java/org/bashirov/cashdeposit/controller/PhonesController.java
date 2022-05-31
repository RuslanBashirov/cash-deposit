package org.bashirov.cashdeposit.controller;

import org.bashirov.cashdeposit.entity.Phones;
import org.bashirov.cashdeposit.entity.Users;
import org.bashirov.cashdeposit.service.PhonesService;
import org.bashirov.cashdeposit.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/phones")
public class PhonesController {

    @Autowired
    private UsersService usersDao;

    @Autowired
    private PhonesService phonesDao;

    @PostMapping(value = "/create/{userId}", produces = "application/json")
    public String create(@RequestBody @Valid Phones newPhones, BindingResult bindingResult, @PathVariable long userId){
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors().toString();
        }

        Users user = usersDao.readUserById(userId);
        newPhones.setUsers(user);
        phonesDao.savePhones(newPhones);
        return "success";
    }
}
