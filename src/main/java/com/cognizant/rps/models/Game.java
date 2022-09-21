package com.cognizant.rps.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;

import javax.persistence.*;
import static net.bytebuddy.implementation.bind.annotation.Argument.BindingMechanic.UNIQUE;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String player1;

    private String player2;

    private int move1;

    private int move2;

    private int goal;
}
