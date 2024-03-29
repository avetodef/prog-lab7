import ASCII.ASCIIArt;
import commands.VideoRzhaka;
import console.ConsoleOutputer;
import console.ConsoleReader;
import console.Console;
import exceptions.EmptyInputException;
import exceptions.ExitException;
import interaction.Request;
import interaction.Response;
import interaction.User;
import json.JsonConverter;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class ClientApp implements Runnable {

    private final ConsoleReader consoleReader = new ConsoleReader();
    private final ConsoleOutputer o = new ConsoleOutputer();
    private final Scanner sc = new Scanner(System.in);
    private final ByteBuffer buffer = ByteBuffer.allocate(60_000);
    private final Console console = new Console();
    private final ReaderSender readerSender = new ReaderSender();
    private User user;
    private final Authorization auth = new Authorization();

    protected void mainClientLoop() {
        try {
            Selector selector = Selector.open();
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            int serverPort = 6666;
            socketChannel.connect(new InetSocketAddress("localhost", serverPort));
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            if (!Authorization.isAuth) {
                user = auth.askIfAuth(sc);
            } else {
                go(selector, socketChannel, user);
            }

        } catch (UnknownHostException e) {
            o.printRed("неизвестный хост. порешай там в коде что нибудь ок?");

        } catch (IOException exception) {
            o.printRed("Сервер пока недоступен. Закончить работу клиента? (напишите {yes} или {no})?");
            String answer;
            try {
                while (!(answer = sc.nextLine()).equals("no")) {
                    switch (answer) {
                        case "":
                            break;
                        case "yes":
                            System.exit(0);
                            break;
                        default:
                            o.printNormal("скажи пожалуйста.... yes или no");
                    }
                }
                o.printNormal("жди...");
            } catch (NoSuchElementException e) {
                throw new ExitException("poka");
            }
        }
    }

    private void go(Selector selector, SocketChannel socketChannel, User user) throws IOException {

        while (true) {

            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                SocketChannel client = (SocketChannel) key.channel();

                if (key.isConnectable()) {

                    connect(client);
                    client.register(selector, SelectionKey.OP_WRITE);
                    continue;
                }
                if (key.isWritable()) {
                    try {
                        List<String> input = consoleReader.reader();

                        Request request = new Request(input, null, user);

                        if (input.contains("exit"))
                            Exit.execute();

                        if (input.contains("mega_rzhaka"))
                            new Thread(new VideoRzhaka()).start();

                        if (input.contains("execute_script")) {
                            readerSender.readAndSend(CommandChecker.ifExecuteScript(input), request, socketChannel, console);
                        } else {
                            readerSender.readAndSend(input, request, socketChannel, console);
                        }

                    } catch (NumberFormatException e) {
                        o.printRed("int введи");
                        continue;
                    } catch (NullPointerException e) {
                        o.printRed("Введённой вами команды не существует. Попробуйте ввести другую команду.");
                        continue;
                    } catch (EmptyInputException e) {
                        o.printRed(e.getMessage());
                        continue;
                    } catch (IndexOutOfBoundsException e) {
                        o.printRed("брат забыл айди ввести походу");
                        continue;
                    }
                    client.register(selector, SelectionKey.OP_READ);
                    continue;
                }

                if (key.isReadable()) {

                    read(socketChannel);

                    client.register(selector, SelectionKey.OP_WRITE);
                }

            }

        }
    }

    private void connect(SocketChannel client) {
        if (client.isConnectionPending()) {
            try {
                client.finishConnect();
                o.printWhite("готов к работе с сервером");
            } catch (IOException e) {
                System.out.println("connection refused");
            }
        }
    }


    private void read(SocketChannel socketChannel) {
        try {

            socketChannel.read(buffer);
            buffer.flip();

            String serverResponse = StandardCharsets.UTF_8.decode(buffer).toString().substring(2);

            Response response = JsonConverter.desResponse(serverResponse);
            printPrettyResponse(response);
            buffer.clear();

        } catch (IOException e) {
            System.out.println("IO ");
        }
    }


    protected void runClient() {

        while (true) {
            try {
                mainClientLoop();
            } catch (ExitException e) {
                o.printRed("heheheha");
                break;
            } catch (RuntimeException e) {
                o.printRed("ошибка.....: " + e.getMessage());
            }
        }

    }

    private void printPrettyResponse(Response r) {
        switch (r.status) {
            case OK: {
                o.printNormal(r.msg);
                break;
            }
            case FILE_ERROR: {
                o.printBlue(r.msg);
                break;
            }
            case UNKNOWN_ERROR: {
                o.printRed(r.msg);
                break;
            }
            case COLLECTION_ERROR: {
                o.printYellow(r.msg);
                break;
            }
            case USER_EBLAN_ERROR: {
                o.printPurple(r.msg);
                break;
            }
            case SERVER_ERROR: {
                o.printRed(r.msg);
            }
        }
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
        runClient();
    }
}