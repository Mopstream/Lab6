package com.mopstream.client;

import com.mopstream.client.utility.UserHandler;
import com.mopstream.common.exceptions.ConnectionErrorException;
import com.mopstream.common.exceptions.NotInDeclaredLimitsException;
import com.mopstream.common.interaction.Request;
import com.mopstream.common.interaction.Response;
import com.mopstream.common.utility.Outputer;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class NewClient {
    private String host;
    private int port;
    private int reconnectionTimeout;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts;
    private UserHandler userHandler;
    private Socket clientSocket;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;

    public NewClient(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, UserHandler userHandler) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.userHandler = userHandler;
    }

    public void run() {
        try {
            boolean processingStatus = true;
            while (processingStatus) {
                try {
                    connectToServer();
                    processingStatus = processRequestToServer();
                } catch (ConnectionErrorException exception) {
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        Outputer.printerror("Превышено количество попыток подключения!");
                        break;
                    }
                    try {
                        Thread.sleep(reconnectionTimeout);
                    } catch (IllegalArgumentException timeoutException) {
                        Outputer.printerror("Время ожидания подключения '" + reconnectionTimeout +
                                "' находится за пределами возможных значений!");
                        Outputer.println("Повторное подключение будет произведено немедленно.");
                    } catch (Exception timeoutException) {
                        Outputer.printerror("Произошла ошибка при попытке ожидания подключения!");
                        Outputer.println("Повторное подключение будет произведено немедленно.");
                    }
                }
                reconnectionAttempts++;
            }
            if (clientSocket != null) clientSocket.close();
            Outputer.println("Работа клиента успешно завершена.");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("Клиент не может быть запущен!");
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при попытке завершить соединение с сервером!");
        }
    }


    private void connectToServer () throws NotInDeclaredLimitsException, ConnectionErrorException {
        try {
            if (reconnectionAttempts >= 1) Outputer.println("Повторное соединение с сервером...");
            clientSocket = new Socket(host,port);
            Outputer.println("Соединение с сервером успешно установлено.");
            Outputer.println("Ожидание разрешения на обмен данными...");


            Outputer.println("Разрешение на обмен данными получено.");
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Адрес сервера введен некорректно!");
            throw new NotInDeclaredLimitsException();
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при соединении с сервером!");
            throw new ConnectionErrorException();
        }
    }

    private boolean processRequestToServer() {
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try {
                requestToServer = (serverResponse != null) ? userHandler.handle(serverResponse.getResponseCode()) :
                        userHandler.handle(null);
                if (requestToServer.isEmpty()) continue;



                serverWriter.writeObject(requestToServer);



                serverResponse = (Response) serverReader.readObject();



                Outputer.print(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException exception) {
                exception.printStackTrace();
                Outputer.printerror("Произошла ошибка при отправке данных на сервер!");
            } catch (ClassNotFoundException exception) {
                Outputer.printerror("Произошла ошибка при чтении полученных данных!");
            } catch (IOException exception) {
                Outputer.printerror("Соединение с сервером разорвано!");
                try {
                    reconnectionAttempts++;
                    connectToServer();
                } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                    if (requestToServer.getCommandName().equals("exit"))
                        Outputer.println("Команда не будет зарегистрирована на сервере.");
                    else Outputer.println("Попробуйте повторить команду позднее.");
                }
            }
        } while (!requestToServer.getCommandName().equals("exit"));
        return false;
    }
}
