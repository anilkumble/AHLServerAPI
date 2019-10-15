package com.ahl.server.controller;

import com.ahl.server.AHLConstants;
import com.ahl.server.entity.PlayerTeamRelation;
import com.ahl.server.repository.PlayerTeamRepository;
import com.google.gson.JsonObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PlayerTeamController {
    @Autowired
    private PlayerTeamRepository playerTeamRepository;

    @RequestMapping("/get-all-players/{id}")
    public int findAllPlayerinaTeam(@PathVariable ObjectId id)
    {
        List<PlayerTeamRelation> players= playerTeamRepository.findAllPlayerinaTeam(id);
        return players.size();
    }
    @PostMapping(path = "/player-relation")
    public ResponseEntity<String> addPlayerRealtion(@RequestBody PlayerTeamRelation playerTeamRelation)
    {
        JsonObject response = new JsonObject();
        try
        {
            if (this.playerTeamRepository.save(playerTeamRelation)!=null)
            {
                response.addProperty(AHLConstants.SUCCESS, AHLConstants.PLAYER_RELATION_CREATED);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
            }
            else
            {
                response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e)
        {
            response.addProperty(AHLConstants.ERROR,e.getMessage());
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }
}
