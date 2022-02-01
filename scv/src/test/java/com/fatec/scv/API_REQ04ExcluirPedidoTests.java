package com.fatec.scv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fatec.scv.matemPedidos.model.Pedido;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class API_REQ04ExcluirPedidoTests {
	Logger logger = LogManager.getLogger(this.getClass());
	String urlBase = "/api/v1/pedidos";
	@Autowired
	TestRestTemplate testRestTemplate;
	@Test
	void ct01_dado_que_o_pedido_esta_cadastrado_quando_exclui_entao_retorna_vazio() {
		ResponseEntity<Pedido> resposta = testRestTemplate.exchange(urlBase +"/{id}",HttpMethod.DELETE, null, Pedido.class, 4);
		assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
	}

}
