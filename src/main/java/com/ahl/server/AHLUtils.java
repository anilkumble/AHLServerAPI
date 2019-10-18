package com.ahl.server;

import com.ahl.server.entity.*;
import com.ahl.server.exception.InvalidDataException;

import com.ahl.server.repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AHLUtils {

  @Autowired
  private static TeamRepository teamRepository;
//  @Autowired
//  TeamRepository teamRepository;

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

  public static boolean isPlayerExistInTeam(PlayerTeamRepository playerTeamRepository, ObjectId teamId, ObjectId playerId) {
    List<PlayerTeamRelation> playerTeamRelation = getPlayerTeamRelation(playerTeamRepository, teamId, playerId);
    return playerTeamRelation.size() > 0;
  }

  public static List<PlayerTeamRelation> getPlayerTeamRelation(PlayerTeamRepository playerTeamRepository, ObjectId teamId, ObjectId playerId){
    return  playerTeamRepository.isPlayerRelationExistInTeam(teamId,playerId);
  }

  public static ObjectId getAgainstTeamId(MatchRepository matchRepository,ObjectId matchId, ObjectId forTeamId) throws InvalidDataException {
    Match match=matchRepository.findFirstById(matchId);
    if(match!=null)
    {
      if(match.getTeam1().equals(forTeamId))
      {
        return match.getTeam2();
      }
      else if(match.getTeam2().equals(forTeamId))
      {
        return match.getTeam1();
      }
      else {
        throw new InvalidDataException("Team is not in the match");
      }
    }
    else
    {
      throw new InvalidDataException("Team not found");
    }
  }
  public static int getGoalsScoredByTeamId(GoalRepository goalRepository, ObjectId TeamId)
  {
    List<Goal> goals=goalRepository.findGoalsScoredByTeamId(TeamId);
    return goals.size();
  }
  public static int getGoalsAgainstByTeamId(GoalRepository goalRepository, ObjectId TeamId)
  {
    List<Goal> goals=goalRepository.findGoalsAgainstByTeamId(TeamId);
    return goals.size();
  }
  public static ObjectId getCurrentTeamByPlayer(PlayerTeamRepository playerTeamRepository,TeamRepository teamRepository,TournamentRepository tournamentRepository,ObjectId playerId) throws InvalidDataException {
      List<PlayerTeamRelation> relations= playerTeamRepository.findAllRelationsByPlayerId(playerId);
      for (PlayerTeamRelation relation : relations) {
          if (AHLUtils.getCurrentTournamentByTeam(teamRepository, tournamentRepository, relation.getTeamId())) {
              return relation.getTeamId();
          }
      }
      throw new InvalidDataException("Invalid Player");
  }
  public static boolean getCurrentTournamentByTeam(TeamRepository teamRepository,TournamentRepository tournamentRepository, ObjectId id)
  {
    Team team=teamRepository.findFirstById(id);
    ObjectId tournamentId=team.getTournamentId();
    if (isCurrentTournament(tournamentRepository,tournamentId))
      return true;
    else
      return false;
  }
  public static boolean isCurrentTournament(TournamentRepository tournamentRepository, ObjectId id)
  {
      Tournament tournament=tournamentRepository.findFirstById(id);
      if(tournament.isLive())
          return true;
      else
          return false;
  }
}
