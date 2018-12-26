package ru.mikaev.sapr.common;

import lombok.Data;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.mikaev.sapr.dto.ConstructionDto;
import ru.mikaev.sapr.dto.ProcessorResult;

import java.util.Optional;

@Component
@Data
public class ProcessorDataHolder {
    private Optional<Pair<ConstructionDto, ProcessorResult>> processorResult;

    public ProcessorDataHolder(){
        processorResult = Optional.empty();
    }
}
