package ru.mikaev.sapr.common;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.mikaev.sapr.dto.ProcessorResult;

import java.util.Optional;

@Component
@Data
public class ProcessorDataHolder {
    private Optional<ProcessorResult> processorResult;
}
