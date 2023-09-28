package com.example.DiceGameBE.controller.ws;

import com.example.DiceGameBE.model.Game;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WsGameplayController {

    // musi wywołać /app/initialize-game-session
//    @MessageMapping("/app/game/initialize")
//    @SendTo("topic/game")
//    public Game initializeGameSession(@Payload Game game,
//                            SimpMessageHeaderAccessor headerAccessor){
//        headerAccessor.getSessionAttributes().put("gameId", game.getGameId());
//        return game;
//    }
//
//    @MessageMapping("/add-new-player")
//    @SendTo("topic/game")
//    public Game addPlayer(@Payload Game game,
//                          SimpMessageHeaderAccessor headerAccessor){
//        headerAccessor.getSessionAttributes().get(game.getGameId());
//        return game;
//    }

}
