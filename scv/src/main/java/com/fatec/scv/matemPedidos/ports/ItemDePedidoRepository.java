package com.fatec.scv.matemPedidos.ports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatec.scv.matemPedidos.model.ItemDePedido;


@Repository
public interface ItemDePedidoRepository extends JpaRepository<ItemDePedido, Long>{

}