package com.ahl.server.entity;

import com.ahl.server.AHLConstants;
import com.ahl.server.exception.InSufficientDataException;

import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Document(collection = "tournaments")
public class Tournament {

  @Id
  private ObjectId id;
  private String season;
  private String theme;
  private String tagline;

  public String getSeason() {
    return season;
  }

  public void setSeason(String season) {
    this.season = season;
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = theme;
  }

  public String getTagline() {
    return tagline;
  }

  public void setTagline(String tagline) {
    this.tagline = tagline;
  }

  public static boolean validateTournament(Tournament tournament) throws InSufficientDataException {

    if (!ObjectUtils.allNotNull(tournament.getSeason(),tournament.getTagline())
        || ObjectUtils.isEmpty(tournament.getSeason())
        || ObjectUtils.isEmpty(tournament.getTagline()) ) {
      Map<String, String> substitueMap = new HashMap<>();
      substitueMap.put("fields", Arrays.toString(new Object[]{AHLConstants.SEASON, AHLConstants.TAG_LINE}) );
      throw new InSufficientDataException(AHLConstants.MINIMUM_REQUIRED_FIELDS, substitueMap);
    }
    return true;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Tournament.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("season='" + season + "'")
        .add("theme='" + theme + "'")
        .add("tagline='" + tagline + "'")
        .toString();
  }
}
