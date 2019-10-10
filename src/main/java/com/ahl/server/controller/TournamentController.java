package com.ahl.server.controller;

import com.google.gson.JsonObject;

import com.ahl.server.AHLConstants;
import com.ahl.server.entity.Tournament;
import com.ahl.server.repository.TournamentRepository;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
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
public class TournamentController {

  private TournamentRepository tournamentRepository;

  public TournamentController(TournamentRepository tournamentRepository) {
    this.tournamentRepository = tournamentRepository;
  }

  @GetMapping("/tournaments")
  public List<Tournament> getAllTournaments(){
    return tournamentRepository.findAll();
  }

  @PostMapping(path = "/tournament")
  public ResponseEntity<String> addTournament(@RequestBody Tournament tournament) {
    JsonObject response = new JsonObject();

    try {
      Tournament.validateTournament(tournament);
    }catch (Exception ex) {
      response.addProperty(AHLConstants.ERROR,ex.getMessage());
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
    }
    this.tournamentRepository.save(tournament);
    response.addProperty(AHLConstants.SUCCESS, AHLConstants.TOURNAMENT_CREATED);
    return new ResponseEntity<String>(response.toString(),null, HttpStatus.OK);

  }

  @DeleteMapping(path="/tournament/{id}")
  public ResponseEntity<String> deleteTournament(@PathVariable ObjectId id){
    JsonObject response = new JsonObject();
    Tournament tournament = this.tournamentRepository.findFirstById(id);
    if(tournament != null) {

      this.tournamentRepository.delete(tournament);
      response.addProperty(AHLConstants.SUCCESS, AHLConstants.TOURNAMENT_DELETED);
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.OK);
    }else{
      response.addProperty(AHLConstants.ERROR, AHLConstants.TOURNAMENT_NOT_FOUND);
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
    }
  }

}
