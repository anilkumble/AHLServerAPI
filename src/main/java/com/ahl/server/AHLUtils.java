package com.ahl.server;

import com.ahl.server.entity.Match;
import com.ahl.server.entity.PlayerRelation;
import com.ahl.server.exception.InvalidDataException;

import com.ahl.server.repository.*;
import org.apache.commons.text.StringSubstitutor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
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

  public static boolean isPlayerExist(PlayerRepository playerRepository, ObjectId playerId) throws InvalidDataException{
    if(playerRepository.findFirstById(playerId)==null)
    {
      throw new InvalidDataException(AHLConstants.INVALID_PLAYER);
    }
    return true;
  }
  public static boolean isTeamExist(TeamRepository teamRepository,ObjectId teamId) throws InvalidDataException{
    if(teamRepository.findFirstById(teamId)==null)
    {
      throw new InvalidDataException(AHLConstants.INVALID_TEAM);
    }
    return true;
  }

  public static boolean isMatchExist(MatchRepository matchRepository, ObjectId matchId) throws InvalidDataException {
    if(matchRepository.findFirstById(matchId)==null)
    {
      throw new InvalidDataException(AHLConstants.INVALID_MATCH);
    }
    return true;
  }
  public static boolean isTournamentExist(TournamentRepository tournamentRepository, ObjectId matchId) throws InvalidDataException {
    if(tournamentRepository.findFirstById(matchId)==null)
    {
      throw new InvalidDataException(AHLConstants.INVALID_TOURNAMENT);
    }
    return true;
  }

    public static boolean isTeamExistInMatch(MatchRepository matchRepository, ObjectId matchId, ObjectId forTeamId) throws InvalidDataException
    {
      Match match=matchRepository.findFirstById(matchId);
    if(match!=null )
    {
      if(!(match.getTeam1().equals(forTeamId) || match.getTeam2().equals(forTeamId)))
        throw new InvalidDataException(AHLConstants.TEAM_NOT_FOUND_IN_MATCH);
      else
        return true;
    }
    else
      return false;
    }

  public static int isPlayerExistInTeam(PlayerRelationRepository playerRelationRepository, ObjectId forTeamId, ObjectId playerId) {
    List<PlayerRelation> playerRelation= playerRelationRepository.isPlayerRelationExistInTeam(forTeamId,playerId);
    return playerRelation.size();
  }
}
