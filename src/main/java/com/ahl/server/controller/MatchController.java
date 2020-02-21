package com.ahl.server.controller;

import com.ahl.server.AHLConstants;
import com.ahl.server.AHLUtils;
import com.ahl.server.comparator.PointsComparator;
import com.ahl.server.entity.*;
import com.ahl.server.enums.MatchStatus;
import com.ahl.server.repository.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MatchController {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private PointsRepository pointsRepository;
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private PlayerTeamRepository playerTeamRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @RequestMapping("/matches")
    public ResponseEntity<String> getAllMatches(@RequestParam Tournament tournament, @RequestParam String category) {

        ResponseEntity<String> response = validateCategory(category);
        if(tournament==null){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AHLConstants.ERROR, "Invalid Tournament");
            response = new ResponseEntity<String>(jsonObject.toString(), null, HttpStatus.BAD_REQUEST);
        }
        if(response==null){
            Iterable<Match> matches = this.matchRepository.findAllMatchByTournament(tournament.getId());
            Map<ObjectId, Team> teamTagMap = getTeamTagMap(tournament.getId(), category);
            return new ResponseEntity<String>(getMatchResult(matches, teamTagMap).toString(), null, HttpStatus.OK);
        }
        return response;

    }

    private ResponseEntity<String> validateCategory(String category) {
        if(!(category.equals(AHLConstants.MEN) || category.equals(AHLConstants.WOMEN) || category.equals(AHLConstants.ALL))) {
            JsonObject response = new JsonObject();
            response.addProperty(AHLConstants.ERROR, "category should be men or women or all");
            return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @PostMapping(path = "/match")
    public ResponseEntity<String> addMatch(@RequestBody Match match) {
        JsonObject response = new JsonObject();
        try {
            Match.validateMatch(match);
            long matchDate = match.getMatchDateTime();
            if(!AHLUtils.isFutureDate(matchDate)) {
                response.addProperty(AHLConstants.ERROR, "Date is not valid " + matchDate);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Team team1 = this.teamRepository.findFirstById(match.getTeam1());
            Team team2 = this.teamRepository.findFirstById(match.getTeam2());

            if (team1!=null
                    && team2!=null
                    && AHLUtils.isTournamentExist(tournamentRepository, match.getTournamentId())
                    && !match.getTeam2().equals(match.getTeam1())
                    && !ObjectUtils.isEmpty(match.getResult())
                    && team1.getTeamTag().getCategory().equals(team2.getTeamTag().getCategory())
            ) {
                match.setStatus(MatchStatus.UPCOMING);
                if (this.matchRepository.save(match) != null) {
                    response.addProperty(AHLConstants.SUCCESS, AHLConstants.MATCH_CREATED);
                    return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
                } else {
                    response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                    return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            response.addProperty(AHLConstants.ERROR, ex.getMessage());
            return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/match/{oldMatch}")
    public ResponseEntity<String> editMatch(@RequestBody Match match, @PathVariable Match oldMatch) {
        JsonObject response = new JsonObject();
        if (oldMatch != null && !match.getTeam1().equals(match.getTeam2())) {
            oldMatch.setResult(match.getResult());
            oldMatch.setTeam1(match.getTeam1());
            oldMatch.setTeam2(match.getTeam2());
            oldMatch.setTournamentId(match.getTournamentId());
            try {
                Match.validateMatch(oldMatch);
                if (AHLUtils.isTeamExist(teamRepository, match.getTeam1()) && AHLUtils.isTeamExist(teamRepository, match.getTeam2())
                        && AHLUtils.isTournamentExist(tournamentRepository, match.getTournamentId()) && !match.getTeam2().equals(match.getTeam1())
                ){
                    this.matchRepository.save(oldMatch);
                    response.addProperty(AHLConstants.SUCCESS, AHLConstants.MATCH_UPDATED);
                    return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
                }else{
                    response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                    return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                response.addProperty(AHLConstants.ERROR, e.getMessage());
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
            }
        } else {
            response.addProperty(AHLConstants.ERROR, AHLConstants.MATCH_NOT_FOUND);
            return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/match/{oldMatch}")
    public ResponseEntity<String> deleteMatch(@PathVariable Match oldMatch) {
        JsonObject response = new JsonObject();
        if (oldMatch != null) {
            this.matchRepository.delete(oldMatch);
            response.addProperty(AHLConstants.SUCCESS, AHLConstants.MATCH_DELETED);
            return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
        } else {
            response.addProperty(AHLConstants.ERROR, AHLConstants.MATCH_NOT_FOUND);
            return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "match/start/{match}")
    public ResponseEntity<String> startMatch(@PathVariable Match match) {
        JsonObject response = new JsonObject();
        HttpStatus status = HttpStatus.OK;

        match.setStatus(MatchStatus.LIVE_MATCH);
        if (matchRepository.save(match) != null) {
            response.addProperty(AHLConstants.SUCCESS, AHLConstants.MATCH_STARTED);

        } else {
            response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<String>(response.toString(), null, status);
    }

    @PutMapping(path = "match/end/{dbMatch}")
    public ResponseEntity<String> endMatch(@PathVariable Match dbMatch, @RequestBody Match match) {
        JsonObject response = new JsonObject();
        if(match.getMom() != null && match.getBuddingPlayer() != null && dbMatch.getStatus() == MatchStatus.LIVE_MATCH){

            dbMatch.setStatus(MatchStatus.COMPLETED);
            dbMatch.setMom(match.getMom());
            dbMatch.setResult(match.getResult());
            dbMatch.setBuddingPlayer(match.getBuddingPlayer());

            ResponseEntity<String> responseEntity = validateMatchData(dbMatch);
            if(responseEntity!=null){
                return responseEntity;
            }


            if (matchRepository.save(dbMatch) != null) {
                response.addProperty(AHLConstants.SUCCESS, AHLConstants.MATCH_ENDED);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
            } else {
                response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            response.addProperty(AHLConstants.ERROR, "Match object should have man of the match and budding player and status should be on going");
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/points")
    public ResponseEntity<String> getPoints(@RequestParam Tournament tournament, @RequestParam String category) {
        ResponseEntity response = validateCategory(category);
        if(response!=null){
            return response;
        }

        Map<ObjectId, Team> teamTagMap = getTeamTagMap(tournament.getId(), category);
        Iterable<Match> matches = this.matchRepository.findCompletedMatch(tournament.getId(), MatchStatus.COMPLETED);
        matches = getMatchesByCategory(matches, teamTagMap);
        Map<ObjectId, Points> pointsTable = new HashMap<>();

        for(Match match : matches){
            Team team1 = this.teamRepository.findFirstById(match.getTeam1());
            Team team2 = this.teamRepository.findFirstById(match.getTeam2());
            Points team1Point = pointsTable.get(team1.getId());
            Points team2Point = pointsTable.get(team2.getId());
            if(team1Point==null){
                team1Point = new Points();
                team1Point.setTeam(team1);
                pointsTable.put(team1.getId(),team1Point);
            }
            if(team2Point==null){
                team2Point = new Points();
                team2Point.setTeam(team2);
                pointsTable.put(team2.getId(),team2Point);
            }

            int result = match.getResult();

            team1Point.increaseMP();
            team2Point.increaseMP();
            if (result == 1) {
                team1Point.increaseWon();
                team2Point.increaseLost();
            } else if (result == 2) {
                team1Point.increaseLost();
                team2Point.increaseWon();
            } else if(result==-1){
                team1Point.increaseDraw();
                team2Point.increaseDraw();
            }
        }

        for (Map.Entry<ObjectId, Points> entry : pointsTable.entrySet()) {
            Points points = entry.getValue();
            int gs = getGoalsScoredByTeamId(entry.getKey());
            int ga = getGoalsAgainstByTeamId(entry.getKey());
            points.setGoalScored(gs);
            points.setGoalAgainst(ga);
            points.setGoalDifference(gs-ga);
        }
        if(pointsTable.size() != teamTagMap.size()){
            for(ObjectId teamId : teamTagMap.keySet()){
                if(pointsTable.get(teamId)==null){
                    Points points = new Points();
                    points.setTeam(teamTagMap.get(teamId));
                    pointsTable.put(teamId, points);
                }
            }
        }
        List<Points> pointsList = new ArrayList(pointsTable.values());
        pointsList.sort(new PointsComparator());

        int position=1;
        for(Points points:pointsList){
            points.setPosition(position);
            position++;
        }

        return new ResponseEntity<String>(new Gson().toJson(pointsList), null, HttpStatus.OK);

    }
    @RequestMapping("/goalscored/{teamId}")
    private int getGoalsScoredByTeamId(@PathVariable ObjectId teamId) {
        List<Goal> goals = this.goalRepository.findAllGoalsScoredByforTeamId(teamId);
        return goals.size();
    }
    @RequestMapping("/goalagainst/{teamId}")
    private int getGoalsAgainstByTeamId(@PathVariable ObjectId teamId) {
        List<Goal> goals = goalRepository.findGoalsAgainstByTeamId(teamId);
        return goals.size();
    }

    private ResponseEntity<String> validateMatchData(Match match) {
        JsonObject response = new JsonObject();

        ObjectId mom = match.getMom();
        ObjectId buddingPlayer = match.getBuddingPlayer();
        int result = match.getResult();

        if(!AHLUtils.isPlayerExistInTeam(playerTeamRepository, match.getTeam1(), mom)
                && !AHLUtils.isPlayerExistInTeam(playerTeamRepository, match.getTeam2(), mom)){
            response.addProperty(AHLConstants.ERROR, "Man of the player not found in either of teams");
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }

        if(!AHLUtils.isPlayerExistInTeam(playerTeamRepository, match.getTeam1(), buddingPlayer)
                && !AHLUtils.isPlayerExistInTeam(playerTeamRepository, match.getTeam2(), buddingPlayer)){
            response.addProperty(AHLConstants.ERROR, "Budding player not found in either of teams");
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }

        if(!(result==-1 || result==1 || result==2)){
            response.addProperty(AHLConstants.ERROR_MSG, "Invalid result should be -1, 1 or 2");
            return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
        }

        return null;
    }

    private Map<String, Integer> getGoalsByTeamInMatch(ObjectId matchId, ObjectId teamId) {

        Map<ObjectId, Integer> tempGoalMap = new HashMap<>();

        Map<String, Integer> goalMap = new HashMap<>();

        List<Goal> goals = goalRepository.findGoalsByTeamInMatch(matchId,teamId);
        for(Goal goal:goals){
            tempGoalMap.merge(goal.getPlayerId(), 1, Integer::sum);
        }
        for(ObjectId playerId : tempGoalMap.keySet()){
            goalMap.put(new Gson().toJson(this.playerRepository.findFirstById(playerId)), tempGoalMap.get(playerId));
        }
        return goalMap;
    }

    private JsonArray getMatchResult(Iterable<Match> matches, Map<ObjectId, Team> teamTagMap){
        JsonArray resultArray = new JsonArray();
        Gson gson = new Gson();
        List<Match> matchList = getMatchesByCategory(matches, teamTagMap);
        if(teamTagMap!=null) {
            for(Match match : matchList) {
                Team team1 = teamTagMap.get(match.getTeam1());
                Team team2 = teamTagMap.get(match.getTeam2());
                JsonObject matchJson = gson.fromJson(gson.toJson(match), JsonObject.class);
                matchJson.add("team1",gson.fromJson(gson.toJson(team1), JsonObject.class));
                matchJson.add("team2", gson.fromJson(gson.toJson(team2), JsonObject.class));
                if (match.getStatus().equals(MatchStatus.COMPLETED) || match.getStatus().equals(MatchStatus.LIVE_MATCH)) {
                    matchJson.add("team1Scorers", gson.fromJson(gson.toJson(getGoalsByTeamInMatch(match.getId(), match.getTeam1())), JsonObject.class));
                    matchJson.add("team2Scorers", gson.fromJson(gson.toJson(getGoalsByTeamInMatch(match.getId(), match.getTeam2())), JsonObject.class));
                    if(match.getStatus().equals(MatchStatus.COMPLETED)) {
                        Player mom = this.playerRepository.findFirstById(match.getMom());
                        Player buddingPlayer = this.playerRepository.findFirstById(match.getBuddingPlayer());
                        matchJson.add("mom", gson.fromJson(gson.toJson(mom), JsonObject.class));
                        if(buddingPlayer!=null) {
                            matchJson.add("buddingPlayer", gson.fromJson(gson.toJson(buddingPlayer), JsonObject.class));
                        }
                    }
                }
                resultArray.add(matchJson);
            }
        }
        return resultArray;
    }

    private List<Match> getMatchesByCategory(Iterable<Match> matches,Map teamTagMap) {
        List<Match> resultList = new ArrayList<>();
        if (teamTagMap != null) {
            for (Match match : matches) {
                if (teamTagMap.get(match.getTeam1())!=null) {
                    resultList.add(match);
                }
            }
        }
        return resultList;
    }

    private Map<ObjectId, Team> getTeamTagMap(ObjectId tournamentId, String category) {
        Iterable<Team> teams = this.teamRepository.findTeamsByTournament(tournamentId);
        Map<ObjectId, Team> teamTagMap = new HashMap<>();
        teams.forEach(team -> {
            if(category.equals(AHLConstants.MEN) && team.getTeamTag().getCategory().equals(AHLConstants.MEN)) {
                teamTagMap.put(team.getId(), team);
            }else if(category.equals(AHLConstants.WOMEN) && team.getTeamTag().getCategory().equals(AHLConstants.WOMEN)) {
                teamTagMap.put(team.getId(), team);
            }else if(category.equals(AHLConstants.ALL)){
                teamTagMap.put(team.getId(), team);
            }
        });
        return teamTagMap;
    }


}
