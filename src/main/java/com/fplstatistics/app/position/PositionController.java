package com.fplstatistics.app.position;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PositionController {

    @GetMapping("/positions")
    public List<PositionDto> getPositions() {
        return Arrays.stream(Position.values())
                .map(position -> new PositionDto(position.getCode(), position.getPositionName()))
                .collect(Collectors.toList());
    }
}
