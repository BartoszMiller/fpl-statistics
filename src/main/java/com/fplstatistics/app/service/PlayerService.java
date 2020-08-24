package com.fplstatistics.app.service;

import com.fplstatistics.app.api.PlayerDto;
import com.fplstatistics.app.model.Player;
import com.fplstatistics.app.model.RoundScore;
import com.fplstatistics.app.model.Season;
import com.fplstatistics.app.model.Team;
import com.fplstatistics.app.repo.PlayerRepository;
import com.fplstatistics.app.repo.RoundScoreRepository;
import com.fplstatistics.app.repo.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class PlayerService {

    private final SeasonRepository seasonRepository;
    private final PlayerRepository playerRepository;
    private final RoundScoreRepository roundScoreRepository;

    @Autowired
    public PlayerService(SeasonRepository seasonRepository, PlayerRepository playerRepository, RoundScoreRepository roundScoreRepository) {
        this.seasonRepository = seasonRepository;
        this.playerRepository = playerRepository;
        this.roundScoreRepository = roundScoreRepository;
    }

    public List<PlayerDto> getPlayers(String seasonFrom,
                                      String seasonTo,
                                      Integer roundFrom,
                                      Integer roundTo) {
        Season activeSeason = seasonRepository.findByActive(true);
        List<Team> activeTeams = activeSeason.getTeams();
        List<Player> players = activeTeams.stream().map(playerRepository::findByCurrentTeam).flatMap(Collection::stream).collect(Collectors.toList());

        int from = Integer.parseInt(seasonFrom.replace("-", "") + String.format("%02d", roundFrom));
        int to = Integer.parseInt(seasonTo.replace("-", "") + String.format("%02d", roundTo));

        return roundScoreRepository.findBySeasonRoundBetweenAndPlayerIn(from, to, players)
                .stream()
                .collect(groupingBy(RoundScore::getPlayer))
                .entrySet()
                .stream().map(playerToRoundScore -> new PlayerDto(playerToRoundScore.getKey(), playerToRoundScore.getValue())).collect(Collectors.toList());
    }
}
