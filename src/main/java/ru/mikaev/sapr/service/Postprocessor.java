package ru.mikaev.sapr.service;

import ru.mikaev.sapr.dto.ConstructionDto;
import ru.mikaev.sapr.dto.PostprocessorDto;
import ru.mikaev.sapr.dto.ProcessorResult;
import ru.mikaev.sapr.dto.RodDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Postprocessor {
    private final ConstructionDto construction;
    private final ProcessorResult processorResult;
    private final PostprocessorUiConfiguration configuration;

    public Postprocessor(ConstructionDto construction,
                         ProcessorResult processorResult,
                         PostprocessorUiConfiguration configuration){
        this.construction = construction;
        this.processorResult = processorResult;
        this.configuration = configuration;
    }

    public List<PostprocessorDto> postprocess(){
        final List<RodDto> rods = construction.getRods();

        List<PostprocessorDto> postprocessorDtos = new ArrayList<>();

        final List<Double> deltas = processorResult.getDeltas();

        for(int i = 0; i < rods.size(); i++){
            double deltaBegin = deltas.get(i);
            double deltaEnd = deltas.get(i+1);

            RodDto currentRod = rods.get(i);

            double step = currentRod.getL() / configuration.getPartitionsCount();
            double x = 0;

            for(int j = 0; j < configuration.getPartitionsCount() + 1; j++)
            {
                PostprocessorDto postprocessorDto = new PostprocessorDto();

                postprocessorDto.setRodNum(i+1);

                postprocessorDto.setX(x);

                double ux = deltaBegin + (x/currentRod.getL())*(deltaEnd - deltaBegin) +
                        ((currentRod.getLoad()*currentRod.getL()*x)/(2*currentRod.getE()*currentRod.getA()))*(1 - (x/currentRod.getL()));
                postprocessorDto.setUx(ux);

                double nx = (currentRod.getE()*currentRod.getA()/currentRod.getL())*(deltaEnd - deltaBegin) +
                        (currentRod.getLoad()*currentRod.getL()/2)*(1 - 2*x/currentRod.getL());
                postprocessorDto.setNx(nx);

                double gx = nx/currentRod.getA();
                postprocessorDto.setGx(gx);

                postprocessorDto.setSigma(currentRod.getSigma());
                x += step;

                postprocessorDtos.add(postprocessorDto);
            }
        }

        return postprocessorDtos;
    }
}
