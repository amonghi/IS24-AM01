package it.polimi.ingsw.am01.model.game;


import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.chat.ChatManager;
import it.polimi.ingsw.am01.model.choice.Choice;
import it.polimi.ingsw.am01.model.choice.MultiChoice;
import it.polimi.ingsw.am01.model.choice.SelectionResult;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.model.player.PlayerData;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Game {
    private final String id;
    private final GameStatus status;
    private final List<PlayerProfile> playerProfiles;
    private final ChatManager chatManager;
    private final Map<PlayerProfile, Choice<Side>> startingCardSideChoices;
    private final Map<PlayerProfile, MultiChoice<PlayerColor, PlayerProfile>.Choice> colorChoices;
    private final Map<PlayerProfile, Choice<Objective>> objectiveChoices;
    private final Map<PlayerProfile, PlayerData> playersData;
    private final Map<PlayerProfile, PlayArea> playAreas;
    private final Set<Objective> commonObjectives;
    private int currentPlayer;
    private final Board board;

    public Game(String id) {
        throw new UnsupportedOperationException("TODO");
    }

    public String getId() {
        throw new UnsupportedOperationException("TODO");
    }

    public List<PlayerProfile> getPlayerProfiles() {
        throw new UnsupportedOperationException("TODO");
    }

    public PlayerData getPlayerData(PlayerProfile pp) {
        throw new UnsupportedOperationException("TODO");
    }

    public PlayArea getPlayArea(PlayerProfile pp) {
        throw new UnsupportedOperationException("TODO");
    }

    public PlayerProfile getCurrentPlayer() {
        throw new UnsupportedOperationException("TODO");
    }

    public Set<Objective> getCommonObjectives() {
        throw new UnsupportedOperationException("TODO");
    }

    public GameStatus getStatus() {
        throw new UnsupportedOperationException("TODO");
    }

    public ChatManager getChatManager(){throw new UnsupportedOperationException("TODO");}

    public void startGame() {
        throw new UnsupportedOperationException("TODO");
    }

    public void pausedGame() {
        throw new UnsupportedOperationException("TODO");
    }

    public void resumeGame() {
        throw new UnsupportedOperationException("TODO");
    }

    public void join(PlayerProfile pp) {
        throw new UnsupportedOperationException("TODO");
    }

    public void selectStartingCardSide(PlayerProfile pp, Side s) {
        throw new UnsupportedOperationException("TODO");
    }

    public SelectionResult selectColor(PlayerProfile pp, PlayerColor pc) {
        throw new UnsupportedOperationException("TODO");
    }

    public void selectObjective(PlayerProfile pp, Objective o) {
        throw new UnsupportedOperationException("TODO");
    }

    public DrawResult drawCard(PlayerProfile pp, DrawSource ds) {
        throw new UnsupportedOperationException("TODO");
    }

    public void placeCard(PlayerProfile pp, Card c, Side s, int i, int j) {
        throw new UnsupportedOperationException("TODO");
    }
}