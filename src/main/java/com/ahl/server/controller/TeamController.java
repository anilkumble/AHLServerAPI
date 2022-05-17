package com.ahl.server.controller;

import com.ahl.server.AHLUtils;
import com.ahl.server.entity.Tournament;
import com.ahl.server.repository.PointsRepository;
import com.ahl.server.repository.TournamentRepository;
import com.google.gson.JsonObject;

import com.ahl.server.AHLConstants;
import com.ahl.server.entity.Team;
import com.ahl.server.repository.TeamRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/api")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private PointsRepository pointsRepository;

    @GetMapping("/teams")
    public Iterable<Team> getTeamsByTournament(@RequestParam Tournament tournament, String category) {

        List<Team> teams = teamRepository.findTeamsByTournament(tournament.getId());
        if(category.equals(AHLConstants.ALL)){
            return teams;
        }
        List<Team> teamList = new ArrayList<>();
        for(Team team : teams){
            if(team.getTeamTag().getCategory().equals(category)){
                teamList.add(team);
            }
        }
        return teamList;
    }

    @PostMapping(path = "/team", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addTeam(@RequestBody Team team) {
        JsonObject response = new JsonObject();
        try {
            Team.validateTeam(team);
            AHLUtils.isTournamentExist(tournamentRepository, team.getTournamentId());
        } catch (Exception ex) {
            response.addProperty(AHLConstants.ERROR, ex.getMessage());
            return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
        }
        if(this.teamRepository.save(team)!=null)
        {
            response.addProperty(AHLConstants.SUCCESS, AHLConstants.TEAM_CREATED);
            return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
        } else{
            response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
            return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(path = "/team/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable ObjectId id) {
        JsonObject response = new JsonObject();
        Team team = this.teamRepository.findFirstById(id);
        if (team != null) {
            this.teamRepository.delete(team);
            response.addProperty(AHLConstants.SUCCESS, AHLConstants.TEAM_DELETED);
            return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
        } else {
            response.addProperty(AHLConstants.ERROR, AHLConstants.TEAM_NOT_FOUND);
            return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/team/{teamId}")
    public ResponseEntity<Team> getTeamByID(@PathVariable ObjectId teamId){

        Team team = teamRepository.findFirstById(teamId);
        return new ResponseEntity<>(team, null, HttpStatus.OK);
    }

}
