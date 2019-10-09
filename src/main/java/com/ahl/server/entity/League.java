package com.ahl.server.entity;

import com.ahl.server.AHLConstants;
import com.ahl.server.exception.InSufficientDataException;

import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.StringJoiner;

@Document(collection = "leagues")
public class League {

  @Id
  private ObjectId id;
  private String year;
  private String tagline;

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getTagline() {
    return tagline;
  }

  public void setTagline(String tagline) {
    this.tagline = tagline;
  }

  public static boolean validateLeague(League league) throws InSufficientDataException {

    if (!ObjectUtils.allNotNull(league.getYear(),
        league.getTagline()
    )) {
      throw new InSufficientDataException(AHLConstants.MINIMUM_REQUIRED_FIELDS);
    }
    return true;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", League.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("year='" + year + "'")
        .add("tagline='" + tagline + "'")
        .toString();
  }
}
