package ru.mikaev.sapr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.mikaev.sapr.domain.PreprocessorData;
import ru.mikaev.sapr.dto.PreprocessorDataDto;
import ru.mikaev.sapr.factory.DefaultPreprocessorDataFactory;
import ru.mikaev.sapr.mapping.PreprocessorDataMapper;
import ru.mikaev.sapr.repository.PreprocessorDataRepository;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PreprocessorDataService {
    @Autowired
    private PreprocessorDataRepository repository;

    @Autowired
    private PreprocessorDataMapper mapper;


    public Optional<PreprocessorData> findByDataName(String dataName) {
        return repository.findByDataName(dataName);
    }

    public List<PreprocessorData> findAll() {
        return repository.findAll();
    }

    @Validated
    public void savePreprocessorData(PreprocessorDataDto data) {
        repository.save(mapper.toPreprocessorData(data));
    }

    public List<PreprocessorData> findLikeDataName(String dataName) {
        return repository.findByDataNameContaining(dataName);
    }

    @Transactional
    public void createData(String dataName) {
        PreprocessorData defaultPreprocessorData =
                DefaultPreprocessorDataFactory.defaultPreprocessorData(dataName, LocalDateTime.now());
        repository.save(defaultPreprocessorData);
    }

    @Transactional
    public void deleteData(PreprocessorDataDto dto){
        repository.deleteByDataName(dto.getDataName());
    }
}
