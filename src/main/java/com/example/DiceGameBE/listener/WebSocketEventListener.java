package com.example.DiceGameBE.listener;

import com.example.DiceGameBE.exceptions.GameErrorResult;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final GameRepository gameRepository;
    private final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String gameId = (String) headerAccessor.getSessionAttributes().get("gameId");
        if (gameId != null){
            log.info("Game nr id: {} is disconnected", gameId);
            Game game = gameRepository.findById(gameId)
                    .orElseThrow(() -> new GameException(GameErrorResult.GAME_NOT_FOUND_EX));
            game.setGameStatus(GameStatus.FINISHED);
            gameRepository.save(game);
            messageTemplate.convertAndSend("/games/game", game);
        }
    }

    @EventListener
    public void handleWebSocketPlayerDisconnectListener(SessionDisconnectEvent event){
        // TODO: implement player leaving
    }
}
