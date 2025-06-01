package com.gestaoprojetos.service;

import com.gestaoprojetos.model.Premio;
import com.gestaoprojetos.repository.PremioRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PremioRepositoryIMP {

    private final PremioRepository premioRepository;

    public PremioRepositoryIMP(PremioRepository premioRepository) {
        this.premioRepository = premioRepository;
    }

    public Long numTotalPremios() {
        return premioRepository.count();
    }

    public Premio findById(Long id) {
        return premioRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Premio not found with id: " + id));
    }

    public Premio save(Premio premio) {
        return premioRepository.save(premio);
    }
    public void deleteById(Long id) {
        premioRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        if (!premioRepository.existsById(id)) {
            throw new NoSuchElementException("Premio not found with id: " + id);
        }
        return true;
    }

    public Premio findByNome(String nome) {
        return premioRepository.findByNome(nome).orElseThrow(() ->
                new NoSuchElementException("Premio not found with name: " + nome));
    }
}
