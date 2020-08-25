package com.fplstatistics.app.api;

import com.fplstatistics.app.api.dto.PlayerDto;
import com.fplstatistics.app.round.RoundDto;
import com.fplstatistics.app.api.dto.SearchForm;
import com.fplstatistics.app.model.Position;
import com.fplstatistics.app.season.Season;
import com.fplstatistics.app.season.SeasonRepository;
import com.fplstatistics.app.team.TeamRepository;
import com.fplstatistics.app.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

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
}
