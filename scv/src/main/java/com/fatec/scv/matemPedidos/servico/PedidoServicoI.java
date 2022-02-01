package com.fatec.scv.matemPedidos.servico;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;

import com.fatec.scv.matemPedidos.model.Cliente;
import com.fatec.scv.matemPedidos.model.ItemDePedido;
import com.fatec.scv.matemPedidos.model.Pedido;
import com.fatec.scv.matemPedidos.ports.ItemDePedidoRepository;
import com.fatec.scv.matemPedidos.ports.PedidoRepository;
import com.fatec.scv.matemPedidos.ports.PedidoServico;
import com.fatec.scv.matemPedidos.ports.ProdutoRepository;

@Service
public class PedidoServicoI implements PedidoServico {
	Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private ItemDePedidoRepository itemRepository;

	public Optional<Pedido> buscaPorId(Long id) {
		return pedidoRepository.findById(id);
	}

	@Override
	public List<Pedido> buscaPorCpf(String cpf) {
		return pedidoRepository.findByCpf(cpf);
	}

	@Transactional
	public Pedido save(Pedido pedido) {
		// pedido.setId(null);
		logger.info(">>>>>> servico save chamado para o pedido - " + pedido.getCpf());
		DateTime dataAtual = new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/YYYY");
		pedido.setDataEmissao(dataAtual.toString(fmt));
		Pedido umPedido = null;
		try {
			umPedido = pedidoRepository.save(pedido);
			logger.info(">>>>>> cabecalho do pedido salvo no repositorio ");
			for (ItemDePedido item : pedido.getItens()) {
				item.setProduto(produtoRepository.getById(item.getProduto().getProduto_id()));
				item.setQuantidade(item.getQuantidade());
			}

			itemRepository.saveAll(pedido.getItens());
			logger.info(">>>>>> item do pedido salvo no repositorio ");
		} catch (UnexpectedRollbackException e) {
			logger.info(">>>>>> PedidoServico save - roolback =>" + e.getMessage());
		} catch (Exception e) {
			logger.info(">>>>>> PedidoServico save - erro nao esperado=>" + e.getMessage());

		}

		return umPedido;
	}

	public boolean validaData(String data) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false); //
		try {
			df.parse(data); // data válida (exemplo 30 fev - 31 nov)
			return true;
		} catch (ParseException ex) {
			return false;
		}
	}

	@Override
	public List<Pedido> consultaTodos() {
		return pedidoRepository.findAll();
	}

	@Transactional
	public void excluiPedido(Long id) {
		pedidoRepository.deleteById(id);
	}

	@Override
	@Transactional
	public Pedido cadastrarPedido(Pedido pedido) {

		logger.info(">>>>>> servico cadastrar pedido  ");
		try {
			if (isCadastrado(pedido.getCpf())) {
				logger.info(">>>>>> servico cadastrar pedido - cliente cadastrado ");
				return save(pedido);
			} else {
				logger.info(">>>>>> servico cadastrar pedido - cliente invalido ");
				return null;
			}
		} catch (Exception e) {
			logger.info(">>>>>> servico cadastrar pedido - erro nao esperado contate o administrador");
			return null;
		}
	}

	public boolean isCadastrado(String cpf) {
		RestTemplate restTemplate = new RestTemplate();
		// envia o JSon de login do usuario
		String user = "{\"username\":\"jose\", \"password\":\"123\"}";
		HttpEntity<String> httpEntity = new HttpEntity<>(user);
		ResponseEntity<String> resposta1 = null;
		// tenta autenticar o usuario para obter o token
		try {
			resposta1 = restTemplate.exchange("https://dw-scc-rest.herokuapp.com/login", HttpMethod.POST, httpEntity,
					String.class);
			logger.info(">>>>>> servico isCadastrado login " + resposta1.getStatusCode().toString());
			HttpHeaders headers = resposta1.getHeaders();
			
			HttpEntity<Cliente> httpEntity2 = new HttpEntity<Cliente>(headers);
			RestTemplate template = new RestTemplate();
			ResponseEntity<Cliente> resposta2 = null;
			String url = "https://dw2-scc-rest.herokuapp.com/api/v1/clientes/{cpf}";
			resposta2 = template.exchange(url, HttpMethod.GET, httpEntity2, Cliente.class, cpf);
			logger.info(">>>>>> servico isCadastrado consulta por cpf=> " + cpf +"-" + resposta2.getStatusCode().toString());
			return true;
		} catch (HttpClientErrorException e) {
			logger.info(">>>>>> servico isCadastrado chamado - erro no login ou cliente nao cadastrado");
			return false;
		} catch (UnknownContentTypeException e) {
			logger.info(">>>>>> servico cadastrar pedido - servico cosulta cliente indisponivel");
			return false;
		}
	}
}
