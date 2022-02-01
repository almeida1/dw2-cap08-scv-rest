package com.fatec.scv.adapters;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fatec.scv.matemPedidos.model.Pedido;
import com.fatec.scv.matemPedidos.ports.PedidoServico;
@RestController
@RequestMapping("/api/v1/pedidos")
public class APIPedidoController {
	@Autowired
	private PedidoServico servico;
	Logger logger = LogManager.getLogger(this.getClass());
	@PostMapping
	public ResponseEntity<Pedido> insert(@Valid @RequestBody Pedido pedido) {
		ResponseEntity<Pedido> response = null;
		Pedido umPedido = servico.cadastrarPedido(pedido);
		logger.info(">>>>>> 1. controller chamou servico cadastrar pedido  " );
		if(umPedido != null) {
			logger.info(">>>>>> controller pedido criado " );
			response = ResponseEntity.status(HttpStatus.CREATED).body(umPedido);
		} else {
			logger.info(">>>>>> controller - cliente nao encontrado =" + pedido.getCpf());
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return response;
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<Pedido> findById(@PathVariable long id) {
		logger.info(">>>>>> 1. controller chamou servico consulta por id => " + id);
		Optional<Pedido> pedido = servico.buscaPorId(id);
		if (pedido.isPresent()) {
			return new ResponseEntity<>(pedido.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping
	public ResponseEntity<List<Pedido>> consultaTodos() {
		logger.info(">>>>>> 1. controller chamou servico consulta todos");
		ResponseEntity<List<Pedido>> response = ResponseEntity.status(HttpStatus.OK).body(servico.consultaTodos());
		return response;
	}
	
	@GetMapping("/{cpf}")
	public ResponseEntity<List<Pedido>> consultaPorCpf(@PathVariable String cpf) {
		logger.info(">>>>>> 1. controller chamou servico consulta por cpf => " + cpf);
		ResponseEntity<List<Pedido>> response = ResponseEntity.status(HttpStatus.OK).body(servico.buscaPorCpf(cpf));
		return response;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> remover(@PathVariable long id) {
		Optional<Pedido> pedido = servico.buscaPorId(id);
		if (pedido.isPresent()) {
			servico.excluiPedido(id);
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}