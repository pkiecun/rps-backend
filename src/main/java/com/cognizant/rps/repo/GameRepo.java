package com.cognizant.rps.repo;

import com.cognizant.rps.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepo extends JpaRepository<Game, Integer> {
    Game save(Game first);
    List<Game> findAll();
    Optional<Game> findById(int id);
}
