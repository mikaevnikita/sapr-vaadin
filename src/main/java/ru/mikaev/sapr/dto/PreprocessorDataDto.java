package ru.mikaev.sapr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreprocessorDataDto implements Cloneable {
    @NotBlank
    private String dataName;

    private LocalDateTime creationDateTime;

    @NotNull
    private ConstructionDto construction;

    public PreprocessorDataDto(PreprocessorDataDto dto) {
        this.dataName = dto.dataName;
        this.creationDateTime = dto.creationDateTime;
        this.construction = new ConstructionDto(dto.getConstruction());
    }
}
