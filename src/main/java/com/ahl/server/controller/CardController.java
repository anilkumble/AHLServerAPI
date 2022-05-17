package com.ahl.server.controller;

import com.ahl.server.AHLConstants;
import com.ahl.server.AHLUtils;
import com.ahl.server.entity.Card;
import com.ahl.server.enums.CardType;
import com.ahl.server.repository.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                    ObjectId tournamentId=matchRepository.findFirstById(card.getMatchId()).getTournamentId();
                    card.setForTeamId(playerForTeamId);
                    card.setTournamentId(tournamentId);

                    Card newCard = this.cardRepository.save(card);
                    if(card != null)
                    {
                        Gson gson = new Gson();
                        JsonObject cardResponse = gson.fromJson(gson.toJson(newCard), JsonObject.class);
                        cardResponse.add("player", gson.fromJson(gson.toJson(playerRepository.findFirstById(card.getPlayerId())), JsonObject.class));
                        return new ResponseEntity<String>(cardResponse.toString(), null, HttpStatus.OK);
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
    @DeleteMapping(path = "/card/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable ObjectId id)
    {
        JsonObject response=new JsonObject();
        try {
            Card oldCard=this.cardRepository.findFirstById(id);
            if(oldCard!=null)
            {
                this.cardRepository.delete(oldCard);
                response.addProperty(AHLConstants.SUCCESS, AHLConstants.CARD_DELETED);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
            }
            else
            {
                response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch (Exception e)
        {
            response.addProperty(AHLConstants.ERROR,e.getMessage());
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping(path="/card/{id}")
    public ResponseEntity<String> editCard(@RequestBody Card card,@PathVariable ObjectId id)
    {
        JsonObject response=new JsonObject();
        try{
            if(card.validateCard(card))
            {
                Card oldCard=this.cardRepository.findFirstById(id);
                if(oldCard!=null)
                {
                    if(AHLUtils.isPlayerExist(playerRepository,card.getPlayerId()) && AHLUtils.isMatchExist(matchRepository,card.getMatchId()))
                    {
                        ObjectId playerForTeamId=AHLUtils.getCurrentTeamByPlayer(playerTeamRepository,teamRepository,tournamentRepository,card.getPlayerId());
                        card.setForTeamId(playerForTeamId);
                        oldCard.setForTeamId(card.getForTeamId());
                        oldCard.setCardType(card.getCardType());
                        oldCard.setPlayerId(card.getPlayerId());
                        oldCard.setMatchId(card.getMatchId());
                        ObjectId tournamentId=matchRepository.findFirstById(card.getMatchId()).getTournamentId();
                        oldCard.setTournamentId(tournamentId);
                        if(this.cardRepository.save(oldCard)!=null)
                        {
                            response.addProperty(AHLConstants.SUCCESS, AHLConstants.CARD_UPDATED);
                            return new ResponseEntity<String>(response.toString(), null, HttpStatus.OK);
                        }
                        else{
                            response.addProperty(AHLConstants.ERROR, AHLConstants.ERROR_MSG);
                            return new ResponseEntity<String>(response.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }
                    else
                    {
                        response.addProperty(AHLConstants.ERROR, "Invalid Player/Match");
                        return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
                    }
                }
                else
                {
                    response.addProperty(AHLConstants.ERROR, "Card not found");
                    return new ResponseEntity<String>(response.toString(), null, HttpStatus.BAD_REQUEST);
                }
            }
            else {
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
    @RequestMapping(path = "/cards")
    public Iterable<Card> getAllCards()
    {
        return this.cardRepository.findAll();
    }
    @RequestMapping(path = "player/cards/{tournamentId}/{playerId}")
    public String getCardsByPlayer(@PathVariable ObjectId tournamentId, @PathVariable ObjectId playerId)
    {
        int greenCount=0, yellowCount=0, redCount=0;
        List<Card> cards=this.cardRepository.findCardsByplayerId(playerId);
        for (Card c:cards)
        {
            ObjectId tId=AHLUtils.getTournamentByMatch(this.matchRepository,c.getMatchId());
            if(!tId.equals(tournamentId))
            {
            }
            else {
                if(c.getCardType()== CardType.GREEN)
                {
                    greenCount++;
                }
                else if(c.getCardType()== CardType.YELLOW)
                {
                    yellowCount++;
                }
                else {
                    redCount++;
                }
            }
        }
        JsonObject j = new JsonObject();
        j.add("green", new JsonPrimitive(greenCount));
        j.add("yellow", new JsonPrimitive(yellowCount));
        j.add("red", new JsonPrimitive(redCount));
        return j.toString();
    }
    @RequestMapping(path = "team/cards/{tournamentId}/{teamId}")
    public String getCardsByTeam(@PathVariable ObjectId tournamentId, @PathVariable ObjectId teamId)
    {
        int greenCount=0, yellowCount=0, redCount=0;
        List<Card> cards=this.cardRepository.findCardsByTeamId(teamId);
        for (Card c:cards)
        {
            ObjectId tId=AHLUtils.getTournamentByMatch(this.matchRepository,c.getMatchId());
            if(!tId.equals(tournamentId))
            {
            }
            else {
                if(c.getCardType()== CardType.GREEN)
                {
                    greenCount++;
                }
                else if(c.getCardType()== CardType.YELLOW)
                {
                    yellowCount++;
                }
                else {
                    redCount++;
                }
            }
        }
        JsonObject j = new JsonObject();
        j.add("green", new JsonPrimitive(greenCount));
        j.add("yellow", new JsonPrimitive(yellowCount));
        j.add("red", new JsonPrimitive(redCount));
        return j.toString();
    }
}
