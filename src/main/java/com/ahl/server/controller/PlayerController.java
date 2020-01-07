package com.ahl.server.controller;


import com.ahl.server.AHLUtils;
import com.ahl.server.entity.*;
import com.ahl.server.repository.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ByteArray;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.google.gson.JsonObject;

import com.ahl.server.AHLConstants;
import com.ahl.server.exception.InvalidDataException;

import com.mongodb.DBObject;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api")
public class PlayerController {

  @Autowired
  private PlayerRepository playerRepository;
  @Autowired
  private PlayerTeamRepository playerTeamRepository;
  @Autowired
  private TournamentRepository tournamentRepository;
  @Autowired
  private TeamRepository teamRepository;
  @Autowired
  private GoalRepository goalRepository;

  @GetMapping(value = "/players", produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Player> getAllPlayers(){
    return  playerRepository.findAll();
  }

  @GetMapping("/players/{team}")
  public ResponseEntity<Object> getAllPlayersByTournamentAndTeamId(@PathVariable Team team){
    JsonObject response = new JsonObject();
    if(team != null) {
      List<PlayerTeamRelation> relations = playerTeamRepository.findAllRelationByTeamId(team.getId());
      List<PlayerResult> playerResultList = new ArrayList<>();
      for(PlayerTeamRelation relation : relations){
        Player p = playerRepository.findFirstById(relation.getPlayerId());
        int goalCount = goalRepository.findAllGoalsByplayerId(relation.getPlayerId()).size();
        playerResultList.add(new PlayerResult(p,goalCount,1,0,0));
      }
      return new ResponseEntity<Object>(playerResultList, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }else{
      response.addProperty(AHLConstants.ERROR, AHLConstants.TEAM_NOT_FOUND);
      return new ResponseEntity<Object>(response, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(path = "/player")
  public ResponseEntity<String> addPlayer(@RequestBody Player player){
    JsonObject response = new JsonObject();
    try {
      Player.validatePlayer(player);
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

  @PostMapping(path = "/bulk/player")
  public ResponseEntity<String> addBulkPlayer(@RequestBody Player player){
    JsonObject response = new JsonObject();
    try {
      Player.validatePlayer(player);
      if(this.playerRepository.save(player) != null) {
        response.addProperty(AHLConstants.SUCCESS, player.toString());
        return new ResponseEntity<String>(player.getId().toString(), null, HttpStatus.OK);
      }else{
        response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
        return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }catch (Exception ex){
      response.addProperty(AHLConstants.ERROR,ex.getMessage());
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path="/player/{oldPlayer}")
  public ResponseEntity<String> editPlayer(@RequestBody Player player, @PathVariable Player oldPlayer){
    JsonObject response = new JsonObject();
    if(oldPlayer != null) {
      oldPlayer.setName(player.getName());
      oldPlayer.setEmailId(player.getEmailId());
      oldPlayer.setAge(player.getAge());
      oldPlayer.setGraduatedYear(player.getGraduatedYear());
      oldPlayer.setDepartment(player.getDepartment());
      oldPlayer.setPhoneNo(player.getPhoneNo());
      oldPlayer.setPosition(player.getPosition());

      try {
        Player.validatePlayer(oldPlayer);
      }catch (Exception ex){
        response.addProperty(AHLConstants.ERROR,ex.getMessage());
        return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
      }
      this.playerRepository.save(oldPlayer);
      response.addProperty(AHLConstants.SUCCESS, AHLConstants.PLAYER_UPDATED);
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.OK);

    }else{
      response.addProperty(AHLConstants.ERROR, AHLConstants.PLAYER_NOT_FOUND);
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping(path="/player/{oldPlayer}")
  public ResponseEntity<String> deletePlayer(@PathVariable Player oldPlayer){
    JsonObject response = new JsonObject();
    if(oldPlayer != null) {
      this.playerRepository.delete(oldPlayer);
      response.addProperty(AHLConstants.SUCCESS, AHLConstants.PLAYER_DELETED);
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.OK);
    }else{
      response.addProperty(AHLConstants.ERROR, AHLConstants.PLAYER_NOT_FOUND);
      return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
    }
  }
  @PutMapping(path="/player/upload/{byteArray}")
  public ResponseEntity<String> uploadPlayerImage(@RequestBody byte[] byteArray) throws IOException {

    try{
      FileInputStream firebaseToken = new FileInputStream("src/main/firebase.json");
      FirebaseOptions options = new FirebaseOptions.Builder()
              .setCredentials(GoogleCredentials.fromStream(firebaseToken))
              .setStorageBucket("dileepkosur3524.appspot.com")
              .build();
      FirebaseApp fireApp = FirebaseApp.initializeApp(options);
      StorageClient storageClient = StorageClient.getInstance(fireApp);
      ByteArrayInputStream test=new ByteArrayInputStream(byteArray);

      String blobString = "Player/" + "barca.JPEG";
      Blob blob = storageClient.bucket("dileepkosur3524.appspot.com")
              .create(blobString, test, Bucket.BlobWriteOption.userProject("dileepkosur3524"));
      blob.getStorage().createAcl(blob.getBlobId(), Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
      System.out.println(blob.getMediaLink());
      return new ResponseEntity<String>(blob.getMediaLink(),null, HttpStatus.OK);
    }
    catch(Exception e)
    {
      System.out.println(e);
    }


    return null;
  }
}
