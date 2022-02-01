package com.fatec.scv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class API_REQ01CadastrarPedidoTests {

	Logger logger = LogManager.getLogger(this.getClass());
	String urlBase = "/api/v1/pedidos";
	@Autowired
	TestRestTemplate testRestTemplate;

	@Test
	@Transactional
	void ct01_quando_dados_validos_cadastra_pedido_com_sucesso() {
		// cpf previamente cadastrado no servico de manutencao de clientes 99504993052 - test fixture
		// nesta versao o login de acesso ao servico de clientes esta fixo 				
		String pedidoString = "{\"cpf\":\"99504993052\", \"itens\": ["
				+ "{\"produto\":{\"produto_id\": 1},\"quantidade\": 20},"
				+ "{\"produto\":{\"produto_id\": 3},\"quantidade\": 10}] }";
		
		logger.info(">>>>>> caso de teste - setup de dados pedidoString criado  ");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> httpEntity = new HttpEntity<String>(pedidoString, headers);
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, String.class);
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
	}

}
