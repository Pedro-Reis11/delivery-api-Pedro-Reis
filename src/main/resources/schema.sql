DROP TABLE IF EXISTS item_pedido;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS produto;
DROP TABLE IF EXISTS restaurante;
DROP TABLE IF EXISTS cliente;

CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(50),
    endereco VARCHAR(255),
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE restaurante (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    categoria VARCHAR(100),
    endereco VARCHAR(255),
    telefone VARCHAR(50),
    taxa_entrega DECIMAL(10,2) DEFAULT 0.0,
    avaliacao DECIMAL(3,2),
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE produto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(255),
    preco DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(100),
    disponivel BOOLEAN DEFAULT TRUE,
    restaurante_id BIGINT NOT NULL,
    CONSTRAINT fk_produto_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurante(id) ON DELETE CASCADE
);

CREATE TABLE pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_pedido VARCHAR(50) NOT NULL UNIQUE,
    data_pedido TIMESTAMP NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    total DECIMAL(10,2),
    observacoes VARCHAR(255),
    endereco_entrega VARCHAR(255) NOT NULL,
    cliente_id BIGINT NOT NULL,
    restaurante_id BIGINT NOT NULL,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE,
    CONSTRAINT fk_pedido_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurante(id) ON DELETE CASCADE
);

CREATE TABLE item_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2),
    observacoes VARCHAR(255),
    CONSTRAINT fk_item_pedido_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_pedido_produto FOREIGN KEY (produto_id) REFERENCES produto(id) ON DELETE CASCADE
);
