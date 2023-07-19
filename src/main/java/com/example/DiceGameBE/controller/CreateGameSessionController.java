package com.example.DiceGameBE.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class CreateGameSessionController {

    @GetMapping("/startGame")
    public String start(Principal principal, HttpSession session){
        return "Start game! " + principal.getName();
    }


}
