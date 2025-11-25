# 🍕 Delivery Tech API

**Delivery Tech API** é um sistema de delivery moderno desenvolvido com **Spring Boot** e **Java 21**, aplicando boas práticas de arquitetura, recursos modernos da linguagem e integração com banco de dados em memória (**H2**).

---

## 🚀 Tecnologias Utilizadas

- **Java 21 LTS** (versão mais recente)
- **Spring Boot 3.2.x**
- **Spring Web**
- **Spring Data JPA**
- **H2 Database**
- **Maven**

---

## ⚡ Recursos Modernos do Java

- 🧱 **Records** (Java 14+)
- 🧾 **Text Blocks** (Java 15+)
- 🧩 **Pattern Matching** (Java 17+)
- 🧵 **Virtual Threads** (Java 21)

---

## 🏃‍♂️ Como executar
- Pré-requisitos: JDK 21 instalado
- Clone o repositório
- Execute: ./mvnw spring-boot:run
- Acesse: http://localhost:8080/health

---

## 📋 Endpoints
- GET /health - Status da aplicação (inclui versão Java)
- GET /info - Informações da aplicação
- GET /h2-console - Console do banco H2
- Testando endpoints públicos

Rotas configuradas como públicas:

POST /auth/login
POST /auth/register
POST /clientes
GET /swagger-ui/
GET /v3/api-docs/
GET /clientes/buscar/{nome}

Essas você pode usar no Swagger direto, clicando:

➡ Try it out
➡ Preencher corpo (JSON)
➡ Execute

Testando endpoints protegidos com JWT

Passo 1 — fazer login

Vá até /auth/login.

Clique Try it out

Envie JSON:

{
  "email": "admin@teste.com",
  "senha": "123456"
}


Ele vai retornar:

{
  "token": "eyJhbGciOiJIUzI1NiIsInR5..."
}

Passo 2 — Enviar o token ao Swagger (Authorization)

No topo direito da interface do Swagger existe um botão:

👉 Authorize

Clique nele e cole:

Bearer seu_token_aqui


⚠ Não esqueça do Bearer + espaço.

Depois disso:

✔ Todas as rotas autenticadas passam a funcionar
✔ Não precisa enviar token manualmente por header

---

## 🔧 Configuração
- Porta: 8080
- Banco: H2 em memória
- Profile: development

---

## 👨‍💻 Desenvolvedor
- Pedro Verissimo Rocha Reis — Una Aimorés
- 💻 Desenvolvido com JDK 21 e Spring Boot 3.2.x
