package com.fplstatistics.app.api;

import com.fplstatistics.app.model.Player;
import com.fplstatistics.app.model.RoundScore;
import com.fplstatistics.app.model.Season;
import com.fplstatistics.app.model.Team;
import com.fplstatistics.app.repo.PlayerRepository;
import com.fplstatistics.app.repo.RoundScoreRepository;
import com.fplstatistics.app.repo.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@RestController
public class StatisticsResource {

    private final SeasonRepository seasonRepository;
    private final PlayerRepository playerRepository;
    private final RoundScoreRepository roundScoreRepository;

    @Autowired
    public StatisticsResource(SeasonRepository seasonRepository, PlayerRepository playerRepository, RoundScoreRepository roundScoreRepository) {
        this.seasonRepository = seasonRepository;
        this.playerRepository = playerRepository;
        this.roundScoreRepository = roundScoreRepository;
    }

    @GetMapping("/statistics")
    public List<PlayerDto> getStatistics(
            @RequestParam("from") Integer from,
            @RequestParam("to") Integer to) {

        Season activeSeason = seasonRepository.findByActive(true);
        List<Team> activeTeams = activeSeason.getTeams();
        List<Player> players = activeTeams.stream().map(playerRepository::findByCurrentTeam).flatMap(Collection::stream).collect(Collectors.toList());
        return roundScoreRepository.findBySeasonRoundBetweenAndPlayerIn(from, to, players)
                .stream()
                .collect(groupingBy(RoundScore::getPlayer))
                .entrySet()
                .stream().map(playerToRoundScore -> new PlayerDto(playerToRoundScore.getKey(), playerToRoundScore.getValue())).collect(Collectors.toList());
    }
}
