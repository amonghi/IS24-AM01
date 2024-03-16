package it.polimi.ingsw.am01.model.game;


import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.choice.SelectionResult;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.model.player.PlayerData;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Game {
    private String id;
    private GameStatus status;
    private List<PlayerProfile> playerProfiles;
    private Map<PlayerProfile, PlayerData> playersData;
    private Map<PlayerProfile, PlayArea> playAreas;
    private Set<Objective> commonObjectives;
    private int currentPlayer;
    private Board board;

    public Game(Iterable<PlayerProfile> playerProfiles) {
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

    public void startGame() {
        throw new UnsupportedOperationException("TODO");
    }

    public void pausedGame() {
        throw new UnsupportedOperationException("TODO");
    }

    public void resumeGame() {
        throw new UnsupportedOperationException("TODO");
    }

    public PlayerData join(PlayerProfile pp) {
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