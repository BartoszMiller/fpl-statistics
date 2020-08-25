package com.fplstatistics.app.season;

import com.fplstatistics.app.round.RoundDto;
import com.fplstatistics.app.round.RoundScoreRepository;
import com.fplstatistics.app.team.TeamDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class SeasonsController {

    private final SeasonRepository seasonRepository;
    private final RoundScoreRepository roundScoreRepository;

    public SeasonsController(SeasonRepository seasonRepository, RoundScoreRepository roundScoreRepository) {
        this.seasonRepository = seasonRepository;
        this.roundScoreRepository = roundScoreRepository;
    }

    @GetMapping("/seasons")
    public List<SeasonDto> getSeasons() {
        return seasonRepository.findAll().stream().map(season -> new SeasonDto(season.getCode(), season.isActive())).collect(Collectors.toList());
    }

    @GetMapping("/seasons/{season-code}/teams")
    public List<TeamDto> getSeasonTeams(@PathVariable("season-code") String seasonCode) {
        Season season = seasonRepository.findByCode(seasonCode);
        return season.getTeams().stream().map(team -> new TeamDto(team.getName(), team.getShortName())).collect(Collectors.toList());
    }

    @GetMapping("/seasons/{season-code}/rounds")
    public List<RoundDto> getSeasonRounds(@PathVariable("season-code") String seasonCode) {
        Season season = seasonRepository.findByCode(seasonCode);
        Integer maxRoundBySeason = roundScoreRepository.findMaxRoundBySeason(season).orElse(0);
        return IntStream.range(1, maxRoundBySeason).mapToObj(i -> new RoundDto(i, "GW " + i)).collect(Collectors.toList());
    }
}
