package com.ahl.server.controller;


import com.google.gson.JsonObject;

import com.ahl.server.AHLConstants;
import com.ahl.server.entity.Player;
import com.ahl.server.exception.InSufficientDataException;
import com.ahl.server.repository.PlayerRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class PlayerController {

  private PlayerRepository playerRepository;

  public PlayerController(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @GetMapping("/players")
  public List<Player> getAllPlayers(){
    return playerRepository.findAll();
  }

  @PostMapping(path = "/players", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<String> addPlayer(@RequestBody MultiValueMap<String,String> playerData){
    JsonObject result=new JsonObject();
    try {
      Player.validatePlayer(playerData);

    }catch (InSufficientDataException insd) {

      result.addProperty(AHLConstants.ERROR,insd.getMessage());
      return new ResponseEntity<String>(result.toString(),null, HttpStatus.BAD_REQUEST);
    }

    Player player = new Player(playerData.getFirst(AHLConstants.NAME), playerData.getFirst(AHLConstants.EMAIL_ID));
    Player response = this.playerRepository.save(player);
    result.addProperty(AHLConstants.NAME, response.getName());
    result.addProperty(AHLConstants.NAME, response.getEmail_id());

    return new ResponseEntity<String>(result.toString(),null, HttpStatus.OK);
  }

}
