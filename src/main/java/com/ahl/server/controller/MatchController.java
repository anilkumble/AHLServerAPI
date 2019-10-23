package com.ahl.server.controller;

import com.ahl.server.AHLConstants;
import com.ahl.server.AHLUtils;
import com.ahl.server.entity.Match;
import com.ahl.server.entity.Team;
import com.ahl.server.entity.Tournament;
import com.ahl.server.repository.MatchRepository;
import com.ahl.server.repository.TeamRepository;
import com.ahl.server.repository.TournamentRepository;
import com.google.gson.JsonObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MatchController {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private MatchRepository matchRepository;

    @PostMapping(path = "/match")
    public ResponseEntity<String> addMatch(@RequestBody  Match match)
    {
        JsonObject response = new JsonObject();
        try {
            Match.validateMatch(match);
            if(AHLUtils.isTeamExist(teamRepository,match.getTeam1()) && AHLUtils.isTeamExist(teamRepository,match.getTeam2())
               && AHLUtils.isTournamentExist(tournamentRepository,match.getTournamentId()) && !match.getTeam2().equals(match.getTeam1()))
            {
                if (this.matchRepository.save(match) != null)
                {
                    response.addProperty(AHLConstants.SUCCESS, AHLConstants.MATCH_CREATED);
                    return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
                }
                else {
                    response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                    return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }else{
                response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception ex){
            response.addProperty(AHLConstants.ERROR,ex.getMessage());
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping("/matches")
    public Iterable<Match> getAllMatches()
    {
        return this.matchRepository.findAll();
    }
    @PutMapping("/match/{id}")
    public ResponseEntity<String> editMatch(@RequestBody Match match,@PathVariable ObjectId id)
    {
        JsonObject response=new JsonObject();
        Match oldMatch=this.matchRepository.findFirstById(id);
        if(oldMatch!=null && !match.getTeam1().equals(match.getTeam2()))
        {
            oldMatch.setResult(match.getResult());
            oldMatch.setTeam1(match.getTeam1());
            oldMatch.setTeam2(match.getTeam2());
            oldMatch.setTournamentId(match.getTournamentId());
            try {
                Match.validateMatch(oldMatch);
            }
            catch (Exception e)
            {
                response.addProperty(AHLConstants.ERROR,e.getMessage());
                return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
            }
            this.matchRepository.save(oldMatch);
            response.addProperty(AHLConstants.SUCCESS, AHLConstants.MATCH_UPDATED);
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.OK);
        }
        else{
            response.addProperty(AHLConstants.ERROR, AHLConstants.MATCH_NOT_FOUND);
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping(path="/match/{id}")
    public ResponseEntity<String> deleteMatch(@PathVariable ObjectId id)
    {
        JsonObject response=new JsonObject();
        Match oldMatch=this.matchRepository.findFirstById(id);
        if(oldMatch!=null)
        {
            this.matchRepository.delete(oldMatch);
            response.addProperty(AHLConstants.SUCCESS, AHLConstants.MATCH_DELETED);
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.OK);
        }
        else
        {
            response.addProperty(AHLConstants.ERROR, AHLConstants.MATCH_NOT_FOUND);
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }
}
