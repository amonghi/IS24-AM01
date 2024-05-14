module it.polimi.ingsw.am01 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.rmi;

    opens it.polimi.ingsw.am01 to javafx.fxml;
    opens it.polimi.ingsw.am01.model.card to com.google.gson;
    opens it.polimi.ingsw.am01.model.card.face to com.google.gson;
    opens it.polimi.ingsw.am01.model.card.face.corner to com.google.gson;
    opens it.polimi.ingsw.am01.model.card.face.placement to com.google.gson;
    opens it.polimi.ingsw.am01.model.card.face.points to com.google.gson;
    opens it.polimi.ingsw.am01.model.collectible to com.google.gson;
    opens it.polimi.ingsw.am01.model.objective to com.google.gson;
    opens it.polimi.ingsw.am01.model.game to com.google.gson;
    opens it.polimi.ingsw.am01.model.chat to com.google.gson;
    opens it.polimi.ingsw.am01.model.choice to com.google.gson;
    opens it.polimi.ingsw.am01.model.player to com.google.gson;
    opens it.polimi.ingsw.am01.network.message to com.google.gson;
    opens it.polimi.ingsw.am01.network.message.json to com.google.gson;
    opens it.polimi.ingsw.am01.network.message.s2c to com.google.gson;
    opens it.polimi.ingsw.am01.network.message.c2s to com.google.gson;
    opens it.polimi.ingsw.am01.model.exception to com.google.gson;
    opens it.polimi.ingsw.am01.eventemitter to com.google.gson;
    opens it.polimi.ingsw.am01.model.event to com.google.gson;
    opens it.polimi.ingsw.am01.controller to com.google.gson;

    exports it.polimi.ingsw.am01;
    exports it.polimi.ingsw.am01.network to java.rmi;
    exports it.polimi.ingsw.am01.network.rmi to java.rmi;
    exports it.polimi.ingsw.am01.network.rmi.server to java.rmi;
    exports it.polimi.ingsw.am01.network.rmi.client to java.rmi;
    exports it.polimi.ingsw.am01.network.message to java.rmi;
    exports it.polimi.ingsw.am01.network.message.c2s to java.rmi;
    exports it.polimi.ingsw.am01.network.message.s2c to java.rmi;
}
