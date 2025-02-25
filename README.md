# Sistema de Gestão da Associação Quilombola do Castainho  

Este repositório contém o sistema web para a gestão da Associação Quilombola do Castainho. O sistema foi desenvolvido para facilitar a administração de associados, dependentes e mensalidades, bem como para oferecer suporte às atividades da associação.  

---

## 🎯 **Objetivo do Projeto**
Este projeto foi desenvolvido como parte das atividades acadêmicas do curso de Análise e Desenvolvimento de Software da Universidade Tiradentes - UNIT.

A associação Quilombola do Castainho enfrenta desafios relacionados aos registros dos associados que são mantidos manualmente. Esse método é propenso a erros, perda de informações e dificuldades para acessar dados relevantes. Com o crescimento da associação, a gestão manual torna-se cada vez mais complexa, dificultando o controle e a organização.

## 🚀 **Funcionalidades Principais**  

- **Cadastro de Usuários:**  
  - Os usuários podem se cadastrar e gerenciar o sistema conforme suas permissões.  
  - Cadastro, edição e visualização dos usuários.

- **Gerenciamento de Associados:**  
  - Cadastro de novos associados.  
  - Visualização e edição de dados dos associados.  
  - Inclusão de dependentes relacionados aos associados.  
  - Controle de status de associados (ativos/inativos).  

- **Gestão de Mensalidades:**  
  - Cadastro de mensalidades para associados.  
  - Registro de pagamentos de mensalidades.  
  - Visualização de histórico de todas as mensalidades e filtros de pesquisa.
  - Gerar mensalidades por lote. 

---

## 📜 **Regras de Negócio**  

1. **Proibido remover associado com mensalidades pagas.**  
2. **Mensalidades pagas não podem ser removidas.**  
3. **Mensalidades pagas podem ser editadas.**  
4. **Não permitir cadastrar mensalidades para associados inativos.**
5. **Não permitir pagar mensalidades sem colocar data de vencimento.**  

---

## 🛠️ **Tecnologias Utilizadas**  

- **Backend:**  
  - Java com Spring Boot.  
  - Spring Data JPA para manipulação de dados.  
  - Banco de Dados relacional.  

- **Frontend:**  
  - Thymeleaf para renderização de páginas dinâmicas. 

- **Testes:**  
  - Junit para testes unitários. 
     
- **Outras Bibliotecas/Recursos:**  
  - iText para geração de relatórios em PDF.  
  - Controle de acesso e autenticação com Spring Security.  

---

## 📐 **Diagrama ER do Banco de Dados**  

![Diagrama ER](db_associacao.png)  

---

## ⚙️ **Como Executar o Projeto**  

1. **Pré-requisitos:**  
   - Java 11+  
   - Maven  
   - Banco de Dados configurado (ex.: MySQL, PostgreSQL)  

2. **Clonar o Repositório:**  
   ```bash  
   git clone https://github.com/dayannaclaudino/gestaoassociacao.git  
   cd gestaoassociacao  
