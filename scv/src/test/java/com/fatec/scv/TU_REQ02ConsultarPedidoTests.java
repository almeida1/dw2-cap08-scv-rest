package com.fatec.scv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fatec.scv.matemPedidos.model.Pedido;
import com.fatec.scv.matemPedidos.ports.PedidoRepository;
import com.fatec.scv.matemPedidos.servico.PedidoServicoI;
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class TU_REQ02ConsultarPedidoTests {
	@MockBean
	PedidoRepository repo;
	@InjectMocks
	PedidoServicoI servico;
	@Test
	void ct01_quando_consulta_cpf_cadastrado_retorna_todos_pedidos_cadastrados_para_o_cpf() {
		List <Pedido> pedidos = new ArrayList<Pedido>();
		Pedido p1 = new Pedido();
		Pedido p2 = new Pedido();
		Pedido p3 = new Pedido();
		pedidos.addAll(Arrays.asList(p1, p2, p3));
		Mockito.when(repo.findByCpf("99504993052")).thenReturn(pedidos);
		assertEquals(3,servico.buscaPorCpf("99504993052").size());
	}
}
