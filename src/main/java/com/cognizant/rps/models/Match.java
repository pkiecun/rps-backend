package com.cognizant.rps.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Match {

    private int limit;
    private boolean count;
    private int move;

}
