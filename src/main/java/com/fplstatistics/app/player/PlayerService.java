package com.fplstatistics.app.player;

import com.fplstatistics.app.position.Position;
import com.fplstatistics.app.round.RoundScore;
import com.fplstatistics.app.round.RoundScoreRepository;
import com.fplstatistics.app.season.Season;
import com.fplstatistics.app.season.SeasonRepository;
import com.fplstatistics.app.team.Team;
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
                                      Integer roundTo,
                                      String teamShortName,
                                      String positionCode) {
        Season activeSeason = seasonRepository.findByActive(true);
        List<Team> activeTeams = activeSeason.getTeams();
        List<Player> players = activeTeams.stream()
                .filter(team -> teamShortName == null || team.getShortName().equals(teamShortName))
                .map(playerRepository::findByCurrentTeam)
                .flatMap(Collection::stream)
                .filter(player -> player.getCurrentPosition().equals(Position.getPositionByCode(positionCode).name()))
                .collect(Collectors.toList());

        int from = Integer.parseInt(seasonFrom.replace("-", "") + String.format("%02d", roundFrom));
        int to = Integer.parseInt(seasonTo.replace("-", "") + String.format("%02d", roundTo));

        return roundScoreRepository.findBySeasonRoundBetweenAndPlayerIn(from, to, players)
                .stream()
                .collect(groupingBy(RoundScore::getPlayer))
                .entrySet()
                .stream().map(playerToRoundScore -> new PlayerDto(playerToRoundScore.getKey(), playerToRoundScore.getValue())).collect(Collectors.toList());
    }
}
