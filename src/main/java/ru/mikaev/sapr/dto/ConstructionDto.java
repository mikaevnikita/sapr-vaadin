package ru.mikaev.sapr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConstructionDto {
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
}
