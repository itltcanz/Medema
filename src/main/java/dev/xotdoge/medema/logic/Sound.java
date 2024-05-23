package dev.xotdoge.medema.logic;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.InputStream;

public class Sound {
    public static void play() {
        try {
            // Получите звукового файла
            String name = "/audio/metal_gear_solid_alert.mp3";
            InputStream is = Sound.class.getResourceAsStream(name);
            // Проверка
            if (is == null) {
                System.err.println("Аудиофайл не найден!");
                return;
            }
            // Воспроизведите звук
            Player player = new Player(is);
            player.play();
        } catch (JavaLayerException e) {
            System.err.println("Ошибка воспроизведения звука: " + e.getMessage());
        }
    }
}
