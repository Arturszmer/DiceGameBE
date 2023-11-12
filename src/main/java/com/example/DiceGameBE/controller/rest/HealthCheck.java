package com.example.DiceGameBE.controller.rest;

import lombok.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HealthCheck {

    @GetMapping("/health")
    public String checkHealth(){
        return "is Live";
    }
}
