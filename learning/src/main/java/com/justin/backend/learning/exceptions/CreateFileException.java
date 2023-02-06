package com.justin.backend.learning.exceptions;

/**
 * Description: exception for creating file.
 *
 * @author Justin_Zhang
 * @date 2/6/2023 10:22
 */
public class CreateFileException extends RuntimeException {
  public CreateFileException(String message) {
    super(message);
  }
}
