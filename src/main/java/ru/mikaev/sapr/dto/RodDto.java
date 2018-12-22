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
}
