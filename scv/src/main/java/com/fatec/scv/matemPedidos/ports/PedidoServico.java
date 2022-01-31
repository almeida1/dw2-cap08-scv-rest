package com.fatec.scv.matemPedidos.ports;

import java.util.List;
import java.util.Optional;

import com.fatec.scv.matemPedidos.model.Pedido;

public interface PedidoServico {
	public Optional <Pedido> buscaPorId (Long id);
	public List<Pedido> buscaPorCpf (String cpf);
	public List<Pedido> consultaTodos();
	public void excluiPedido(Long id);
	public Pedido cadastrarPedido(Pedido pedido);
	public boolean isCadastrado(String cpf);
}