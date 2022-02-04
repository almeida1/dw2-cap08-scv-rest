package com.fatec.scv;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatec.scv.adapters.APIPedidoController;
import com.fatec.scv.matemPedidos.model.Pedido;
import com.fatec.scv.matemPedidos.ports.PedidoServico;
import com.fatec.scv.matemPedidos.ports.ProdutoRepository;


@WebMvcTest(APIPedidoController.class)
class TU_REQ02ConsultarPedidoControllerTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	@MockBean
	PedidoServico pedidoServico;
	@MockBean
	ProdutoRepository produtoRepository;
	@Test
	void quandoConsultaTodos_retorna_lista_de_pedidos_cadastrados() throws Exception {
		//Dado que existe uma lista de pedidos cadastrados
		List <Pedido> listaDePedidos = new ArrayList<Pedido>();
		Pedido p1 = new Pedido();
		p1.setId(1L);
		p1.setCpf("17323670780");
		p1.setDataEmissao("01/10/2020");
		Pedido p2 = new Pedido();
		p2.setId(2L);
		p2.setCpf("17323670780");
		p2.setDataEmissao("01/11/2021");
		Pedido p3 = new Pedido();
		p3.setId(3L);
		p3.setCpf("54248461811");
		p1.setDataEmissao("15/06/2021");
		listaDePedidos.addAll(Arrays.asList(p1, p2, p3));
		//quando consulta todos
		Mockito.when(pedidoServico.consultaTodos()).thenReturn(listaDePedidos);
		String url = "/api/v1/pedidos";
		MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();
		String resultadoObtidoJson = mvcResult.getResponse().getContentAsString();
		//entao retorna lista de pedidos
		//converte os dados de entrada para json permitindo a comparacao 
		String resultadoEsperadoJson = objectMapper.writeValueAsString(listaDePedidos);
		assertThat(resultadoEsperadoJson).isEqualToIgnoringWhitespace(resultadoObtidoJson);
	}

}
