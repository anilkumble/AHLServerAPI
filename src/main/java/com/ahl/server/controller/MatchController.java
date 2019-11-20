package com.ahl.server.controller;

import com.ahl.server.AHLConstants;
import com.ahl.server.AHLUtils;
import com.ahl.server.comparator.PointsComparator;
import com.ahl.server.entity.*;
import com.ahl.server.enums.MatchStatus;
import com.ahl.server.enums.TeamTag;
import com.ahl.server.repository.*;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    private Map<ObjectId, TeamTag> teamTagMap;


    @RequestMapping("/matches")
    public Iterable<Match> getAllMatches() {
        return this.matchRepository.findAll();
    }

    @RequestMapping("/men/matches")
    public List<Match> getAllMensMatch() {
        List<Match> mensMatch = new ArrayList<>();
        Iterable<Match> matches = this.matchRepository.findAll();
        setTeamTagMap();
        if(teamTagMap!=null) {
            matches.forEach(match -> {
                if(teamTagMap.get(match.getTeam1()).getCategory().equals(AHLConstants.MEN)){
                    mensMatch.add(match);
                }
            });
        }

        return mensMatch;
    }

    @RequestMapping("/women/matches")
    public List<Match> getAllWomensMatch() {
        List<Match> womensMatch = new ArrayList<>();
        Iterable<Match> matches = this.matchRepository.findAll();
        setTeamTagMap();
        if(teamTagMap!=null) {
            matches.forEach(match -> {
                if(teamTagMap.get(match.getTeam1()).getCategory().equals(AHLConstants.WOMEN)){
                    womensMatch.add(match);
                }
            });
        }

        return womensMatch;
    }

    @PostMapping(path = "/match")
    public ResponseEntity<String> addMatch(@RequestBody Match match) {
        JsonObject response = new JsonObject();
        try {
            Match.validateMatch(match);
            Date matchDate = match.getMatchDateTime();
            if(!AHLUtils.isFutureDate(matchDate)){
                response.addProperty(AHLConstants.ERROR, "Date is not valid "+matchDate);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (AHLUtils.isTeamExist(teamRepository, match.getTeam1()) && AHLUtils.isTeamExist(teamRepository, match.getTeam2())
                    && AHLUtils.isTournamentExist(tournamentRepository, match.getTournamentId()) && !match.getTeam2().equals(match.getTeam1())
                    && !ObjectUtils.isEmpty(match.getResult())
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
                        && ObjectUtils.isEmpty(match.getResult())
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

    @DeleteMapping(path = "/match/{id}")
    public ResponseEntity<String> deleteMatch(@PathVariable ObjectId id) {
        JsonObject response = new JsonObject();
        Match oldMatch = this.matchRepository.findFirstById(id);
        if (oldMatch != null) {
            this.matchRepository.delete(oldMatch);
            response.addProperty(AHLConstants.SUCCESS, AHLConstants.MATCH_DELETED);
            return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
        } else {
            response.addProperty(AHLConstants.ERROR, AHLConstants.MATCH_NOT_FOUND);
            return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/start-match/{match}")
    public ResponseEntity<String> startMatch(@PathVariable Match match) {
        JsonObject response = new JsonObject();
        if(match != null){
            match.setStatus(MatchStatus.LIVE_MATCH);
            if(matchRepository.save(match) != null) {
                response.addProperty(AHLConstants.SUCCESS, AHLConstants.MATCH_STARTED);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
            }else{
                response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
            }
        }
        else{
            response.addProperty(AHLConstants.ERROR, AHLConstants.PLAYER_NOT_FOUND);
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/end-match/{match}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> endMatch(@PathVariable Match match, @RequestBody MultiValueMap<String,Object> data) {
        JsonObject response = new JsonObject();
        if(match != null){

            int result =  Integer.parseInt((String)Objects.requireNonNull(data.getFirst("result")));
            ObjectId mom = (ObjectId)Objects.requireNonNull(data.getFirst("mom"));
            ObjectId buddingPlayer = (ObjectId)Objects.requireNonNull(data.getFirst("buddingPlayer"));

            ResponseEntity<String> responseEntity = validateMatchData(match, result, mom, buddingPlayer);
            if(responseEntity!=null){
                return responseEntity;
            }

            match.setResult(result);
            match.setStatus(MatchStatus.COMPLETED);
            match.setMom(mom);
            match.setBuddingPlayer(buddingPlayer);

            if (matchRepository.save(match) != null) {
                response.addProperty(AHLConstants.SUCCESS, AHLConstants.MATCH_ENDED);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
            } else {
                response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
            }

        }
        else{
            response.addProperty(AHLConstants.ERROR, AHLConstants.PLAYER_NOT_FOUND);
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/points/men/{tournament}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public List<Points> getMensPoint(@PathVariable Tournament tournament) {

        Iterable<Match> matches = getAllMensMatch();
        Map<ObjectId, Points> pointsTable = new HashMap<>();

        matches.forEach(match -> {
            Points team1Point = pointsTable.get(match.getTeam1());
            Points team2Point = pointsTable.get(match.getTeam2());
            if(team1Point==null){
                team1Point = new Points();
            }
            if(team2Point==null){
                team2Point = new Points();
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
            } else {
                team1Point.increaseDraw();
                team2Point.increaseDraw();
            }
        });

        for (Map.Entry<ObjectId, Points> entry : pointsTable.entrySet()) {
            Points points = entry.getValue();
            points.setGoalScored(getGoalsScoredByTeamId(entry.getKey()));
            points.setGoalAgainst(getGoalsAgainstByTeamId(entry.getKey()));
        }
        List<Points> pointsList = new ArrayList(pointsTable.values());
        Collections.sort(pointsList, new PointsComparator());

        int position=1;
        for(Points points:pointsList){
            points.setPoints(position);
            position++;
        }

        return pointsList;

    }

    private ResponseEntity<String> validateMatchData(Match match, int result, ObjectId mom, ObjectId buddingPlayer) {
        JsonObject response = new JsonObject();


        if(!AHLUtils.isPlayerExistInTeam(playerTeamRepository, match.getTeam1(), mom)
                && !AHLUtils.isPlayerExistInTeam(playerTeamRepository, match.getTeam1(), mom)){
            response.addProperty(AHLConstants.ERROR, "Man of the player not found in either of teams");
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }

        if(!AHLUtils.isPlayerExistInTeam(playerTeamRepository, match.getTeam1(), buddingPlayer)
                && !AHLUtils.isPlayerExistInTeam(playerTeamRepository, match.getTeam1(), buddingPlayer)){
            response.addProperty(AHLConstants.ERROR, "Budding player not found in either of teams");
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }

        if(!(result==0 || result==1 || result==2)){
            response.addProperty(AHLConstants.ERROR_MSG, "Invalid result should be 1, -1 or 0");
            return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
        }

        return null;
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

    private void setTeamTagMap() {
        Iterable<Team> teams = this.teamRepository.findAll();
        teamTagMap = new HashMap<>();
        teams.forEach(team -> {
            teamTagMap.put(team.getId(), team.getTeamTag());
        });
    }

}
