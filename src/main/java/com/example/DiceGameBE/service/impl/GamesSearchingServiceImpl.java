package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.exceptions.GameErrorResult;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.GamesSearchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GamesSearchingServiceImpl implements GamesSearchingService {

    private final GameRepository gameRepository;

    @Override
    public Game findGameByGameId(String gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new GameException(GameErrorResult.GAME_NOT_FOUND_EX));
    }

    @Override
    public List<Game> findOpenGames(GameStatus status) {
        return gameRepository.findGamesByGameStatus(status);
    }

    @Override
    public Page<Game> findGamesByPage(int page, int size) {
        Pageable pagesRequest = createPagesRequestUsing(page, size);
        List<Game> openGames = findOpenGames(GameStatus.OPEN);
        int start = (int) pagesRequest.getOffset();
        int end = Math.min(start + pagesRequest.getPageSize(), openGames.size());

        List<Game> pageContent = openGames.subList(start, end);
        return new PageImpl<>(pageContent, pagesRequest, openGames.size());
    }

    private Pageable createPagesRequestUsing(int page, int size) {
        return PageRequest.of(page, size);
    }
}
