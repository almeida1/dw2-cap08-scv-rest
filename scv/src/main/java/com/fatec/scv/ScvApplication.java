package com.fatec.scv;
import java.util.Arrays;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fatec.scv.matemPedidos.model.ItemDePedido;
import com.fatec.scv.matemPedidos.model.Pedido;
import com.fatec.scv.matemPedidos.model.Produto;
import com.fatec.scv.matemPedidos.ports.ItemDePedidoRepository;

import com.fatec.scv.matemPedidos.ports.PedidoServico;
import com.fatec.scv.matemPedidos.ports.ProdutoRepository;

@SpringBootApplication
public class ScvApplication implements CommandLineRunner{
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private PedidoServico pedidoServico;
	@Autowired
	private ItemDePedidoRepository itemDePedidoRepository;
	Logger logger = LogManager.getLogger(this.getClass());
	
	public static void main(String[] args) {
		SpringApplication.run(ScvApplication.class, args);
	}
	
	@Override
	public void run (String... args) throws Exception{
		// cliente prviamente cadastrado 43011831084 - test fixture
		//cadastra produto
		Produto produto1 = new Produto("parafuso", 10, 30); //descricao e quantidade no estoque
		Produto produto3 = new Produto("bucha", 5, 50);    
		Produto produto2 = new Produto("tijolo",  15, 60);
		produtoRepository.saveAll(Arrays.asList(produto1, produto2, produto3));
		
		Pedido pedido1 = new Pedido("43011831084");
		
		Optional<Produto> umProduto = produtoRepository.findById(1L);
		Produto produtoComprado1 = umProduto.get();
		umProduto = produtoRepository.findById(3L);
		Produto produtoComprado2 = umProduto.get();
		
		ItemDePedido ip1 = new ItemDePedido(produtoComprado1, 20); // quantidade comprada
		ItemDePedido ip2 = new ItemDePedido(produtoComprado2, 10); // quantidade comprada
		pedido1.getItens().addAll(Arrays.asList(ip1, ip2)); 
		itemDePedidoRepository.saveAll(pedido1.getItens());
		pedidoServico.cadastrarPedido(pedido1);
	}
}
