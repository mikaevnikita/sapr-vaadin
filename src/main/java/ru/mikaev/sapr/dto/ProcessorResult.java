package ru.mikaev.sapr.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProcessorResult {
    private final List<Double> deltas;

    public ProcessorResult(List<Double> deltas) {
        this.deltas = deltas;
    }
}
