package com.fplstatistics.app.knapsack;

import com.fplstatistics.app.player.PlayerDto;
import com.fplstatistics.app.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class MoneyballController {

    private final PlayerService playerService;
    private final DreamTeamService dreamTeamService;
    private final BlackWhiteListService blackWhiteListService;

    @Autowired
    public MoneyballController(PlayerService playerService, DreamTeamService dreamTeamService, BlackWhiteListService blackWhiteListService) {
        this.playerService = playerService;
        this.dreamTeamService = dreamTeamService;
        this.blackWhiteListService = blackWhiteListService;
    }

    @GetMapping("/blacklist/add/{playerId}")
    public void addToBlackList(@PathVariable Integer playerId) {
        blackWhiteListService.addToBlackList(playerId);
    }

    @GetMapping("/blacklist/remove/{playerId}")
    public void removeFromBlackList(@PathVariable Integer playerId) {
        blackWhiteListService.removeFromBlackList(playerId);
    }

    @GetMapping("/whitelist/add/{playerId}")
    public void addToWhiteList(@PathVariable Integer playerId) {
        blackWhiteListService.addToWhiteList(playerId);
    }

    @GetMapping("/whitelist/remove/{playerId}")
    public void removeFromWhiteList(@PathVariable Integer playerId) {
        blackWhiteListService.removeFromWhiteList(playerId);
    }

    @GetMapping("/moneyball")
    public DreamTeam getMoneyball(
            @RequestParam("budget") Integer budget,
            @RequestParam("fromSeason") String fromSeason,
            @RequestParam("toSeason") String toSeason,
            @RequestParam("fromRound") Integer fromRound,
            @RequestParam("toRound") Integer toRound,
            @RequestParam("sort") String strategy,
            @RequestParam(value = "app", required = false) String appPercentage,
            @RequestParam(value = "team", required = false) List<String> teamShortName,
            @RequestParam(value = "homeGames", required = false) Boolean includeHomeGames,
            @RequestParam(value = "awayGames", required = false) Boolean includeAwayGames) {

        List<PlayerDto> players = playerService.getPlayers(fromSeason, toSeason, fromRound, toRound, teamShortName, null, appPercentage, includeHomeGames, includeAwayGames);
        ToDoubleFunction<PlayerDto> function = getFunction(strategy);
        players.sort(Comparator.comparingDouble(function).reversed());

        List<PlayerDto> blackList = blackWhiteListService.getBlackList();
        List<PlayerDto> whiteList = blackWhiteListService.getWhiteList();
        players.removeAll(blackList);

        DreamTeam dreamTeam = dreamTeamService.getDreamEleven(budget, players, function, whiteList);
        dreamTeam.setBlackList(blackList);
        dreamTeam.setWhiteList(whiteList);
        return dreamTeam;
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
