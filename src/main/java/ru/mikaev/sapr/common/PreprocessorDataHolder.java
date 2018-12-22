package ru.mikaev.sapr.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mikaev.sapr.dto.PreprocessorDataDto;

import java.util.Optional;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PreprocessorDataHolder {
    private Optional<PreprocessorDataDto> preprocessorData;
}
