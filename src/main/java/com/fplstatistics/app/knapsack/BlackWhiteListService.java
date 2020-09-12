package com.fplstatistics.app.knapsack;

import com.fplstatistics.app.player.Player;
import com.fplstatistics.app.player.PlayerDto;
import com.fplstatistics.app.player.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlackWhiteListService {

    private final PlayerRepository playerRepository;
    private final BlackListRepository blackListRepository;
    private final WhiteListRepository whiteListRepository;

    public BlackWhiteListService(PlayerRepository playerRepository, BlackListRepository blackListRepository, WhiteListRepository whiteListRepository) {
        this.playerRepository = playerRepository;
        this.blackListRepository = blackListRepository;
        this.whiteListRepository = whiteListRepository;
    }

    List<PlayerDto> getBlackList() {
        return blackListRepository.findAll()
                .stream()
                .map(blackList -> new PlayerDto(blackList.getPlayer(), new ArrayList<>()))
                .collect(Collectors.toList());
    }

    List<PlayerDto> getWhiteList() {
        return whiteListRepository.findAll()
                .stream()
                .map(whiteList -> new PlayerDto(whiteList.getPlayer(), new ArrayList<>()))
                .collect(Collectors.toList());
    }

    void addToBlackList(Integer playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Cannot find player"));
        BlackList blackList = new BlackList();
        blackList.setPlayer(player);
        blackListRepository.save(blackList);
    }

    void addToWhiteList(Integer playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Cannot find player"));
        WhiteList whiteList = new WhiteList();
        whiteList.setPlayer(player);
        whiteListRepository.save(whiteList);
    }

    void removeFromBlackList(Integer playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Cannot find player"));
        BlackList blackList = blackListRepository.findByPlayer(player);
        blackListRepository.deleteById(blackList.getId());
    }

    void removeFromWhiteList(Integer playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Cannot find player"));
        WhiteList whiteList = whiteListRepository.findByPlayer(player);
        whiteListRepository.deleteById(whiteList.getId());
    }
}
