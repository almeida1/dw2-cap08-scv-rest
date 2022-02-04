package com.fatec.scv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatec.scv.matemPedidos.model.ItemDePedido;
import com.fatec.scv.matemPedidos.model.Pedido;
import com.fatec.scv.matemPedidos.model.Produto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class API_REQ01CadastrarPedidoTests {

	Logger logger = LogManager.getLogger(this.getClass());
	String urlBase = "/api/v1/pedidos";
	@Autowired
	TestRestTemplate testRestTemplate;
	@Autowired
	ObjectMapper objectMapper;
	@Test
	@Transactional
	void ct01_quando_dados_validos_cadastra_pedido_com_sucesso() throws JsonProcessingException {
		// cpf previamente cadastrado no servico de manutencao de clientes 99504993052 -
		// test fixture
		// nesta versao o login de acesso ao servico de clientes esta fixo
		String pedidoJsonF1 = "{\"cpf\":\"99504993052\", \"itens\": ["
				+ "{\"produto\":{\"produto_id\": 1},\"quantidade\": 20},"
				+ "{\"produto\":{\"produto_id\": 3},\"quantidade\": 10}] }";

		logger.info(">>>>>> caso de teste - setup de dados pedidoJson criado  ");
		// entrada de dados do pedido (cliente previamente cadastrado)
		Pedido p1 = new Pedido("43011831084");
		p1.setDataEmissao("01/10/2020");
		// entrada de dados dos detalhes do pedido
		Produto produtoComprado1 = new Produto();
		produtoComprado1.setProduto_id(1L);
		Produto produtoComprado2 = new Produto();
		produtoComprado2.setProduto_id(2L);
		ItemDePedido ip1 = new ItemDePedido(produtoComprado1, 20); // quantidade comprada
		ItemDePedido ip2 = new ItemDePedido(produtoComprado2, 10); // quantidade comprada
		p1.setItens(Arrays.asList(ip1, ip2));
		//converte para Json
		String pedidoJson = objectMapper.writeValueAsString(p1);
		System.out.println(pedidoJson);
		String jsonInString2 = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pedidoJson);
		System.out.println(jsonInString2);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> httpEntity = new HttpEntity<String>(pedidoJson, headers);
		ResponseEntity<String> resposta = testRestTemplate.exchange(urlBase, HttpMethod.POST, httpEntity, String.class);
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
	}

}
