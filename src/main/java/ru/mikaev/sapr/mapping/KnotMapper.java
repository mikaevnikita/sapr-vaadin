package ru.mikaev.sapr.mapping;

import org.springframework.stereotype.Component;
import ru.mikaev.sapr.domain.Knot;
import ru.mikaev.sapr.dto.KnotDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class KnotMapper {
    public Knot toKnot(KnotDto knotDto) {
        Knot knot = new Knot();
        knot.setLoad(knotDto.getLoad());

        return knot;
    }

    public KnotDto fromKnot(Knot knot) {
        return new KnotDto(knot.getLoad());
    }

    public List<Knot> toKnots(List<KnotDto> dtos) {
        return dtos
                .stream()
                .map(this::toKnot)
                .collect(Collectors.toList());
    }

    public List<KnotDto> fromKnots(List<Knot> knots) {
        return knots
                .stream()
                .map(this::fromKnot)
                .collect(Collectors.toList());
    }
}
