package ru.mikaev.sapr.common;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.mikaev.sapr.dto.PreprocessorDataDto;

import java.util.Optional;

@Component
@Data
public class PreprocessorDataHolder {
    private Optional<PreprocessorDataDto> preprocessorData;

    public PreprocessorDataHolder() {
        preprocessorData = Optional.empty();
    }
}
