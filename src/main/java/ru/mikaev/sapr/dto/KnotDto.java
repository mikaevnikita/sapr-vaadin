package ru.mikaev.sapr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class KnotDto {
    private double load;

    public KnotDto(KnotDto dto) {
        this.load = dto.load;
    }

    public double getLoad() {
        return load;
    }

    public void setLoad(double load) {
        this.load = load;
    }
}
