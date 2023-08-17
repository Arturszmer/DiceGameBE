package com.example.DiceGameBE.controller;

import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.service.impl.DiceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/dice")
public class DiceController {

    private final DiceService diceService;

    public DiceController(DiceService diceService) {
        this.diceService = diceService;
    }

    @GetMapping ("/roll")
    public List<Dice> rollDice() {
        return diceService.rollDice();
    }
}


