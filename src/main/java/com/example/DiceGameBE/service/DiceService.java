package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.message.DiceMessage;
import com.example.DiceGameBE.dto.message.GameMessage;

/**
 * Implementation of the DiceService interface for rolling and checking dices in a game.
 */
public interface DiceService {

    /**
     * Roll the dices for a game and update their values.
     * @param message The DiceMessage containing game and dice information.
     * @param owner The owner of the game.
     * @return A GameMessage containing the game's updated status after rolling the dices.
     */
    GameMessage rollDices(DiceMessage message, String owner);

    /**
     * Check the dices and update their values for a game.
     * @param message The DiceMessage containing game and dice information.
     * @return A GameMessage containing the game's updated status after checking the dices.
     */
    GameMessage checkDices(DiceMessage message, String owner);
}
