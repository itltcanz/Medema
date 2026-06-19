package dev.itltcanz.medema.services;

import dev.itltcanz.medema.exception.XMLException;
import dev.itltcanz.medema.model.entity.Detector;
import dev.itltcanz.medema.util.XmlUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class MessageProcessorService {

  private final ScanService scanService;

  public void process(Detector detector, String message) throws XMLException {
    String detectorId = XmlUtil.getValue(message, "/host/@id");
    String metalValue = XmlUtil.getValue(message, "/host/detector/metal_found/@value");

    if (!detector.getId().equals(detectorId)) {
      throw new XMLException("Неверный id модуля");
    }

    if (!(metalValue.equals("0") || metalValue.equals("1"))) {
      throw new XMLException("Неправильное значение металла");
    }

    byte metal = Byte.parseByte(metalValue);
    scanService.registerScan(detector, metal);
  }
}