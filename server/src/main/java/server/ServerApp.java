package server;

import console.ConsoleOutputer;
import interaction.Response;
import interaction.Status;
import interaction.User;
import server.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class ServerApp {

    ConsoleOutputer output = new ConsoleOutputer();

    protected void mainServerLoop() throws IOException {

        Response errorResponse = new Response();
        errorResponse.setStatus(Status.SERVER_ERROR);

        try {

            int port = 6666;
            output.printPurple("Ожидаю подключение клиента");

            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);

            while (true) {

                try {

                    Socket client = serverSocket.accept();
                    output.printPurple("Клиент подключился ");
                    ClientHandler clientHandler = new ClientHandler(client);


                    new Thread(clientHandler).start();

                } catch (SocketException e) {
                    System.err.println("клиент упал. подожди немного");
                }

            }

        } catch (IllegalArgumentException e) {
            System.err.println("ну и зачем менять номер порта");
            System.err.println("исправляй а потом запускай");
        } catch (IOException e) {
            System.out.println("IO troubles " + e.getMessage());
        }
    }


}
