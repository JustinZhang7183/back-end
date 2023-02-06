package com.justin.backend.learning.controller;

import com.justin.backend.learning.service.EnglishWordsBookService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description: file controller.
 *
 * @author Justin_Zhang
 * @date 2/3/2023 17:27
 */
@RestController
@RequestMapping("/english")
public class EnglishWordsBookController {
  @Autowired
  private EnglishWordsBookService englishWordsBookService;

  /**
   * augment words from you dao dict to discrete txt file.
   *
   * @param allWordsFile all words file in you dao.
   * @param existWordBooks existed discrete word book file.
   * @return ResponseEntity bytes of multiple files.
   */
  @PostMapping("/wordbook")
  public ResponseEntity<byte[]> augmentWords(
      @RequestParam("allWordsFile") MultipartFile allWordsFile,
      @RequestParam(value = "existWordBooks", required = false) MultipartFile existWordBooks) {
    ByteArrayOutputStream outputStream = null;
    try {
      outputStream = englishWordsBookService.augmentWords(allWordsFile, existWordBooks);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // set response header.
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentLength(outputStream.size());
    headers.setContentDispositionFormData("attachment", "wordbook.zip");
    return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
  }
}
