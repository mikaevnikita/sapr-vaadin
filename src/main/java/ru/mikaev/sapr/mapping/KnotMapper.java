package ru.mikaev.sapr.mapping;

import org.springframework.stereotype.Component;
import ru.mikaev.sapr.domain.Knot;
import ru.mikaev.sapr.dto.KnotDto;

import java.util.LinkedHashSet;
import java.util.Set;
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

    public Set<Knot> toKnots(Set<KnotDto> dtos) {
        return dtos
                .stream()
                .map(this::toKnot)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<KnotDto> fromKnots(Set<Knot> knots) {
        return knots
                .stream()
                .map(this::fromKnot)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
