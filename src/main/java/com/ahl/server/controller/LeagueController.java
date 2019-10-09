package com.ahl.server.controller;

import com.google.gson.JsonObject;

import com.ahl.server.AHLConstants;
import com.ahl.server.entity.League;
import com.ahl.server.entity.Team;
import com.ahl.server.repository.LeagueRepository;
import com.ahl.server.repository.PlayerRepository;
import com.ahl.server.repository.TeamRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class LeagueController {

  private LeagueRepository leagueRepository;

  public LeagueController(LeagueRepository leagueRepository) {
    this.leagueRepository = leagueRepository;
  }

  @GetMapping("/leagues")
  public List<League> getAllLeagues(){
    return leagueRepository.findAll();
  }

  @PostMapping(path = "/league")
  public ResponseEntity<String> addTeam(@RequestBody League league) {
    JsonObject response = new JsonObject();

    try {
      League.validateLeague(league);
    }catch (Exception ex) {
      response.addProperty(AHLConstants.ERROR,ex.getMessage());
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
    }
    this.leagueRepository.save(league);
    response.addProperty(AHLConstants.SUCCESS, AHLConstants.TEAM_CREATED);
    return new ResponseEntity<String>(response.toString(),null, HttpStatus.OK);

  }

}
