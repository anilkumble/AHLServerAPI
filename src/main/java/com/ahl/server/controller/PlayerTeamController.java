package com.ahl.server.controller;

import com.ahl.server.AHLConstants;
import com.ahl.server.AHLUtils;
import com.ahl.server.entity.PlayerTeamRelation;
import com.ahl.server.entity.Team;
import com.ahl.server.entity.Tournament;
import com.ahl.server.repository.PlayerRepository;
import com.ahl.server.repository.PlayerTeamRepository;
import com.ahl.server.repository.TeamRepository;
import com.ahl.server.repository.TournamentRepository;
import com.google.gson.JsonObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PlayerTeamController {
    @Autowired
    private PlayerTeamRepository playerTeamRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TournamentRepository tournamentRepository;

    @GetMapping(value = "/relations", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterator<PlayerTeamRelation> getAllRelations(){
        return  playerTeamRepository.findAll().iterator();
    }

    @PostMapping(path = "/player-relation")
    public ResponseEntity<String> addPlayerTeamRelation(@RequestBody PlayerTeamRelation playerTeamRelation)
    {
        JsonObject response = new JsonObject();
        try
        {
            PlayerTeamRelation.validatePlayerTeam(playerTeamRelation);
            AHLUtils.isPlayerExist(playerRepository, playerTeamRelation.getPlayerId());
            AHLUtils.isTeamExist(teamRepository, playerTeamRelation.getTeamId());

            checkRelationExist(playerTeamRelation);

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

    private void checkRelationExist(PlayerTeamRelation playerTeamRelation)throws Exception {

        Team newTeam = this.teamRepository.findFirstById(playerTeamRelation.getTeamId());
        ObjectId newTeamTournamentId = newTeam.getTournamentId();
        Tournament newTournament = tournamentRepository.findFirstById(newTeamTournamentId);
        List<PlayerTeamRelation> relations = playerTeamRepository.findAllRelationByPlayerId(playerTeamRelation.getPlayerId());
        for(PlayerTeamRelation relation : relations){
            Team oldTeam = this.teamRepository.findFirstById(relation.getTeamId());
            ObjectId oldTeamTournamentId = oldTeam.getTournamentId();
            if(oldTeamTournamentId.equals(newTeamTournamentId)){
                throw new Exception("Player already mapped to "+oldTeam.getName()+" for tournament "+newTournament.getSeason());
            }
        }
    }

    @PutMapping(path="/player-relation/{oldPTRelation}")
    public ResponseEntity<String> editPlayerTeamRelation(@RequestBody PlayerTeamRelation ptRelation, @PathVariable PlayerTeamRelation oldPTRelation){
        JsonObject response = new JsonObject();
        if(oldPTRelation != null) {
            oldPTRelation.setPlayerId(ptRelation.getPlayerId());
            oldPTRelation.setTeamId(ptRelation.getTeamId());
            try {
                PlayerTeamRelation.validatePlayerTeam(oldPTRelation);
            }catch (Exception ex){
                response.addProperty(AHLConstants.ERROR,ex.getMessage());
                return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
            }
            if (this.playerTeamRepository.save(oldPTRelation)!=null)
            {
                response.addProperty(AHLConstants.SUCCESS, AHLConstants.PLAYER_RELATION_CREATED);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
            }
            else
            {
                response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
            }

        }else{
            response.addProperty(AHLConstants.ERROR, AHLConstants.PLAYER_NOT_FOUND);
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path="/player-relation/{oldPlayer}")
    public ResponseEntity<String> deletePlayerTeamRelation(@PathVariable PlayerTeamRelation oldPlayer){
        JsonObject response = new JsonObject();
        if(oldPlayer != null) {
            this.playerTeamRepository.delete(oldPlayer);
            response.addProperty(AHLConstants.SUCCESS, AHLConstants.PLAYER_DELETED);
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.OK);
        }else{
            response.addProperty(AHLConstants.ERROR, AHLConstants.PLAYER_NOT_FOUND);
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }

}
