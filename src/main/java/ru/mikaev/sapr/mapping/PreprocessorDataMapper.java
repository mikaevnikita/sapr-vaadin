package ru.mikaev.sapr.mapping;

import org.springframework.stereotype.Component;
import ru.mikaev.sapr.domain.PreprocessorData;
import ru.mikaev.sapr.dto.PreprocessorDataDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PreprocessorDataMapper {
    private final ConstructionMapper constructionMapper;

    public PreprocessorDataMapper(ConstructionMapper constructionMapper) {
        this.constructionMapper = constructionMapper;
    }

    public PreprocessorData toPreprocessorData(PreprocessorDataDto dto) {
        return PreprocessorData
                .builder()
                .dataName(dto.getDataName())
                .creationDateTime(LocalDateTime.now())
                .construction(constructionMapper.toConstruction(dto.getConstruction()))
                .build();
    }

    public void updateEntityByDto(PreprocessorData data, PreprocessorDataDto dto){
        data.setDataName(dto.getDataName());
        data.setCreationDateTime(dto.getCreationDateTime());
        constructionMapper.updateEntityByDto(data.getConstruction(), dto.getConstruction());
    }

    public PreprocessorDataDto fromPreprocessorData(PreprocessorData preprocessorData) {
        return PreprocessorDataDto
                .builder()
                .dataName(preprocessorData.getDataName())
                .creationDateTime(preprocessorData.getCreationDateTime())
                .construction(constructionMapper.fromConstruction(preprocessorData.getConstruction()))
                .build();
    }

    public List<PreprocessorData> toPreprocessorDataList(List<PreprocessorDataDto> dtos) {
        return dtos
                .stream()
                .map(this::toPreprocessorData)
                .collect(Collectors.toList());
    }

    public List<PreprocessorDataDto> fromPreprocessorDataList(List<PreprocessorData> dataList) {
        return dataList
                .stream()
                .map(this::fromPreprocessorData)
                .collect(Collectors.toList());
    }
}
