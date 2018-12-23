package ru.mikaev.sapr.mapping;

import org.springframework.stereotype.Component;
import ru.mikaev.sapr.domain.Construction;
import ru.mikaev.sapr.domain.Rod;
import ru.mikaev.sapr.dto.ConstructionDto;
import ru.mikaev.sapr.dto.RodDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public void updateEntityByDto(Construction construction, ConstructionDto dto){
        construction.setSupportLeft(dto.isSupportLeft());
        construction.setSupportRight(dto.isSupportRight());

        List<RodDto> rodDtos = dto.getRods();

        List<Rod> rods = new ArrayList<>();

        boolean isStart = true;

        for(RodDto rodDto : rodDtos){
            if(isStart){
                rods.add(rodMapper.toRod(rodDto));
                isStart = false;
            }
            else{
                Rod rod = rodMapper.toRod(rodDto);
                rod.setLeftKnot(rods.get(rods.size() - 1).getRightKnot());
                rods.add(rod);
            }
        }

        construction.setRods(rods);
    }
}
