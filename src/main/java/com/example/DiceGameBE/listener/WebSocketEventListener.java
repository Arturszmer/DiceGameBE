package com.example.DiceGameBE.listener;

import com.example.DiceGameBE.dto.message.MessageMapper;
import com.example.DiceGameBE.exceptions.GameErrorResult;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.utils.MessageTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

import static com.example.DiceGameBE.utils.GameAttributes.GAME_ID;

@Component
@Slf4j
public class WebSocketEventListener {

    private final GameRepository gameRepository;
    private final SimpMessageSendingOperations messageTemplate;
    private final String BASE_PATH;

    public WebSocketEventListener(GameRepository gameRepository,
                                  SimpMessageSendingOperations messageTemplate,
                                  @Value("${topic.basePath}") String BASE_PATH) {
        this.gameRepository = gameRepository;
        this.messageTemplate = messageTemplate;
        this.BASE_PATH = BASE_PATH;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String gameId = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get(GAME_ID.getName());
        if(gameId != null){
            Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameException(GameErrorResult.GAME_NOT_FOUND_EX));
            if(game.getPlayers().isEmpty()){
                game.setGameStatus(GameStatus.FINISHED);
                gameRepository.save(game);
                messageTemplate.convertAndSend(BASE_PATH + gameId, MessageMapper.gameToMessage(game, MessageTypes.GAME_CLOSED));
            }
        }
    }
}
