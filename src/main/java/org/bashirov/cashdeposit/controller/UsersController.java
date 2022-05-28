package org.bashirov.cashdeposit.controller;

import org.bashirov.cashdeposit.config.jwt.JwtProvider;
import org.bashirov.cashdeposit.entity.Users;
import org.bashirov.cashdeposit.repository.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService dao;

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("/users")
    public List<Users> readAllUsers(@RequestParam int page){
        return dao.readAllUsers(page);
    }

    @GetMapping("/usersByUserId/{userId}")
    public Users readUser(@PathVariable long userId){
        return dao.readUserById(userId);
    }

    @GetMapping("/usersByName")
    public List<Users> readUsersByName(@RequestParam String name, @RequestParam int page){
        return dao.readUsersByName(name, page);
    }

    @GetMapping("/usersByAgeBetween")
    public List<Users> readUsersByAgeBetween(@RequestParam int fromAge, @RequestParam int toAge, @RequestParam int page){
        return dao.readUsersByAgeBetween(fromAge, toAge, page);
    }

    @GetMapping("/usersByYearsBetween")
    public List<Users> readUsersByYearsBetween(@RequestParam int fromYear, @RequestParam int toYear, @RequestParam int page){
        int localYear = LocalDate.now().getYear();
        return dao.readUsersByAgeBetween(localYear - toYear - 1, localYear - fromYear + 1, page);
    }

    @GetMapping("/usersByEmail")
    public Users readUsersByEmail(@RequestParam String email){
        return dao.readUsersByEmail(email);
    }

    @GetMapping("/usersByPhoneValue")
    public List<Users> readUsersByPhoneValue(@RequestParam String phoneValue, @RequestParam int page){
        return dao.readUsersByPhoneValue(phoneValue, page);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public String create(@RequestBody @Valid Users newUser, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors().toString();
        }

        dao.saveUser(newUser);
        return jwtProvider.generateToken(newUser.getId(), "USER");
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, produces = "application/json")
    public String update(@RequestBody @Valid Users updatedUser, BindingResult bindingResult, @PathVariable long userId){
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors().toString();
        }

        dao.update(userId, updatedUser);
        return "success";
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        dao.delete(userId);
    }
}
