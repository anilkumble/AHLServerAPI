package com.ahl.server.controller;

import com.ahl.server.entity.Player;
import com.google.gson.JsonObject;

import com.ahl.server.AHLConstants;
import com.ahl.server.entity.Tournament;
import com.ahl.server.repository.TournamentRepository;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class TournamentController {

  private TournamentRepository tournamentRepository;

  public TournamentController(TournamentRepository tournamentRepository) {
    this.tournamentRepository = tournamentRepository;
  }

  @GetMapping("/tournaments")
  public Iterable<Tournament> getAllTournaments(){
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

  @PutMapping(path="/tournament/{oldTournament}")
  public ResponseEntity<String> editTournament(@RequestBody Tournament tournament, @PathVariable Tournament oldTournament){
    JsonObject response = new JsonObject();
    if(oldTournament != null) {
      oldTournament.setSeason(tournament.getSeason());
      oldTournament.setTagline(tournament.getTagline());
      oldTournament.setTheme(tournament.getTheme());
      oldTournament.setIsLive(tournament.getIsLive());

      try {
        Tournament.validateTournament(oldTournament);
      }catch (Exception ex){
        response.addProperty(AHLConstants.ERROR,ex.getMessage());
        return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
      }
      this.tournamentRepository.save(oldTournament);
      response.addProperty(AHLConstants.SUCCESS, AHLConstants.PLAYER_UPDATED);
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.OK);

    }else{
      response.addProperty(AHLConstants.ERROR, AHLConstants.PLAYER_NOT_FOUND);
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping(path="/tournament/{oldTournament}")
  public ResponseEntity<String> deleteTournament(@PathVariable Tournament oldTournament){
    JsonObject response = new JsonObject();
    if(oldTournament != null) {
      this.tournamentRepository.delete(oldTournament);
      response.addProperty(AHLConstants.SUCCESS, AHLConstants.TOURNAMENT_DELETED);
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.OK);
    }else{
      response.addProperty(AHLConstants.ERROR, AHLConstants.TOURNAMENT_NOT_FOUND);
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
    }
  }

}
