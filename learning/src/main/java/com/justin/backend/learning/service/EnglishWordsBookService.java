package com.justin.backend.learning.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description: file service interface.
 *
 * @author Justin_Zhang
 * @date 2/5/2023 14:50
 */
public interface EnglishWordsBookService {
  ByteArrayOutputStream augmentWords(MultipartFile allWordsFile,
                                     MultipartFile existWordBooks) throws IOException;
}
