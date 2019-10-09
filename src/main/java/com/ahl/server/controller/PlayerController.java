package com.ahl.server.controller;


import com.google.gson.JsonObject;

import com.ahl.server.AHLConstants;
import com.ahl.server.entity.Player;
import com.ahl.server.exception.InSufficientDataException;
import com.ahl.server.exception.InvalidDataException;
import com.ahl.server.repository.LeagueRepository;
import com.ahl.server.repository.PlayerRepository;
import com.ahl.server.repository.TeamRepository;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class PlayerController {

  private PlayerRepository playerRepository;
  private LeagueRepository leagueRepository;
  private TeamRepository teamRepository;

  public PlayerController(PlayerRepository playerRepository, LeagueRepository leagueRepository, TeamRepository teamRepository) {
    this.playerRepository = playerRepository;
    this.leagueRepository = leagueRepository;
    this.teamRepository   = teamRepository;
  }

  @GetMapping("/players")
  public List<Player> getAllPlayers(){
    return playerRepository.findAll();
  }

  @PostMapping(path = "/player")
  public ResponseEntity<String> addPlayer(@RequestBody Player player){
    JsonObject response = new JsonObject();
    try {
      Player.validatePlayer(player);
      if(player.getTeamDetails() != null){
        if(!ObjectUtils.allNotNull(
            teamRepository.findFirstById( player.getTeamDetails().get(0).getTeamId()),
            leagueRepository.findFirstById( player.getTeamDetails().get(0).getLeagueId()))){
          throw new InvalidDataException(AHLConstants.INVALID_DATA);
        }
      }
      if(this.playerRepository.save(player) != null) {
        response.addProperty(AHLConstants.SUCCESS, AHLConstants.PLAYER_CREATED);
        return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
      }else{
        response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
        return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }catch (Exception ex){
      response.addProperty(AHLConstants.ERROR,ex.getMessage());
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path="player/{emailId}")
  public ResponseEntity<String> editPlayer(@RequestBody Player player, @PathVariable String emailId){
    JsonObject response = new JsonObject();
    Player oldPlayer = this.playerRepository.findFirstByEmailId(emailId);
    if(oldPlayer != null) {
      oldPlayer.setName(player.getName());
      oldPlayer.setEmailId(emailId);
      oldPlayer.setAge(player.getAge());
      oldPlayer.setGraduatedYear(player.getGraduatedYear());
      oldPlayer.setDepartment(player.getDepartment());
      oldPlayer.setPhoneNo(player.getPhoneNo());
      oldPlayer.setPosition(player.getPosition());
      oldPlayer.getTeamDetails().add(player.getTeamDetails().get(0));

      this.playerRepository.save(oldPlayer);
      response.addProperty(AHLConstants.SUCCESS, AHLConstants.PLAYER_UPDATED);
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.OK);

    }else{
      response.addProperty(AHLConstants.ERROR, AHLConstants.PLAYER_NOT_FOUND);
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping(path="player/{emailId}")
  public ResponseEntity<String> deletePlayer(@PathVariable String emailId){
    JsonObject response = new JsonObject();
    Player oldPlayer = this.playerRepository.findFirstByEmailId(emailId);
    if(oldPlayer != null) {

      this.playerRepository.delete(oldPlayer);
      response.addProperty(AHLConstants.SUCCESS, AHLConstants.PLAYER_DELETED);

      return new ResponseEntity<String>(response.toString(),null, HttpStatus.OK);
    }else{
      response.addProperty(AHLConstants.ERROR, AHLConstants.PLAYER_NOT_FOUND);
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
    }
  }

}
