package it.polimi.ingsw.am01.model.game;


import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.chat.ChatManager;
import it.polimi.ingsw.am01.model.choice.Choice;
import it.polimi.ingsw.am01.model.choice.DoubleChoiceException;
import it.polimi.ingsw.am01.model.choice.MultiChoice;
import it.polimi.ingsw.am01.model.choice.SelectionResult;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.model.player.PlayerData;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.*;

public class Game {
    private final String id;
    private GameStatus status;
    private TurnPhase turnPhase;
    private GameStatus recoverStatus;
    private final List<PlayerProfile> playerProfiles;
    private final ChatManager chatManager;
    private final Map<PlayerProfile, Choice<Side>> startingCardSideChoices;
    private final Map<PlayerProfile, Card> startingCards;
    private final Map<PlayerProfile, MultiChoice<PlayerColor, PlayerProfile>.Choice> colorChoices;
    private final Map<PlayerProfile, Choice<Objective>> objectiveChoices;
    private final Map<PlayerProfile, PlayerData> playersData;
    private final Map<PlayerProfile, PlayArea> playAreas;
    private final Set<Objective> commonObjectives;
    private int currentPlayer;
    private final int maxPlayers;
    private final Board board;

    public Game(String id, int maxPlayers) {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.status = GameStatus.AWAITING_PLAYERS;
        this.playerProfiles = new ArrayList<>();
        this.chatManager = new ChatManager();
        this.startingCardSideChoices = new HashMap<>();
        this.startingCards = new HashMap<>();
        this.colorChoices = new HashMap<>();
        this.objectiveChoices = new HashMap<>();
        this.playersData = new HashMap<>();
        this.playAreas = new HashMap<>();
        this.commonObjectives = new HashSet<>();
        this.currentPlayer = 0;
        this.turnPhase = TurnPhase.PLACING;
        this.board = Board.createShuffled(new Deck(GameAssets.getInstance().getResourceCards()), new Deck(GameAssets.getInstance().getGoldenCards()));
    }

    public Set<Objective> getObjectiveOptions(PlayerProfile pp) {
        return objectiveChoices.get(pp).getOptions();
    }

    public String getId() {
        return id;
    }

    public List<PlayerProfile> getPlayerProfiles() {
        return playerProfiles;
    }

    public PlayerData getPlayerData(PlayerProfile pp) {
        return playersData.get(pp);
    }

    public PlayArea getPlayArea(PlayerProfile pp) {
        return playAreas.get(pp);
    }

    public Board getBoard() {
        return board;
    }

    public PlayerProfile getCurrentPlayer() {
        return playerProfiles.get(currentPlayer);
    }

    public Set<Objective> getCommonObjectives() {
        return commonObjectives;
    }

    public GameStatus getStatus() {
        return status;
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void startGame() {
        if (status != GameStatus.AWAITING_START) {
            throw new IllegalMoveException();
        }

        for (PlayerProfile player : playerProfiles) {
            List<Card> hand = new ArrayList<>();
            hand.add(board.getResourceCardDeck().draw().orElseThrow());
            hand.add(board.getResourceCardDeck().draw().orElseThrow());
            hand.add(board.getGoldenCardDeck().draw().orElseThrow());

            playersData.put(player,
                    new PlayerData(hand,
                            objectiveChoices.get(player).getSelected().orElseThrow(),
                            colorChoices.get(player).getSelected().orElseThrow()));
        }
        setFirstPlayer();
        transition(GameStatus.PLAY);
    }

    private void setFirstPlayer() {
        Random random = new Random();
        Collections.rotate(playerProfiles, random.nextInt(maxPlayers));
    }

    public void pausedGame() {
        if (status == GameStatus.SUSPENDED) {
            throw new IllegalMoveException();
        }
        recoverStatus = status;
        status = GameStatus.SUSPENDED;
    }

    public void resumeGame() {
        if (status != GameStatus.SUSPENDED) {
            throw new IllegalMoveException();
        }
        status = recoverStatus;
    }

    private void transition(GameStatus nextStatus) {
        this.status = nextStatus;
    }

    private void setUpChoices() {
        // Setup starter card choices
        Deck starterCardDeck = new Deck(GameAssets.getInstance().getStarterCards());
        starterCardDeck.shuffle();

        for (PlayerProfile player : playerProfiles) {
            startingCardSideChoices.put(player, new Choice<>(EnumSet.allOf(Side.class)));
            startingCards.put(player, starterCardDeck.draw().orElseThrow());
        }

        // Setup color choices
        MultiChoice<PlayerColor, PlayerProfile> multiChoice = new MultiChoice<>(EnumSet.allOf(PlayerColor.class), new HashSet<>(playerProfiles));
        playerProfiles.forEach((player) -> colorChoices.put(player, multiChoice.getChoices().get(player)));

        // Setup objective choices
        List<Objective> objectiveDeck = new ArrayList<>(GameAssets.getInstance().getObjectives());
        Collections.shuffle(objectiveDeck);

        commonObjectives.add(objectiveDeck.removeFirst());
        commonObjectives.add(objectiveDeck.removeFirst());

        for (PlayerProfile player : playerProfiles) {
            Set<Objective> secretObjectives = new HashSet<>();
            secretObjectives.add(objectiveDeck.removeFirst());
            secretObjectives.add(objectiveDeck.removeFirst());
            objectiveChoices.put(player, new Choice<>(secretObjectives));
        }


    }

    public void join(PlayerProfile pp) {
        if (status != GameStatus.AWAITING_PLAYERS) {
            throw new IllegalMoveException();
        }

        playerProfiles.add(pp);

        if (playerProfiles.size() == maxPlayers) {
            transition(GameStatus.SETUP_STARTING_CARD_SIDE);

            setUpChoices();
        }
    }

    public void selectStartingCardSide(PlayerProfile pp, Side s) throws DoubleChoiceException {
        if (status != GameStatus.SETUP_STARTING_CARD_SIDE) {
            throw new IllegalMoveException();
        }
        startingCardSideChoices.get(pp).select(s);

        playAreas.put(pp, new PlayArea(startingCards.get(pp), s));

        if (startingCardSideChoices.values().stream().noneMatch(choice -> choice.getSelected().isEmpty())) {
            transition(GameStatus.SETUP_COLOR);
        }
    }

    public SelectionResult selectColor(PlayerProfile pp, PlayerColor pc) {
        if (status != GameStatus.SETUP_COLOR) {
            throw new IllegalMoveException();
        }
        SelectionResult sr = colorChoices.get(pp).select(pc);
        if (colorChoices.get(pp).isSettled()) { // FIXME: MultiChoice class
            transition(GameStatus.SETUP_OBJECTIVE);
        }
        return sr;
    }

    public void selectObjective(PlayerProfile pp, Objective o) {
        if (status != GameStatus.SETUP_OBJECTIVE) {
            throw new IllegalMoveException();
        }
        objectiveChoices.get(pp).select(o);
        if (objectiveChoices.values().stream().noneMatch(choice -> choice.getSelected().isEmpty())) {
            //all players had chosen their objective -> go to the next state (waiting for start "turn phase")
            transition(GameStatus.AWAITING_START);
        }
    }

    public DrawResult drawCard(PlayerProfile pp, DrawSource ds) {
        if ((status != GameStatus.PLAY && status != GameStatus.SECOND_LAST_TURN) || turnPhase != TurnPhase.DRAWING) {
            throw new IllegalMoveException();
        }
        if (currentPlayer != playerProfiles.indexOf(pp)) {
            throw new IllegalTurnException();
        }

        Optional<Card> card = ds.draw();
        card.ifPresent(c -> playersData.get(pp).getHand().add(c));
        if (card.isEmpty()) {
            return DrawResult.EMPTY;
        }

        switch (status) {
            case GameStatus.PLAY -> {
                if (board.getResourceCardDeck().isEmpty() && board.getGoldenCardDeck().isEmpty()) {
                    if (currentPlayer != maxPlayers - 1) {
                        // all decks are empty --> game is on "final phase"
                        transition(GameStatus.SECOND_LAST_TURN);
                    } else {
                        //this is the "last" player of round
                        transition(GameStatus.LAST_TURN);

                    }
                }
            }
            case GameStatus.SECOND_LAST_TURN -> {
                if (currentPlayer == maxPlayers - 1) {
                    //this is the "last" player of round
                    transition(GameStatus.LAST_TURN);
                }
            }
        }
        setTurnPhase(TurnPhase.PLACING);
        changeCurrentPlayer();
        return DrawResult.OK;
    }

    private void changeCurrentPlayer() {
        currentPlayer = (currentPlayer + 1) % maxPlayers;
    }

    public void placeCard(PlayerProfile pp, Card c, Side s, int i, int j) {
        if ((status == GameStatus.PLAY || status == GameStatus.SECOND_LAST_TURN || status == GameStatus.LAST_TURN) && turnPhase == TurnPhase.PLACING) {
            if (currentPlayer == playerProfiles.indexOf(pp)) {
                //place on play area
                playAreas.get(pp).placeAt(i, j, c, s);

                //delete card from hand
                // TODO: what if card is not in hand?
                playersData.get(pp).getHand().remove(c);

                switch (status) {
                    case GameStatus.PLAY:
                        if (playAreas.get(pp).getScore() >= 20) {
                            //this is the second last one round
                            transition(GameStatus.SECOND_LAST_TURN);
                        }
                        setTurnPhase(TurnPhase.DRAWING);
                        break;

                    case GameStatus.SECOND_LAST_TURN:
                        setTurnPhase(TurnPhase.DRAWING);
                        break;

                    case GameStatus.LAST_TURN:
                        if (currentPlayer == maxPlayers - 1) {
                            //this player is the last one -> game is finished. It's useless drawing a card. This is the only one point from where finishing game
                            transition(GameStatus.FINISHED);
                        } else {
                            //change current player (state and turn phase are not updated because in this phase it's useless to draw card)
                            changeCurrentPlayer();
                        }
                        break;
                }
            } else {
                throw new IllegalTurnException();
            }
        } else {
            throw new IllegalMoveException();
        }
    }

    private void setTurnPhase(TurnPhase turnPhase) {
        this.turnPhase = turnPhase;
    }


    public List<PlayerProfile> getWinners() {
        if (status != GameStatus.FINISHED) {
            throw new IllegalMoveException();
        }

        Map<PlayerProfile, Integer> scores = getTotalScores();
        int maxScore = scores.values().stream().mapToInt(s -> s).max().orElse(0);

        return scores.entrySet().stream()
                .filter(entry -> entry.getValue() == maxScore)
                .map(Map.Entry::getKey)
                .toList();
    }

    public Map<PlayerProfile, Integer> getTotalScores() {
        if (status != GameStatus.FINISHED) {
            throw new IllegalMoveException();
        }
        Map<PlayerProfile, Integer> r = new HashMap<>();
        playAreas.forEach((player, playArea) ->
                r.put(player, playArea.getScore()
                        + playersData.get(player).getObjectiveChoice().getEarnedPoints(playArea)
                        + commonObjectives.stream().mapToInt(objective -> objective.getEarnedPoints(playArea)).sum()) // points earned from play area plus points earned from objective cards
        );
        return r;
    }
}