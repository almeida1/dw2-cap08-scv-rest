package com.fatec.scv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.scv.matemPedidos.model.ItemDePedido;
import com.fatec.scv.matemPedidos.model.Pedido;
import com.fatec.scv.matemPedidos.model.Produto;
import com.fatec.scv.matemPedidos.ports.PedidoRepository;
import com.fatec.scv.matemPedidos.ports.PedidoServico;
import com.fatec.scv.matemPedidos.ports.ProdutoRepository;
@SpringBootTest
class REQ01CadastrarPedidoTests {
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private PedidoServico pedidoServico;
	@Autowired 
	private ProdutoRepository produtoRepository;
	@Test
	@Transactional
	void ct01_quando_dados_validos_cadastra_pedido_com_sucessot() {
		// cliente cadastrado 99504993052 - test fixture
		// cliente cadastrado 43011831084
		Pedido pedido1 = new Pedido("99504993052");
		
		Optional<Produto> umProduto = produtoRepository.findById(1L);
		Produto produtoComprado1 = umProduto.get();
		umProduto = produtoRepository.findById(3L);
		Produto produtoComprado2 = umProduto.get();
		
		ItemDePedido ip1 = new ItemDePedido(produtoComprado1, 20); // quantidade comprada
		ItemDePedido ip2 = new ItemDePedido(produtoComprado2, 10); // quantidade comprada
		pedido1.getItens().addAll(Arrays.asList(ip1, ip2));        //pedido1 comprou parafuso e bucha
		Pedido pedidoRetornado = pedidoServico.cadastrarPedido(pedido1);
		System.out.println("data de emissao do pedido=>" + pedidoRetornado.getDataEmissao());
		assertEquals (250.0 , pedidoRetornado.getValorTotal());
		assertTrue(pedidoRepository.findByCpf("99504993052").size()>=1);
		pedido1 = new Pedido("63628494699");
	}
	@Test
	void ct02_quando_cpf_valido_sem_pedido_cadastrado_entao_retorna_vazio() {
		// cliente cadastrado 99504993052 - test fixture
		// cliente cadastrado 43011831084
		Pedido pedido1 = new Pedido("63628494699");
		
		Optional<Produto> umProduto = produtoRepository.findById(1L);
		Produto produtoComprado1 = umProduto.get();
		umProduto = produtoRepository.findById(3L);
		Produto produtoComprado2 = umProduto.get();
		
		ItemDePedido ip1 = new ItemDePedido(produtoComprado1, 20); // quantidade comprada
		ItemDePedido ip2 = new ItemDePedido(produtoComprado2, 10); // quantidade comprada
		pedido1.getItens().addAll(Arrays.asList(ip1, ip2));        //pedido1 comprou parafuso e bucha
		Pedido pedidoRetornado = pedidoServico.cadastrarPedido(pedido1);
		assertNull(pedidoRetornado);
	}
}
