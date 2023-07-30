package com.example.DiceGameBE.assemblers;

import com.example.DiceGameBE.dto.AdminPlayerDto;
import com.example.DiceGameBE.model.Player;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerAssembler {

    public static Player toEntity(AdminPlayerDto adminPlayerDto){
        return new Player(adminPlayerDto.getId(), adminPlayerDto.getUsername());
    }

}
