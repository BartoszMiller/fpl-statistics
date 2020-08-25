package com.fplstatistics.app.api;

import com.fplstatistics.app.model.Position;
import com.fplstatistics.app.model.Season;
import com.fplstatistics.app.repo.SeasonRepository;
import com.fplstatistics.app.repo.TeamRepository;
import com.fplstatistics.app.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@ControllerAdvice
public class IndexController {

    private final SeasonRepository seasonRepository;
    private final PlayerService playerService;
    private final TeamRepository teamRepository;

    @Autowired
    public IndexController(SeasonRepository seasonRepository, PlayerService playerService, TeamRepository teamRepository) {
        this.seasonRepository = seasonRepository;
        this.playerService = playerService;
        this.teamRepository = teamRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @ModelAttribute("players")
    public List<PlayerDto> playerDtos() {
        List<Season> seasons = seasonRepository.findAll();
        return playerService.getPlayers(seasons.get(0).getCode(), seasons.get(seasons.size() - 1).getCode(), 1, 38);
    }

    @ModelAttribute("teams")
    public List<TeamDto> teams() {
        Season season = seasonRepository.findByActive(true);
        return season.getTeams().stream().map(team -> new TeamDto(team.getName(), team.getShortName())).collect(Collectors.toList());
    }

    @ModelAttribute("positions")
    public Position[] positions() {
        return Position.values();
    }

    @ModelAttribute("searchForm")
    public SearchForm searchForm() {
        List<Season> seasons = seasonRepository.findAll();
        SearchForm searchForm = new SearchForm();
        searchForm.setSeasonFrom(seasons.get(0).getCode());
        searchForm.setSeasonTo(seasons.get(seasons.size() - 1).getCode());
        searchForm.setRoundFrom(1);
        searchForm.setRoundTo(38);
        return searchForm;
    }

    @ModelAttribute("allSeasons")
    public List<SeasonDto> allSeasons() {
        return seasonRepository.findAll().stream().map(season -> new SeasonDto(season.getCode())).collect(Collectors.toList());
    }

    @ModelAttribute("allRounds")
    public List<RoundDto> to() {
        List<RoundDto> from = new ArrayList<>();
        for (int i = 1; i <= 38; i++) {
            from.add(new RoundDto(i, "GW " + i));
        }
        return from;
    }
}
