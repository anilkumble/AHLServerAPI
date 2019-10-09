package com.ahl.server.entity;

import com.ahl.server.AHLConstants;
import com.ahl.server.exception.InSufficientDataException;

import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;

public class League {

  private ObjectId id;
  private String year;
  private String slogan;

  public League(String year, String slogan) {
    this.year = year;
    this.slogan = slogan;
  }

  public static boolean validateLeague(League league) throws InSufficientDataException {

    if (!ObjectUtils.allNotNull(league.getYear())) {
      throw new InSufficientDataException(AHLConstants.MINIMUM_REQUIRED_FIELDS);
    }
    return true;
  }


  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getSlogan() {
    return slogan;
  }

  public void setSlogan(String slogan) {
    this.slogan = slogan;
  }
}
