package ru.mikaev.sapr.mapping;

import org.springframework.stereotype.Component;
import ru.mikaev.sapr.domain.Rod;
import ru.mikaev.sapr.dto.RodDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RodMapper {
    private final KnotMapper knotMapper;

    public RodMapper(KnotMapper knotMapper) {
        this.knotMapper = knotMapper;
    }

    public Rod toRod(RodDto dto) {
        return Rod
                .builder()
                .a(dto.getA())
                .e(dto.getE())
                .l(dto.getL())
                .load(dto.getLoad())
                .sigma(dto.getSigma())
                .leftKnot(knotMapper.toKnot(dto.getLeftKnot()))
                .rightKnot(knotMapper.toKnot(dto.getRightKnot()))
                .build();
    }

    public RodDto fromRod(Rod rod) {
        return RodDto
                .builder()
                .a(rod.getA())
                .e(rod.getE())
                .l(rod.getL())
                .load(rod.getLoad())
                .sigma(rod.getSigma())
                .leftKnot(knotMapper.fromKnot(rod.getLeftKnot()))
                .rightKnot(knotMapper.fromKnot(rod.getRightKnot()))
                .build();
    }

    public List<Rod> toRods(List<RodDto> dtos) {
        return dtos
                .stream()
                .map(this::toRod)
                .collect(Collectors.toList());
    }

    public List<RodDto> fromRods(List<Rod> rods) {
        List<RodDto> rodDtos = new ArrayList<>();

        boolean isStart = true;

        for(Rod rod : rods){
            if(isStart){
                rodDtos.add(fromRod(rod));
                isStart = false;
            }
            else{
                RodDto rodDto = fromRod(rod);
                rodDto.setLeftKnot(rodDtos.get(rodDtos.size() - 1).getRightKnot());
                rodDtos.add(rodDto);
            }
        }

        return rodDtos;
    }
}
