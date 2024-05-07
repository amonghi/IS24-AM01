package it.polimi.ingsw.am01.network.tcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.ReceiveNetworkException;
import it.polimi.ingsw.am01.network.SendNetworkException;
import it.polimi.ingsw.am01.network.message.NetworkMessage;
import it.polimi.ingsw.am01.network.message.json.NetworkMessageTypeAdapterFactory;

import java.io.*;
import java.net.Socket;

public abstract class BaseTCPConnection<S extends NetworkMessage, R extends NetworkMessage> implements Connection<S, R> {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new NetworkMessageTypeAdapterFactory())
            .create();
    private final Class<S> sendType;
    private final Class<R> receiveType;
    protected final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    public BaseTCPConnection(Class<S> sendType, Class<R> receiveType, Socket socket) throws IOException {
        this.sendType = sendType;
        this.receiveType = receiveType;
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void send(S message) throws SendNetworkException {
        try {
            String json = gson.toJson(message, sendType);
            out.write(json);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            throw new SendNetworkException(e);
        }
    }

    @Override
    public R receive() throws ReceiveNetworkException {
        try {
            String json = in.readLine();
            return gson.fromJson(json, receiveType);
        } catch (IOException | JsonParseException e) {
            throw new ReceiveNetworkException(e);
        }
    }
}
