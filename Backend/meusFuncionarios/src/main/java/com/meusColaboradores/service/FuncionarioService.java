package com.meusColaboradores.service;

import com.meusColaboradores.model.Funcionario;
import com.meusColaboradores.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioService {
    // TODO: Implementar os metodos CRUD de Funcionarios
    private final FuncionarioRepository funcionarioRepository;

    @Autowired
    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public Funcionario getFuncionario(Long id) {
        return funcionarioRepository.findById(id).orElse(null);
    }

}
