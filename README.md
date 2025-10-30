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

## 🏃‍♂️ Como Executar o Projeto

### 📋 Pré-requisitos
- JDK 21 instalado

### 🔧 Passos para execução

```bash
# Clonar o repositório
git clone https://github.com/usuario/delivery-api-Pedro-Reis.git
cd delivery-api-Pedro-Reis

# Executar a aplicação
./mvnw spring-boot:run

🌐 Endpoints de Acesso

Recurso	URL
Status da aplicação	http://localhost:8080/health

Informações da aplicação	http://localhost:8080/info

Console do banco H2	http://localhost:8080/h2-console

📦 Endpoints Principais

🧍 Cliente
Método	Endpoint	Descrição
POST	/clientes	Cadastrar novo cliente
GET	/clientes	Listar clientes ativos
GET	/clientes/{id}	Buscar cliente por ID
PUT	/clientes/{id}	Atualizar cliente
DELETE	/clientes/{id}	Inativar cliente
GET	/clientes/buscar?nome={nome}	Buscar clientes por nome
GET	/clientes/email/{email}	Buscar cliente por e-mail

🧾 Exemplo de Request Body
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "99999-9999"
}

🍔 Produto

Método	Endpoint	Descrição
POST	/produtos/restaurante/{restauranteId}	Cadastrar produto por restaurante
GET	/produtos?restauranteId={id}	Listar produtos por restaurante
GET	/produtos/{id}	Buscar produto por ID
PUT	/produtos/{id}	Atualizar produto
DELETE	/produtos/{id}	Desativar produto
GET	/produtos/restaurante/{restauranteId}/disponiveis	Listar produtos disponíveis por restaurante

🧾 Exemplo de Request Body
{
  "nome": "Pizza Margherita",
  "descricao": "Pizza com mussarela, tomate e manjericão",
  "preco": 39.90
}

📦 Pedido

Método	Endpoint	Descrição
POST	/pedidos	Criar pedido
GET	/pedidos?clienteId={id}	Listar pedidos por cliente
GET	/pedidos/{id}	Buscar pedido por ID
PUT	/pedidos/{id}/status	Atualizar status do pedido
PUT	/pedidos/{id}	Atualizar informações do pedido

🧾 Exemplo de Request Body
{
  "cliente": { "id": 1 },
  "itens": [
    { "produtoId": 1, "quantidade": 2 }
  ],
  "enderecoEntrega": "Rua das Flores, 123"
}

🍽️ Restaurante

Método	Endpoint	Descrição
POST	/restaurantes	Cadastrar restaurante
GET	/restaurantes	Listar restaurantes ativos
GET	/restaurantes/{id}	Buscar restaurante por ID
PUT	/restaurantes/{id}	Atualizar restaurante
DELETE	/restaurantes/{id}	Inativar restaurante
GET	/restaurantes/categoria?categoria={categoria}	Buscar restaurantes por categoria

🧾 Exemplo de Request Body
{
  "nome": "Pizzaria Bella",
  "categoria": "Italiana",
  "endereco": "Av. Brasil, 1000",
  "telefone": "99999-0000"
}

⚙️ Configurações da Aplicação

Configuração	Valor
Porta padrão	8080
Banco de dados	H2 (em memória)
Profile ativo	development
CORS	Habilitado para todas as origens (*)

🧠 Observações Importantes

✅ Utilize Postman ou curl para testar os endpoints.

🧩 O banco H2 é em memória, portanto os dados são perdidos ao reiniciar a aplicação.

⚙️ Inclui validações de campos obrigatórios e regras de negócio (ex: status de pedidos).

🚀 Para ambiente de produção, configure PostgreSQL e ajuste os profiles no application.yml.

👨‍💻 Desenvolvedor
Pedro Verissimo Rocha Reis — Una Aimorés
💻 Desenvolvido com JDK 21 e Spring Boot 3.2.x
