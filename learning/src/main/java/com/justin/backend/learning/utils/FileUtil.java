package com.justin.backend.learning.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: file util.
 *
 * @author Justin_Zhang
 * @date 2/5/2023 15:38
 */
@Slf4j
public class FileUtil {
  private FileUtil() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * read lien by line from zip input stream.
   *
   * @param zipInputStream zip input stream.
   * @return List
   */
  public static List<String> readOneZipInputStreamByLine(ZipInputStream zipInputStream)
      throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(zipInputStream));
    List<String> result = new ArrayList<>();
    String line;
    while ((line = reader.readLine()) != null) {
      result.add(line);
    }
    return result;
  }
}
