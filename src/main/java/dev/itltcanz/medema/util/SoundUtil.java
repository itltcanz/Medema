package dev.itltcanz.medema.util;

import java.io.InputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SoundUtil {
  public static void play() {
    try {
      String name = "/audio/metal_gear_solid_alert.mp3";
      InputStream is = SoundUtil.class.getResourceAsStream(name);
      // Проверка
      if (is == null) {
        log.error("Аудиофайл не найден!");
        return;
      }
      // Воспроизведите звук
      Player player = new Player(is);
      player.play();
    } catch (JavaLayerException e) {
      log.error("Ошибка воспроизведения звука", e);
    }
  }
}
