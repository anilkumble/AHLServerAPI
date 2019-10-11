package com.ahl.server.controller;


import com.ahl.server.AHLConstants;
import com.ahl.server.entity.Goal;
import com.ahl.server.repository.GoalRepository;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GoalController {

    private GoalRepository goalRepository;

    public GoalController(GoalRepository goalRepository)
    {
        this.goalRepository=goalRepository;
    }
    @PostMapping(path = "/goal",consumes = "application/json")
    public ResponseEntity<String> addGoal(@RequestBody Goal goal)
    {
        JsonObject response = new JsonObject();
        try
        {
            Goal.validateGoal(goal);

        }
        catch (Exception e)
        {
            response.addProperty(AHLConstants.ERROR,e.getMessage());
            return new ResponseEntity<String>(response.toString(),null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>(response.toString(),null, HttpStatus.OK);
    }
}
