package ru.mikaev.sapr.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.atmosphere.config.service.Get;

@Getter
public class PostprocessorUiConfiguration {
    private final int partitionsCount;

    public PostprocessorUiConfiguration(final int partitionsCount){
        this.partitionsCount = partitionsCount;
    }
}
