# Peer-Review 2: Sequence diagrams

Dario Crosa, Alessandro Del Fatti, Matteo Garzone, Matteo Gatti

Gruppo AM01

Valutazione del diagramma UML sequence diagram del gruppo AM10.

## Lati positivi

Abbiamo trovato molto carina l'idea di permettere all'utente di ritrarre la carta piazzata se non ha ancora pescato.
Anche se questo aspetto in realtà non è specificato nelle regole, crediamo che sia un'aggiunta che migliora l'esperienza
di gioco.

## Lati negativi

### Correttezza del client

Secondo noi, il problema più grave è racchiuso in questa frase:

> Il client è a conoscenza delle mosse valide grazie a variabili interne ad esso, per cui non è necessario gestire gli
> errori in caso di mosse errate.

Infatti il server non può presumere che il client sia corretto, quindi bisogna gestire i casi in cui questo tenti di
eseguire operazioni "illegali", come ad esempio

- operazioni svolte nel momento sbagliato della partita
- operazioni con parametri non corretti

Magari modellare tutti questi casi nei digrammi può risultare eccessivo, ma devono comunque essere presi in
considerazione quando si progetta l'architettura del server, che deve fare opportuni controlli per intercettarli.
Inoltre, anche da un punto di vista della sicurezza, non verificare che le azioni del client sono valide prima di
eseguirle potrebbe permettere a un client "malevolo" di aggirare le regole del gioco.

### Riconnessione del client

Il secondo aspetto negativo che abbiamo riscontrato riguarda la fase di riconnessione: il protocollo prevede che sia il
client a informare il server che si sta riconnettendo in seguito a una precedente disconnessione.
Per fare ciò è necessario che il client abbia un meccanismo che gli permetta di rendersi conto di questa condizione, che
è facile da implementare per il caso in cui il client si disconnette e riconnette senza terminare il proprio
processo; lo è di meno se il client crasha.
Pensiamo che sarebbe più semplice se fosse il server a tenere traccia di questa informazione per poi informare il
client in seguito alla sua riconnessione.

Inoltre, voi dite

> Il server risponde inviando un messaggio "YouCanJoinMTC", aggiornando il client su tutti i cambiamenti avvenuti
> durante la sua assenza.

Questo presuppone che

- il client si ricordi come stavano le cose prima della sua disconnessione
- il server tenga traccia di tutto ciò che avviene durante l'assenza del client

A questo punto non conviene inviare al client tutto lo stato della partita nel momento in cui si ricollega? Così non
serve tenere traccia dei cambiamenti nè lato client nè lato server.

### Fase di setup

Abbiamo visto che è stata modellata la scelta dell'obbiettivo segreto, tuttavia mancano la scelta del colore della
pedina e della faccia su cui piazzare la carta iniziale (fronte/retro).

### Oggetti sul campo di gioco

Inoltre, non essendo specificato il contenuto dei vari messaggi, non ci è del tutto chiaro quali informazioni dovrebbe
trasmettere `LoadSetupMTC`; noi abbiamo speculato che siano:

- mani iniziali dei giocatori
- carte posizionate a faccia in su
- obiettivi comuni

Queste informazioni sono sufficienti a rappresentare lo stato iniziale del tavolo, tuttavia queste andranno anche
aggiornate mano a mano che questo stato evolve.
Per esempio, quando un giocatore pesca una delle carte a faccia in su, tutti i giocatori dovrebbero conoscere quale
carta andrà a rimpiazzare quella pescata.
O ancora: se pesco e successivamente un deck risulta vuoto, è necessario informare tutti i giocatori.

Abbiamo visto che c'è il messaggio `UpdateMTC` che viene usato per aggiornare lo stato della board dopo il turno del
giocatore; però il giocatore stesso che ha fatto la mossa non lo riceve e quindi non potrebbe sapere in che stato ha
lasciato la board.

### End of turn inutile

Il client dopo aver pescato informa il server che ha finito il turno, ma questo è superfluo poiché non potrebbe fare
nient'altro da regolamento.

### Uso improprio di `alt`

Questa è proprio una piccolezza, però per denotare una interazione che potrebbe avvenire opzionalmente sarebbe più
corretto utilizzare il blocco `opt` piuttosto che il blocco `alt` (che andrebbe utilizzato per mostrare diverse
alternative possibili per una stessa interazione).

## Confronto tra le architetture

La differenza principale del nostro diagramma rispetto a quello revisionato è che nel nostro sono stati modellati anche
i casi in cui il client cerchi di compiere azioni illecite.

Inoltre, noi ci siamo attenuti strettamente alle regole e quindi non abbiamo permesso al client di rimuovere una carta
precedentemente piazzata sotto nessuna condizione. Questa procedura potrebbe comunque tornare utile nel caso in cui si
verifichino dei casi
di [deadlock](https://ingsoft2024.slack.com/archives/C06KECZG8Q5/p1713772362855749?thread_ts=1713392351.610069&cid=C06KECZG8Q5).

Oppure quando il client perde la connessione a seguito del piazzamento, ma prima di aver pescato (anche se in questo
caso non dovrebbe essere il client a richiedere l'annullamento della mossa ma bensì il server).
