package com.mopstream.server;

import com.mopstream.common.exceptions.ClosingSocketException;
import com.mopstream.common.exceptions.ConnectionErrorException;
import com.mopstream.common.exceptions.OpeningServerSocketException;
import com.mopstream.common.interaction.Request;
import com.mopstream.common.interaction.Response;
import com.mopstream.common.interaction.ResponseCode;
import com.mopstream.common.utility.Outputer;
import com.mopstream.server.utility.RequestHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Runs the server.
 */
public class Server {
    private int port;
    private int soTimeout;
    private ServerSocket serverSocket;
    private RequestHandler requestHandler;

    public Server(int port, int soTimeout, RequestHandler requestHandler) {
        this.port = port;
        this.soTimeout = soTimeout;
        this.requestHandler = requestHandler;
    }

    /**
     * Begins server operation.
     */
    public void run() {
        try {
            openServerSocket();
            boolean processingStatus = true;
            while (processingStatus) {
                try (Socket clientSocket = connectToClient()) {
                    processingStatus = processClientRequest(clientSocket);
                } catch (ConnectionErrorException | SocketTimeoutException exception) {
                    break;
                } catch (IOException exception) {
                    Outputer.printerror("Произошла ошибка при попытке завершить соединение с клиентом!");
                }
            }
            stop();
        } catch (OpeningServerSocketException exception) {
            Outputer.printerror("Сервер не может быть запущен!");
        }
    }

    /**
     * Finishes server operation.
     */
    private void stop() {
        try {
            if (serverSocket == null) throw new ClosingSocketException();
            serverSocket.close();
            Outputer.println("Работа сервера успешно завершена.");
        } catch (ClosingSocketException exception) {
            Outputer.printerror("Невозможно завершить работу еще не запущенного сервера!");
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при завершении работы сервера!");
        }
    }

    /**
     * Open server socket.
     */
    private void openServerSocket() throws OpeningServerSocketException {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(soTimeout);
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Порт '" + port + "' находится за пределами возможных значений!");
            throw new OpeningServerSocketException();
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при попытке использовать порт '" + port + "'!");
            throw new OpeningServerSocketException();
        }
    }

    /**
     * Connecting to client.
     */
    private Socket connectToClient() throws ConnectionErrorException, SocketTimeoutException {
        try {
            Outputer.println("Прослушивание порта '" + port + "'...");
            Socket clientSocket = serverSocket.accept();
            Outputer.println("Соединение с клиентом успешно установлено.");
            return clientSocket;
        } catch (SocketTimeoutException exception) {
            Outputer.printerror("Превышено время ожидания подключения!");
            throw new SocketTimeoutException();
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при соединении с клиентом!");
            throw new ConnectionErrorException();
        }
    }

    /**
     * The process of receiving a request from a client.
     */
    private boolean processClientRequest(Socket clientSocket) {
        Request userRequest = null;
        Response responseToUser;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            do {
                userRequest = (Request) clientReader.readObject();
                responseToUser = requestHandler.handle(userRequest);
                clientWriter.writeObject(responseToUser);
                clientWriter.flush();
            } while (responseToUser.getResponseCode() != ResponseCode.SERVER_EXIT);
            return false;
        } catch (ClassNotFoundException exception) {
            Outputer.printerror("Произошла ошибка при чтении полученных данных!");
        } catch (InvalidClassException | NotSerializableException exception) {
            Outputer.printerror("Произошла ошибка при отправке данных на клиент!");
        } catch (IOException exception) {
            if (userRequest == null) {
                Outputer.printerror("Непредвиденный разрыв соединения с клиентом!");
            } else {
                Outputer.println("Клиент успешно отключен от сервера!");
            }
        }
        return true;
    }
}
