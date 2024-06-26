package it.polimi.ingsw.am01.network.tcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.am01.model.json.PositionSerDes;
import it.polimi.ingsw.am01.network.CloseNetworkException;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.ReceiveNetworkException;
import it.polimi.ingsw.am01.network.SendNetworkException;
import it.polimi.ingsw.am01.network.message.NetworkMessage;
import it.polimi.ingsw.am01.network.message.json.NetworkMessageTypeAdapterFactory;

import java.io.*;
import java.net.Socket;

/**
 * Base class for TCP connections.
 *
 * @param <S> The type of the messages that can be sent
 * @param <R> The type of the message that can be received
 */
public abstract class BaseTCPConnection<S extends NetworkMessage, R extends NetworkMessage>
        implements Connection<S, R> {

    private static final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapterFactory(new NetworkMessageTypeAdapterFactory())
            .registerTypeAdapter(PositionSerDes.class, new PositionSerDes())
            .create();
    private final Class<S> sendType;
    private final Class<R> receiveType;
    protected final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    /**
     * Creates a new {@link BaseTCPConnection}.
     *
     * @param sendType    The type of the messages that can be sent
     * @param receiveType The type of the message that can be received
     * @param socket      The socket to use for the connection
     * @throws IOException If an I/O error occurs when creating the connection
     */
    public BaseTCPConnection(Class<S> sendType, Class<R> receiveType, Socket socket) throws IOException {
        this.sendType = sendType;
        this.receiveType = receiveType;
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public R receive() throws ReceiveNetworkException {
        try {
            String json = in.readLine();
            if (json == null) {
                throw new ReceiveNetworkException("Connection closed");
            }

            return gson.fromJson(json, receiveType);
        } catch (IOException | JsonParseException e) {
            throw new ReceiveNetworkException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws CloseNetworkException {
        try {
            this.socket.close();
        } catch (IOException e) {
            throw new CloseNetworkException(e);
        }
    }
}
