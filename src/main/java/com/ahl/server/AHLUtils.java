package com.ahl.server;

import com.ahl.server.exception.InvalidDataException;

public class AHLUtils {

  public static boolean isValidEmailAddress(String email) throws InvalidDataException {

    java.util.regex.Pattern p = java.util.regex.Pattern.compile(AHLConstants.EMAIL_PATTERN);
    java.util.regex.Matcher m = p.matcher(email);
    if(m.matches())
      return true;
    throw new InvalidDataException(AHLConstants.INVALID_DATA + email);
  }

}
