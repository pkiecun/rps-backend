package com.cognizant.rps.service;

import com.cognizant.rps.models.Game;
import com.cognizant.rps.models.Match;
import com.cognizant.rps.models.Message;
import com.cognizant.rps.repo.GameRepo;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GameService {
    private GameRepo gr;

    public GameService(GameRepo gr){
        this.gr = gr;
    }

    public Message startMatch(Message first){
        Game starter = new Game(0,first.getSenderName(), "", first.getMessage().getMove(), 0, first.getMessage().getLimit());
        Game open = gr.save(starter);
        return  new Message(open.getPlayer1(),"",new Match(open.getGoal(),false, open.getMove1(), open.getId()), first.getStatus());
    }
    public Message findMatch(Message second){
        List<Game> lobbies = gr.findAll();
        boolean found = false;
        Message matched = new Message();
        for(Game lobby: lobbies){
            if(lobby.getPlayer2() != null && !lobby.getPlayer1().equals(second.getSenderName()) && !found && lobby.getGoal() == second.getMessage().getLimit()){
                Game closing = gr.save(new Game(lobby.getId(), lobby.getPlayer1(), second.getSenderName(), lobby.getMove1(), second.getMessage().getMove(), lobby.getGoal()));
                matched = new Message(closing.getPlayer1(), closing.getPlayer2(), new Match(closing.getGoal(), true, closing.getMove1(), closing.getId()),second.getStatus());
                found = true;
            }
        }
        if(found){
        return matched;}else{return new Message("NoMatch", second.getSenderName(), new Match(),second.getStatus());}
    }
    public Message findResult(Message third){
        Optional<Game> result = gr.findById(third.getMessage().getId());
        if(result.isPresent()){
            return new Message(result.get().getPlayer2(), result.get().getPlayer1(), new Match(result.get().getGoal(), true, result.get().getMove2(), result.get().getId()),third.getStatus());
        }else{return new Message("NoMatch", third.getSenderName(), new Match(),third.getStatus());}
    }
}
