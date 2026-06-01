package br.com.clinica.service;

import br.com.clinica.model.Consulta;
import br.com.clinica.repository.ConsultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    private final ConsultaRepository repository;
    private final RestTemplate restTemplate;

    public ConsultaService(ConsultaRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public List<Consulta> listarTodas() {
        return repository.findAll();
    }

    public Optional<Consulta> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Consulta salvar(Consulta consulta) {
        // COMUNICAÇÃO ENTRE MICROSSERVIÇOS:
        // Monta a URL apontando para o Microsserviço 1 na porta 8081
        String urlPaciente = "http://localhost:8081/pacientes/" + consulta.getPacienteId();
        
        try {
            // Tenta buscar o paciente na outra API. Se não existir, vai estourar um erro 404 e entrar no catch
            restTemplate.getForEntity(urlPaciente, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            // Regra de negócio: impede o agendamento caso o ID do paciente seja inválido
            throw new IllegalArgumentException("Erro de Agendamento: O paciente com ID " + consulta.getPacienteId() + " não está cadastrado na clínica!");
        }

        return repository.save(consulta);
    }

    public Consulta atualizar(Long id, Consulta dadosAtualizados) {
        return repository.findById(id).map(consulta -> {
            consulta.setDataHora(dadosAtualizados.getDataHora());
            consulta.setDentista(dadosAtualizados.getDentista());
            return repository.save(consulta);
        }).orElseThrow(() -> new RuntimeException("Consulta não encontrada com o ID: " + id));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Não é possível deletar. Consulta não encontrada.");
        }
        repository.deleteById(id);
    }
}