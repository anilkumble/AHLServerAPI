package com.ahl.server.controller;

import com.google.gson.JsonObject;

import com.ahl.server.AHLConstants;
import com.ahl.server.entity.Tournament;
import com.ahl.server.repository.TournamentRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

  @GetMapping("/tournament")
  public Tournament getLiveTournament(@RequestParam boolean status){
    return tournamentRepository.findLiveTournament(status);
  }

  @PostMapping(path = "/tournament")
  public ResponseEntity<String> addTournament(@Valid @RequestBody Tournament tournament) {
    this.tournamentRepository.save(tournament);
    return new ResponseEntity<String>(AHLConstants.TOURNAMENT_CREATED, HttpStatus.OK);
  }

  @PutMapping(path="/tournament/{oldTournament}")
  public ResponseEntity<String> editTournament(@RequestBody Tournament tournament, @PathVariable Tournament oldTournament){
    JsonObject response = new JsonObject();
    if(oldTournament != null) {
      oldTournament.setSeason(tournament.getSeason());
      oldTournament.setTagLine(tournament.getTagLine());
      oldTournament.setTheme(tournament.getTheme());
      oldTournament.setLive(tournament.isLive());
      oldTournament.setTournamentName(tournament.getTournamentName());

      try {
//        Tournament.validateTournament(oldTournament);
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
