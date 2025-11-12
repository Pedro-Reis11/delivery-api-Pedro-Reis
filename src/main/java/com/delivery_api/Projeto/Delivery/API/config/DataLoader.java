package com.delivery_api.Projeto.Delivery.API.config;

import com.delivery_api.Projeto.Delivery.API.entity.*;
import com.delivery_api.Projeto.Delivery.API.enums.PedidoStatus;
import com.delivery_api.Projeto.Delivery.API.repository.ClienteRepository;
import com.delivery_api.Projeto.Delivery.API.repository.PedidoRepository;
import com.delivery_api.Projeto.Delivery.API.repository.ProdutoRepository;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class DataLoader implements CommandLineRunner {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n===== INICIANDO DATALOADER =====\n");

        // Limpar dados existentes opcional
        limparDados();

        // Inserir dados de teste
        inserirClientes();
        inserirRestaurantes();
        inserirProdutos();
        inserirPedidos();

        // Validar consultas
        validarConsultas();

        System.out.println("\n ===== DATALOADER CONCLUÍDO =====\n");
    }

    private void inserirClientes() {
        System.out.println("Inserindo clientes...");

        Cliente cliente1 = new Cliente();
        cliente1.setNome("Carlos Eduardo Santos");
        cliente1.setEmail("carlos.santos@email.com");
        cliente1.setTelefone("(11) 98765-4321");
        cliente1.setEndereco("Rua das Palmeiras, 500 - São Paulo/SP");
        cliente1.setAtivo(true);
        cliente1.setDataCadastro(LocalDateTime.now());

        Cliente cliente2 = new Cliente();
        cliente2.setNome("Ana Paula Oliveira");
        cliente2.setEmail("ana.oliveira@email.com");
        cliente2.setTelefone("(11) 97654-3210");
        cliente2.setEndereco("Av. Brigadeiro, 1200 - São Paulo/SP");
        cliente2.setAtivo(true);
        cliente2.setDataCadastro(LocalDateTime.now());

        Cliente cliente3 = new Cliente();
        cliente3.setNome("Roberto Silva Junior");
        cliente3.setEmail("roberto.silva@email.com");
        cliente3.setTelefone("(11) 96543-2109");
        cliente3.setEndereco("Rua Oscar Freire, 800 - São Paulo/SP");
        cliente3.setAtivo(true);
        cliente3.setDataCadastro(LocalDateTime.now());

        clienteRepository.saveAll(List.of(cliente1, cliente2, cliente3));
        System.out.println("3 clientes inseridos com sucesso!\n");
    }

    private void inserirRestaurantes() {
        System.out.println("Inserindo restaurantes...");

        Restaurante restaurante1 = new Restaurante();
        restaurante1.setNome("Cantina Italiana Bella Vista");
        restaurante1.setCategoria("Italiana");
        restaurante1.setEndereco("Rua Bela Cintra, 1500 - São Paulo/SP");
        restaurante1.setTelefone("(11) 3456-7890");
        restaurante1.setTaxaEntrega(new BigDecimal("8.00"));
        restaurante1.setAvaliacao(new BigDecimal("4.7"));
        restaurante1.setAtivo(true);
        restaurante1.setDataCadastro(LocalDateTime.now());

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setNome("Sushi Premium Tokyo");
        restaurante2.setCategoria("Japonesa");
        restaurante2.setEndereco("Av. Liberdade, 350 - São Paulo/SP");
        restaurante2.setTelefone("(11) 3567-8901");
        restaurante2.setTaxaEntrega(new BigDecimal("12.00"));
        restaurante2.setAvaliacao(new BigDecimal("4.9"));
        restaurante2.setAtivo(true);
        restaurante2.setDataCadastro(LocalDateTime.now());

        restauranteRepository.saveAll(List.of(restaurante1, restaurante2));
        System.out.println("2 restaurantes inseridos com sucesso!\n");
    }

    private void inserirProdutos() {
        System.out.println("Inserindo produtos...");

        List<Restaurante> restaurantes = restauranteRepository.findAll();
        if (restaurantes.size() < 2) {
            System.out.println("Erro: Restaurantes não encontrados!");
            return;
        }

        Restaurante italianRestaurant = restaurantes.get(0);
        Restaurante japaneseRestaurant = restaurantes.get(1);

        // Produtos do restaurante italiano
        Produto produto1 = new Produto();
        produto1.setNome("Lasanha à Bolonhesa");
        produto1.setDescricao("Lasanha tradicional com molho bolonhesa caseiro");
        produto1.setPreco(new BigDecimal("42.90"));
        produto1.setCategoria("Massas");
        produto1.setDisponivel(true);
        produto1.setRestaurante(italianRestaurant);

        Produto produto2 = new Produto();
        produto2.setNome("Risoto de Funghi");
        produto2.setDescricao("Risoto cremoso com cogumelos frescos");
        produto2.setPreco(new BigDecimal("38.90"));
        produto2.setCategoria("Risotos");
        produto2.setDisponivel(true);
        produto2.setRestaurante(italianRestaurant);

        Produto produto3 = new Produto();
        produto3.setNome("Tiramisu Clássico");
        produto3.setDescricao("Sobremesa italiana com café e mascarpone");
        produto3.setPreco(new BigDecimal("18.90"));
        produto3.setCategoria("Sobremesas");
        produto3.setDisponivel(true);
        produto3.setRestaurante(italianRestaurant);

        // Produtos do restaurante japonês
        Produto produto4 = new Produto();
        produto4.setNome("Combo Premium 30 peças");
        produto4.setDescricao("30 peças variadas de sushi e sashimi premium");
        produto4.setPreco(new BigDecimal("89.90"));
        produto4.setCategoria("Combinados");
        produto4.setDisponivel(true);
        produto4.setRestaurante(japaneseRestaurant);

        Produto produto5 = new Produto();
        produto5.setNome("Temaki Salmão Especial");
        produto5.setDescricao("Temaki de salmão com cream cheese e cebolinha");
        produto5.setPreco(new BigDecimal("24.90"));
        produto5.setCategoria("Temaki");
        produto5.setDisponivel(true);
        produto5.setRestaurante(japaneseRestaurant);

        produtoRepository.saveAll(List.of(produto1, produto2, produto3, produto4, produto5));
        System.out.println("5 produtos inseridos com sucesso!\n");
    }

    private void inserirPedidos() {
        System.out.println("Inserindo pedidos...");

        List<Cliente> clientes = clienteRepository.findAll();
        List<Restaurante> restaurantes = restauranteRepository.findAll();
        List<Produto> produtos = produtoRepository.findAll();

        if (clientes.isEmpty() || restaurantes.isEmpty() || produtos.isEmpty()) {
            System.out.println("Erro: Dados base não encontrados!");
            return;
        }

        // Pedido 1: Cliente 1 no Restaurante Italiano
        Pedido pedido1 = new Pedido();
        pedido1.setNumeroPedido("PED-" + System.currentTimeMillis());
        pedido1.setCliente(clientes.get(0));
        pedido1.setRestaurante(restaurantes.get(0));
        pedido1.setEnderecoEntrega(clientes.get(0).getEndereco());
        pedido1.setObservacoes("Entregar após às 19h");
        pedido1.setStatus(PedidoStatus.PENDENTE);
        pedido1.setDataPedido(LocalDateTime.now());
        pedido1.setDataCriacao(LocalDateTime.now());

        // Adicionar itens ao pedido 1
        ItemPedido item1 = new ItemPedido();
        item1.setPedido(pedido1);
        item1.setProduto(produtos.get(0)); // Lasanha
        item1.setQuantidade(2);
        item1.setPrecoUnitario(produtos.get(0).getPreco());
        item1.calcularSubtotal();
        item1.setObservacoes("Bem quente, por favor");

        ItemPedido item2 = new ItemPedido();
        item2.setPedido(pedido1);
        item2.setProduto(produtos.get(2)); // Tiramisu
        item2.setQuantidade(1);
        item2.setPrecoUnitario(produtos.get(2).getPreco());
        item2.calcularSubtotal();

        pedido1.setItens(List.of(item1, item2));
        pedido1.calcularTotal();

        // Pedido 2: Cliente 2 no Restaurante Japonês
        Pedido pedido2 = new Pedido();
        pedido2.setNumeroPedido("PED-" + (System.currentTimeMillis() + 1000));
        pedido2.setCliente(clientes.get(1));
        pedido2.setRestaurante(restaurantes.get(1));
        pedido2.setEnderecoEntrega(clientes.get(1).getEndereco());
        pedido2.setObservacoes("Sem wasabi");
        pedido2.setStatus(PedidoStatus.CONFIRMADO);
        pedido2.setDataPedido(LocalDateTime.now().minusHours(1));
        pedido2.setDataCriacao(LocalDateTime.now().minusHours(1));

        // Adicionar itens ao pedido 2
        ItemPedido item3 = new ItemPedido();
        item3.setPedido(pedido2);
        item3.setProduto(produtos.get(3)); // Combo Premium
        item3.setQuantidade(1);
        item3.setPrecoUnitario(produtos.get(3).getPreco());
        item3.calcularSubtotal();

        ItemPedido item4 = new ItemPedido();
        item4.setPedido(pedido2);
        item4.setProduto(produtos.get(4)); // Temaki
        item4.setQuantidade(2);
        item4.setPrecoUnitario(produtos.get(4).getPreco());
        item4.calcularSubtotal();
        item4.setObservacoes("Extra cream cheese");

        pedido2.setItens(List.of(item3, item4));
        pedido2.calcularTotal();

        pedidoRepository.saveAll(List.of(pedido1, pedido2));
        System.out.println("2 pedidos inseridos com sucesso!\n");
    }

    private void validarConsultas() {
        System.out.println("===== VALIDANDO CONSULTAS DERIVADAS =====\n");

        // 1. Testar busca de cliente por email
        System.out.println("1️Buscar cliente por email:");
        clienteRepository.findByEmail("carlos.santos@email.com")
                .ifPresentOrElse(
                        cliente -> System.out.println("Cliente encontrado: " + cliente.getNome()),
                        () -> System.out.println("Cliente não encontrado")
                );

        // 2. Testar produtos por restaurante
        System.out.println("\nProdutos por restaurante:");
        List<Produto> produtos = produtoRepository.findByRestauranteId(1L);
        System.out.println("Encontrados " + produtos.size() + " produtos");
        produtos.forEach(p -> System.out.println("      - " + p.getNome() + ": R$ " + p.getPreco()));

        // 3. Testar pedidos recentes
        System.out.println("\nTop 10 pedidos mais recentes:");
        List<Pedido> pedidosRecentes = pedidoRepository.findTop10ByOrderByDataPedidoDesc();
        System.out.println("Encontrados " + pedidosRecentes.size() + " pedidos");
        pedidosRecentes.forEach(p -> System.out.println("      - " + p.getNumeroPedido() +
                " | Status: " + p.getStatus() +
                " | Total: R$ " + p.getTotal()));

        // 4. Testar restaurantes por taxa
        System.out.println("\nRestaurantes com taxa até R$ 10,00:");
        List<Restaurante> restaurantes = restauranteRepository
                .findByTaxaEntregaLessThanEqual(new BigDecimal("10.00"));
        System.out.println("Encontrados " + restaurantes.size() + " restaurantes");
        restaurantes.forEach(r -> System.out.println("      - " + r.getNome() +
                " | Taxa: R$ " + r.getTaxaEntrega()));

        // 5. Testar clientes ativos
        System.out.println("\nClientes ativos:");
        List<Cliente> clientesAtivos = clienteRepository.findByAtivoTrue();
        System.out.println("Encontrados " + clientesAtivos.size() + " clientes ativos");

        // 6. Testar produtos disponíveis
        System.out.println("\nProdutos disponíveis:");
        List<Produto> produtosDisponiveis = produtoRepository.findByDisponivelTrue();
        System.out.println("Encontrados " + produtosDisponiveis.size() + " produtos disponíveis");

        // 7. Testar pedidos por status
        System.out.println("\nPedidos PENDENTES:");
        List<Pedido> pedidosPendentes = pedidoRepository.findByStatus(PedidoStatus.PENDENTE);
        System.out.println("Encontrados " + pedidosPendentes.size() + " pedidos pendentes");

        // 8. Testar top 5 restaurantes por nome
        System.out.println("\nTop 5 restaurantes (ordem alfabética):");
        List<Restaurante> top5 = restauranteRepository.findTop5ByOrderByNomeAsc();
        System.out.println("Encontrados " + top5.size() + " restaurantes");
        top5.forEach(r -> System.out.println("      - " + r.getNome()));

        // 9. Testar produtos por faixa de preço
        System.out.println("\nProdutos até R$ 50,00:");
        List<Produto> produtosBaratos = produtoRepository
                .findByPrecoLessThanEqual(new BigDecimal("50.00"));
        System.out.println("Encontrados " + produtosBaratos.size() + " produtos");

        // 10. Verificar relacionamentos
        System.out.println("\nVerificando relacionamentos:");
        pedidoRepository.findAll().forEach(pedido -> {
            System.out.println("Pedido " + pedido.getNumeroPedido() + ":");
            System.out.println("Cliente: " + pedido.getCliente().getNome());
            System.out.println("Restaurante: " + pedido.getRestaurante().getNome());
            System.out.println("Itens: " + pedido.getItens().size());
        });

        System.out.println("\n ===== VALIDAÇÃO CONCLUÍDA =====\n");
    }

    private void limparDados() {
        System.out.println("Limpando dados existentes...");
        pedidoRepository.deleteAll();
        produtoRepository.deleteAll();
        restauranteRepository.deleteAll();
        clienteRepository.deleteAll();
        System.out.println("Dados limpos!\n");
    }
}
