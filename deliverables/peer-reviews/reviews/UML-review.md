# Peer-Review 1: UML

Dario Crosa, Alessandro Del Fatti, Matteo Garzone, Matteo Gatti

Gruppo AM01

Valutazione del diagramma UML delle classi del gruppo AM10.

## Comunicazioni con l'altro gruppo

Ad una prima lettura dell'UML abbiamo riscontrato varie problematiche:

- non sono rispettate le convenzioni del progetto:
  - ci sono nomi di attributi e metodi in italiano
- non sono rispettate le convenzioni di Java:
  - i nomi di molti attributi e metodi non sono in camelCase
- non è rispettata la sintassi UML
  - mancano gli "access modifiers" degli attributi e di alcuni metodi.
  - ci sono frecce del tipo "implementazione" che partono da package e raggiungono "classi".
  - le suddette "classi" in realtà potrebbero non essere classi (leggere la mail sotto).
- non è rispettata la sintassi Java
  - ci sono svariati errori sulla definizione dei tipi di attributi, come ad esempio gli Enum CardColor e CardType (nella classe PlayableCard)
  - mancano i costruttori
  - ci sono svariati metodi setter senza parametro
  - ci sono vari metodi/attributi che utilizzano tipi che non sono dichiarati da nessuna parte, anche se questi hanno nomi abbastanza simili ad altre classi presenti nell'UML (probabilmente non sono stati aggiornati i riferimenti a classi modificate/cancellate).

Inoltre la mancanza di frecce tra le classi rende difficile capire quali siano le relazioni tra di esse.

A causa di questi fattori ci è stato parecchio difficile comprendere il funzionamento del model che l'altro gruppo ha immaginato.
Per questa ragione abbiamo deciso di porre loro alcune domande tramite email (inviata `ven 29/03/2024 15:12`):

```
Ciao,
abbiamo riscontrato alcuni dubbi in merito all'uml.
Il testo che avete fornito ci ha chiarito diversi aspetti tuttavia vorremmo
farvi alcune domande sia di carattere generale sia su alcune specifiche classi.

Per prima cosa, non abbiamo capito se ModelSubject e ModelObserver siano delle
classi oppure una qualche sorta di classificazione. Il dubbio ci sorge perché:
• non hanno nessun attributo o metodo
• ci sono delle frecce che partono da alcuni package (board, cards, ...) e
    arrivano in ModelSubject, questo ci ha fatto pensare che forse le frecce
    non rappresentano una relazione di ereditarietà, ma a questo punto non
    sappiamo che cosa significhino
• il fatto che ModelObserver venga utilizzato come tipo per un attributo di
    GameModel ci fa invece pensare che potrebbe effettivamente trattarsi di
    classi.  Anche in questo caso però il significato delle frecce è da
    chiarire.

Inoltre, non comprendiamo quale sia esattamente il ruolo di alcune delle classi
che estendono EndOfTurnObserver:
• ScoreBoardRefresher: immaginiamo che questo si occupi di aggiornare la
    posizione dei giocatori all'interno della ScoreBoard, se è così dateci
    conferma.
• PlayerBoardRefresher: sicuramente aggiorna qualcosa all'interno della
    playerBoard, ma non abbiamo capito cosa.

Infine, abbiamo alcune domande inerenti a specifici attributi/metodi di alcune classi:
• classe GameModel
• Board non esiste, intendevate ScoreBoard?
• getDrawableCards() che carte mi dà? solo quelle scoperte o anche quelle in
    cima ai mazzi (coperte)?
• perchè getBoard restituisce una arrayList<PlayableCard>? Non dovrebbe essere
    una HashMap come in tutti gli altri getBoard()?
• classe ObjectiveCard
• Board non esiste, intendevate ScoreBoard?
• classe PlayerBoard
• la classe Point non esiste. Intendevate java.awt.Point? Oppure avete pensate
    che fosse così ovvia da non includerla?
• non capiamo in quale contesto andrebbe usato il metodo LoadBoard(). La nostra
    interpretazione è che "carica", nel senso che piazza in una volta sola, un
    insieme di carte nella PlayerBoard, però non abbiamo comunque capito in
    quale contesto sarebbe utile questo metodo.

Come gruppo ci rendiamo conto che siano tante domande e che le vacanze di
Pasqua sono vicine (in realtà già iniziate) quindi probabilmente non
riuscirete a fornirci tutte le risposte subito. Però vi chiediamo di non
aspettare di avere tutte le risposte alle domande per rispondere alla mail,
piuttosto mandateci anche delle risposte parziali così possiamo iniziare a
metterci al lavoro.

Buona giornata,
Dario Crosa
```

In realtà questa email non contiene tutte le domande che sarebbero state
necessarie per chiarire completamente i dubbi di questo UML, ma abbiamo
deciso di limitarci all'essenziale in quanto l'email già così è parecchio corposa.

Di seguito la loro risposta (ricevuta `ven 29/03/2024 15:46`):

```
Sì, scusami, non abbiamo chiarito.
Gli observers sono delle classi che ancora non abbiamo curato ma che si
occuperanno di aggiornare la view ad ogni cambiamento rilevante del model.
Ad esempio, alla fine di un turno vengono aggiornati i punteggi e gli observers
si occupano per l'appunto di notificare questo aggiornamento alla view.
Al momento non abbiamo ancora pensato alla View, quindi è solo un concetto.

Board non esiste ma era una superclasse di PlayerBoard e ScoreBoard.
Ci siamo dimenticati di aggiornale le funzioni dopo averla rimossa, scusate.
Similmente, GetBoard restituisce un'ArrayList perchè abbiamo successivamente
deciso di usare una HashMap per la Board e ci siamo dimenticati di correggere
il metodo.

getDrawableCards ritorna le carte a faccia in sù.

Per Point intendiamo la classe standard di Java. Ha tutto ciò di cui abbiamo
bisogno per gestire una coordinata in un piano 2D.

LoadBoard serve per visualizzare la Board di un altro giocatore. L'idea era di
caricare l'hashmap di un altro giocatore e visualizzarla (probabilmente manca
una variabile per tenere l'Hashmap caricata). Cambieremo il nome della funzione
con uno più esplicativo.
```

Dopo qualche giorno, abbiamo sollecitato ulteriormente (mail inviata `mar 02/04/2024 18:25`)

```
Ciao,
vorremmo sollecitare una vostra risposta in merito alla prima domanda:
ModelSubject e ModelObservers alla fine sono delle classi? E le frecce
tratteggiate che cosa significano precisamente?

Inoltre, non abbiamo capito la spiegazione che ci avete dato in merito al
metodo LoadBoard: non capiamo perché sarebbe necessario caricare la HashMap di
un altro giocatore per poterla visualizzare. Alla fine non è il model che si
occupa di visualizzare.

Grazie.
```

Il gruppo valutato ha risposto (`mar 02/04/2024 21:21`)

```
Ciao,
l’idea era di tenere tutti i dati sul server e di tenere lo stretto necessario
sul client. LoadBoard dovrebbe prendere i dati da caricare a livello di view
dal server visto che altrimenti la view non ha i dati necessari per
visualizzarla. Credete che ci siano metodi migliori o che non sia necessario
mettere la funzione nel model?

ModelSubject e ModelObservers probabilmente saranno classi. Serviranno per
l’interazione fra model view e non sono rilevanti per la logica di gioco quindi
non credo sia necessario valutarle in questa peer review.

Grazie mille per l’interesse,
buona serata.
```

## Lati positivi

Abbiamo riscontrato che tutti i concetti presenti nel gioco sono stati presi in considerazione e modellizzati. Non ci sono particolari mancanze riguardanti la gestione della logica del gioco.

Inoltre, abbiamo apprezzato l'idea di predisporre un sistema di Observers, nonostante non sia stato ancora modellato completamente, come spiegato nella email di risposta.

Infine, è presente un'organizzazione delle classi in package in base alla loro funzione che ne semplifica la lettura.

## Lati negativi

Abbiamo deciso di separare le problematiche generali da quelle specifiche di ciascuna classe.

### Problemi generali

#### Parti di UML non completamente specificate

Ci sono metodi "vaghi", ovvero che non prendono nessun parametro e non restituiscono nulla, probabilmente per delle dimenticanze.

Nessun attributo ha un access modifier. Inoltre, i metodi per i quali è stato specificato l'access modifier risultano essere tutti pubblici. Questo ci fa sospettare che in realtà questa cosa sia stata fatta "a tappeto".

Ci sono poi alcune situazioni in cui è presente un attributo che dovrebbe avere un metodo getter (o anche un setter) ma non ce l'ha (es: attributo punti nella classe `Face`).
Ma anche l'opposto: metodi getter/setter che però non hanno dati da cui attingere.

#### Classi `ModelSubject` e `ModelObservers`

Come si può leggere dalle email, le due "classi" `ModelSubject` e `ModelObservers` ci hanno creato dubbi fin da subito. Sopratutto perchè non capivamo se fossero delle classi oppure una sorta di nota oppure una classificazione delle altre classi.

Dato che neanche il gruppo valutato ha chiaro se queste siano classi o meno, considereremo entrambe le possibilità:

- se fossero classi: ci sarebbero due superclassi da cui ogni classe dell'UML deriva. Questa non è una buona pratica, soprattutto considerando che queste due classi sono prive di attributi e metodi.
- se non fossero classi: può avere senso, da un punto di vista concettuale, distinguere le classi che fanno parte del model da quelle che lo osservano. Ma a questo punto avrebbe più senso utilizzare dei package e magari aggiungere una nota per chiarire.

Come ultima considerazione: gli observer implementano già un'interfaccia comune (`EndOfTurnObserver`), quindi non c'è bisogno della superclasse `ModelObservers`.

Tuttalpiù, se si volessero aggiungere altri observer che non osservano solo la fine del turno

- si potrebbe creare un'altra interfaccia `Observer` che tutti gli observer implementano
- oppure si potrebbe rinominare `EndOfTurnObserver` in `Observer` e far implementare quella interfaccia a tutti gli observers

### Classe `GameModel`

##### Stato del gioco

È presente un metodo `GameState getGameState()` ma non è chiaro da dove derivi l'informazione sullo stato corrente in quanto non è presente nessun attributo di tipo `GameState`.

Inoltre è presente un metodo `void updateGameState()` che non prende parametri e non si capisce in base a quale logica decida di aggiornare lo stato.

##### Metodo `shuffle()`

Ci sono due metodi shuffle:

- `void shuffle(ArrayList<PlayableCard> deck)`
- `void shuffle(ArrayList<ObjectiveCard> deck)`

Per prima cosa, non ha senso che i mazzi vengano passati come parametri in quanto sono memorizzati negli attributi:

- `ArrayList<playableCard> deckGoldCards`
- `ArrayList<playebleCard> deckResourceCards`
- `ArrayList<objectiveCard> deckObjectiveCards`
- `ArrayList<playablecard> firstCards`

Come seconda cosa, l'operazione di shuffle non dipende dal tipo degli elementi nell'array, potremmo dire che è un'operazione _generica_ sul tipo degli elementi nell'array. Quindi:

- Se volete mantenere il metodo `shuffle` nella classe `GameModel` potreste rendere shuffle generico: `<T> void shuffle(ArrayList<T> deck)`.

- Altrimenti potreste creare una classe `Deck<T>` di carte, la quale possiede il metodo shuffle, e poi sostituire i vari `ArrayList` con dei `Deck`.

- Altrimenti ancora, potreste invocare `Collections.shuffle()` sugli array quando li volete mescolare, anche se questa ultima soluzione ricorda uno stile di programmazione imperativo piuttosto che ad oggetti.

##### Metodi vaghi

Come già detto nella [sezione sui problemi generali](#problemi-generali) ci sono metodi il cui scopo è intuibile dal nome, ma non il loro utilizzo/funzionamento:

- `void addPlayer()`: non è chiaro da dove provenga il player da aggiungere
- `void firstTurn()`: possiamo supporre che questo metodo gestisca il primo turno del gioco, quello che non possiamo supporre è come lo faccia
- `void placeCard(String nickname)`: non è specificato dove piazzare la carta. Inoltre, non sarebbe più semplice passare un riferimento al giocatore, piuttosto che al suo nickname?
- `void setSecretObjectives()`: non è chiaro per quale giocatore venga impostato l'obiettivo. E tra l'altro, l'obiettivo non è nemmeno specificato
- `void setCommonObjectives()`: stessa cosa del metodo sopra.

##### Observers

La classe `GameModel` ha una lista di Observers, però manca un metodo per registrarli.

### package `Cards`

#### Enum `CardType`

Questo enum viene utilizzato nella classe `PlayableCard` al fine di identificare il tipo della carta, mentre le carte obiettivo sono modellizzate dalla classe `ObjectiveCard`.

Abbiamo notato però che tra i diversi `CardType` è contemplato anche `OBJECTIVECARD` nonostante il concetto di carta giocabile e carta obbiettivo sia già rappresentato da classi diverse.

#### Classe `Face`

È presente un attributo `int points`, ma non c'è un metodo per ottenere il punteggio, ipotizziamo che sia stato dimenticato.

Nella classe è definito il metodo `getFaceResource`. Non si capisce a quali risorse faccia riferimento: alle risorse centrali sul retro delle carte, a quelle sugli angoli o entrambe? Inoltre, non è chiaro se questo metodo debba fornire una lista "statica" delle risorse stampate sulla carta (intesa come pezzetto di cartoncino) oppure una lista "dinamica" delle risorse visibili (non coperte da altre carte piazzate).

#### Classe `ObjectiveCard`

È presente un attributo del tipo `Objectives` che permette di distinguere la tipologia di obbiettivo, ma ciò non è sufficiente per descrivere completamente la carta obiettivo.
Per esempio, se so che una carta obiettivo è di tipo `SCHEME` come faccio a sapere esattamente quale dei vari pattern viene rappresentato? (tre carte in diagonale, ...)

Inoltre, una carta obbiettivo che richiede delle risorse avrà degli attributi differenti da una che richiede di "costruire" un pattern. Forse sarebbe meglio utilizzare diverse classi per rappresentare le diverse tipologie di obbiettivi.

#### Enum `Objectives`

Questo enum viene utilizzato in due classi: `Face` e `ObjectiveCard`, in entrambi i casi viene utilizzato per distinguere le diverse tipologie di carte (diversi tipi di carta giocabile nel primo caso e diversi tipi di carta obbiettivo nel secondo caso).
Sarebbe meglio utilizzare due enum differenti in modo da non permettere la creazione di carte "invalide", come ad esempio una carta obbiettivo di tipo `CORNER`.

#### Classe `ScoreBoard`

Secondo noi questa classe potrebbe essere rimossa.
Questo perchè il punteggio di ciascun giocatore è già memorizzato nella classe `Player`, il che rende questa classe ridondante.

Inoltre, nel modo in cui è stata progettata, non è per nulla efficiente. Bisognerebbe allocare un array di 30 posizioni (o anche di più, perchè sono possibili punteggi maggiori) per poi non riempirne mai più di quattro.

Se volete mantenerla, però, potreste usare una hashmap.

#### Classe `PlayerBoard`

##### Uso della classe `Point`

La classe `Point` appartiene al package `java.awt`.
Sebbene questa classe faccia parte della libreria standard di Java, e rappresenti un punto 2D con tutti gli attributi necessari per l'uso che ne viene fatto, secondo noi sarebbe comunque meglio implementare una classe `Point` ad-hoc.
Questo perchè il pacchetto `java.awt` contiene appunto classi della libreria grafica AWT che però non viene utilizzata.

##### Attributo `placedCards`

L'attributo `placedCards` è una `HashMap<Point, PlayableCard>`, non dovrebbe essere una `HashMap<Point, PlacedCard>` visto che la playerboard è il campo da gioco? Crediamo che questa possa essere una semplice svista tuttavia lo segnaliamo lo stesso.

##### Attributo `playablePosiions`

L'attributo `playablePositions` è una `HashMap<Point, boolean>`, questo significa che:

- o vengono messe nella mappa tutte le possibili posizioni del campo da gioco (che sono finite solo perchè il numero di carte giocabili è finto)
- o vengono messe nella mappa solo quelle posizioni che sono effettivamente giocabili, ma a questo punto il valore associato sarà sempre `true`.

Quindi conviene utilizzare un `Set<Point>`: se una posizione è nel set allora è giocabile, se una posizione non è nel set allora non è giocabile.

##### Metodo `getBoard()`

Il metodo `HashMap<Point, PlayableCard> getBoard()` espone la struttura interna della classe, creando così un problema di incapsulamento.
Con una struttura del genere una classe utilizzatrice di `PlayerBoard` potrebbe mutare la mappa che registra i piazzamenti anche in maniera non valida, bypassando gli eventuali check presenti in `placeNewCard()`.

##### Metodo `loadBoard()`

Dalla seconda email di risposta, si può evincere che questo metodo verrà utilizzato dalla view per caricare i dati provenienti dal model. Consigliamo quindi di spostarlo all'interno della view.

#### Classe `PlacedCard`

Il metodo `ArrayList<CornerPosition> getFreeCorners()` come fa a produrre la lista di angoli liberi se non ha parametri e non c'è un riferimento alla classe `PlayerBoard` all'interno di `PlacedCard`?

### Package `player`

#### Classe `Player`

Secondo noi ha poco senso avere un metodo `setNickname()` in quanto i giocatori non possono cambiare nome, il nome lo metteremmo piuttosto come parametro del costruttore.

Inoltre, non avrebbe più senso che `getBoard()` restituisca una classe PlayerBoard, invece di una mappa?

Infine, l'attributo `ArrayList<ObjectiveCard> objectives` contiene una lista di obbiettivi, i quali poi si differenziano tra obbiettivi segreti e obbiettivi comuni tramite il `boolean secret` nella classe `ObjectiveCard`.
Tuttavia, visto che gli obbiettivi comuni sono appunto comuni a tutti i giocatori della partita, non avrebbe più senso associare queste carte al `GameModel`?

Non c'è un modo per definire il colore scelto dal giocatore. Sarebbe utile avere un enum apposito.

#### Classe `Inventory`

Piuttosto che avere tutte queste variabili simili tra loro, questa classe potrebbe basarsi un una `Map<Resource, Integer>`

## Confronto tra le architetture

Osservando l'UML dell'altro gruppo ci siamo resi conto di aver tralasciato alcuni aspetti, come:

- La gestione della fine del gioco, in particolare non abbiamo inserito un modo per dichiarare il vincitore
- Nella nostra classe Game c'era un attributo che indicava il numero massimo di giocatori, che è utile quando i giocatori non si sono ancora connessi tutti.
  Tuttavia in seguito a varie modifiche questo attributo è stato cancellato e ce ne eravamo dimenticati.
  Rileggendo l'UML dell'altro gruppo ce ne siamo ricordati.
- Infine abbiamo notato il metodo che calcolo al lista delle posizioni giocabili, probabilmente potrebbe esserci utile quando implementeremo il client.
