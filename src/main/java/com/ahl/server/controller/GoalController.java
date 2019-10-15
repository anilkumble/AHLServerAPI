package com.ahl.server.controller;


import com.ahl.server.AHLConstants;
import com.ahl.server.AHLUtils;
import com.ahl.server.entity.Goal;
import com.ahl.server.entity.PlayerRelation;
import com.ahl.server.repository.*;
import com.google.gson.JsonObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GoalController {
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerRelationRepository playerRelationRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MatchRepository matchRepository;

    public GoalController(GoalRepository goalRepository)
    {
        this.goalRepository=goalRepository;
    }
    @RequestMapping("/goalsByPlayerId/{id}")
    public int getGoalsByPlayerId(@PathVariable ObjectId id)
    {
        List<Goal> goals=goalRepository.findAllGoalsByplayerId(id);
        return goals.size();
    }

    @RequestMapping("/testgoal/{goal}")
    public boolean test(@PathVariable Goal goal)
    {
        return AHLUtils.isPlayerExistInTeam(playerRelationRepository,goal.getForTeamId(), goal.getPlayerId());
    }

    @PostMapping(path = "/goal")
    public ResponseEntity<String> addGoal(@RequestBody Goal goal)
    {
        JsonObject response = new JsonObject();
        try
        {
            if(Goal.validateGoal(goal))
            {
                List<PlayerRelation> againstTeamIdList=playerRelationRepository.findAllPlayerinaTeam(goal.getForTeamId());
                if(AHLUtils.isPlayerExist(playerRepository,goal.getPlayerId()) && AHLUtils.isTeamExist(teamRepository,goal.getForTeamId()) && AHLUtils.isMatchExist(matchRepository,goal.getMatchId()))
                {
                    if(AHLUtils.isPlayerExistInTeam(playerRelationRepository,goal.getForTeamId(),goal.getPlayerId()))
                    {
                        ObjectId againstTeamId=AHLUtils.getAgainstTeamId(matchRepository,goal.getMatchId(),goal.getForTeamId());
                        goal.setAgainstTeamId(againstTeamId);
                        if(AHLUtils.isTeamExistInMatch(matchRepository,goal.getMatchId(),goal.getForTeamId()) &&
                                AHLUtils.isTeamExistInMatch(matchRepository,goal.getMatchId(),goal.getAgainstTeamId()))
                        {
                            if(!goal.getForTeamId().equals(goal.getAgainstTeamId()))
                            {
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
                            else
                            {
                                response.addProperty(AHLConstants.ERROR,"Both teams are equal");
                                return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
                            }

                        }
                        else {
                            response.addProperty(AHLConstants.ERROR, "Team not found in Match");
                            return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }
                    else{
                        response.addProperty(AHLConstants.ERROR, "Player not found in team");
                        return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                else {
                    response.addProperty(AHLConstants.ERROR,"Invalid Player/Team/Match");
                    return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            else {
                response.addProperty(AHLConstants.ERROR,"Minimum required fields not met");
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
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
        Goal oldGoal=this.goalRepository.findFirstById(id);
        try{
            ObjectId againstTeamId=AHLUtils.getAgainstTeamId(matchRepository,goal.getMatchId(),goal.getForTeamId());
            ObjectId oldagainstTeamId=AHLUtils.getAgainstTeamId(matchRepository,oldGoal.getMatchId(),oldGoal.getForTeamId());
            if(Goal.validateGoal(goal)){
                if(AHLUtils.isPlayerExist(playerRepository,goal.getPlayerId()) && AHLUtils.isTeamExist(teamRepository,goal.getForTeamId()) && AHLUtils.isMatchExist(matchRepository,goal.getMatchId()))
                {
                    if(AHLUtils.isPlayerExistInTeam(playerRelationRepository,goal.getForTeamId(),goal.getPlayerId()))
                    {
                        goal.setAgainstTeamId(againstTeamId);
                        if(AHLUtils.isTeamExistInMatch(matchRepository,goal.getMatchId(),goal.getForTeamId()) &&
                                AHLUtils.isTeamExistInMatch(matchRepository,goal.getMatchId(),goal.getAgainstTeamId()))
                        {
                            if(oldGoal!=null && !againstTeamId.equals(goal.getForTeamId()))
                            {
                                oldGoal.setMatchId(goal.getMatchId());
                                oldGoal.setAgainstTeamId(goal.getAgainstTeamId());
                                oldGoal.setForTeamId(goal.getForTeamId());
                                oldGoal.setPlayerId(goal.getPlayerId());
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
                                response.addProperty(AHLConstants.ERROR, "Goal not found");
                                return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
                            }

                        }
                        else {
                            response.addProperty(AHLConstants.ERROR,"Team not found in Match");
                            return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }
                    else
                    {
                        response.addProperty(AHLConstants.ERROR,"Player not found in team");
                        return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                else
                {
                    response.addProperty(AHLConstants.ERROR,"Invalid Player/Team/Match");
                    return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            else {
                response.addProperty(AHLConstants.ERROR, "Minimum required fields not met");
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch (Exception e)
        {
            response.addProperty(AHLConstants.ERROR,e.getMessage());
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }
}
