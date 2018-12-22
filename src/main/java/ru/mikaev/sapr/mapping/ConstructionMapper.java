package ru.mikaev.sapr.mapping;

import org.springframework.stereotype.Component;
import ru.mikaev.sapr.domain.Construction;
import ru.mikaev.sapr.dto.ConstructionDto;

@Component
public class ConstructionMapper {
    private final RodMapper rodMapper;
    private final KnotMapper knotMapper;

    public ConstructionMapper(RodMapper rodMapper, KnotMapper knotMapper) {
        this.rodMapper = rodMapper;
        this.knotMapper = knotMapper;
    }

    public Construction toConstruction(ConstructionDto dto) {
        return Construction
                .builder()
                .supportLeft(dto.isSupportLeft())
                .supportRight(dto.isSupportRight())
                .rods(rodMapper.toRods(dto.getRods()))
                .build();
    }

    public ConstructionDto fromConstruction(Construction construction) {
        return ConstructionDto
                .builder()
                .supportLeft(construction.isSupportLeft())
                .supportRight(construction.isSupportRight())
                .rods(rodMapper.fromRods(construction.getRods()))
                .build();
    }
}
