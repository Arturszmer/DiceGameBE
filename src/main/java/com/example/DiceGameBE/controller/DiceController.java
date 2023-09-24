package com.example.DiceGameBE.controller;

import com.example.DiceGameBE.dto.RollDicesResult;
import com.example.DiceGameBE.dto.RollDto;
import com.example.DiceGameBE.service.DiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dice")
public class DiceController {

    private final DiceService diceService;
    @PostMapping("/roll")
    public RollDicesResult rollDices(@RequestBody RollDto rollDto) {
        return diceService.rollDices(rollDto);
    }

    @PostMapping("check")
    public RollDicesResult checkDice(@RequestBody RollDto rollDto){
        return diceService.checkDices(rollDto);
    }
}


