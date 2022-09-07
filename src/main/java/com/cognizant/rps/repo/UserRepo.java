package com.cognizant.rps.repo;

import com.cognizant.rps.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    User findByUsername(String username);
    User save(User person);
    List<User> findAll();

}
