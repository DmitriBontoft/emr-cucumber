package helpers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

public class XmlFileLoader {

  public static String loadXmlFile(String file) throws IOException {
    InputStream resourceAsStream = XmlFileLoader.class.getResourceAsStream(file);
    return IOUtils
        .toString(
            resourceAsStream,
            StandardCharsets.UTF_8);
  }
}
