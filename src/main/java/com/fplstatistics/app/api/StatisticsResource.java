package com.fplstatistics.app.api;

import com.fplstatistics.app.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsResource {

    private final PlayerService playerService;

    @Autowired
    public StatisticsResource(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/statistics")
    public String getStatistics(
            @RequestParam("seasonFrom") String seasonFrom,
            @RequestParam("seasonTo") String seasonTo,
            @RequestParam("roundFrom") Integer roundFrom,
            @RequestParam("roundTo") Integer roundTo,
            Model model) {
        model.addAttribute("players", playerService.getPlayers(seasonFrom, seasonTo, roundFrom, roundTo));
        return "index";
    }
}
