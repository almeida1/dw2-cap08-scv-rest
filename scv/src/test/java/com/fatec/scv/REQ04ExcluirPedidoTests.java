package com.fatec.scv;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
class REQ04ExcluirPedidoTests {
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired 
	private ProdutoRepository produtoRepository;
	@Autowired
	private PedidoServico pedidoServico;
	@Transactional
	@Test
	void ct01_dado_que_o_pedido_esta_cadastrado_quando_exclui_entao_retorna_vazio() {
		//Dado que o pedido esta cadastrado
		Pedido pedido1 = new Pedido("99504993052");
		Optional<Produto> umProduto = produtoRepository.findById(1L);
		Produto produtoComprado1 = umProduto.get();
		umProduto = produtoRepository.findById(3L);
		Produto produtoComprado2 = umProduto.get();
		ItemDePedido ip1 = new ItemDePedido(produtoComprado1, 20); // quantidade comprada
		ItemDePedido ip2 = new ItemDePedido(produtoComprado2, 10); // quantidade comprada
		pedido1.getItens().addAll(Arrays.asList(ip1, ip2));        //pedido1 comprou parafuso e bucha
		Pedido pedidoRetornado = pedidoServico.cadastrarPedido(pedido1);
		List<Pedido> pedidos = pedidoRepository.findByCpf("99504993052");
		pedidoRetornado = pedidos.get(0);
		//Quando solicita exclusão
		pedidoRepository.deleteById(pedidoRetornado.getId());
		//Retorna pedido vazio
		Optional<Pedido> umPedido = pedidoRepository.findById(pedidoRetornado.getId());
		assertTrue(umPedido.isEmpty());
	}

}
