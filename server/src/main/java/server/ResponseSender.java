package server;

import interaction.Response;
import interaction.Status;
import json.JsonConverter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ResponseSender implements Runnable {
    private final Socket clientSocket;
    private final OutputStream outputStream;
    private final DataOutputStream dataOutputStream;
    private final Response response;

    public ResponseSender(Socket clientSocket, OutputStream outputStream, DataOutputStream dataOutputStream, Response response) {
        this.clientSocket = clientSocket;
        this.outputStream = outputStream;
        this.dataOutputStream = dataOutputStream;
        this.response = response;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {

            dataOutputStream.writeUTF(JsonConverter.serResponse(response));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
