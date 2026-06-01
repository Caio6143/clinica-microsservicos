\# 🏥 Sistema de Gestão de Clínica Odontológica - Microsserviços



Este projeto consiste em uma solução de arquitetura de software baseada em \*\*Microsserviços\*\* para gerenciar o fluxo de atendimento de uma clínica odontológica. O sistema foi desenvolvido utilizando \*\*Java 11\*\*, \*\*Spring Boot 2.7.14\*\*, \*\*Spring Data JPA\*\* e banco de dados \*\*H2\*\* em memória.



O ecossistema é composto por dois microsserviços independentes que se comunicam de forma síncrona via requisições HTTP para garantir a consistência e integridade das regras de negócio.



\---



\## 🎨 Tema e Domínio do Sistema



O objetivo principal do sistema é isolar as regras de gerenciamento cadastral de pacientes das operações lógicas de agendamento de consultas médicas. 



1\. \*\*Paciente Service:\*\* Responsável pelo ciclo de vida dos dados cadastrais dos clientes da clínica (CRUD de pacientes).

2\. \*\*Consulta Service:\*\* Responsável pela marcação, listagem e controle de agendas dos dentistas. Este serviço depende do histórico do Paciente Service para validar os agendamentos.



\---



\## 🔌 Portas Utilizadas



Para evitar conflitos de barramento no ambiente local (localhost), os serviços foram configurados nas seguintes portas:



\* \*\*`paciente-service`\*\*: Porta `8081`

\* \*\*`consulta-service`\*\*: Porta `8082`



\---



\## 🚀 Como Executar os Microsserviços



\### Pré-requisitos

\* Java 11 ou superior instalado.

\* IDE de sua preferência (Eclipse, IntelliJ ou VS Code) com suporte a projetos Maven.



\### Passo a Passo

1\.  Clone este repositório no seu computador ou faça o download das pastas.

2\.  Importe ambas as pastas (`paciente-service` e `consulta-service`) para a sua IDE como projetos Maven existentes.

3\.  Execute primeiramente a classe principal do serviço de pacientes: `PacienteServiceApplication.java` como \*\*Java Application\*\*.

4\.  Em seguida, execute a classe principal do serviço de consultas: `ConsultaServiceApplication.java` como \*\*Java Application\*\*.

5\.  Certifique-se no console da IDE de que ambos os serviços inicializaram com sucesso nas portas `8081` e `8082`, respectivamente.



\---



\## 🛣️ Endpoints Principais (Documentação Swagger)



Ambos os serviços possuem o módulo \*\*Springdoc OpenAPI (Swagger)\*\* integrado. Você pode interagir com as APIs e efetuar os testes diretamente pelo navegador através dos links abaixo (com as aplicações rodando):



\* \*\*Swagger Pacientes:\*\* \[http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

\* \*\*Swagger Consultas:\*\* \[http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)



\### 👤 1. Paciente Service (`Porta 8081`)



| Método | Endpoint | Descrição | Corpo da Requisição (JSON) |

| :--- | :--- | :--- | :--- |

| \*\*POST\*\* | `/pacientes` | Cadastra um novo paciente no sistema | Nome, CPF e Telefone |

| \*\*GET\*\* | `/pacientes` | Lista todos os pacientes cadastrados | Nenhum |

| \*\*GET\*\* | `/pacientes/{id}` | Busca os detalhes de um paciente específico | ID na URL |

| \*\*PUT\*\* | `/pacientes/{id}` | Atualiza as informações de um paciente | Dados atualizados no corpo |

| \*\*DELETE\*\*| `/pacientes/{id}` | Remove um paciente do banco de dados | ID na URL |



\### 📅 2. Consulta Service (`Porta 8082`)



| Método | Endpoint | Descrição | Corpo da Requisição (JSON) |

| :--- | :--- | :--- | :--- |

| \*\*POST\*\* | `/consultas` | Registra um agendamento após validação síncrona | Data/Hora, Dentista, PacienteId |

| \*\*GET\*\* | `/consultas` | Lista todos os agendamentos da clínica | Nenhum |

| \*\*DELETE\*\*| `/consultas/{id}` | Cancela uma consulta agendada | ID na URL |



\---



\## 🔗 Exemplo de Comunicação entre os Serviços



O ecossistema utiliza comunicação síncrona ponta a ponta. Quando uma requisição de agendamento chega ao `consulta-service` (Porta 8082), ele intercepta o parâmetro `pacienteId` e efetua uma chamada interna do tipo `GET` para o `paciente-service` (Porta 8081) para validar a integridade da operação.



\### 🛑 Cenário de Erro (Validação Lógica de Negócio)

Se for enviado um comando `POST` para `/consultas` tentando agendar um horário para um paciente com \*\*`pacienteId: 99`\*\* (que não existe no sistema), o sistema aciona a validação síncrona e barra a gravação:



\* \*\*Chamada Efetuada (Porta 8082):\*\*

&#x20;   ```json

&#x20;   {

&#x20;     "dataHora": "30/05/2026 14:00",

&#x20;     "dentista": "Dr. Rodrigo Silva",

&#x20;     "pacienteId": 99

&#x20;   }

&#x20;   ```

\* \*\*Retorno do Sistema (HTTP Status 400 - Bad Request):\*\*

&#x20;   ```json

&#x20;   "Erro de Agendamento: O paciente com ID 99 não está cadastrado na clínica!"

&#x20;   ```



\### 🟢 Cenário de Sucesso (Fluxo Integrado)

1\.  Efetua-se um `POST` em `http://localhost:8081/pacientes` cadastrando um paciente válido. O banco de dados atribui e retorna o `id: 1`.

2\.  Efetua-se um `POST` em `http://localhost:8082/consultas` passando o parâmetro `"pacienteId": 1`. O serviço de consultas faz a checagem na porta 8081, recebe a confirmação de que o registro existe, e conclui o agendamento retornando \*\*HTTP Status 201 (Created)\*\*.

