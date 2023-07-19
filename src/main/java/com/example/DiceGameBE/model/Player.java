package com.example.DiceGameBE.model;

import com.example.DiceGameBE.dto.PlayerDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
@Getter
@Setter
public class Player {
    private Integer id;
    private String name;
    private Integer points;
    private Validations validations;

    public Player(Integer id, String name, Integer points, boolean turn) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.validations = new Validations(false, turn, false, false);
    }

    public static List<Player> createPlayers(List<PlayerDto> playersDto) {
        List<Player> players = new ArrayList<>();
        playersDto.forEach(p -> invokePlayers(players, p));
        return players;
    }

    private static void invokePlayers(List<Player> players, PlayerDto p) {
        if(p.getId() == 0){
            players.add(new Player(0, p.getUsername(), p.getPoints(), true));
        } else {
            players.add(new Player(p.getId(), p.getUsername(), p.getPoints(), false));
        }
    }
}
