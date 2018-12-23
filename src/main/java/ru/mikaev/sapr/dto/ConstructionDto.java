package ru.mikaev.sapr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConstructionDto{
    /**
     * Заделка слева
     */
    private boolean supportLeft;

    /**
     * Заделка справа
     */
    private boolean supportRight;

    /**
     * Стержни
     */
    private List<RodDto> rods;

    public ConstructionDto(ConstructionDto dto){
        this.supportLeft = dto.supportLeft;
        this.supportRight = dto.supportRight;
        this.rods = dto.getRods().stream().map(RodDto::new).collect(Collectors.toList());
    }
}
