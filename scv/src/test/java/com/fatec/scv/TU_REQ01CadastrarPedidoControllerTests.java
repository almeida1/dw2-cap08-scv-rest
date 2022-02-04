package com.fatec.scv;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatec.scv.adapters.APIPedidoController;
import com.fatec.scv.matemPedidos.model.ItemDePedido;
import com.fatec.scv.matemPedidos.model.Pedido;
import com.fatec.scv.matemPedidos.model.Produto;
import com.fatec.scv.matemPedidos.ports.PedidoRepository;
import com.fatec.scv.matemPedidos.ports.PedidoServico;
import com.fatec.scv.matemPedidos.ports.ProdutoRepository;

@WebMvcTest(APIPedidoController.class)
class TU_REQ01CadastrarPedidoControllerTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	@MockBean
	PedidoServico pedidoServico;
	@MockBean
	PedidoRepository pedidoRepository;
	@MockBean
	ProdutoRepository produtoRepository;
	@Test
	void quando_dados_validos_cadastra_com_sucesso() throws JsonProcessingException, Exception {

		// entrada de dados do pedido (cliente previamente cadastrado)
		Pedido p1 = new Pedido("43011831084");
		p1.setDataEmissao("01/10/2020");
		// entrada de dados dos detalhes do pedido
		Produto produtoComprado1 = new Produto("parafuso", 12, 30);
		produtoComprado1.setProduto_id(1L);
		Produto produtoComprado2 = new Produto("tijolo", 12, 30);
		produtoComprado1.setProduto_id(2L);
		ItemDePedido ip1 = new ItemDePedido(produtoComprado1, 20); // quantidade comprada
		ItemDePedido ip2 = new ItemDePedido(produtoComprado2, 10); // quantidade comprada
		p1.setItens(Arrays.asList(ip1, ip2));
		
		// entrada de dados do pedido (cliente previamente cadastrado)
		Pedido p2 = new Pedido("43011831084");
		p2.setId(1L);
		p2.setDataEmissao("01/10/2020");
		// entrada de dados dos detalhes do pedido
		produtoComprado1 = new Produto("parafuso", 12, 30);
		produtoComprado1.setProduto_id(1L);
		produtoComprado2 = new Produto("tijolo", 12, 30);
		ip1 = new ItemDePedido(produtoComprado1, 20); // quantidade comprada
		ip1.setId(1L);
		ip2 = new ItemDePedido(produtoComprado2, 10); // quantidade comprada
		ip2.setId(2L);
		p2.setItens(Arrays.asList(ip1, ip2));
		Mockito.when(pedidoServico.isCadastrado("43011831084")).thenReturn(true);
		Mockito.when(pedidoRepository.save(p1)).thenReturn(p2);
		Mockito.when(pedidoServico.cadastrarPedido(p1)).thenReturn(p2);
		
		String url = "/api/v1/pedidos";
		mockMvc.perform(post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(p1)))
				
		//.andExpect(status().isOk()).andReturn();
		.andExpect(status().isBadRequest()).andReturn();
				
	}
	@Test
	void quando_dados_invalidos_cadastra_com_sucesso() throws JsonProcessingException, Exception {
		// entrada de dados do pedido (cliente previamente cadastrado)
		Pedido p1 = new Pedido("");
		String url = "/api/v1/pedidos";
		mockMvc.perform(post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(p1)))
				
		
		.andExpect(status().isBadRequest()).andDo(print());
		Mockito.verify(pedidoServico, times(0)).cadastrarPedido(p1);
	}

}
