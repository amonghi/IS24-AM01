# UML diagrams

This UML diagram is 🚧 work in progress 🚧

## Main Game class

```mermaid
classDiagram
    note for GameAssets "Reads assets from JSON file"
    class GameAssets {
        + List~Card~ getResourceCards()$
        + List~Card~ getGoldenCards()$
        + List~Card~ getStarterCards()$
        + List~Objective~ getObjectives()$

        - GameAssets()
    }

    note for Game "The players are stored in the order in which they play.
    players.get(0) is the first one to play."
    note for Game "currentPlayer is the index of the player currently playing."
    class Game {
        - String id
        - GameStatus status

        - List~PlayerProfile~ playerProfiles
        - Map~PlayerProfile, PlayerData~ playersData

        - Map~PlayerProfile, PlayArea~ playAreas
        - Set~Objective~ commonObjectives

        - int currentPlayer
        - Board board
    
        + Game(Iterable~PlayerProfiles~ playerProfiles)

        %% ASK: is it better to leave everything accessible and let the the controller handle it or to expose methods that perform various actions
        + getId() String
        + getPlayerProfiles() List~PlayerProfile~
        + getPlayerData(PlayerProfile pp) PlayerData
        + getPlayArea(PlayerProfile pp) PlayArea
        
        + getCurrentPlayer() PlayerProfile
    
        + getCommonObjectives() Set~Objective~
        
        + getStatus() GameStatus
        + startGame() void
        + pausedGame() void
        + resumeGame() void

        + join(PlayerProfile pp) PlayerData
        + selectStartingCardSide(PlayerProfile pp, Side s) void
        + selectColor(PlayerProfile pp, PlayerColor pc) SelectionResult
        + selectObjective(PlayerProfile pp, Objective o) void
        + drawCard(PlayerProfile pp, DrawSource ds) DrawResult
        + placeCard(PlayerProfile pp, Card c, Side s, int i, int j) void
    }

    class DrawResult {
        <<enumeration>>
        OK
        EMPTY
        ALL_EMPTY
    }

    class Board {
        %% cards facing up that any player can pick
        - Set~FaceUpCard~ faceUpResourceCards
        - Set~FaceUpCard~ faceUpGoldenCards

        %% decks of cards that the player can draw from
        - Deck resourceCardDeck
        - Deck goldenCardDeck

        + Board()
        + getFaceUpResourceCards() Set~Card~
        + getFaceUpGoldenCards() Set~Card~
        + getResourceCardDeck() Deck
        + getGoldenCardDeck() Deck
    }

    note for Deck "If a Deck is empty the players are forced to draw from the other deck.
    If both decks are empty the ending phase is triggered."
    Deck <|.. DrawSource : implements
    class Deck {
        - List~Card~ cards

        + Deck(Iterable~Card~ cards)

        + shuffle() void
        + isEmpty() boolean
        + draw() Optional~Card~
    }

    class GameStatus {
        <<enumeration>>
        AWAITING_PLAYERS
        SETUP_STARTING_CARD_SIDE
        SETUP_COLOR
        SETUP_OBJECTIVE
        PLAY_PLACING
        PLAY_DRAWING
        FINISHED
        SUSPENDED
    }
```

```mermaid
classDiagram
    class PlayerProfile {
        - String name
        + PlayerProfile(String name)

        %% TODO: how is this implemented?
        + isConnected() boolean
    }

    note for Choice "if !isResolved(), getSelected() throws NoSuchElementException"
    class Choice~T~ {
        - Set~T~ options
        - OptionalInt selection

        + Choice(Iterable~T~ choiches)

        + getOptions() Set~T~
        + select(T selection) void
        + hasSelected() boolean
        + getSelected() T
    }

    class MultichooserChoiceArbiter~T, I~ {
        - Set~T~ options
        - Map~I, T~ selections
        - boolean settled

        - List~MultichooserChoice~T~~ choosers

        + MultichooserChoiceArbiter(Iterable~T~ choiches, int choosers)
        + getChoice(I identity) MultichooserChoice~T~
    }

    MultichooserChoiceArbiter *-- MultichooserChoice : nested class
    class MultichooserChoice~T, I~ {
        - MultichooserChoiceArbiter parent
        - I identity

        - MultichooserChoice(MultichooserChoiceArbiter parent, I identity)

        + getOptions() List~T~
        
        + select(T choice) SelectionResult
        + hasSelected() boolean
        + getSelected() T
        + getContenders(T choice) Set~I~

        + isSettled() boolean
    }

    class SelectionResult {
        OK
        CONTENDED
    }

    class PlayerData {
        - List~Card~ hand
        - Choice~Side~ startingCardSideChoice
        - Choice~Objective~ objectiveChoice
        - MultichooserChoice~PlayerColor, PlayerProfile~ colorChoice

        + PlayerData(Card starterCard, Choice~Side~ startingCardSideChoice, Choice~Objective~ objectiveChoice, MultichooserChoice~PlayerColor, PlayerProfile~ colorChoice)

        + getHand() List~Card~
        + getObjectiveChoice() Choice~Objective~
        + getColorChoice() MultichooserChoice~PlayerColor, PlayerProfile~

        %% note: score is calculated in playArea
    }

    class PlayerColor {
        <<enumeration>>
        🔴 RED
        🟢 GREEN
        🔵 BLUE
        🟡 YELLOW
    }
```

## The play area

Each player's play area is modeled as a grid of `CardPlacement`s.

```mermaid
classDiagram
    Iterable~CardPlacement~ <|.. PlayArea : implements 
    class PlayArea {
        - Map~Position, CardPlacement~ cards
        - int score
        - int seq
        - Map~Collectible, Integer~ collectibleCount

        + PlayArea(Card starterCard, Side side)

        + placeAt(int i, int j, Card c, Side side) CardPlacement
        + getAt(int i, int j) Optional~CardPlacement~

        + getCollectibleCount() Map~Collectible, Integer~
        + getScore() int

        + iterator() Iterator~CardPlacement~
        + windows(int width, int height) WindowIterator
    }

    Iterator~Window~ <|.. WindowIterator
    class WindowIterator {
        - int offsetI
        - int offsetJ
        - int maxI
        - int maxJ
        - int w
        - int h

        + hasNext() boolean
        + next() Window
    }

    class Window {
        - PlayArea playArea
        - int offsetI
        - int offsetJ
        - int width
        - int height

        + width() int
        + height() int
        + getAt(int i, int j) Optional~CardPlacement~
    }

    Comparable~CardPlacement~ <|.. CardPlacement : implements
    PlayArea *-- CardPlacement : nested class
    class CardPlacement {
        - PlayArea playArea
        - int i
        - int j

        - Card card
        - Side side
        - int seq

        %% NOTE: only set at construction time
        - int points

        - CardPlacement(PlayArea playArea, int i, int j, Card card, Side side, int seq);
        %% calculates how many points does the player get because of this card placement
        %% NOTE: only called at construction time, because the result could vary based on the resource count on the play area
        - calculatePoints() int

        + getPlayArea() PlayArea
        + getI() int
        + getJ() int

        + getCard() Card
        + getSide() Side
        + getVisibleFace() CardFace
        + getPoints() int
        
        + getCollectibleAtCorner(CornerPosition cp) Optional~Collectible~
        + getRelative(CornerPosition cp) Optional~CardPlacement~
        + getCovered() Map~CornerPosition, CardPlacement~

        + compareTo(CardPlacement other) int
    }

    class DrawSource {
        <<interface>>
        + draw() Optional~Card~
    }

    DrawSource <|.. FaceUpCard : implements
    class FaceUpCard {
        - Optional~Card~ card
        - Deck source

        + FaceUpCard()
        + draw() Optional~Card~
        + getCard() Optional~Card~
    }

    class Position {
        - int i
        - int j

        + Position(int i, int j)
        + getI()
        + getJ()
    }

    class Side {
        <<enumeration>>
        FRONT
        BACK
    }
```

## Cards and their components

Every card has two faces: a front and a back.

Both sides may have:

- sockets placed at their corners
    - each socket may contain a resource
- a minimum amount of resources that are needed in order to be able to place the card
- some points that may be earned when the card is places
    - some cards give a fixed number of points
    - some cards give a number of points for each resource of a certain type on the `PlayArea`
    - some cards give a number of points for each other card that they are placed over

```mermaid
classDiagram
    class Card {
        - CardColor baseColor
        - boolean isStarter
        - boolean isGold
        - FrontCardFace front
        - BackCardFace back

        + color() CardColor
        + isStarter() boolean
        + isGold() boolean
        + getFace(Side s) CardFace

        %% TODO: add a way to get the corresponding image path
    }

    class CardColor {
        <<enumeration>>
        🔴 RED
        🟢 GREEN
        🔵 BLUE
        🟣 PURPLE

        %% ASK: keep neutral or add Optional<> inside Card ?
        ⚪ NEUTRAL
    }
```

```mermaid
classDiagram
    class CardFace {
        <<interface>>
        %% ASK: should we have Optional(s) or should we give "default" implementations for these things?
        %% ASK: and if we decide to give default implementations, should we add (boolean) methods to tell them apart?
        + corner(CornerPosition cornerPosition) Corner
        + getPointsPanel() Optional~PointsPanel~
        + getPlacementConstraint() Optional~PlacementConstraint~
        + getCenterResources() Map~Resource, Integer~
    }

    note for BaseCardFace "default implementation of
     - getPointsPanel()
     - getPlacementConstraint()
     returns Optional.empty()"
    note for BaseCardFace "default implementation of
     - getCenterResource()
     returns empty list"
    
    CardFace <|.. BaseCardFace : implements
    class BaseCardFace {
        <<abstract>>
        - Corner tl
        - Corner tr
        - Corner br
        - Corner bl

        + BaseCardFace(Corner tl, Corner tr, Corner br, Corner bl)
        + corner(CornerPosition cornerPosition) Corner
        + getPointsPanel() Optional~PointsPanel~
        + getPlacementConstraint() Optional~PlacementConstraint~
        + getCenterResources() Map~Resource, Integer~
    }

    note for BackCardFace "if it's not a starter card, getCenterResources() is a one element list"
    BaseCardFace <|-- BackCardFace
    class BackCardFace {
        - Map~Resource, Integer~ resources

        + BackCardFace(Corner tl, Corner tr, Corner br, Corner bl, Iterable~Resource~ resources)
        + getCenterResources() Map~Resource, Integer~
    }

    note for FrontCardFace "represents a resource card or a golden card"
    BaseCardFace <|-- FrontCardFace
    class FrontCardFace {
        - PlacementConstraint placementConstraint
        - Optional~PointsPanel~ pointsPanel

        + FrontCardFace(Corner tl, Corner tr, Corner br, Corner bl, lacementConstraint placementConstraint,  Optional~PointsPanel~ pointsPanel)
        + getPointsPanel() Optional~PointsPanel~
        + getPlacementConstraint() Optional~PlacementConstraint~
    }
```

```mermaid
classDiagram
    class CornerPosition {
        <<enumeration>>
        TOP_RIGHT
        TOP_LEFT
        BOTTOM_LEFT
        BOTTOM_RIGHT
    }
    
    note for Corner "!isSocket() ==> getCollectible().isEmpty()"
    class Corner {
        - socket boolean
        - Optional~Collectible~ collectible

        + missing()$ Corner
        + empty()$ Corner
        + filled(Collectible collectible)$ Corner

        - Corner(boolean isSocket, Optional~Collectible~)
        + isSocket() boolean
        + getCollectible() Optional~Collectible~
    }

    class Collectible {
        <<sealed interface>>
    }

    Collectible <|.. Resource : implements
    class Resource {
        <<enumeration>>
        🍃 PLANT
        🍄 FUNGI
        🦊 ANIMAL
        🦋 INSECT

        + getAssociatedColor() CardColor
    }

    Collectible <|.. Item : implements
    class Item {
        🪶 QUILL
        🫙 INKWELL
        📜 MANUSCRIPT
    }
```

Some cards cannot be placed until a certain number of resources are available on the `PlayArea`.

```mermaid
classDiagram
    class PlacementConstraint {
        - Map~Resource, Integer~ requiredResources

        + PlacementConstraint(Map~Resource, Integer~ requiredResources)
        + isSatisfied(PlayArea playArea) boolean
    }
```

Some cards give points when the player places them

```mermaid
classDiagram
    class PointsPanel {
        <<interface>>
        + calculateScoredPoints(CardPlacement cp) int
    }

    note for SimplePointsPanel "Is always met and points are always the same"
    PointsPanel <|.. SimplePointsPanel : implements
    class SimplePointsPanel {
        - int points
        + SimplePointsPanel(int points)
        + calculateScoredPoints(CardPlacement cp) int
    }

    note for CornerCoverPointsPanel "Gives points for every corner the is covered by the card that holds this header"
    PointsPanel <|.. CornerCoverPointsPanel : implements
    class CornerCoverPointsPanel {
        - int pointsPerCorner
        + CornerCoverPointsPanel(int pointsPerCorner)
        + calculateScoredPoints(CardPlacement cp) int
    }

    note for ItemPointsPanel "Gives points for every resource of a certain type that is owned by the player"
    PointsPanel <|.. ItemPointsPanel : implements
    class ItemPointsPanel {
        - Item item
        - int pointsPerItem
        + ItemPointsPanel(Item item, int pointsPerItem)
        + calculateScoredPoints(CardPlacement cp) int
    }
```

## Objectives

Objectives are calculated at the end of the game.
If the condition for the objective has been met, it gives points.

```mermaid
classDiagram
    note for Objective "test() returns a set containing all the sets of cards that satisfy the objective"
    class Objective {
        <<abstract>>
        - int points

        + Objective(int points)
        + getPointsPerMatch() int
        + test(PlayArea pa) Set~Set~CardPlacement~~
    }

    Objective <|-- CollectibleObjective
    class CollectibleObjective {
        - Map~Collectible, Integer~ requiredCollectibles
        + CollectibleObjective(int points, Map~Collectible, Integer~ requiredCollectibles)
        + test(PlayArea pa) Set~Set~CardPlacement~~
    }

    Objective <|-- PatternObjective
    note for PatternObjective "pattern is a 3x3 matrix representing the pattern with colors and nulls"
    class PatternObjective {
        - CardColor[][] pattern
        + PatternObjective(int points, CardColor[][] pattern)
        + test(PlayArea pa) Set~Set~CardPlacement~~
    }
```
