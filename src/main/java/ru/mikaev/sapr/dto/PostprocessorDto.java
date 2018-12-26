package ru.mikaev.sapr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostprocessorDto {
    private int rodNum;

    private double x;

    private double ux;

    private double nx;

    private double gx;

    private double sigma;
}
