package com.justin.backend.learning.service.impl;

import com.justin.backend.learning.exceptions.DocumentParserConfigException;
import com.justin.backend.learning.service.EnglishWordsBookService;
import com.justin.backend.learning.utils.FileUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Description: implementation of file service.
 *
 * @author Justin_Zhang
 * @date 2/5/2023 14:51
 */
@Slf4j
@Service
public class EnglishWordsBookServiceImpl implements EnglishWordsBookService {
  @Override
  public ByteArrayOutputStream augmentWords(MultipartFile allWordsFile,
                                            MultipartFile existWordBooks) throws IOException {
    // parse the all words file.
    Document document = parseFileToDocument(allWordsFile.getInputStream());
    Map<String, List<String>> wordBookMap = transformXmlToWordBook(document);
    // parse the existed words book file, and add new words in list.
    if (existWordBooks != null) {
      augmentNewWordsToWordBookMap(existWordBooks, wordBookMap);
    } else {
      log.info("no words before upload");
    }
    // add all word to a new zip
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
    log.info("after upload:");
    wordBookMap.forEach((key, value) -> {
      log.info("{} wordbook has {} words", key, value.size());
      augmentContentListToZipOutputStream(zipOutputStream, key, value);
    });
    return byteArrayOutputStream;
  }

  private void augmentContentListToZipOutputStream(ZipOutputStream zipOutputStream,
                                                   String filename, List<String> content) {
    try {
      ZipEntry entry = new ZipEntry(filename);
      zipOutputStream.putNextEntry(entry);
      for (String word : content) {
        zipOutputStream.write(word.getBytes());
        zipOutputStream.write("\n".getBytes());
      }
      zipOutputStream.flush();
      zipOutputStream.closeEntry();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  private void augmentNewWordsToWordBookMap(MultipartFile existWordBooks, Map<String,
      List<String>> wordBookMap) throws IOException {
    try (ZipInputStream zipInputStream = new ZipInputStream(existWordBooks.getInputStream())) {
      ZipEntry nextEntry = zipInputStream.getNextEntry();
      log.info("before upload:");
      while (nextEntry != null) {
        String name = nextEntry.getName();
        List<String> existedWords = FileUtil.readOneZipInputStreamByLine(zipInputStream);
        log.info("{} wordbook has {} words", name, existedWords.size());
        List<String> newWords = wordBookMap.get(name);
        existedWords.removeAll(newWords);
        newWords.addAll(existedWords);
        nextEntry = zipInputStream.getNextEntry();
      }
    }
  }

  private Map<String, List<String>> transformXmlToWordBook(Document document) {
    if (document == null) {
      return new HashMap<>();
    }
    NodeList itemNodeList = document.getElementsByTagName("item");
    NodeList wordNodeList = document.getElementsByTagName("word");
    NodeList tagNodeList = document.getElementsByTagName("tags");
    Map<String, List<String>> wordBookMap = new HashMap<>();
    for (int i = 0; i < itemNodeList.getLength(); i++) {
      String word = wordNodeList.item(i).getFirstChild().getNodeValue();
      String tag = tagNodeList.item(i).getFirstChild().getNodeValue() + ".txt";
      if (wordBookMap.containsKey(tag)) {
        wordBookMap.get(tag).add(word);
      } else {
        List<String> newList = new ArrayList<>();
        newList.add(word);
        wordBookMap.put(tag, newList);
      }
    }
    return wordBookMap;
  }

  private Document parseFileToDocument(InputStream inputStream) {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    try {
      documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
      documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    } catch (ParserConfigurationException e) {
      throw new DocumentParserConfigException(e.getMessage());
    }
    documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    documentBuilderFactory.setExpandEntityReferences(false);
    try {
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      return documentBuilder.parse(inputStream);
    } catch (ParserConfigurationException | SAXException | IOException e) {
      log.info("parse file to document failed");
      return null;
    }
  }
}
