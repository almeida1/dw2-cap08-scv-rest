package com.fatec.scv.matemPedidos.ports;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fatec.scv.matemPedidos.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
