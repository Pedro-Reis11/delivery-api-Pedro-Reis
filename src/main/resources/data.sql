-- CLIENTES
INSERT INTO cliente (nome, email, telefone, endereco, ativo, data_cadastro) VALUES
('Carlos Eduardo Santos', 'carlos.santos@email.com', '(11) 98765-4321', 'Rua das Palmeiras, 500 - São Paulo/SP', TRUE, CURRENT_TIMESTAMP),
('Ana Paula Oliveira', 'ana.oliveira@email.com', '(11) 97654-3210', 'Av. Brigadeiro, 1200 - São Paulo/SP', TRUE, CURRENT_TIMESTAMP),
('Roberto Silva Junior', 'roberto.silva@email.com', '(11) 96543-2109', 'Rua Oscar Freire, 800 - São Paulo/SP', TRUE, CURRENT_TIMESTAMP);

-- RESTAURANTES
INSERT INTO restaurante (nome, categoria, endereco, telefone, taxa_entrega, avaliacao, ativo, data_cadastro) VALUES
('Cantina Italiana Bella Vista', 'Italiana', 'Rua Bela Cintra, 1500 - São Paulo/SP', '(11) 3456-7890', 8.00, 4.7, TRUE, CURRENT_TIMESTAMP),
('Sushi Premium Tokyo', 'Japonesa', 'Av. Liberdade, 350 - São Paulo/SP', '(11) 3567-8901', 12.00, 4.9, TRUE, CURRENT_TIMESTAMP);

-- PRODUTOS (Italiano)
INSERT INTO produto (nome, descricao, preco, categoria, disponivel, restaurante_id) VALUES
('Lasanha à Bolonhesa', 'Lasanha tradicional com molho bolonhesa caseiro', 42.90, 'Massas', TRUE, 1),
('Risoto de Funghi', 'Risoto cremoso com cogumelos frescos', 38.90, 'Risotos', TRUE, 1),
('Tiramisu Clássico', 'Sobremesa italiana com café e mascarpone', 18.90, 'Sobremesas', TRUE, 1);

-- PRODUTOS (Japonês)
INSERT INTO produto (nome, descricao, preco, categoria, disponivel, restaurante_id) VALUES
('Combo Premium 30 peças', '30 peças variadas de sushi e sashimi premium', 89.90, 'Combinados', TRUE, 2),
('Temaki Salmão Especial', 'Temaki de salmão com cream cheese e cebolinha', 24.90, 'Temaki', TRUE, 2);

-- PEDIDOS
INSERT INTO pedido (numero_pedido, data_pedido, data_criacao, status, total, observacoes, endereco_entrega, cliente_id, restaurante_id) VALUES
('PED-1001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'PENDENTE', 104.70, 'Entregar após às 19h', 'Rua das Palmeiras, 500 - São Paulo/SP', 1, 1),
('PED-1002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'CONFIRMADO', 139.70, 'Sem wasabi', 'Av. Brigadeiro, 1200 - São Paulo/SP', 2, 2);

-- ITENS DO PEDIDO 1 (Italiano)
INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario, subtotal, observacoes) VALUES
(1, 1, 2, 42.90, 85.80, 'Bem quente, por favor'),
(1, 3, 1, 18.90, 18.90, 'Sem observações');

-- ITENS DO PEDIDO 2 (Japonês)
INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario, subtotal, observacoes) VALUES
(2, 4, 1, 89.90, 89.90, NULL),
(2, 5, 2, 24.90, 49.80, 'Extra cream cheese');
