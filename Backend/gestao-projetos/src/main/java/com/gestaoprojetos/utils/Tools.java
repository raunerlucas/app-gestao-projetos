package com.gestaoprojetos.utils;

import com.gestaoprojetos.model.Pessoa;
import com.gestaoprojetos.service.AutorServiceIMP;
import com.gestaoprojetos.service.AvaliadorServiceIMP;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Transactional
@Component
public class Tools {

    private static AutorServiceIMP autorService;
    private static AvaliadorServiceIMP avaliadorService;

    @Autowired
    protected Tools(AutorServiceIMP autorService, AvaliadorServiceIMP avaliadorService) {
        Tools.autorService = autorService;
        Tools.avaliadorService = avaliadorService;
    }

    public static Pessoa findPessoaById(Long pessoaId) {
        if (pessoaId == null) {
            return null;
        }

        Optional<? extends Pessoa> pessoa = autorService.findById(pessoaId);
        if (pessoa.isEmpty()) {
            pessoa = avaliadorService.findById(pessoaId);
        }
        return pessoa.orElse(null);
    }
}
