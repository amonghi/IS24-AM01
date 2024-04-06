# Progetto di Ingegneria del Software a.a. 2023-24

## Team AM01

Components:

- Dario Crosa
- Alessandro Del Fatti
- Matteo Garzone
- Matteo Gatti

## Implementation progress

|    | Meaning          |
|:--:|------------------|
| ✅  | Completed        |
| ❌  | Not completed    |
| 🚧 | Work in progress |
| ⬛  | N/A              |

### Card

|                        | Class               | Implemented | Documented | Tested |
|------------------------|---------------------|:-----------:|:----------:|:------:|
|                        | Card                |      ✅      |     ✅      |   ✅    |
| ![](img/enum.svg)      | CardColor           |      ✅      |     ✅      |   ⬛    |
| ![](img/enum.svg)      | Side                |      ✅      |     ✅      |   ⬛    |
| ![](img/interface.svg) | CardFace            |      ✅      |     ❌      |   ⬛    |
| ![](img/abstract.svg)  | BaseCardFace        |      ✅      |     ✅      |   ⬛    |
|                        | BackCardFace        |      ✅      |     ✅      |   ✅    |
|                        | FrontCardFace       |      ✅      |     ✅      |   ✅    |
|                        | Corner              |      ✅      |     ✅      |   ✅    |
| ![](img/enum.svg)      | CornerPosition      |      ✅      |     ✅      |   ⬛    |
|                        | PlacementConstraint |      ✅      |     ✅      |   ✅    |
| ![](img/interface.svg) | Points              |      ✅      |     ✅      |   ⬛    |
|                        | SimplePoints        |      ✅      |     ✅      |   ✅    |
|                        | ItemPoints          |      ✅      |     ✅      |   ✅    |
|                        | CornerCoverPoints   |      ✅      |     ✅      |   ✅    |

### Chat

|                       | Class            | Implemented | Documented | Tested |
|-----------------------|------------------|:-----------:|:----------:|:------:|
|                       | Chat             |      ✅      |     ❌      |   ❌    |
| ![](img/abstract.svg) | Message          |      ✅      |     ❌      |   ⬛    |
|                       | DirectMessage    |      ✅      |     ❌      |   ❌    |
|                       | BroadcastMessage |      ✅      |     ❌      |   ❌    |

### Choice

|                   | Class           | Implemented | Documented | Tested |
|-------------------|-----------------|:-----------:|:----------:|:------:|
|                   | Choice          |      ✅      |     ✅      |   ✅    |
|                   | MultiChoice     |      ✅      |     ❌      |   ✅    |
| ![](img/enum.svg) | SelectionResult |      ✅      |     ❌      |   ⬛    |

### Collectible

|                        | Class       | Implemented | Documented | Tested |
|------------------------|-------------|:-----------:|:----------:|:------:|
| ![](img/interface.svg) | Collectible |      ✅      |     ✅      |   ⬛    |
| ![](img/enum.svg)      | Item        |      ✅      |     ✅      |   ⬛    |
| ![](img/enum.svg)      | Resource    |      ✅      |     ✅      |   ⬛    |

### Objective

|                       | Class                         | Implemented | Documented | Tested |
|-----------------------|-------------------------------|:-----------:|:----------:|:------:|
| ![](img/abstract.svg) | Objective                     |      ✅      |     ✅      |   ⬛    |
|                       | SameCollectibleObjective      |      ✅      |     ✅      |   ✅    |
|                       | DifferentCollectibleObjective |      ✅      |     ✅      |   ✅    |
|                       | PatternObjective              |     🚧      |     ✅      |   ✅    |

### Player

|                   | Class         | Implemented | Documented | Tested |
|-------------------|---------------|:-----------:|:----------:|:------:|
|                   | PlayerProfile |     🚧      |     ✅      |   ❌    |
|                   | PlayerData    |      ✅      |     ❌      |   ❌    |
| ![](img/enum.svg) | PayerColor    |      ✅      |     ✅      |   ⬛    |

### Game

|                        | Class       | Implemented | Documented | Tested |
|------------------------|-------------|:-----------:|:----------:|:------:|
|                        | Game        |     🚧      |     ❌      |   ❌    |
|                        | GameManager |      ❌      |     ❌      |   ❌    |
| ![](img/enum.svg)      | GameStatus  |      ✅      |     ❌      |   ⬛    |
|                        | PlayArea    |      ✅      |     ✅      |   ✅    |
|                        | Board       |      ✅      |     ✅      |   ❌    |
|                        | Deck        |      ✅      |     ✅      |   ✅    |
|                        | FaceUpCard  |      ✅      |     ✅      |   ✅    |
| ![](img/interface.svg) | DrawSource  |      ✅      |     ❌      |   ⬛    |
| ![](img/enum.svg)      | DrawResult  |      ✅      |     ❌      |   ⬛    |
|                        | GameAssets  |      ✅      |     ❌      |   ✅    |
