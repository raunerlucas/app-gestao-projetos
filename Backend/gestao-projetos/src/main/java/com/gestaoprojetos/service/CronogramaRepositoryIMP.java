package com.gestaoprojetos.service;

import com.gestaoprojetos.model.Cronograma;
import com.gestaoprojetos.repository.CronogramaRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CronogramaRepositoryIMP {

    private final CronogramaRepository cronogramaRepository;

    public CronogramaRepositoryIMP(CronogramaRepository cronogramaRepository) {
        this.cronogramaRepository = cronogramaRepository;
    }

    public Long numTotalCronogramas() {
        return cronogramaRepository.count();
    }

    public Cronograma findById(Long id) {
        return cronogramaRepository.findById(id).orElse(null);
    }

    public Cronograma save(Cronograma cronograma) {
        return cronogramaRepository.save(cronograma);
    }

    public void deleteById(Long id) {
        cronogramaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return cronogramaRepository.existsById(id);
    }

    public Cronograma findByPremioId(Long premioId) {
        return cronogramaRepository.findByPremioId(premioId).orElseThrow(
                () -> new NoSuchElementException("Cronograma not found for Premio ID: " + premioId)
        );
    }
}
