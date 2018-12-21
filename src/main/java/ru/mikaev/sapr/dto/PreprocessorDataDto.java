package ru.mikaev.sapr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mikaev.sapr.domain.Rod;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreprocessorDataDto {
    @NotBlank
    private String dataName;

    private LocalDateTime creationDateTime;

    @NotEmpty
    private List<Rod> rods;
}
