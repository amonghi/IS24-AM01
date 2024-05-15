package it.polimi.ingsw.am01.model.game;


import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.eventemitter.EventEmitterImpl;
import it.polimi.ingsw.am01.eventemitter.EventListener;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.chat.ChatManager;
import it.polimi.ingsw.am01.model.chat.Message;
import it.polimi.ingsw.am01.model.choice.Choice;
import it.polimi.ingsw.am01.model.choice.DoubleChoiceException;
import it.polimi.ingsw.am01.model.choice.MultiChoice;
import it.polimi.ingsw.am01.model.choice.SelectionResult;
import it.polimi.ingsw.am01.model.event.*;
import it.polimi.ingsw.am01.model.exception.*;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.model.player.PlayerData;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * It manages an entire game: from player joining to decreeing winners.
 * It defines {@link PlayerData} for each {@link PlayerProfile} by looking on choices made.
 *
 * @see PlayerData
 * @see PlayerProfile
 * @see Choice
 * @see MultiChoice
 */
public class Game implements EventEmitter<GameEvent> {

    private static final int HAND_CARDS = 3;
    private final int id;
    private final List<PlayerProfile> playerProfiles;
    private final ChatManager chatManager;
    private final Map<PlayerProfile, Choice<Side>> startingCardSideChoices;
    private final Map<PlayerProfile, Card> startingCards;
    private final Map<PlayerProfile, MultiChoice<PlayerColor, PlayerProfile>.Choice> colorChoices;
    private final Map<PlayerProfile, Choice<Objective>> objectiveChoices;
    private final Map<PlayerProfile, PlayerData> playersData;
    private final Map<PlayerProfile, PlayArea> playAreas;
    private final Map<PlayerProfile, Boolean> connections;
    private final Set<Objective> commonObjectives;
    private final int maxPlayers;
    private final Board board;
    transient private EventEmitterImpl<GameEvent> emitter;
    private GameStatus status;
    private TurnPhase turnPhase;
    /**
     * This attribute is used to save the previous valid status after a game pause.
     *
     * @see Game#pauseGame() pausedGame
     * @see Game#resumeGame() resumeGame
     */
    private GameStatus recoverStatus;
    /**
     * It stores the current player's index referred to {@code playerProfiles} list
     */
    private int currentPlayer;

    /**
     * Constructs a new {@code Game} and set id and maxPlayers fields. {@link Board} is set with standard decks (40 cards per each deck).
     *
     * @param id         the unique id of the game
     * @param maxPlayers the maximum number of players that can play this game
     * @throws InvalidMaxPlayersException if {@code maxPlayers} is not between 2 and 4
     * @see Board
     */
    public Game(int id, int maxPlayers) throws InvalidMaxPlayersException {
        if (maxPlayers < 2 || maxPlayers > 4) {
            throw new InvalidMaxPlayersException(maxPlayers);
        }


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
        this.connections = new HashMap<>();
        this.commonObjectives = new HashSet<>();
        this.currentPlayer = 0;
        this.turnPhase = TurnPhase.PLACING;
        this.board = Board.createShuffled(new Deck(GameAssets.getInstance().getResourceCards()),
                new Deck(GameAssets.getInstance().getGoldenCards()));
        emitter = new EventEmitterImpl<>();
        for (FaceUpCard faceUpCard : board.getFaceUpCards()) {
            emitter.bubble(faceUpCard);
        }
    }

    /**
     * Constructs a new {@code Game} and set id, maxPlayers and {@link Board} fields.
     * This constructor is used to create a new game with custom decks.
     * It throws an {@code IllegalArgumentException} if {@code maxPlayers} is not between 2 and 4
     *
     * @param id         the unique id of the game
     * @param maxPlayers the maximum number of players that can play this game
     * @param board      the board of the game, that includes all {@link FaceUpCard} and {@link Deck}
     * @throws InvalidMaxPlayersException if {@code maxPlayers} is not between 2 and 4
     * @see Board
     */
    public Game(int id, int maxPlayers, Board board) throws InvalidMaxPlayersException {
        if (maxPlayers < 2 || maxPlayers > 4) {
            throw new InvalidMaxPlayersException(maxPlayers);
        }

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
        this.connections = new HashMap<>();
        this.commonObjectives = new HashSet<>();
        this.currentPlayer = 0;
        this.turnPhase = TurnPhase.PLACING;
        this.board = board;
        this.emitter = new EventEmitterImpl<>();
        for (FaceUpCard faceUpCard : board.getFaceUpCards()) {
            emitter.bubble(faceUpCard);
        }
    }

    /**
     * Implements the event emitter if null
     *
     * @return The event emitter
     */
    private EventEmitterImpl<GameEvent> getEmitter() {
        if (emitter == null) {
            emitter = new EventEmitterImpl<>();
            for (FaceUpCard faceUpCard : board.getFaceUpCards()) {
                emitter.bubble(faceUpCard);
            }
        }
        return emitter;
    }

    /**
     * @param pp the {@link PlayerProfile} associated to player whose objective choices are required
     * @return an unmodifiable set of {@link Objective} that represents all possible options that player {@code pp} could choose
     */
    public synchronized Set<Objective> getObjectiveOptions(PlayerProfile pp) {
        if (!playerProfiles.contains(pp)) {
            throw new IllegalArgumentException("Player is not in this game");
        }

        return Collections.unmodifiableSet(objectiveChoices.get(pp).getOptions());
    }

    /**
     * @return the id of the game
     */
    public synchronized int getId() {
        return id;
    }

    /**
     * @return all players' starting cards
     */
    public synchronized Map<PlayerProfile, Card> getStartingCards() {
        return Collections.unmodifiableMap(startingCards);
    }

    /**
     * @return an unmodifiable list of {@link PlayerProfile} that have joined in game already
     */
    public synchronized List<PlayerProfile> getPlayerProfiles() {
        return Collections.unmodifiableList(playerProfiles);
    }

    /**
     * @param pp the {@link PlayerProfile} associated to player whose {@link PlayerData} is required
     * @return the {@link PlayerData} of {@code pp}
     * @see PlayerData
     */
    public synchronized PlayerData getPlayerData(PlayerProfile pp) {
        if (!playerProfiles.contains(pp)) {
            throw new IllegalArgumentException("Player is not in this game");
        }

        return playersData.get(pp);
    }

    /**
     * @param pp the {@link PlayerProfile} associated to player whose {@link PlayArea} is required
     * @return the {@link PlayArea} of {@code pp}
     * @see PlayArea
     */
    public synchronized PlayArea getPlayArea(PlayerProfile pp) {
        if (!playerProfiles.contains(pp)) {
            throw new IllegalArgumentException("Player is not in this game");
        }

        return playAreas.get(pp);
    }

    /**
     * @return the {@link Board} of this game
     * @see Board
     */
    public synchronized Board getBoard() {
        return board;
    }

    /**
     * @return the {@link PlayerProfile} that has to play at this moment of the game
     * @throws IllegalGameStateException if the current {@link GameStatus} is not a playing status
     */
    public synchronized PlayerProfile getCurrentPlayer() throws IllegalGameStateException {
        if (status != GameStatus.PLAY && status != GameStatus.SECOND_LAST_TURN && status != GameStatus.LAST_TURN && status != GameStatus.SUSPENDED) {
            throw new IllegalGameStateException();
        }

        return playerProfiles.get(currentPlayer);
    }

    /**
     * @return an unmodifiable set of common {@link Objective} of the game.
     * Those {@link Objective} are available for everyone
     */
    public synchronized Set<Objective> getCommonObjectives() {
        return Collections.unmodifiableSet(commonObjectives);
    }

    /**
     * @return the current macro-phase of the game
     * @see GameStatus
     */
    public synchronized GameStatus getStatus() {
        return status;
    }

    /**
     * @return the current {@link TurnPhase}: placing or drawing
     * @throws IllegalGameStateException if the current {@link GameStatus} is not a playing status
     * @see TurnPhase
     */
    public synchronized TurnPhase getTurnPhase() throws IllegalGameStateException {
        if (status != GameStatus.PLAY && status != GameStatus.SECOND_LAST_TURN && status != GameStatus.LAST_TURN) {
            throw new IllegalGameStateException();
        }

        return turnPhase;
    }

    /**
     * This method permits to set the current turn phase
     *
     * @param turnPhase the new {@link TurnPhase} to be set.
     * @see TurnPhase
     */
    private void setTurnPhase(TurnPhase turnPhase) {
        this.turnPhase = turnPhase;
    }

    /**
     * @return the {@link ChatManager} of this game, that manages messages sent by each player
     * @see ChatManager
     * @see Message
     */
    public synchronized ChatManager getChatManager() {
        return chatManager;
    }

    /**
     * @return the maximum number of players that can play this game
     */
    public synchronized int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * This method permits to prepare and start the turn-based phase of the game, after choices phase.
     * It throws a {@code NotEnoughGameResourcesException} if there are not enough resource or golden cards into decks
     *
     * @see GameStatus
     */
    private void setupAndStartTurnPhase() {
        for (PlayerProfile player : playerProfiles) {
            List<Card> hand = new ArrayList<>();
            hand.add(board.getResourceCardDeck().draw().orElseThrow(() -> new NotEnoughGameResourcesException("Resource card deck should not be empty")));
            hand.add(board.getResourceCardDeck().draw().orElseThrow(() -> new NotEnoughGameResourcesException("Resource card deck should not be empty")));
            hand.add(board.getGoldenCardDeck().draw().orElseThrow(() -> new NotEnoughGameResourcesException("Golden card deck should not be empty")));

            playersData.put(player,
                    new PlayerData(hand,
                            objectiveChoices.get(player).getSelected().orElseThrow(),
                            colorChoices.get(player).getSelected().orElseThrow()));
        }
        setFirstPlayer();
        if (board.getResourceCardDeck().isEmpty() && board.getGoldenCardDeck().isEmpty()) {
            transition(GameStatus.LAST_TURN);
        } else {
            transition(GameStatus.PLAY);
        }

    }

    /**
     * This method randomly selects the first player that has to play. Players sequence, in a round, is based on joining order
     */
    private void setFirstPlayer() {
        Random random = new Random();
        Collections.rotate(playerProfiles, random.nextInt(playerProfiles.size()));
    }

    /**
     * This method permits to pause the game, if it is not already {@code SUSPENDED}.
     * No action will be performed while {@code SUSPENDED} status is set.
     *
     * @throws IllegalGameStateException is the current {@link GameStatus} is already {@link GameStatus#SUSPENDED}
     * @see GameStatus
     */
    public synchronized void pauseGame() throws IllegalGameStateException {
        if (status == GameStatus.SUSPENDED) {
            throw new IllegalGameStateException();
        }
        recoverStatus = status;
        status = GameStatus.SUSPENDED;
        getEmitter().emit(new GamePausedEvent());
    }

    /**
     * This method permits to resume the game, if it is {@code SUSPENDED}. The previous valid status will be recovered.
     * No action will be performed while {@code SUSPENDED} status is not set.
     *
     * @throws IllegalGameStateException is the current {@link GameStatus} is not {@link GameStatus#SUSPENDED}
     * @see GameStatus
     */
    public synchronized void resumeGame() throws IllegalGameStateException {
        if (status != GameStatus.SUSPENDED) {
            throw new IllegalGameStateException();
        }
        status = recoverStatus;
        getEmitter().emit(new GameResumedEvent(status));
    }

    /**
     * This method set game's status. It permits to perform status' transition
     *
     * @param nextStatus the new current status
     * @see GameStatus
     */
    private void transition(GameStatus nextStatus) {
        this.status = nextStatus;
    }

    /**
     * This method prepares all choices: starting card side, player color and secret objective.
     * It throws a {@code NotEnoughGameResourcesException} if there are not enough starting or objective cards
     * It requires that all players have already joined the game, with {@link Game#join(PlayerProfile) join} method.
     *
     * @see Choice
     * @see MultiChoice
     */
    private void setUpChoices() {
        // Setup starter card choices
        Deck starterCardDeck = new Deck(GameAssets.getInstance().getStarterCards());
        starterCardDeck.shuffle();

        for (PlayerProfile player : playerProfiles) {
            startingCardSideChoices.put(player, new Choice<>(EnumSet.allOf(Side.class)));
            startingCards.put(player, starterCardDeck.draw().orElseThrow(() -> new NotEnoughGameResourcesException("Starting cards list should not be empty")));
        }

        // Setup color choices
        MultiChoice<PlayerColor, PlayerProfile> multiChoice = new MultiChoice<>(EnumSet.allOf(PlayerColor.class), new HashSet<>(playerProfiles));
        playerProfiles.forEach((player) -> colorChoices.put(player, multiChoice.getChoices().get(player)));

        // Setup objective choices
        List<Objective> objectiveList = new ArrayList<>(GameAssets.getInstance().getObjectives());
        if (objectiveList.size() < (playerProfiles.size() + 1) * 2) {
            throw new NotEnoughGameResourcesException("There are not enough objective cards");
        }

        Collections.shuffle(objectiveList);

        commonObjectives.add(objectiveList.removeFirst());
        commonObjectives.add(objectiveList.removeFirst());

        for (PlayerProfile player : playerProfiles) {
            Set<Objective> secretObjectives = new HashSet<>();
            secretObjectives.add(objectiveList.removeFirst());
            secretObjectives.add(objectiveList.removeFirst());
            objectiveChoices.put(player, new Choice<>(secretObjectives));
        }
        getEmitter().emit(new AllPlayersJoinedEvent());
    }

    /**
     * This method add a new {@link PlayerProfile} to game, and performs status transition if there are {@code maxPlayers} players joined.
     *
     * @param pp the {@link PlayerProfile} of new player
     * @throws IllegalGameStateException     if the current {@link GameStatus} is not {@link GameStatus#AWAITING_PLAYERS}
     * @throws PlayerAlreadyPlayingException if the specified {@link PlayerProfile} try to join a game in which he is already playing
     */
    public synchronized void join(PlayerProfile pp) throws IllegalGameStateException, PlayerAlreadyPlayingException {
        if (status != GameStatus.AWAITING_PLAYERS) {
            throw new IllegalGameStateException();
        }
        if (playerProfiles.stream().anyMatch(p -> p.getName().equals(pp.getName()))) {
            throw new PlayerAlreadyPlayingException(pp.getName());
        }

        playerProfiles.add(pp);
        connections.put(pp, true);

        getEmitter().emit(new PlayerJoinedEvent(pp));

        if (playerProfiles.size() == maxPlayers) {
            transition(GameStatus.SETUP_STARTING_CARD_SIDE);
            setUpChoices();
        }
    }


    /**
     * This method permits to start the game (set {@code SETUP_STARTING_CARD_SIDE} status),
     * despite there aren't yet connected {@code maxPlayers} players.
     * Game starts automatically as soon as the maximum threshold of connected players is reached
     *
     * @throws IllegalGameStateException if the current {@link GameStatus} is not {@link GameStatus#AWAITING_PLAYERS}
     * @throws NotEnoughPlayersException if someone try to start a game with only 1 player
     */
    public synchronized void startGame() throws IllegalGameStateException, NotEnoughPlayersException {
        if (status != GameStatus.AWAITING_PLAYERS) {
            throw new IllegalGameStateException();
        }
        if (playerProfiles.size() < 2) {
            throw new NotEnoughPlayersException();
        }

        transition(GameStatus.SETUP_STARTING_CARD_SIDE);
        setUpChoices();
    }

    /**
     * This method performs a player choice: starting card side choice.
     * Starting card is also placed on {@link PlayArea} of player {@code pp}, on side {@code s}.
     * If all players have chosen side, this method performs status transition.
     *
     * @param pp the {@link PlayerProfile} of player tha want to choose
     * @param s  the chosen side of starting card
     * @throws DoubleChoiceException     if the player has already chosen the starting card side
     * @throws IllegalGameStateException if the current {@link GameStatus} is not {@link GameStatus#SETUP_STARTING_CARD_SIDE}
     * @throws PlayerNotInGameException  if the specified {@link PlayerProfile} is not in game
     * @see Choice
     */
    public synchronized void selectStartingCardSide(PlayerProfile pp, Side s) throws DoubleChoiceException, IllegalGameStateException, PlayerNotInGameException {
        if (status != GameStatus.SETUP_STARTING_CARD_SIDE) {
            throw new IllegalGameStateException();
        }
        if (!playerProfiles.contains(pp)) {
            throw new PlayerNotInGameException();
        }

        startingCardSideChoices.get(pp).select(s);

        playAreas.put(pp, new PlayArea(startingCards.get(pp), s));

        getEmitter().emit(new CardPlacedEvent(pp, playAreas.get(pp).getAt(PlayArea.Position.ORIGIN).orElse(null)));

        if (startingCardSideChoices.values().stream().noneMatch(choice -> choice.getSelected().isEmpty())) {
            transition(GameStatus.SETUP_COLOR);
            getEmitter().emit(new AllPlayersChoseStartingCardSideEvent());
        }
    }

    /**
     * This method performs a player choice: color choice.
     * If all players have chosen their color, this method performs status transition.
     *
     * @param pp the {@link PlayerProfile} of player that want to choose
     * @param pc the {@link PlayerColor} chosen by player {@code pp}
     * @return the {@link SelectionResult} referred to the choice made
     * @throws IllegalGameStateException if the current {@link GameStatus} is not {@link GameStatus#SETUP_COLOR}
     * @throws PlayerNotInGameException  if the specified {@link PlayerProfile} is not in game
     * @see SelectionResult
     * @see MultiChoice
     */
    public synchronized SelectionResult selectColor(PlayerProfile pp, PlayerColor pc) throws IllegalGameStateException, PlayerNotInGameException {
        if (status != GameStatus.SETUP_COLOR) {
            throw new IllegalGameStateException();
        }
        if (!playerProfiles.contains(pp)) {
            throw new PlayerNotInGameException();
        }
        SelectionResult sr = colorChoices.get(pp).select(pc);
        getEmitter().emit(new PlayerChangedColorChoiceEvent(pp, pc, sr));

        if (colorChoices.get(pp).isSettled()) {
            transition(GameStatus.SETUP_OBJECTIVE);
            getEmitter().emit(new AllColorChoicesSettledEvent());
        }
        return sr;
    }

    /**
     * This method performs a player choice: objective choice.
     * If all players have chosen their secret objective, this method performs status transition.
     *
     * @param pp the {@link PlayerProfile} of player that want to choose
     * @param o  the {@link Objective} chosen by player {@code pp}
     * @throws DoubleChoiceException     if the player has already chosen the secret objective
     * @throws IllegalGameStateException if the current {@link GameStatus} is not {@link GameStatus#SETUP_OBJECTIVE}
     * @throws PlayerNotInGameException  if the specified {@link PlayerProfile} is not in game
     * @throws InvalidObjectiveException if the specified {@link Objective} is not a possible choice
     * @see Choice
     */
    public synchronized void selectObjective(PlayerProfile pp, Objective o) throws IllegalGameStateException, PlayerNotInGameException, DoubleChoiceException, InvalidObjectiveException {
        if (status != GameStatus.SETUP_OBJECTIVE) {
            throw new IllegalGameStateException();
        }
        if (!playerProfiles.contains(pp)) {
            throw new PlayerNotInGameException();
        }

        try {
            objectiveChoices.get(pp).select(o);
            getEmitter().emit(new SecretObjectiveChosenEvent(objectiveChoices.keySet().stream()
                    .filter(playerProfile -> objectiveChoices.get(playerProfile).getSelected().isPresent())
                    .collect(Collectors.toSet())));
        } catch (NoSuchElementException e) {
            throw new InvalidObjectiveException();
        }
        if (objectiveChoices.values().stream().noneMatch(choice -> choice.getSelected().isEmpty())) {
            //all players had chosen their objective -> go to the next state (start "turn phase")
            setupAndStartTurnPhase();

            getEmitter().emit(new SetUpPhaseFinishedEvent(commonObjectives, board.getFaceUpCards(), playersData));
            getEmitter().emit(new UpdateGameStatusAndTurnEvent(status, turnPhase, getCurrentPlayer()));
        }
    }

    /**
     * This method allow player {@code pp} to draw a card from {@link DrawSource} {@code ds},
     * only if source is not empty and game is in compatible status ({@code PLAY} or {@code SECOND_LAST_TURN})
     * and compatible turn phase ({@code DRAWING}).
     * Notice that in {@code LAST_TURN} status, players are not allowed to draw cards:
     * drawing a card on last turn is useless because it is an action that doesn't affect the current turn.
     * It also manages status, turn phase transition and set next player
     *
     * @param pp the {@link PlayerProfile} of player that want to draw
     * @param ds the {@link DrawSource} from where to get the card
     * @return the result of drawing
     * @throws IllegalTurnException      if it's not the turn of the specified {@link PlayerProfile}
     * @throws IllegalGameStateException if the current {@link GameStatus} is neither {@link GameStatus#PLAY} nor {@link GameStatus#SECOND_LAST_TURN}
     *                                   or the current {@link TurnPhase} is not {@link TurnPhase#DRAWING}
     * @throws PlayerNotInGameException  if the specified {@link PlayerProfile} is not in game
     * @see DrawSource
     * @see DrawResult
     * @see TurnPhase
     * @see GameStatus
     */
    public synchronized DrawResult drawCard(PlayerProfile pp, DrawSource ds) throws IllegalMoveException, PlayerNotInGameException {
        if ((status != GameStatus.PLAY && status != GameStatus.SECOND_LAST_TURN) || turnPhase != TurnPhase.DRAWING) {
            throw new IllegalGameStateException();
        }
        if (!playerProfiles.contains(pp)) {
            throw new PlayerNotInGameException();
        }
        if (currentPlayer != playerProfiles.indexOf(pp)) {
            throw new IllegalTurnException();
        }

        Optional<Card> card = ds.draw();
        card.ifPresent(c -> playersData.get(pp).getHand().add(c));
        if (card.isEmpty()) {
            getEmitter().emit(new CardDrawnFromEmptySourceEvent(ds));
            return DrawResult.EMPTY;
        }

        getEmitter().emit(new HandChangedEvent(pp, new HashSet<>(playersData.get(pp).getHand())));
        getEmitter().emit(new CardDrawnFromDeckEvent(getBoard().getResourceCardDeck(), getBoard().getGoldenCardDeck()));

        switch (status) {
            case GameStatus.PLAY -> {
                if (board.getResourceCardDeck().isEmpty() && board.getGoldenCardDeck().isEmpty()) {
                    if (!isLastConnectedPlayer(currentPlayer)) {
                        // all decks are empty --> game is on "final phase"
                        transition(GameStatus.SECOND_LAST_TURN);
                    } else {
                        //this is the "last" player of round
                        transition(GameStatus.LAST_TURN);
                    }
                }
            }
            case GameStatus.SECOND_LAST_TURN -> {
                if (isLastConnectedPlayer(currentPlayer)) {
                    //this is the "last" player of round
                    transition(GameStatus.LAST_TURN);
                }
            }
        }
        setTurnPhase(TurnPhase.PLACING);
        changeCurrentPlayer();

        return DrawResult.OK;
    }

    /**
     * This method permits to change the player that have to play
     */
    private void changeCurrentPlayer() throws IllegalGameStateException {
        do {
            currentPlayer = (currentPlayer + 1) % playerProfiles.size();
        } while (!connections.get(playerProfiles.get(currentPlayer)));
        getEmitter().emit(new UpdateGameStatusAndTurnEvent(status, turnPhase, getCurrentPlayer()));
    }

    /**
     * This method allow player {@code pp} to place a card on his {@link PlayArea} on coordinates {@code i} and {@code j},
     * only if placement is valid, game is in compatible status ({@code PLAY}, {@code SECOND_LAST_TURN} or {@code LAST_TURN})
     * and compatible turn phase ({@code DRAWING}) and player have the card {@code c} on his hand.
     * It also manages status, turn phase transition and set next player (if needed)
     *
     * @param pp the {@link PlayerProfile} of player that want to place
     * @param c  the {@link Card} to be placed
     * @param s  the visible {@link Side} of the placement
     * @param i  the coordinate i of the placement
     * @param j  the coordinate j of the placement
     * @throws IllegalTurnException      if it's not the turn of the specified {@link PlayerProfile}
     * @throws IllegalGameStateException if the current {@link GameStatus} is not a playing state or the current {@link TurnPhase} is not {@link TurnPhase#PLACING}
     * @throws PlayerNotInGameException  if the specified {@link PlayerProfile} is not in game
     * @throws CardNotInHandException    if the player does not have the specified {@link Card} in his hand
     * @throws IllegalPlacementException if the specified position is not a playable position
     * @see TurnPhase
     * @see GameStatus
     * @see PlayerData
     * @see PlayArea
     * @see PlayArea.CardPlacement
     */
    public synchronized void placeCard(PlayerProfile pp, Card c, Side s, int i, int j) throws IllegalMoveException, PlayerNotInGameException, CardNotInHandException, IllegalPlacementException {
        if ((status != GameStatus.PLAY && status != GameStatus.SECOND_LAST_TURN && status != GameStatus.LAST_TURN)
                || turnPhase != TurnPhase.PLACING) {
            throw new IllegalGameStateException();
        }
        if (!playerProfiles.contains(pp)) {
            throw new PlayerNotInGameException();
        }
        if (currentPlayer != playerProfiles.indexOf(pp)) {
            throw new IllegalTurnException();
        }
        if (!playersData.get(pp).getHand().contains(c)) {
            throw new CardNotInHandException();
        }

        //delete card from hand
        playersData.get(pp).getHand().remove(c);

        getEmitter().emit(new HandChangedEvent(pp, new HashSet<>(playersData.get(pp).getHand())));

        //place on play area
        PlayArea.CardPlacement cardPlacement = playAreas.get(pp).placeAt(i, j, c, s);

        getEmitter().emit(new CardPlacedEvent(pp, cardPlacement));

        switch (status) {
            case GameStatus.PLAY:
                if (playAreas.get(pp).getScore() >= 20) {
                    //this is the second last one round
                    transition(GameStatus.SECOND_LAST_TURN);
                }
                setTurnPhase(TurnPhase.DRAWING);

                getEmitter().emit(new UpdateGameStatusAndTurnEvent(status, turnPhase, getCurrentPlayer()));
                break;

            case GameStatus.SECOND_LAST_TURN:
                setTurnPhase(TurnPhase.DRAWING);

                getEmitter().emit(new UpdateGameStatusAndTurnEvent(status, turnPhase, getCurrentPlayer()));
                break;

            case GameStatus.LAST_TURN:
                if (isLastConnectedPlayer(currentPlayer)) {
                    //this player is the last one -> game is finished. It's useless drawing a card. This is the only one point from where finishing game
                    transition(GameStatus.FINISHED);

                    getEmitter().emit(new GameFinishedEvent(getTotalScores()));
                    getEmitter().emit(new GameClosedEvent());
                } else {
                    //change current player (state and turn phase are not updated because in this phase it's useless to draw card)
                    changeCurrentPlayer();
                }
                break;
        }
    }

    /**
     * @param currentPlayer the index of the player who is currently playing
     * @return whether the specified {@code currentPlayer} is the last of the current turn
     * @throws IllegalGameStateException if there are no connected players
     */
    private boolean isLastConnectedPlayer(int currentPlayer) throws IllegalGameStateException {
        return currentPlayer ==
                connections
                        .keySet()
                        .stream()
                        .filter(connections::get)
                        .mapToInt(playerProfiles::indexOf)
                        .max()
                        .orElseThrow(IllegalGameStateException::new);
    }

    /**
     * This method provides winners, only if game status is set to {@code FINISHED}
     *
     * @return a list that contains all winners of the game. It is based on total scores
     * @throws IllegalGameStateException if the current {@link GameStatus} is not {@link GameStatus#FINISHED}
     * @see Game#getTotalScores() getTotalScores
     * @see GameStatus
     */
    public synchronized List<PlayerProfile> getWinners() throws IllegalGameStateException {
        Map<PlayerProfile, Integer> scores = getTotalScores();
        int maxScore = scores.values().stream().mapToInt(s -> s).max().orElse(0);

        return scores.entrySet().stream()
                .filter(entry -> entry.getValue() == maxScore)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * This method provides total score for each player, only if game status is set to {@code FINISHED}
     *
     * @return a map that contains all total scores
     * @throws IllegalGameStateException if the current {@link GameStatus} is not {@link GameStatus#FINISHED}
     */
    public synchronized Map<PlayerProfile, Integer> getTotalScores() throws IllegalGameStateException {
        if (status != GameStatus.FINISHED) {
            throw new IllegalGameStateException();
        }
        Map<PlayerProfile, Integer> r = new HashMap<>();
        playAreas.forEach((player, playArea) ->
                r.put(player, playArea.getScore()
                        + playersData.get(player).getObjectiveChoice().getEarnedPoints(playArea)
                        + commonObjectives.stream().mapToInt(objective -> objective.getEarnedPoints(playArea)).sum()) // points earned from play area plus points earned from objective cards
        );
        return r;
    }

    private void removePlayer(PlayerProfile pp) {
        playerProfiles.remove(pp);
        connections.remove(pp);
        startingCardSideChoices.remove(pp);
        startingCards.remove(pp);
        colorChoices.remove(pp);
        objectiveChoices.remove(pp);
        playersData.remove(pp);
        playAreas.remove(pp);
    }

    public synchronized void handleDisconnection(PlayerProfile pp) {
        getEmitter().emit(new PlayerDisconnectedEvent(pp));
        getEmitter().emit(new PlayerKickedEvent(pp));
        if (status == GameStatus.AWAITING_PLAYERS) {
            removePlayer(pp);
            getEmitter().emit(new PlayerLeftEvent(pp));

            if (playerProfiles.isEmpty()) {
                getEmitter().emit(new GameClosedEvent());
            }
            return;
        }
        if (status == GameStatus.SETUP_STARTING_CARD_SIDE || status == GameStatus.SETUP_COLOR || status == GameStatus.SETUP_OBJECTIVE) {
            removePlayer(pp);
            getEmitter().emit(new PlayerLeftEvent(pp));

            // TODO: manage SETUP_COLOR case
            if (status == GameStatus.SETUP_STARTING_CARD_SIDE && startingCardSideChoices.values().stream().noneMatch(choice -> choice.getSelected().isEmpty())) {
                transition(GameStatus.SETUP_COLOR);
                getEmitter().emit(new AllPlayersChoseStartingCardSideEvent());
            } else if (status == GameStatus.SETUP_OBJECTIVE && objectiveChoices.values().stream().noneMatch(choice -> choice.getSelected().isEmpty())) {
                setupAndStartTurnPhase();
                getEmitter().emit(new SetUpPhaseFinishedEvent(commonObjectives, board.getFaceUpCards(), playersData));
                try {
                    getEmitter().emit(new UpdateGameStatusAndTurnEvent(status, turnPhase, getCurrentPlayer()));
                } catch (IllegalGameStateException e) {
                    throw new RuntimeException(e);
                }
            }

            if (playerProfiles.size() < 2) {
                getEmitter().emit(new GameAbortedEvent());
                getEmitter().emit(new GameClosedEvent());
            }
            return;
        }
        connections.replace(pp, false);
        try {
            if (getCurrentPlayer().equals(pp)) {
                if (getTurnPhase() == TurnPhase.DRAWING && playersData.get(pp).getHand().size() < HAND_CARDS) {
                    try {
                        PlayArea.CardPlacement lastPlacement = playAreas.get(pp).undoPlacement();
                        playersData.get(pp).getHand().add(lastPlacement.getCard());
                        getEmitter().emit(new UndoPlacementEvent(pp, lastPlacement.getPosition(),
                                playAreas.get(pp).getScore(), playAreas.get(pp).getSeq()));
                    } catch (NotUndoableOperationException e) {
                        e.printStackTrace(); // TODO: handle exception
                    }
                }
                //The next player has to place the card
                setTurnPhase(TurnPhase.PLACING);
                changeCurrentPlayer();
            }

            if (connections.values().stream().filter(connected -> connected.equals(true)).count() < 2) {
                pauseGame();
                //TODO: handle timer
            }

        } catch (IllegalGameStateException e) {
            e.printStackTrace(); // TODO: handle exception
        }
    }

    public synchronized void handleReconnection(PlayerProfile player) throws PlayerNotInGameException, PlayerAlreadyConnectedException, IllegalGameStateException {
        if (!playerProfiles.contains(player)) {
            throw new PlayerNotInGameException();
        }
        if (connections.get(player)) {
            throw new PlayerAlreadyConnectedException();
        }
        getEmitter().emit(new PlayerReconnectedEvent(player));
        connections.replace(player, true);
        // TODO: add reconnection events
        if (status == GameStatus.SUSPENDED && playerProfiles.stream().filter(connections::get).count() >= 2) {
            resumeGame();
        }
    }

    public synchronized boolean isConnected(PlayerProfile pp) {
        // FIXME: if pp is not in playerProfiles, should we return null or throw a PlayerNotInGameException?
        return playerProfiles.contains(pp) && connections.get(pp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String toString() {
        return "Game{" +
                "\n\tid=" + id +
                ",\n\tplayerProfiles=" + playerProfiles +
                ",\n\tchatManager=" + chatManager +
                ",\n\tstartingCardSideChoices=" + startingCardSideChoices +
                ",\n\tstartingCards=" + startingCards +
                ",\n\tcolorChoices=" + colorChoices +
                ",\n\tobjectiveChoices=" + objectiveChoices +
                ",\n\tplayersData=" + playersData +
                ",\n\tplayAreas=" + playAreas +
                ",\n\tcommonObjectives=" + commonObjectives +
                ",\n\tmaxPlayers=" + maxPlayers +
                ",\n\tboard=" + board +
                ",\n\tstatus=" + status +
                ",\n\tturnPhase=" + turnPhase +
                ",\n\trecoverStatus=" + recoverStatus +
                ",\n\tcurrentPlayer=" + currentPlayer +
                "\n}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Game game = (Game) other;
        return id == game.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Registration onAny(EventListener<GameEvent> listener) {
        return getEmitter().onAny(listener);
    }

    @Override
    public <T extends GameEvent> Registration on(Class<T> eventClass, EventListener<T> listener) {
        return getEmitter().on(eventClass, listener);
    }

    @Override
    public boolean unregister(Registration registration) {
        return getEmitter().unregister(registration);
    }
}