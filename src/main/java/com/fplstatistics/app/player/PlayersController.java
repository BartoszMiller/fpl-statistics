package com.fplstatistics.app.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PlayersController {

    private final PlayerService playerService;

    @Autowired
    public PlayersController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public List<PlayerDto> getPlayers(
            @RequestParam("fromSeason") String fromSeason,
            @RequestParam("toSeason") String toSeason,
            @RequestParam("fromRound") Integer fromRound,
            @RequestParam("toRound") Integer toRound,
            @RequestParam(value = "team", required = false) String teamShortName,
            @RequestParam(value = "position", required = false) String positionCode
    ) {
        return playerService.getPlayers(fromSeason, toSeason, fromRound, toRound, teamShortName, positionCode);
    }
}
