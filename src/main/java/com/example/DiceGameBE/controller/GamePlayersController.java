package com.example.DiceGameBE.controller;

import com.example.DiceGameBE.service.GamePlayersProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GamePlayersController {

    private final GamePlayersProvider gamePlayersProvider;
}
