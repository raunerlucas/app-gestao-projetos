package com.gestaoprojetos.service;

import com.gestaoprojetos.model.Projeto;
import com.gestaoprojetos.repository.ProjetoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjetoRepositoryIMP {

    private final ProjetoRepository projetoRepository;

    public ProjetoRepositoryIMP(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    public Long numTotalProj() {
        return projetoRepository.count();
    }

    public List<Projeto> findAll() {
        return projetoRepository.findAll();
    }

    public Projeto findById(Long id) {
        return projetoRepository.findById(id).orElse(null);
    }

    public Projeto save(Projeto projeto) {
        return projetoRepository.save(projeto);
    }

    public void deleteById(Long id) {
        projetoRepository.deleteById(id);
    }

    public List<Projeto> findByTituloContainingIgnoreCase(String titulo) {
        return projetoRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Projeto> findByDataEnvioBefore(LocalDate dataEnvioBefore) {
        return projetoRepository.findByDataEnvioBefore(dataEnvioBefore);
    }

    public boolean existsById(Long id) {
        return projetoRepository.existsById(id);
    }

    public List<Projeto> findByAreaTematica(String areaTematica) {
        return projetoRepository.findAll().stream()
                .filter(projeto -> projeto.getAreaTematica().equalsIgnoreCase(areaTematica))
                .toList();
    }
}
