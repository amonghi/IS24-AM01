# Progetto di Ingegneria del Software a.a. 2023-24

## Team AM01

Components:

- Dario Crosa
- Alessandro Del Fatti
- Matteo Garzone
- Matteo Gatti

## Repository map

- :file_folder: `deliverables`
    - :file_folder: `javadocs`
    - :file_folder: `UML`
        - :file_folder: `high-level`
        - :file_folder: `detail` (auto generated)
    - :file_folder: `sequence-diagrams`
    - :file_folder: `peer-reviews`
        - :file_folder: `delivered-for-reviews`
            - :file_folder: `UML`
            - :file_folder: `sequence-diagram`
        - :file_folder: `reviews`
            - :page_facing_up: `UML-review.md`
            - :page_facing_up: `sequence-diagram-review.md`
- :file_folder: `docs`
    - :page_facing_up: `conventions.md`: conventions that we decided to follow for the development of this project
- :file_folder: `src`: source code, unit tests and resources
- :page_facing_up: `tui.sh` and :page_facing_up: `tui.bat`: helper scripts, made to aid in the development of the TUI

## Functionalities

| Feature        |   Status    |
|----------------|:-----------:|
| Basic rules    | Implemented |
| Advanced rules | Implemented |
| Socket         | Implemented |
| RMI            | Implemented |
| TUI            | Implemented |
| GUI            | Implemented |

## Advanced features

| Feature                      |   Status    |
|------------------------------|:-----------:|
| Concurrent games             | Implemented |
| Persistence                  | Implemented |
| Resilience to disconnections | Implemented |
| Chat                         | Implemented |

## How to run the application

### Server

To run the server, you can use the following command:

```shell
java -jar AM01-1.0-SNAPSHOT-server.jar <TCP_PORT> <RMI_PORT> 
```

Default ports: `8888` for TCP and `7777` for RMI.

### Client

#### GUI

To run the client with the GUI, you can use the following command:

```shell
java -jar AM01-1.0-SNAPSHOT-client-gui.jar
```

#### TUI

##### Unix

To run the client with the TUI on Unix systems, you can use the following command:

```shell
java -jar AM01-1.0-SNAPSHOT-client-tui.jar
```

##### Windows

To run the client with the TUI on Windows systems, it is required to enable Unicode support in the terminal.
To do that:

1. Use the [Windows Terminal](https://apps.microsoft.com/detail/9n0dx20hk701)
2. Run the following command: `chcp 65001`

Then you can run the client with the TUI using the following command:

```shell
java -jar AM01-1.0-SNAPSHOT-client-tui.jar
```

#### MacOS

MacOS is not supported for the TUI client.