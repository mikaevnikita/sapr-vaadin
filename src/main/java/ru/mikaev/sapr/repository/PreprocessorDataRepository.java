package ru.mikaev.sapr.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mikaev.sapr.domain.PreprocessorData;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreprocessorDataRepository extends CrudRepository<PreprocessorData, Long> {
    Optional<PreprocessorData> findByDataName(String dataName);

    List<PreprocessorData> findAll();

    List<PreprocessorData> findByDataNameContaining(String dataName);

    void deleteByDataName(String dataName);
}
