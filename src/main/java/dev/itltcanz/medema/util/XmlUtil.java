package dev.itltcanz.medema.util;

import dev.itltcanz.medema.exception.XMLException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

@SuppressWarnings("java:S2755")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XmlUtil {

  public static String getValue(String message, String path) throws XMLException {
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(new InputSource(new StringReader(message)));
      XPath xPath = XPathFactory.newInstance().newXPath();
      return (String) xPath.compile(path).evaluate(doc, XPathConstants.STRING);
    } catch (Exception e) {
      throw new XMLException("Не удалось прочитать сообщение", e);
    }
  }
}
