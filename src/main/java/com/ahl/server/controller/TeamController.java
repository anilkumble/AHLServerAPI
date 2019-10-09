package com.ahl.server.controller;

import com.google.gson.JsonObject;

import com.ahl.server.AHLConstants;
import com.ahl.server.entity.League;
import com.ahl.server.entity.Player;
import com.ahl.server.entity.Team;
import com.ahl.server.repository.PlayerRepository;
import com.ahl.server.repository.TeamRepository;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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

  private TeamRepository teamRepository;

  public TeamController(TeamRepository teamRepository) {
    this.teamRepository = teamRepository;
  }

  @GetMapping("/teams")
  public List<Team> getAllTeams(){
    return teamRepository.findAll();
  }

  @PostMapping(path = "/team",consumes = "application/json")
  public ResponseEntity<String> addTeam(@RequestBody Team team) {
    JsonObject response = new JsonObject();

    try {
      Team.validateTeam(team);
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
