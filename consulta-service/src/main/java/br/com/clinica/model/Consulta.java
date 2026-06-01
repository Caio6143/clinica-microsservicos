package br.com.clinica.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dataHora;
    private String dentista;
    private Long pacienteId; // Chave estrangeira lógica para o Microsserviço 1

    public Consulta() {}

    public Consulta(Long id, String dataHora, String dentista, Long pacienteId) {
        this.id = id;
        this.dataHora = dataHora;
        this.dentista = dentista;
        this.pacienteId = pacienteId;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }
    public String getDentista() { return dentista; }
    public void setDentista(String dentista) { this.dentista = dentista; }
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
}