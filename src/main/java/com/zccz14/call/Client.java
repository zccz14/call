package com.zccz14.call;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class Client {
    Socket socket;
    AudioReader audioReader;
    Thread handleAudio;
    private DatagramSocket datagramSocket;

    public Client(String address, int port) {
        InetAddress inetAddress = null;
        try {
            socket = new Socket(address, port);
            inetAddress = InetAddress.getByName(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioReader = new AudioReader(Main.defaultAudioFormat);
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        InetAddress finalInetAddress = inetAddress;
        handleAudio = new Thread(() -> {
            while (true) {
                byte[] bytes = audioReader.read();
                DatagramPacket packet = new DatagramPacket(new byte[0], 0, finalInetAddress, port);
                packet.setData(bytes);
                packet.setLength(bytes.length);
                try {
                    datagramSocket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
        handleAudio.start();
    }

    public static void main(String[] args) {
        Arrays.stream(args).forEach(System.out::println);
        Client client = new Client(args[0], Integer.valueOf(args[1]));
        while (true) {
            byte[] bytes = new byte[20];
            try {
                if (System.in.read(bytes) <= 0) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.send(bytes);
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioReader.stop();
    }

    public void send(byte[] bytes) {
        try {
            if (socket.isConnected()) {
                socket.getOutputStream().write(bytes);
                System.out.println("sent");
            } else {
                System.out.println("die");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}