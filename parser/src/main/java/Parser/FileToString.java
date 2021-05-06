package Parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileToString {

  public static List<String> fileReader(String path) {
    List<String> list = new ArrayList<String>();
    File file = new File(path);
    if (file.exists()) {
      try {
        list = Files.readAllLines(file.toPath(), Charset.defaultCharset());
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return list;
  }

  public static List<String> fileReader(File file) {
    List<String> list = new ArrayList<String>();
    if (file.exists()) {
      try {
        list = Files.readAllLines(file.toPath(), Charset.defaultCharset());
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return list;
  }
}
