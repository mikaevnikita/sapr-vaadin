package ru.mikaev.sapr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RodDto {
    private double l;

    private double a;

    private double e;

    private double sigma;

    private double load;

    private KnotDto leftKnot;

    private KnotDto rightKnot;

    public RodDto(RodDto dto) {
        this.l = dto.l;
        this.a = dto.a;
        this.e = dto.e;
        this.sigma = dto.sigma;
        this.load = dto.load;
        this.leftKnot = new KnotDto(dto.leftKnot);
        this.rightKnot = new KnotDto(dto.rightKnot);
    }

    public double getL() {
        return l;
    }

    public void setL(double l) {
        this.l = l;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getE() {
        return e;
    }

    public void setE(double e) {
        this.e = e;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public double getLoad() {
        return load;
    }

    public void setLoad(double load) {
        this.load = load;
    }

    public KnotDto getLeftKnot() {
        return leftKnot;
    }

    public void setLeftKnot(KnotDto leftKnot) {
        this.leftKnot = leftKnot;
    }

    public KnotDto getRightKnot() {
        return rightKnot;
    }

    public void setRightKnot(KnotDto rightKnot) {
        this.rightKnot = rightKnot;
    }
}
