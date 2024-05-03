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

    private final EventEmitterImpl<GameEvent> emitter;

    private final int id;
    private final List<PlayerProfile> playerProfiles;
    private final ChatManager chatManager;
    private final Map<PlayerProfile, Choice<Side>> startingCardSideChoices;
    private final Map<PlayerProfile, Card> startingCards;
    private final Map<PlayerProfile, MultiChoice<PlayerColor, PlayerProfile>.Choice> colorChoices;
    private final Map<PlayerProfile, Choice<Objective>> objectiveChoices;
    private final Map<PlayerProfile, PlayerData> playersData;
    private final Map<PlayerProfile, PlayArea> playAreas;
    private final Set<Objective> commonObjectives;
    private final int maxPlayers;
    private final Board board;
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
        this.commonObjectives = new HashSet<>();
        this.currentPlayer = 0;
        this.turnPhase = TurnPhase.PLACING;
        this.board = Board.createShuffled(new Deck(GameAssets.getInstance().getResourceCards()),
                new Deck(GameAssets.getInstance().getGoldenCards()));
        emitter = new EventEmitterImpl<>();
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
        this.commonObjectives = new HashSet<>();
        this.currentPlayer = 0;
        this.turnPhase = TurnPhase.PLACING;
        this.board = board;
        this.emitter = new EventEmitterImpl<>();
        /* FIXME
        for (FaceUpCard faceUpCard : board.getFaceUpCards()) {
            emitter.bubble(faceUpCard);
        }
        */
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
    public synchronized Map<PlayerProfile, Card> getStartingCards(){
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
     */
    public synchronized PlayerProfile getCurrentPlayer() throws IllegalGameStateException {
        if (status != GameStatus.PLAY && status != GameStatus.SECOND_LAST_TURN && status != GameStatus.LAST_TURN) {
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
     * @see GameStatus
     */
    public synchronized void pauseGame() throws IllegalGameStateException {
        if (status == GameStatus.SUSPENDED) {
            throw new IllegalGameStateException();
        }
        recoverStatus = status;
        status = GameStatus.SUSPENDED;
    }

    /**
     * This method permits to resume the game, if it is {@code SUSPENDED}. The previous valid status will be recovered.
     * No action will be performed while {@code SUSPENDED} status is not set.
     *
     * @see GameStatus
     */
    public synchronized void resumeGame() throws IllegalGameStateException {
        if (status != GameStatus.SUSPENDED) {
            throw new IllegalGameStateException();
        }
        status = recoverStatus;
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
        emitter.emit(new AllPlayersJoinedEvent());
    }

    /**
     * This method add a new {@link PlayerProfile} to game, and performs status transition if there are {@code maxPlayers} players joined.
     * It throws an {@code IllegalArgumentException} if player is already in game
     *
     * @param pp the {@link PlayerProfile} of new player
     */
    public synchronized void join(PlayerProfile pp) throws IllegalGameStateException, PlayerAlreadyPlayingException {
        if (status != GameStatus.AWAITING_PLAYERS) {
            throw new IllegalGameStateException();
        }
        if (playerProfiles.stream().anyMatch(p -> p.getName().equals(pp.getName()))) {
            throw new PlayerAlreadyPlayingException(pp.getName());
        }

        playerProfiles.add(pp);

        emitter.emit(new PlayerJoinedEvent(this, pp));

        if (playerProfiles.size() == maxPlayers) {
            transition(GameStatus.SETUP_STARTING_CARD_SIDE);
            setUpChoices();
        }
    }

    /**
     * This method permits to start the game (set {@code SETUP_STARTING_CARD_SIDE} status),
     * despite there aren't yet connected {@code maxPlayers} players.
     * Game starts automatically as soon as the maximum threshold of connected players is reached
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
     * @throws DoubleChoiceException if player has already chosen starting card side
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

        emitter.emit(new CardPlacedEvent(pp.getName(), playAreas.get(pp).getAt(PlayArea.Position.ORIGIN).orElse(null)));

        if (startingCardSideChoices.values().stream().noneMatch(choice -> choice.getSelected().isEmpty())) {
            transition(GameStatus.SETUP_COLOR);
            emitter.emit(new AllPlayersChoseStartingCardSideEvent());
        }
    }

    /**
     * This method performs a player choice: color choice.
     * If all players have chosen their color, this method performs status transition.
     *
     * @param pp the {@link PlayerProfile} of player that want to choose
     * @param pc the {@link PlayerColor} chosen by player {@code pp}
     * @return the {@link SelectionResult} referred to the choice made
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
        emitter.emit(new PlayerChangedColorChoiceEvent(pp, pc, sr));

        if (colorChoices.get(pp).isSettled()) { // FIXME: MultiChoice class
            transition(GameStatus.SETUP_OBJECTIVE);
            emitter.emit(new AllColorChoicesSettledEvent());
        }
        return sr;
    }

    /**
     * This method performs a player choice: objective choice.
     * If all players have chosen their secret objective, this method performs status transition.
     *
     * @param pp the {@link PlayerProfile} of player that want to choose
     * @param o  the {@link Objective} chosen by player {@code pp}
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
            emitter.emit(new SecretObjectiveChosenEvent(objectiveChoices.keySet().stream()
                            .filter(playerProfile -> objectiveChoices.get(playerProfile).getSelected().isPresent())
                            .collect(Collectors.toSet())));
        } catch (NoSuchElementException e) {
            throw new InvalidObjectiveException();
        }
        if (objectiveChoices.values().stream().noneMatch(choice -> choice.getSelected().isEmpty())) {
            //all players had chosen their objective -> go to the next state (start "turn phase")
            setupAndStartTurnPhase();

            emitter.emit(new SetUpPhaseFinishedEvent(commonObjectives, board.getFaceUpCards(), playersData));
            emitter.emit(new UpdateGameStatusAndTurnEvent(status, turnPhase, getCurrentPlayer()));
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
     * @see DrawSource
     * @see DrawResult
     * @see TurnPhase
     * @see GameStatus
     */
    public synchronized DrawResult drawCard(PlayerProfile pp, DrawSource ds) throws IllegalTurnException, IllegalGameStateException, PlayerNotInGameException {
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
            emitter.emit(new CardDrawnFromEmptySourceEvent(ds));
            return DrawResult.EMPTY;
        }

        emitter.emit(new CardDrawnFromDeckEvent(getBoard().getResourceCardDeck(), getBoard().getGoldenCardDeck()));

        switch (status) {
            case GameStatus.PLAY -> {
                if (board.getResourceCardDeck().isEmpty() && board.getGoldenCardDeck().isEmpty()) {
                    if (currentPlayer != playerProfiles.size() - 1) {
                        // all decks are empty --> game is on "final phase"
                        transition(GameStatus.SECOND_LAST_TURN);
                    } else {
                        //this is the "last" player of round
                        transition(GameStatus.LAST_TURN);
                    }
                }
            }
            case GameStatus.SECOND_LAST_TURN -> {
                if (currentPlayer == playerProfiles.size() - 1) {
                    //this is the "last" player of round
                    transition(GameStatus.LAST_TURN);
                }
            }
        }
        setTurnPhase(TurnPhase.PLACING);
        changeCurrentPlayer();

        emitter.emit(new UpdateGameStatusAndTurnEvent(status, turnPhase, getCurrentPlayer()));

        return DrawResult.OK;
    }

    /**
     * This method permits to change the player that have to play
     */
    private void changeCurrentPlayer() {
        currentPlayer = (currentPlayer + 1) % playerProfiles.size();
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
     * @see TurnPhase
     * @see GameStatus
     * @see PlayerData
     * @see PlayArea
     * @see PlayArea.CardPlacement
     */
    public synchronized void placeCard(PlayerProfile pp, Card c, Side s, int i, int j) throws IllegalTurnException, PlayerNotInGameException, IllegalGameStateException, CardNotInHandException, IllegalPlacementException {
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

        //place on play area
        PlayArea.CardPlacement cardPlacement = playAreas.get(pp).placeAt(i, j, c, s);

        //delete card from hand
        playersData.get(pp).getHand().remove(c);

        switch (status) {
            case GameStatus.PLAY:
                if (playAreas.get(pp).getScore() >= 20) {
                    //this is the second last one round
                    transition(GameStatus.SECOND_LAST_TURN);
                }
                setTurnPhase(TurnPhase.DRAWING);

                emitter.emit(new UpdateGameStatusAndTurnEvent(status, turnPhase, getCurrentPlayer()));
                break;

            case GameStatus.SECOND_LAST_TURN:
                setTurnPhase(TurnPhase.DRAWING);

                emitter.emit(new UpdateGameStatusAndTurnEvent(status, turnPhase, getCurrentPlayer()));
                break;

            case GameStatus.LAST_TURN:
                if (currentPlayer == playerProfiles.size() - 1) {
                    //this player is the last one -> game is finished. It's useless drawing a card. This is the only one point from where finishing game
                    transition(GameStatus.FINISHED);

                    emitter.emit(new GameFinishedEvent(getTotalScores()));
                } else {
                    //change current player (state and turn phase are not updated because in this phase it's useless to draw card)
                    changeCurrentPlayer();

                    emitter.emit(new UpdateGameStatusAndTurnEvent(status, turnPhase, getCurrentPlayer()));
                }
                break;
        }
        emitter.emit(new CardPlacedEvent(pp.getName(), cardPlacement));
    }

    /**
     * This method provides winners, only if game status is set to {@code FINISHED}
     *
     * @return a list that contains all winners of the game. It is based on total scores
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
        return emitter.onAny(listener);
    }

    @Override
    public <T extends GameEvent> Registration on(Class<T> eventClass, EventListener<T> listener) {
        return emitter.on(eventClass, listener);
    }

    @Override
    public boolean unregister(Registration registration) {
        return emitter.unregister(registration);
    }
}