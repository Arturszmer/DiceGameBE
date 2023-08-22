package com.example.DiceGameBE.controller;

import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.service.impl.DiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dice")
public class DiceController {

    private final DiceService diceService;
    @PostMapping("/roll")
    public List<Dice> rollDices(@RequestBody int numberOfDicesToRoll) {
        return diceService.rollDices(numberOfDicesToRoll);
    }
}


