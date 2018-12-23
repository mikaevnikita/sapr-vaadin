package ru.mikaev.sapr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class KnotDto {
    private int load;

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public KnotDto(KnotDto dto){
        this.load = dto.load;
    }
}
