package dev.itltcanz.medema.logic;

import dev.itltcanz.medema.config.TimerConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Module {
    private final String ip;
    private final int port;
    private final String id;
    private final String location;
    private BufferedReader bufferedReader;
    private Socket socket;

    public Module(String ip, int port, String id, String location) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.location = location;
    }

    public void connect() throws IOException {
        socket = new Socket();
        socket.setSoTimeout(TimerConfig.SO_TIMEOUT);
        socket.connect(new InetSocketAddress(ip, port));
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void closeResources() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getMessageByInput() throws IOException {
        StringBuilder messageBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null && !line.endsWith("</host>")) {
            messageBuilder.append(line).append("\n");
        }
        if (line != null) {
            messageBuilder.append(line).append("\n");
        }
        return messageBuilder.toString();
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

}