package com.ahl.server.controller;


import com.ahl.server.AHLConstants;
import com.ahl.server.AHLUtils;
import com.ahl.server.entity.Goal;
import com.ahl.server.entity.Match;
import com.ahl.server.entity.Player;
import com.ahl.server.entity.Team;
import com.ahl.server.exception.InvalidDataException;
import com.ahl.server.repository.*;
import com.google.gson.JsonObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GoalController {
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerTeamRepository playerTeamRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @RequestMapping("/goals/{tournamentId}/{id}")
    public int getGoalsByPlayerId(@PathVariable ObjectId tournamentId,@PathVariable ObjectId id)
    {
        List<Goal> goals=goalRepository.findAllGoalsByplayerId(id);
        int count=0;
        for(Goal i:goals)
        {
            if (i.getTournamentId().equals(tournamentId))
            {
                count++;
            }
        }
        return count;
    }

    @RequestMapping("/testgoal/{id}")
    public ResponseEntity<String> test(@PathVariable ObjectId id)
    {
        JsonObject response = new JsonObject();
        try
        {
            ObjectId forTeamId=AHLUtils.getCurrentTeamByPlayer(playerTeamRepository,teamRepository,tournamentRepository,id);
            System.out.println("test");
            response.addProperty(AHLConstants.SUCCESS,forTeamId.toString());
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e)
        {
            response.addProperty(AHLConstants.ERROR,e.getMessage());
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/goal")
    public ResponseEntity<String> addGoal(@RequestBody Goal goal)
    {
        JsonObject response = new JsonObject();
        try
        {
            if(Goal.validateGoal(goal))
            {
                if(AHLUtils.isPlayerExist(playerRepository,goal.getPlayerId()) && AHLUtils.isMatchExist(matchRepository,goal.getMatchId()))
                {
                    ObjectId forTeamId=AHLUtils.getCurrentTeamByPlayer(playerTeamRepository,teamRepository,tournamentRepository,goal.getPlayerId());
                    ObjectId againstTeamId=getAgainstTeamId(goal.getMatchId(),forTeamId);
                    ObjectId tournamentId=matchRepository.findFirstById(goal.getMatchId()).getTournamentId();
                    goal.setTournamentId(tournamentId);
                    if(this.goalRepository.save(goal)!=null)
                    {
                        response.addProperty(AHLConstants.SUCCESS, AHLConstants.GOAL_CREATED);
                        return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
                    }
                    else{
                        response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                        return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                else {
                    response.addProperty(AHLConstants.ERROR,"Invalid Player/Team");
                    return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
                }
            }
            else {
                response.addProperty(AHLConstants.ERROR,"Minimum required fields not met");
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e)
        {
            response.addProperty(AHLConstants.ERROR,e.getMessage());
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping("/goals")
    public Iterable<Goal> getAllGoals(){
        return this.goalRepository.findAll();
    }
    @DeleteMapping(path = "/goal/{id}")
    public ResponseEntity<String> deleteGoal(@PathVariable ObjectId id)
    {
        JsonObject response=new JsonObject();
        try{
            Goal oldGoal=this.goalRepository.findFirstById(id);
            if(oldGoal!=null)
            {
                this.goalRepository.delete(oldGoal);
                response.addProperty(AHLConstants.SUCCESS, AHLConstants.GOAL_DELETED);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
            }
            else
            {
                response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch (Exception e)
        {
            response.addProperty(AHLConstants.ERROR,e.getMessage());
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("goal/{id}")
    public ResponseEntity<String> editGoal(@RequestBody Goal goal, @PathVariable ObjectId id)
    {
        JsonObject response=new JsonObject();
        try{
            if(Goal.validateGoal(goal)){
                Goal oldGoal=this.goalRepository.findFirstById(id);
                if(oldGoal!=null)
                {
                    if(AHLUtils.isPlayerExist(playerRepository,goal.getPlayerId())  && AHLUtils.isMatchExist(matchRepository,goal.getMatchId()))
                    {
                        ObjectId forTeamId=AHLUtils.getCurrentTeamByPlayer(playerTeamRepository,teamRepository,tournamentRepository,goal.getPlayerId());
                        ObjectId againstTeamId=getAgainstTeamId(goal.getMatchId(),forTeamId);
                        oldGoal.setMatchId(goal.getMatchId());
                        oldGoal.setPlayerId(goal.getPlayerId());
                        ObjectId tournamentId=matchRepository.findFirstById(goal.getMatchId()).getTournamentId();
                        oldGoal.setTournamentId(tournamentId);
                        if(this.goalRepository.save(oldGoal)!=null)
                        {
                            response.addProperty(AHLConstants.SUCCESS, AHLConstants.GOAL_UPDATED);
                            return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
                        }
                        else {
                            response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                            return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }
                    else
                    {
                        response.addProperty(AHLConstants.ERROR,"Invalid Player/Team/Match");
                        return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
                    }
                }
                else
                {
                    response.addProperty(AHLConstants.ERROR, "Goal not found");
                    return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
                }
            }
            else {
                response.addProperty(AHLConstants.ERROR, "Minimum required fields not met");
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e)
        {
            response.addProperty(AHLConstants.ERROR,e.getMessage());
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }

    private ObjectId getAgainstTeamId(ObjectId matchId, ObjectId forTeamId) throws InvalidDataException {
        Match match = matchRepository.findFirstById(matchId);
        if (match != null) {
            if (match.getTeam1().equals(forTeamId)) {
                return match.getTeam2();
            } else if (match.getTeam2().equals(forTeamId)) {
                return match.getTeam1();
            } else {
                throw new InvalidDataException("Team is not in the match");
            }
        } else {
            throw new InvalidDataException("Team not found");
        }
    }

}
