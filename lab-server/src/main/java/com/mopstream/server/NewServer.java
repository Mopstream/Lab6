package com.mopstream.server;

import com.mopstream.common.interaction.Request;
import com.mopstream.common.interaction.Response;
import com.mopstream.common.utility.Outputer;
import com.mopstream.server.utility.RequestHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class NewServer {
    private int port;
    private RequestHandler requestHandler;

    public NewServer(int port, RequestHandler requestHandler) {
        this.port = port;
        this.requestHandler = requestHandler;
    }

    public void run() {
        Scanner serverScanner = new Scanner(System.in);
        Outputer.print("$ ");
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.socket().bind(new InetSocketAddress("localhost", port));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            while (serverSocket.isOpen()) {
                selector.select(1);
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = readyKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    if (key.isAcceptable()) {
                        register(selector, serverSocket);
                    } else if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();

                        ByteBuffer readBuffer = ByteBuffer.allocate(4096);
                        try {
                            socketChannel.read(readBuffer);
                            Request request = deSerializeRequest(readBuffer.array());
                            Response response = requestHandler.handle(request);
                            ByteBuffer buffer = serializeResponse(response);
                            socketChannel.write(buffer);
                        } catch (IOException e) {
                            socketChannel.close();
                        }
                    }
                }
                while (System.in.available() > 0) {
                    String serverInput = serverScanner.nextLine().trim();
                    if (serverInput.equals("save")) {
                        requestHandler.handle(new Request("save", "", null));
                        Outputer.println("Коллекция успешно сохранена");
                    } else {
                        Outputer.printerror("Команда '" + serverInput + "' не найдена.");
                    }
                    Outputer.print("$ ");
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            requestHandler.handle(new Request("save", "", null));
        }
    }

    private static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    private static Request deSerializeRequest(byte[] acceptedBuf) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(acceptedBuf);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Request request = (Request) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        return request;
    }

    private static ByteBuffer serializeResponse(Response response) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(response);
        objectOutputStream.flush();
        ByteBuffer bufToSend = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return bufToSend;
    }

}