# Sistema de Controle de Bicicletário

Este repositório contém a implementação de uma API desenvolvida como parte do trabalho prático da disciplina de **Projeto de Monitoramento** no período 2024.1. O objetivo do trabalho foi construir uma API que gerencia um sistema de controle de bicicletário, desenvolvida em equipe, onde cada membro ficou responsável por um microserviço específico. Esta API segue os princípios da **Clean Architecture**, utilizando o framework **Spring Boot** para fornecer uma estrutura modular e de fácil manutenção.

## Descrição do Projeto

A API foi desenvolvida a partir de uma série de requisitos do sistema e casos de uso fornecidos pelo professor e documentados no Swagger. O sistema de controle de bicicletário permite a gestão de ciclistas, bicicletas, trancas, totens, e outras funcionalidades relacionadas ao aluguel e manutenção de bicicletas

### Tecnologias Utilizadas

- **Spring Boot**: Framework para desenvolvimento de aplicações Java.
- **OpenAPI/Swagger**: Para documentar e definir a API.
- **Arquitetura Limpa (Clean Architecture)**: Seguindo uma estrutura organizada em camadas com domínio, aplicação e infraestrutura separados.
- **CI/CD com AWS**: Integração e entrega contínua utilizando serviços da AWS como EC2 e S3, além de pipelines de build e deploy automatizados.
- **Testes Unitários e de Integração**: Cobertura de testes para garantir a qualidade do código.
## Estrutura do Repositórioo

A estrutura segue os princípios da Clean Architecture, organizada da seguinte forma:
- **Domain**: Contém as entidades e objetos de domínio.
- **Application**: Regras de negócio e serviços.
- **Infrastructure**: Implementação de repositórios, configuração do banco de dados, e outras integrações com serviços externos.
- **Shared**: Código compartilhado e utilitários comuns.

## Microserviços

Cada membro da equipe foi responsável por implementar um microserviço específico. O **Microsserviço de Equipamento**, que gerencia a parte de cadastro, manutenção e integração de bicicletas, trancas e totens na rede de bicicletários foi feito pelo Rafael Ventura

### Equipamento - Endpoints Desenvolvidos

#### Bicicleta

- **`GET /bicicleta`**: Recupera todas as bicicletas cadastradas.
- **`POST /bicicleta`**: Cadastra uma nova bicicleta.
- **`POST /bicicleta/integrarNaRede`**: Integra uma bicicleta nova ou retornando de reparo na rede de totens.
- **`POST /bicicleta/retirarDaRede`**: Remove uma bicicleta para reparo ou aposentadoria.
- **`GET /bicicleta/{idBicicleta}`**: Recupera uma bicicleta pelo ID.
- **`PUT /bicicleta/{idBicicleta}`**: Atualiza os dados de uma bicicleta.
- **`DELETE /bicicleta/{idBicicleta}`**: Remove uma bicicleta da rede.

#### Tranca

- **`GET /tranca`**: Recupera todas as trancas cadastradas.
- **`POST /tranca`**: Cadastra uma nova tranca.
- **`POST /tranca/integrarNaRede`**: Integra uma tranca nova ou retornando de reparo na rede de totens.
- **`POST /tranca/retirarDaRede`**: Remove uma tranca para reparo ou aposentadoria.
- **`POST /tranca/{idTranca}/trancar`**: Tranca uma tranca e associa uma bicicleta a ela.
- **`POST /tranca/{idTranca}/destrancar`**: Destranca uma tranca e desassocia uma bicicleta.

#### Totem

- **`GET /totem`**: Recupera todos os totens cadastrados.
- **`POST /totem`**: Cadastra um novo totem.
- **`PUT /totem/{idTotem}`**: Atualiza os dados de um totem.
- **`DELETE /totem/{idTotem}`**: Remove um totem da rede.

## Swagger

Toda a API foi documentada usando **OpenAPI 3.0** e pode ser acessada por meio do Swagger para facilitar o entendimento dos endpoints e suas respectivas funcionalidades. A documentação descreve detalhadamente os parâmetros e respostas esperadas para cada operação.

## Integração e Deploy

Foi utilizada uma infraestrutura em **AWS** para o CI/CD, com pipelines automatizados para build, testes e deploy da API em instâncias **EC2**.

