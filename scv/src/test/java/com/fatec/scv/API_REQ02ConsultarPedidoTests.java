package com.fatec.scv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fatec.scv.matemPedidos.model.ItemDePedido;
import com.fatec.scv.matemPedidos.model.Pedido;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class API_REQ02ConsultarPedidoTests {
	Logger logger = LogManager.getLogger(this.getClass());
	String urlBase = "/api/v1/pedidos";
	@Autowired
	TestRestTemplate testRestTemplate;
	@Test
	void ct01_quando_consulta_cpf_cadastrado_retorna_todos_pedidos_cadastrados_para_o_cpf() {
		//Dado que existem pedidos cadastrados previamente
		//Quando solicita a consulta por cpf
		ResponseEntity<List<Pedido>> resposta = testRestTemplate.exchange(urlBase + "/43011831084", HttpMethod.GET, null, new ParameterizedTypeReference<List<Pedido>>() {});
		//Entao retorna todos os pedidos cadastrados para este cpf
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		Pedido pedido = resposta.getBody().get(0);
		List<ItemDePedido> itens = pedido.getItens();
		assertEquals(2,itens.size());
		assertEquals(250.00,pedido.getValorTotal(),0);
		assertEquals("43011831084", pedido.getCpf());
		assertTrue(resposta.getBody().size()>=1);
	}
	@Test
	void ct02_quando_cpf_valido_nao_tem_venda_cadastrada_consulta_retorna_vazio() {
		ResponseEntity<List<Pedido>> resposta = testRestTemplate.exchange(urlBase + "/{cpf}", HttpMethod.GET, null, new ParameterizedTypeReference<List<Pedido>>() {}, "02966942312");
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
		assertEquals(0, resposta.getBody().size());
	}
	@Test
	void ct03_quando_cpf_invalido_consulta_retorna_vazio() {
		ResponseEntity<List<Pedido>> resposta = testRestTemplate.exchange(urlBase + "/{cpf}", HttpMethod.GET, null, new ParameterizedTypeReference<List<Pedido>>() {}, "0296694231");
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
		assertEquals(0, resposta.getBody().size());
	}
	@Test
	void ct04_quando_consulta_todos_retorna_todos_pedidos_cadastrados() {
		ResponseEntity<List<Pedido>> resposta = testRestTemplate.exchange(urlBase, HttpMethod.GET, null, new ParameterizedTypeReference<List<Pedido>>() {});
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertTrue(resposta.getBody().size()>=1);
	}
	@Test
	void ct05_quando_consulta_id_cadastrado_retorna_detalhes_do_pedido() {
		ResponseEntity<Pedido> resposta = testRestTemplate.getForEntity(urlBase + "/id/4", Pedido.class);
		//Entao retorna os detalhes do cliente
		Pedido pedido = resposta.getBody();
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals("43011831084", pedido.getCpf());
	}
}
