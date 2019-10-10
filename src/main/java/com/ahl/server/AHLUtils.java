package com.ahl.server;

import com.ahl.server.exception.InvalidDataException;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class AHLUtils {

  public static boolean isValidEmailAddress(String email) throws InvalidDataException {

    java.util.regex.Pattern p = java.util.regex.Pattern.compile(AHLConstants.EMAIL_PATTERN);
    java.util.regex.Matcher m = p.matcher(email);
    if(m.matches()) {
      return true;
    }
    Map<String, String> substitueMap = new HashMap<>();
    substitueMap.put("fields", email);
    throw new InvalidDataException(AHLConstants.INVALID_DATA, substitueMap);
  }

}
