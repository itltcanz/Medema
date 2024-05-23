package dev.itltcanz.medema.logic;

import dev.itltcanz.medema.config.TimerConfig;
import dev.itltcanz.medema.entity.Scan;
import dev.itltcanz.medema.exception.XMLException;
import dev.itltcanz.medema.javafx.Controller;
import dev.itltcanz.medema.services.ScanService;
import javafx.application.Platform;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("SameParameterValue")
public class ModuleTracker {
    private final AtomicBoolean threadStopFlag = new AtomicBoolean(false);
    private final Controller controller;
    private final Module module;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final Object threadLock;

    public ModuleTracker(Controller controller, Object threadLock, Module module) {
        this.controller = controller;
        this.module = module;
        this.threadLock = threadLock;
    }

    public Module getModule() {
        return module;
    }

    public void runModule() {
        // Цикл подключения
        while (!threadStopFlag.get()) {
            boolean firstMessage = true;
            // Подключение
            try {
                tryConnectToModule();
            } catch (IOException e) {
                throwErrorAndClose("Ошибка подключения модуля", e);
                sleep();
                continue;
            }
            // Цикл сообщений
            while (!threadStopFlag.get()) {
                // Чтение сообщений
                String message;
                try {
                    message = tryReadMessage();
                } catch (IOException | NullPointerException e) {
                    throwErrorAndClose("Ошибка чтения сообщения от модуля", e);
                    break;
                }
                // Отметание приветственного сообщения
                if (firstMessage) {
                    firstMessage = false;
                    continue;
                }
                // Обработка XML документа и сохранение записи
                try {
                    Scan scan = tryProcessXmlDocument(message);
                    saveScan(scan);
                    Platform.runLater(controller::setStartPageToday);
                } catch (XPathExpressionException | ParserConfigurationException | IOException | SAXException e) {
                    throwErrorAndClose("Ошибка обработки сообщения от модуля", e);
                    break;
                } catch (XMLException e) {
                    throwError("Ошибка обработки сообщения от модуля", e);
                }
            }
        }
    }

    private void tryConnectToModule() throws IOException {
        sendInfo("Подключение к модулю");
        module.connect();
        synchronized (threadLock) {
            Platform.runLater(() -> controller.setOnlineStatus(module.getId()));
        }
        sendInfo("Модуль подключен");
    }

    private void sleep() {
        try {
            Thread.sleep(TimerConfig.RECONNECT_TIMEOUT);
        } catch (InterruptedException e) {
            throwErrorAndClose("Ошибка потока", e);
        }
    }

    private String tryReadMessage() throws IOException, NullPointerException {
        sendInfo("Чтение сообщения от модуля");
        String message;
        message = module.getMessageByInput();
        if (message.equals("")) {
            throw new IOException();
        }
        sendInfo(message.trim());
        return message;
    }

    private Scan tryProcessXmlDocument(String message) throws XPathExpressionException, ParserConfigurationException, IOException, SAXException, XMLException {
        sendInfo("Обработка документа от модуля");
        String moduleId = processXmlDocument(message, "/host/@id");
        String metalValueStr = processXmlDocument(message, "/host/detector/metal_found/@value");
        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        // Отметание ложных срабатываний
        if (!moduleId.equals(module.getId())) {
            throw new XMLException("Неверный id модуля: " + moduleId);
        }
        if (ScanService.findScanByDateTime(time) != null) {
            throw new XMLException("Совпадение времени: " + time);
        }
        if (metalValueStr.equals("101")) {
            throw new XMLException("Обнаружено ложное срабатывание: metal = " + metalValueStr);
        }
        byte metal = Byte.parseByte(metalValueStr);
        // Воспроизведение звука
        if (metal == 1) {
            new Thread(Sound::play).start();
        }
        return ScanService.createScan(moduleId, metal, time);
    }

    public String processXmlDocument(String message, String path) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new StringReader(message)));
        XPath xPath = XPathFactory.newInstance().newXPath();
        return (String) xPath.compile(path).evaluate(doc, XPathConstants.STRING);
    }

    private void saveScan(Scan scan) {
        synchronized (threadLock) {
            ScanService.saveScan(scan);
        }
    }

    private void throwErrorAndClose(String errorText, Exception e) {
        synchronized (threadLock) {
            String errorMessage = String.format("%s, %s, %s:\n%s\n%s\n\n", module.getId(), module.getLocation(),
                    LocalDateTime.now().format(formatter), errorText, e.getMessage());
            Platform.runLater(() -> controller.addErrorToDebug(errorMessage));
            Platform.runLater(() -> controller.setOfflineStatus(module.getId()));
            module.closeResources();
        }
    }

    private void throwError(String errorText, Exception e) {
        synchronized (threadLock) {
            String errorMessage = String.format("%s, %s, %s:\n%s\n%s\n\n", module.getId(), module.getLocation(),
                    LocalDateTime.now().format(formatter), errorText, e.getMessage());
            Platform.runLater(() -> controller.addErrorToDebug(errorMessage));
        }
    }

    private void sendInfo(String infoText) {
        synchronized (threadLock) {
            String infoMessage = String.format("%s, %s, %s:\n%s\n\n", module.getId(),
                    module.getLocation(), LocalDateTime.now().format(formatter), infoText);
            Platform.runLater(() -> controller.addInfoToDebug(infoMessage));
        }
    }

    public void stop() {
        threadStopFlag.set(true);
        module.closeResources();
    }
}