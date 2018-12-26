package ru.mikaev.sapr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnotDto {
    private double load;

    public KnotDto(KnotDto dto) {
        this.load = dto.load;
    }
}
