package com.fatec.scv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
class REQ02ConsultarPedidoTests {
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private PedidoServico pedidoServico;
	@Autowired
	private ProdutoRepository produtoRepository;

	@Test
	@Transactional
	void ct01_quando_cpf_tem_venda_cadastrada_consulta_retorna_detalhes_do_pedido() {

		Pedido pedido1 = new Pedido("99504993052");

		Optional<Produto> umProduto = produtoRepository.findById(1L);
		Produto produtoComprado1 = umProduto.get();
		umProduto = produtoRepository.findById(3L);
		Produto produtoComprado2 = umProduto.get();

		ItemDePedido ip1 = new ItemDePedido(produtoComprado1, 20); // quantidade comprada
		ItemDePedido ip2 = new ItemDePedido(produtoComprado2, 10); // quantidade comprada
		pedido1.getItens().addAll(Arrays.asList(ip1, ip2)); // pedido1 comprou parafuso e bucha
		Pedido pedidoRetornado = pedidoServico.cadastrarPedido(pedido1);
		assertTrue(pedidoRepository.findByCpf("99504993052").size() >= 1);
		List<Pedido> pedidos = pedidoRepository.findByCpf("99504993052");
		pedidoRetornado = pedidos.get(0);
		assertEquals(250.0, pedidoRetornado.getValorTotal());
		List<ItemDePedido> itens = pedidoRetornado.getItens();

		List<ItemDePedido> lista = new ArrayList<>();
		for (ItemDePedido i : itens) {
			lista.add(i);
		}
		double soma = 0;
		for (ItemDePedido i : lista) {
			System.out.println(i.getId());
			soma = soma + i.getSubTotal();
		}
		assertEquals(250.0, soma);
	}
	@Test
	void ct02_quando_cpf_valido_nao_tem_venda_cadastrada_consulta_retorna_vazio() {
		assertTrue(pedidoRepository.findByCpf("81380757320").isEmpty());
	}
	@Test
	void ct03_quando_cpf_invalido_consulta_retorna_vazio() {
		assertTrue(pedidoRepository.findByCpf("813807573").isEmpty());
	}
}
