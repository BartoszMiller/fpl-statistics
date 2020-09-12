package com.fplstatistics.app.knapsack;

import com.fplstatistics.app.player.Player;
import com.fplstatistics.app.player.PlayerDto;
import com.fplstatistics.app.player.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        Optional<BlackList> blackListOptional = blackListRepository.findByPlayer(player);
        if (!blackListOptional.isPresent()) {
            BlackList blackList = new BlackList();
            blackList.setPlayer(player);
            blackListRepository.save(blackList);
        }
    }

    void addToWhiteList(Integer playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Cannot find player"));
        Optional<WhiteList> whiteListOptional = whiteListRepository.findByPlayer(player);
        if (!whiteListOptional.isPresent()) {
            WhiteList whiteList = new WhiteList();
            whiteList.setPlayer(player);
            whiteListRepository.save(whiteList);
        }
    }

    void removeFromBlackList(Integer playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Cannot find player"));
        Optional<BlackList> blackList = blackListRepository.findByPlayer(player);
        blackList.ifPresent(blackList1 -> blackListRepository.deleteById(blackList1.getId()));
    }

    void removeFromWhiteList(Integer playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Cannot find player"));
        Optional<WhiteList> whiteList = whiteListRepository.findByPlayer(player);
        whiteList.ifPresent(whiteList1 -> whiteListRepository.deleteById(whiteList1.getId()));
    }
}
