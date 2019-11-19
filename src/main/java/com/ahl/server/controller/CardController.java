package com.ahl.server.controller;

import com.ahl.server.AHLConstants;
import com.ahl.server.AHLUtils;
import com.ahl.server.entity.Card;
import com.ahl.server.entity.Goal;
import com.ahl.server.repository.*;
import com.google.gson.JsonObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TournamentController tournamentController;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerTeamRepository playerTeamRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @PostMapping(path= "/card")
    private ResponseEntity<String> addCard(@RequestBody Card card)
    {
        JsonObject response=new JsonObject();
        try {
            if(card.validateCard(card))
            {
                if(AHLUtils.isPlayerExist(playerRepository,card.getPlayerId()) && AHLUtils.isMatchExist(matchRepository,card.getMatchId()))
                {
                    ObjectId playerForTeamId=AHLUtils.getCurrentTeamByPlayer(playerTeamRepository,teamRepository,tournamentRepository,card.getPlayerId());
                    card.setForTeamId(playerForTeamId);
                    if(this.cardRepository.save(card)!=null)
                    {
                        response.addProperty(AHLConstants.SUCCESS, AHLConstants.CARD_CREATED);
                        return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
                    }
                    else{
                        response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                        return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                else {
                    response.addProperty(AHLConstants.ERROR, "Invalid Player/Match");
                    return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
                }
            }
            else
            {
                response.addProperty(AHLConstants.ERROR,"Minimum required fields not met");
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e)
        {
            response.addProperty(AHLConstants.ERROR,e.getMessage());
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }
}
