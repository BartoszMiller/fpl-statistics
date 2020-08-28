package com.fplstatistics.app.player;

import com.fplstatistics.app.knapsack.DreamTeam;
import com.fplstatistics.app.knapsack.DreamTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            @RequestParam(value = "position", required = false) String positionCode) {

        List<PlayerDto> players = playerService.getPlayers(fromSeason, toSeason, fromRound, toRound, teamShortName, positionCode, appPercentage);

        DreamTeam dreamTeam = dreamTeamService.getDreamEleven(83, players, PlayerDto::getPoints);
        System.out.println("Team price: " + dreamTeam.getTeamPrice());
        System.out.println("Total points: " + dreamTeam.getTotalPoints());
        System.out.println("Total value: " + dreamTeam.getTeamValue());
        IntStream.range(0, 11).boxed().map(i -> {
            System.out.print(i + 1 + ". ");
            return dreamTeam.getPlayers().get(i);
        }).forEach(p -> System.out.println(p.getPosition() + " " + p    .getWebName() + " " + p.getCost() + " " + p.getClub() + " " + p.getAppearances()+ " " + p.getPoints()));
        return returnPlayersSorted(sort, players);
    }

    private List<PlayerDto> returnPlayersSorted(@RequestParam("sort") String sort, List<PlayerDto> players) {
        if (sort.equalsIgnoreCase("Cost")) {
            return players.stream().sorted(Comparator.comparing(PlayerDto::getCost).reversed()).collect(Collectors.toList());
        } else if (sort.equalsIgnoreCase("Appearances")) {
            return players.stream().sorted(Comparator.comparing(PlayerDto::getAppearances).reversed()).collect(Collectors.toList());
        } else if (sort.equalsIgnoreCase("Minutes")) {
            return players.stream().sorted(Comparator.comparing(PlayerDto::getMinutesPerAppearance).reversed()).collect(Collectors.toList());
        } else if (sort.equalsIgnoreCase("Points")) {
            return players.stream().sorted(Comparator.comparing(PlayerDto::getPoints).reversed()).collect(Collectors.toList());
        } else if (sort.equalsIgnoreCase("Points per Apps")) {
            return players.stream().sorted(Comparator.comparing(PlayerDto::getPointsPerAppearance).reversed()).collect(Collectors.toList());
        } else if (sort.equalsIgnoreCase("Value")) {
            return players.stream().sorted(Comparator.comparing(PlayerDto::getValue).reversed()).collect(Collectors.toList());
        } else if (sort.equalsIgnoreCase("Value per Apps")) {
            return players.stream().sorted(Comparator.comparing(PlayerDto::getValuePerAppearance).reversed()).collect(Collectors.toList());
        }

        return players;
    }
}
