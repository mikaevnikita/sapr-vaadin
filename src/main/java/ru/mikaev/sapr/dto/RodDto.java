package ru.mikaev.sapr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RodDto {
    private int l;

    private int a;

    private int e;

    private int sigma;

    private int load;

    private KnotDto leftKnot;

    private KnotDto rightKnot;

    public RodDto(RodDto dto){
        this.l = dto.l;
        this.a = dto.a;
        this.e = dto.e;
        this.sigma = dto.sigma;
        this.load = dto.load;
        this.leftKnot = new KnotDto(dto.leftKnot);
        this.rightKnot = new KnotDto(dto.rightKnot);
    }
}
