# Progetto di Ingegneria del Software a.a. 2023-24

## Team AM01

Components:

- Dario Crosa
- Alessandro Del Fatti
- Matteo Garzone
- Matteo Gatti

## Repository map

- :file_folder: `deliverables`: artifacts to be turned in
- :file_folder: `src`: source code

## Advanced features

| Feature                      | Status  |
|------------------------------|:-------:|
| Concurrent games             | Planned |
| Persistence                  | Planned |
| Resilience to disconnections | Planned |
| Chat                         | Planned |

## Implementation progress

|        Emoji         | Meaning          |
|:--------------------:|------------------|
| :white_large_square: | Not completed    |
|    :construction:    | Work in progress |
|  :white_check_mark:  | Completed        |
|       `empty`        | N/A              |

### Card

|                                     | Class               |    Implemented     |     Documented     |       Tested       |
|-------------------------------------|---------------------|:------------------:|:------------------:|:------------------:|
|                                     | Card                | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| ![Enum](img/enum.svg)               | CardColor           | :white_check_mark: | :white_check_mark: |                    |
| ![Enum](img/enum.svg)               | Side                | :white_check_mark: | :white_check_mark: |                    |
| ![Interface](img/interface.svg)     | CardFace            | :white_check_mark: | :white_check_mark: |                    |
| ![Abstract class](img/abstract.svg) | BaseCardFace        | :white_check_mark: | :white_check_mark: |                    |
|                                     | BackCardFace        | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|                                     | FrontCardFace       | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|                                     | Corner              | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| ![Enum](img/enum.svg)               | CornerPosition      | :white_check_mark: | :white_check_mark: |                    |
|                                     | PlacementConstraint | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| ![Interface](img/interface.svg)     | Points              | :white_check_mark: | :white_check_mark: |                    |
|                                     | SimplePoints        | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|                                     | ItemPoints          | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|                                     | CornerCoverPoints   | :white_check_mark: | :white_check_mark: | :white_check_mark: |

### Chat

|                                     | Class            |    Implemented     |     Documented     |       Tested       |
|-------------------------------------|------------------|:------------------:|:------------------:|:------------------:|
|                                     | ChatManager      | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| ![Abstract class](img/abstract.svg) | Message          | :white_check_mark: | :white_check_mark: |                    |
|                                     | DirectMessage    | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|                                     | BroadcastMessage | :white_check_mark: | :white_check_mark: | :white_check_mark: |

### Choice

|                       | Class           |    Implemented     |      Documented      |       Tested       |
|-----------------------|-----------------|:------------------:|:--------------------:|:------------------:|
|                       | Choice          | :white_check_mark: |  :white_check_mark:  | :white_check_mark: |
|                       | MultiChoice     | :white_check_mark: | :white_large_square: | :white_check_mark: |
| ![Enum](img/enum.svg) | SelectionResult | :white_check_mark: | :white_large_square: |                    |

### Collectible

|                                 | Class       |    Implemented     |     Documented     | Tested |
|---------------------------------|-------------|:------------------:|:------------------:|:------:|
| ![Interface](img/interface.svg) | Collectible | :white_check_mark: | :white_check_mark: |        |
| ![Enum](img/enum.svg)           | Item        | :white_check_mark: | :white_check_mark: |        |
| ![Enum](img/enum.svg)           | Resource    | :white_check_mark: | :white_check_mark: |        |

### Objective

|                                     | Class                         |    Implemented     |     Documented     |       Tested       |
|-------------------------------------|-------------------------------|:------------------:|:------------------:|:------------------:|
| ![Abstract class](img/abstract.svg) | Objective                     | :white_check_mark: | :white_check_mark: |                    |
|                                     | SameCollectibleObjective      | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|                                     | DifferentCollectibleObjective | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|                                     | PatternObjective              | :white_check_mark: | :white_check_mark: | :white_check_mark: |

### Player

|                       | Class         |      Implemented      |     Documented     |       Tested       |
|-----------------------|---------------|:---------------------:|:------------------:|:------------------:|
|                       | PlayerProfile |  :white_check_mark:   | :white_check_mark: | :white_check_mark: |
|                       | PlayerData    |  :white_check_mark:   | :white_check_mark: | :white_check_mark: |
| ![Enum](img/enum.svg) | PlayerColor   |  :white_check_mark:   | :white_check_mark: |                    |

### Game

|                                 | Class       |    Implemented     |     Documented     |       Tested       |
|---------------------------------|-------------|:------------------:|:------------------:|:------------------:|
|                                 | Game        | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| ![Enum](img/enum.svg)           | GameStatus  | :white_check_mark: | :white_check_mark: |                    |
| ![Enum](img/enum.svg)           | TurnPhase   | :white_check_mark: | :white_check_mark: |                    |
|                                 | GameManager | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|                                 | PlayArea    | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|                                 | Board       | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|                                 | Deck        | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|                                 | FaceUpCard  | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| ![Interface](img/interface.svg) | DrawSource  | :white_check_mark: | :white_check_mark: |                    |
| ![Enum](img/enum.svg)           | DrawResult  | :white_check_mark: | :white_check_mark: |                    |
|                                 | GameAssets  | :white_check_mark: | :white_check_mark: | :white_check_mark: |
