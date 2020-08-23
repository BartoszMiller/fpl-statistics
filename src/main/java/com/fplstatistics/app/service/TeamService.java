package com.fplstatistics.app.service;

import com.fplstatistics.app.model.Team;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    public List<Team> getCurrentTeams() {
        return new ArrayList<>();
    }
}
