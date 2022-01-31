package com.fatec.scv.matemPedidos.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //manipula - lazy loaded properties
@Entity
public class Produto {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long produto_id;
	private String descricao;
	private double custo;
	private int quantidade;
	public Produto(String descricao, double custo, int quantidade) {
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.custo = custo;
	}
	public Produto() {
	}
	public Long getProduto_id() {
		return produto_id;
	}
	public void setProduto_id(Long produto_id) {
		this.produto_id = produto_id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public double getCusto() {
		return custo;
	}
	public void setCusto(double custo) {
		this.custo = custo;
	}
}
