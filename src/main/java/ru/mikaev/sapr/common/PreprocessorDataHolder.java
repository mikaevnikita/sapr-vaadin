package ru.mikaev.sapr.common;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.mikaev.sapr.dto.PreprocessorDataDto;
import ru.mikaev.sapr.mapping.PreprocessorDataMapper;
import ru.mikaev.sapr.service.PreprocessorDataService;

import java.util.Optional;

@Component
@Data
public class PreprocessorDataHolder {
    private Optional<PreprocessorDataDto> preprocessorData;
    private PreprocessorDataService dataService;
    private PreprocessorDataMapper mapper;

    public PreprocessorDataHolder(PreprocessorDataService dataService,
                                  PreprocessorDataMapper mapper) {
        this.dataService = dataService;
        this.mapper = mapper;
        preprocessorData = Optional.empty();
    }

    public void updateHolder(){
        if(!preprocessorData.isPresent())
            return;

        final PreprocessorDataDto dto =
                mapper.fromPreprocessorData(dataService.findByDataName(preprocessorData.get().getDataName()).get());
        setPreprocessorData(Optional.of(dto));
    }
}
