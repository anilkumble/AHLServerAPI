package com.ahl.server.exception;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

public class InSufficientDataException extends Exception{

  private String message;

  public InSufficientDataException(String message, Map<String, String> substituteMap) {

    if (!substituteMap.isEmpty()) {
      StringSubstitutor substitutor = new StringSubstitutor(substituteMap);
      this.message = substitutor.replace(message);
    }
  }

  @Override
  public String getMessage() {
    return this.message;
  }
}
