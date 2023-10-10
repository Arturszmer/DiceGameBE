package com.example.DiceGameBE.controller.ws;

import com.example.DiceGameBE.dto.message.*;
import com.example.DiceGameBE.service.DiceService;
import com.example.DiceGameBE.service.GameCreatorService;
import com.example.DiceGameBE.service.GamePlayersProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Objects;

import static com.example.DiceGameBE.utils.GameAttributes.PLAYER;

@Controller
public class WsGameplayController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameCreatorService gameCreatorService;
    private final GamePlayersProvider gamePlayersProvider;
    private final DiceService diceService;
    private final String DESTINATION_CONST;

    public WsGameplayController(SimpMessagingTemplate simpMessagingTemplate,
                                GameCreatorService gameCreatorService,
                                GamePlayersProvider gamePlayersProvider,
                                DiceService diceService,
                                @Value("${topic.basePath}") String destinationPath) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.gameCreatorService = gameCreatorService;
        this.gamePlayersProvider = gamePlayersProvider;
        this.diceService = diceService;
        this.DESTINATION_CONST = destinationPath;
    }

    @MessageMapping("/game.connect")
    public void connectGame(
            @Payload JoinMessage message,
            SimpMessageHeaderAccessor headerAccessor){
        GameMessage gameMessage = gameCreatorService.connectGame(message);
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("gameId", gameMessage.getGameId());
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("player", message.getPlayerName());
        simpMessagingTemplate.convertAndSend(DESTINATION_CONST + gameMessage.getGameId(), gameMessage);
    }

    @MessageMapping("/game.leave")
    public void leaveGame(@Payload LeaveMessage message,
                          SimpMessageHeaderAccessor headerAccessor){
        String playerName = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get(PLAYER.getName());
        GameMessage gameMessage = gamePlayersProvider.leaveGame(message, playerName);
        simpMessagingTemplate.convertAndSend(DESTINATION_CONST + message.getGameId(), gameMessage);
    }

    @MessageMapping("/game.roll")
    public void rollDices(@Payload DiceMessage message){
        GameMessage gameMessage = diceService.rollDices(message);
        simpMessagingTemplate.convertAndSend(DESTINATION_CONST + gameMessage.getGameId(), gameMessage);
    }


    @MessageMapping("/game.check")
    public void checkDice(@Payload DiceMessage message){
        //TODO: do poprawnej implementacji
        GameMessage gameMessage = diceService.checkDices(message);
        this.simpMessagingTemplate.convertAndSend(DESTINATION_CONST + gameMessage.getGameId(), gameMessage);
    }


}
