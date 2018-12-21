package ru.mikaev.sapr.mapping;

import org.springframework.stereotype.Component;
import ru.mikaev.sapr.domain.PreprocessorData;
import ru.mikaev.sapr.dto.PreprocessorDataDto;

import java.time.LocalDateTime;

@Component
public class PreprocessorDataMapper {
    public PreprocessorData toPreprocessorData(PreprocessorDataDto dto) {
        return PreprocessorData
                .builder()
                .dataName(dto.getDataName())
                .creationDateTime(LocalDateTime.now())
                .rods(dto.getRods())
                .build();
    }

    public PreprocessorDataDto toDto(PreprocessorData preprocessorData) {
        return PreprocessorDataDto
                .builder()
                .dataName(preprocessorData.getDataName())
                .creationDateTime(preprocessorData.getCreationDateTime())
                .rods(preprocessorData.getRods())
                .build();
    }
}
