package com.mopstream.client;

import com.mopstream.client.utility.UserHandler;
import com.mopstream.common.exceptions.ConnectionErrorException;
import com.mopstream.common.exceptions.NotInDeclaredLimitsException;
import com.mopstream.common.interaction.Request;
import com.mopstream.common.interaction.Response;
import com.mopstream.common.utility.Outputer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Runs the client.
 */
public class Client {
    private String host;
    private int port;
    private int reconnectionTimeout;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts;
    private UserHandler userHandler;
    private SocketChannel socketChannel;

    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, UserHandler userHandler) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.userHandler = userHandler;
    }

    /**
     * Begins client operation.
     */
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
            if (socketChannel != null) socketChannel.close();
            Outputer.println("Работа клиента успешно завершена.");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("Клиент не может быть запущен!");
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при попытке завершить соединение с сервером!");
        }
    }

    /**
     * Connecting to server.
     */
    private void connectToServer() throws ConnectionErrorException, NotInDeclaredLimitsException {
        try {
            if (reconnectionAttempts >= 1) Outputer.println("Повторное соединение с сервером...");
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
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

    /**
     * Server request process.
     */
    private boolean processRequestToServer() {
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try {
                requestToServer = (serverResponse != null) ? userHandler.handle(serverResponse.getResponseCode()) :
                        userHandler.handle(null);
                if (requestToServer.isEmpty()) continue;
                ByteBuffer buffer = serializeRequest(requestToServer);
                socketChannel.write(buffer);
                ByteBuffer readBuffer = ByteBuffer.allocate(4096);
                socketChannel.read(readBuffer);
                serverResponse = deSerializeResponse(readBuffer.array());
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
                        Outputer.println("Выключаюсь.");
                    else Outputer.println("Попробуйте повторить команду позднее.");
                }
            }
        } while (!requestToServer.getCommandName().equals("exit"));
        return false;
    }

    public static ByteBuffer serializeRequest(Request request) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(request);
        objectOutputStream.flush();
        ByteBuffer bufToSend = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return bufToSend;
    }

    public static Response deSerializeResponse(byte[] acceptedBuf) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(acceptedBuf);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Response response = (Response) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        return response;
    }

}