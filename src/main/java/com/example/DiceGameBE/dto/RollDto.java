package com.example.DiceGameBE.dto;


import com.example.DiceGameBE.model.Dice;

import java.util.List;

public record RollDto(List<Dice> dices, String gameId) {
}
