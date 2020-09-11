package com.fplstatistics.app.player;

import com.fplstatistics.app.knapsack.DreamTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PlayersController {

    private final PlayerService playerService;
    private final DreamTeamService dreamTeamService;

    @Autowired
    public PlayersController(PlayerService playerService, DreamTeamService dreamTeamService) {
        this.playerService = playerService;
        this.dreamTeamService = dreamTeamService;
    }

    @GetMapping("/players")
    public List<PlayerDto> getPlayers(
            @RequestParam("fromSeason") String fromSeason,
            @RequestParam("toSeason") String toSeason,
            @RequestParam("fromRound") Integer fromRound,
            @RequestParam("toRound") Integer toRound,
            @RequestParam("sort") String sort,
            @RequestParam(value = "app", required = false) String appPercentage,
            @RequestParam(value = "team", required = false) List<String> teamShortName,
            @RequestParam(value = "position", required = false) String positionCode,
            @RequestParam(value = "homeGames", required = false) Boolean includeHomeGames,
            @RequestParam(value = "awayGames", required = false) Boolean includeAwayGames) {

        List<PlayerDto> players = playerService.getPlayers(fromSeason, toSeason, fromRound, toRound, teamShortName, positionCode, appPercentage, includeHomeGames, includeAwayGames);
        ToDoubleFunction<PlayerDto> function = getFunction(sort);
        players.sort(Comparator.comparingDouble(function).reversed());
        return players;
    }

    private ToDoubleFunction<PlayerDto> getFunction(@RequestParam("sort") String sort) {
        if (sort.equalsIgnoreCase("Cost")) {
            return PlayerDto::getCost;
        } else if (sort.equalsIgnoreCase("Appearances")) {
            return PlayerDto::getAppearances;
        } else if (sort.equalsIgnoreCase("Minutes")) {
            return PlayerDto::getMinutesPerAppearance;
        } else if (sort.equalsIgnoreCase("Points")) {
            return PlayerDto::getPoints;
        } else if (sort.equalsIgnoreCase("Points per Apps")) {
            return PlayerDto::getPointsPerAppearance;
        } else if (sort.equalsIgnoreCase("Value")) {
            return PlayerDto::getValue;
        } else if (sort.equalsIgnoreCase("Value per Apps")) {
            return PlayerDto::getValuePerAppearance;
        }
        return PlayerDto::getPoints;
    }
}
