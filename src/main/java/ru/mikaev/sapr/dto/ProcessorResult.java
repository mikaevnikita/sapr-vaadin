package ru.mikaev.sapr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
public class ProcessorResult {
    private final List<Double> deltas;

    public ProcessorResult(List<Double> deltas){
        this.deltas = deltas;
    }
}
