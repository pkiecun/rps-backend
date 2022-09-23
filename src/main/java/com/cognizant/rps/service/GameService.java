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

//    public Message startMatch(Message first){
//        Game starter = new Game(0,first.getSenderName(), "", first.getMessage().getMove(), 0, first.getMessage().getLimit());
//        Game open = gr.save(starter);
//        return  new Message(open.getPlayer1(),"",new Match(open.getGoal(),false, open.getMove1(), open.getId()), first.getStatus());
//    }
    public Message findMatch(Message second){
        Optional<Game> lobby = gr.findById(second.getMessage().getId());
        if(lobby.isPresent()){
            if (lobby.get().getPlayer1().equals(second.getSenderName()) && lobby.get().getMove1() == 0) {
                Game opener = gr.save(new Game(lobby.get().getId(), lobby.get().getPlayer1(), lobby.get().getPlayer2(),
                        second.getMessage().getMove(), lobby.get().getMove2(), lobby.get().getGoal()));
                if (opener.getMove2() != 0) {
                    Game follower = gr.save(new Game(0, lobby.get().getPlayer1(), lobby.get().getPlayer2(), 0, 0, 0));
                    return new Message(opener.getPlayer2(), opener.getPlayer1(), new Match(opener.getGoal(), opener.getMove2(), follower.getId()), second.getStatus());
                } else {
                    return new Message(opener.getPlayer2(), opener.getPlayer1(), new Match(opener.getGoal(), opener.getMove2(), opener.getId()), second.getStatus());
                }
            } else if (lobby.get().getPlayer2().equals(second.getSenderName()) && lobby.get().getMove2() == 0) {
                Game opener = gr.save(new Game(lobby.get().getId(), lobby.get().getPlayer1(), lobby.get().getPlayer2(), lobby.get().getMove1(),
                        second.getMessage().getMove(), lobby.get().getGoal()));
                if (opener.getMove1() != 0) {
                    Game follower = gr.save(new Game(0, lobby.get().getPlayer1(), lobby.get().getPlayer2(), 0, 0, 0));
                    return new Message(opener.getPlayer1(), opener.getPlayer2(), new Match(opener.getGoal(), opener.getMove1(), follower.getId()), second.getStatus());
                } else {
                    return new Message(opener.getPlayer1(), opener.getPlayer2(), new Match(opener.getGoal(), opener.getMove1(), opener.getId()), second.getStatus());
                }
            }
        }
        return new Message("NoMatch", second.getSenderName(), second.getMessage(), second.getStatus());
    }
//    public Message findResult(Message third){
//        Optional<Game> result = gr.findById(third.getMessage().getId());
//        if(result.isPresent()){
//            return new Message(result.get().getPlayer2(), result.get().getPlayer1(), new Match(result.get().getGoal(), true, result.get().getMove2(), result.get().getId()),third.getStatus());
//        }else{return new Message("NoMatch", third.getSenderName(), new Match(),third.getStatus());}
//    }

    public Message setGoal(Message setting){
        List<Game> lobbies = gr.findAll();
        boolean found = false;
        for(Game lobby : lobbies){
            if(!found && !lobby.getPlayer1().equals(setting.getSenderName()) && lobby.getPlayer2().equals("")
                    && lobby.getGoal() == setting.getMessage().getLimit()){
                lobby.setPlayer2(setting.getSenderName());
                Game matched = gr.save(lobby);
                found = true;
                return new Message(matched.getPlayer1(), matched.getPlayer2(),
                        new Match(matched.getGoal(), 0, matched.getId()),setting.getStatus());
            }
        }
        if(!found){
            Game unmatched = gr.save(new Game(0, setting.getSenderName(), "",0,0,setting.getMessage().getLimit()));
            return new Message(setting.getSenderName(), setting.getSenderName(), new Match(unmatched.getGoal(),0, unmatched.getId()),setting.getStatus());
        }
        return setting;
    }

}
