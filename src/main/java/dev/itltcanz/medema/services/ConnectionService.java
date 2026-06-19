package dev.itltcanz.medema.services;

import dev.itltcanz.medema.constant.TimerConstant;
import dev.itltcanz.medema.model.entity.Detector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectionService {
  private Socket socket;
  private BufferedReader bufferedReader;

  public void connect(Detector detector) throws IOException {
    socket = new Socket();
    socket.setSoTimeout(TimerConstant.SO_TIMEOUT);
    socket.connect(new InetSocketAddress(detector.getIp(), Integer.parseInt(detector.getPort())));
    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  public String readMessage() throws IOException, NullPointerException {
    String message;
    message = getMessageByInput();
    if (message.isEmpty()) {
      throw new IOException();
    }
    return message;
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

  public void close() throws IOException {
    if (socket != null && !socket.isClosed()) {
      socket.close();
    }
    if (bufferedReader != null) {
      bufferedReader.close();
    }
  }

}
