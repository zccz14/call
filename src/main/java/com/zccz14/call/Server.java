package com.zccz14.call;

import com.zccz14.call.event.ReceiveMessage;
import com.zccz14.call.event.ReceiveMessageListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private ServerSocket serverSocket;
    private Set<ReceiveMessageListener> listeners = new HashSet<>();
    private DatagramSocket datagramSocket;
    private AudioWriter audioWriter;
    private Thread handleUDP;
    private Thread handleTCP;

    public Server(int port) {
        try {
            datagramSocket = new DatagramSocket(port);
            System.out.println("UDP listen " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioWriter = new AudioWriter(Main.defaultAudioFormat);
        handleUDP = new Thread(() -> {
            while (true) {
                byte[] bytes = new byte[Main.defaultBufferSize];
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                try {
                    datagramSocket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                audioWriter.play(packet.getData());
            }
        });
        handleUDP.start();
    }

    public static void main(String[] args) {
        Server server = new Server(Main.defaultPort);
        System.out.println("Server listen");
        while (!server.serverSocket.isClosed()) {
            System.out.println("waiting for new socket");
            Socket socket = null;
            try {
                socket = server.serverSocket.accept();
                System.out.println("connection established");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Socket finalSocket = socket;
            handleTCP(server, finalSocket);

        }
    }


    private static void handleTCP(Server server, Socket finalSocket) {
        new Thread(() -> {
            BufferedInputStream inputStream;
            try {
                inputStream = new BufferedInputStream(finalSocket.getInputStream());
                finalSocket.getOutputStream().write(0x41);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            while (true) {
                byte[] bytes = new byte[20];
                int readBytes = 0;
                try {
                    readBytes = inputStream.read(bytes);
                    finalSocket.getOutputStream().write(("echo> " + bytes).getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (readBytes <= 0) {
                    break;
                }
                ReceiveMessage message = new ReceiveMessage(server, bytes);
                System.out.printf("%s> %s\n", finalSocket.getRemoteSocketAddress(), new String(bytes, StandardCharsets.UTF_8));
                server.listeners.forEach(e -> e.onMessage(message));
            }
            System.out.printf("connection from %s closed\n", finalSocket.getRemoteSocketAddress());
        }).start();
        System.out.printf("local %s <=> remote %s\n", finalSocket.getLocalSocketAddress(), finalSocket.getRemoteSocketAddress());
    }
}