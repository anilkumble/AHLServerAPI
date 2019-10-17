package com.ahl.server.controller;

import com.ahl.server.AHLUtils;
import com.ahl.server.repository.TournamentRepository;
import com.google.gson.JsonObject;

import com.ahl.server.AHLConstants;
import com.ahl.server.entity.Team;
import com.ahl.server.repository.TeamRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class TeamController {

  @Autowired
  private TeamRepository teamRepository;
  @Autowired
  private TournamentRepository tournamentRepository;

  @GetMapping("/teams")
  public Iterable<Team> getAllTeams(){
    return teamRepository.findAll();
  }

  @PostMapping(path = "/team",consumes= MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> addTeam(@RequestBody Team team) {
    JsonObject response = new JsonObject();

    try {
      Team.validateTeam(team);
      AHLUtils.isTournamentExist(tournamentRepository, team.getTournamentId());
    }catch (Exception ex) {
      response.addProperty(AHLConstants.ERROR,ex.getMessage());
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
    }
    this.teamRepository.save(team);
    response.addProperty(AHLConstants.SUCCESS, AHLConstants.TEAM_CREATED);
    return new ResponseEntity<String>(response.toString(),null, HttpStatus.OK);

  }


  @DeleteMapping(path="/team/{id}")
  public ResponseEntity<String> deleteTeam(@PathVariable ObjectId id){
    JsonObject response = new JsonObject();
    Team team = this.teamRepository.findFirstById(id);
    if(team != null) {
      this.teamRepository.delete(team);
      response.addProperty(AHLConstants.SUCCESS, AHLConstants.TEAM_DELETED);
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.OK);
    }else{
      response.addProperty(AHLConstants.ERROR, AHLConstants.TEAM_NOT_FOUND);
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
    }
  }

}
