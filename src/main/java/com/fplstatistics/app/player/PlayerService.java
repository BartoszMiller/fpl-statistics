package com.fplstatistics.app.player;

import com.fplstatistics.app.position.Position;
import com.fplstatistics.app.round.RoundScore;
import com.fplstatistics.app.round.RoundScoreRepository;
import com.fplstatistics.app.season.Season;
import com.fplstatistics.app.season.SeasonRepository;
import com.fplstatistics.app.team.Team;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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
                                      List<String> teamShortNames,
                                      String positionCode,
                                      String appPercentage,
                                      Boolean includeHomeGames,
                                      Boolean includeAwayGames) {

        int yearDiff = Integer.parseInt(seasonTo.substring(seasonTo.indexOf('-') + 1)) - Integer.parseInt(seasonFrom.substring(seasonFrom.indexOf('-') + 1));
        int roundDiff = roundTo - roundFrom + 1;
        int rangeSize = yearDiff * 38 + roundDiff;

        int percentageFrom = 0;
        if (appPercentage != null) {
            percentageFrom = Integer.parseInt(appPercentage.substring(1));
        }
        Season activeSeason = seasonRepository.findByActive(true);
        List<Team> activeTeams = activeSeason.getTeams();
        List<Player> players = activeTeams.stream()
                .filter(team -> CollectionUtils.isEmpty(teamShortNames) || teamShortNames.contains(team.getShortName()))
                .map(playerRepository::findByCurrentTeam)
                .flatMap(Collection::stream)
                .filter(player -> StringUtils.isEmpty(positionCode) || player.getCurrentPosition().equals(Position.getPositionByCode(positionCode).name()))
                .collect(Collectors.toList());

        int from = Integer.parseInt(seasonFrom.replace("-", "") + String.format("%02d", roundFrom));
        int to = Integer.parseInt(seasonTo.replace("-", "") + String.format("%02d", roundTo));

        int finalPercentageFrom = percentageFrom;
        Set<PlayerDto> playersWithScores = roundScoreRepository.findBySeasonRoundBetweenAndPlayerInAndHomeGameIn(from, to, players, Arrays.asList(includeHomeGames, !includeAwayGames))
                .stream()
                .collect(groupingBy(RoundScore::getPlayer))
                .entrySet()
                .stream()
                .map(playerToRoundScore -> new PlayerDto(playerToRoundScore.getKey(), playerToRoundScore.getValue()))
                .collect(Collectors.toSet());

        Set<PlayerDto> allPlayers = players.stream().map(player -> new PlayerDto(player, new ArrayList<>())).collect(Collectors.toSet());
        playersWithScores.addAll(allPlayers);

        return playersWithScores.stream().filter(playerDto -> (finalPercentageFrom <= (playerDto.getAppearances() * 100 / rangeSize))).collect(Collectors.toList());
    }
}
