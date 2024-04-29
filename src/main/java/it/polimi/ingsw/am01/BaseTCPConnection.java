package it.polimi.ingsw.am01;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.NetworkMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public abstract class BaseTCPConnection<S extends NetworkMessage, R extends NetworkMessage> implements Connection<S, R> {
    private static final Gson gson = new GsonBuilder()
            .create();
    private final Class<S> sendType;
    private final Class<R> receiveType;
    private final Scanner in;
    private final PrintWriter out;

    public BaseTCPConnection(Class<S> sendType, Class<R> receiveType, Socket socket) throws IOException {
        this.sendType = sendType;
        this.receiveType = receiveType;
        this.in = new Scanner(socket.getInputStream());
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void send(S message) {
        String json = gson.toJson(message, sendType);
        out.println(json);
    }

    @Override
    public R receive() {
        String json = in.nextLine();
        return gson.fromJson(json, receiveType);
    }
}
