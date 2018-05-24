package com.example.user.votechain.ServerApp;

import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {
    private boolean runnning = false;
    private ServerSocket serverSocket;

    public SocketServer() {

    }

    private void runServer() {
        runnning = true;

        try {
            serverSocket = new ServerSocket(1234);

            while (runnning) {
                Socket client = serverSocket.accept();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        runServer();
    }
}
