package com.example.DiceGameBE.assemblers;

import com.example.DiceGameBE.dto.CreatePlayerDto;
import com.example.DiceGameBE.model.Player;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerAssembler {

    public static Player toEntity(CreatePlayerDto createPlayerDto){
        return new Player(createPlayerDto.getId(), createPlayerDto.getUsername());
    }

}
