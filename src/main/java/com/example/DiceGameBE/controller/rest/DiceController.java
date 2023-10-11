package com.example.DiceGameBE.controller.rest;

import com.example.DiceGameBE.dto.RollDicesResult;
import com.example.DiceGameBE.dto.RollDto;
import com.example.DiceGameBE.service.DiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dice")
public class DiceController {

    private final DiceService diceService;

    @PostMapping("/roll")
    public ResponseEntity<?> rollDices(@RequestBody RollDto rollDto) {
        RollDicesResult rollDicesResult = diceService.rollDices(rollDto);
        return ResponseEntity.ok(rollDicesResult);
    }

    @PostMapping("check")
    public RollDicesResult checkDice(@RequestBody RollDto rollDto){
        return diceService.checkDices(rollDto);
    }

}


