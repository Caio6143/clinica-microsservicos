package br.com.clinica.service;

import br.com.clinica.model.Paciente;
import br.com.clinica.repository.PacienteRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    private final PacienteRepository repository;

    // Injeção de dependência via construtor (padrão recomendado)
    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    public List<Paciente> listarTodos() {
        return repository.findAll();
    }

    public Optional<Paciente> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Paciente salvar(Paciente paciente) {
        // Validação obrigatória de regra de negócio
        if (paciente.getCpf() == null || paciente.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("Erro de Validação: O CPF do paciente é obrigatório.");
        }
        return repository.save(paciente);
    }

    public Paciente atualizar(Long id, Paciente dadosAtualizados) {
        return repository.findById(id).map(paciente -> {
            paciente.setNome(dadosAtualizados.getNome());
            paciente.setCpf(dadosAtualizados.getCpf());
            paciente.setTelefone(dadosAtualizados.getTelefone());
            return repository.save(paciente);
        }).orElseThrow(() -> new RuntimeException("Paciente não encontrado com o ID: " + id));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Não é possível deletar. Paciente não encontrado.");
        }
        repository.deleteById(id);
    }
}