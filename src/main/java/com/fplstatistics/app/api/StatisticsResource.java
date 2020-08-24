package com.fplstatistics.app.api;

import com.fplstatistics.app.model.RoundScore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StatisticsResource {

    @GetMapping("/statistics")
    public List<RoundScore> getStatistics(
            @RequestParam("season") String season,
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("view") String view) {
        return new ArrayList<>();
    }
}
