package com.cognizant.rps.service;

import com.cognizant.rps.models.User;
import com.cognizant.rps.repo.UserRepo;
import com.cognizant.rps.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserService {

    private Utility jwt;

    private UserRepo ur;

    PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo ur, Utility jwt){
        this.ur = ur;
        this.jwt = jwt;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    public String loggedIn(User attempt){

        User getUser = ur.findByUsername(attempt.getUsername());
        boolean verify = passwordEncoder.matches(attempt.getPassphrase(), getUser.getPassphrase());

        if (verify) {
            getUser.setLogin(1);
            ur.save(getUser);
            return "Bearer " + jwt.generateToken(getUser);
        } else {
            //throw new InvalidCredentialsException();
            return "Wrong Password";
        }
    }

    public String register(User attempt){
        List<User> registry = ur.findAll();
        Set<String> registered = new HashSet<>();
        for(User name : registry){
            registered.add(name.getUsername());
        }
        String encoded =  passwordEncoder.encode(attempt.getPassphrase());
        User user = new User(0, attempt.getUsername(), encoded, 1);
        if(registered.add(user.getUsername())){
            ur.save(user);
            return "Bearer " + jwt.generateToken(user);
        }else{
//            throw new InvalidCredentialsException();
            return "EXCEPTION ON LINE 61";
        }
    }

}

