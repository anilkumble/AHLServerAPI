package com.ahl.server.controller;

import com.ahl.server.AHLConstants;
import com.ahl.server.repository.CardRepository;
import com.ahl.server.repository.MatchRepository;
import com.ahl.server.repository.PlayerRepository;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping(path= "/card")
    private ResponseEntity<String> addCard()
    {
        JsonObject response=new JsonObject();
        try {

        }
        catch (Exception e)
        {
            response.addProperty(AHLConstants.ERROR,e.getMessage());
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
