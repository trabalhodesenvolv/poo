package com.trabalho.controller;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.trabalho.entity.Cliente;
import com.trabalho.entity.Usuario;
import com.trabalho.repository.ClienteRepository;
import com.trabalho.repository.UsuarioRepository;
import javax.swing.text.html.Option;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TrabalhoController {
	@Autowired
    UsuarioRepository usuarioRepository;
	
	@Autowired
    ClienteRepository clienteRepository;
		
	@PostMapping(value = "login")
    public ResponseEntity loginUsuario(@RequestBody Usuario usuarioRequest){
        //EXEMPLO REQUEST USUARIO
        //{
       	//	  "email": "eu@gmail.com",
        //        "senha": "eu123"
        //}	
		try {
	         Optional<Usuario> usuario = usuarioRepository.findByEmail(usuarioRequest.getEmail());
	         if(usuario.isPresent()){	
	             if(usuario.get().equals(usuarioRequest)){
	                 return ResponseEntity.ok().headers(getHeaders()).build();
	             }else{
	                 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(getHeaders()).build();
	             }
	
	         }else{
	             return ResponseEntity.notFound().headers(getHeaders()).build();
	         }
	
	     }catch (Exception e){
	         throw e;
	     }
	 }

	@GetMapping(value = "listarCliente")
	public ResponseEntity listarCliente() {
		List<Cliente> clientes = clienteRepository.findAll(); //new ArrayList<Cliente>();
		return new ResponseEntity<List<Cliente>>(clientes, HttpStatus.OK);
	}
	
	@PostMapping(value = "incluirCliente")
	public ResponseEntity incluirCliente(@RequestBody Cliente clienteRequest) {
		// validações
		if(clienteRequest.getEmail().isEmpty() || clienteRequest.getNome().isEmpty() || clienteRequest.getTelefone().isEmpty() || clienteRequest.getUltimaConsulta().isEmpty() ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(getHeaders()).body("Um ou mais campos inválidos");
		}
		
		// verifica se email já existe		
		try {
			Optional<Cliente> opt = clienteRepository.findByEmail(clienteRequest.getEmail());
				    
		    if(opt.isPresent()) {
		       return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(getHeaders()).body("Email já cadastrado");
		    }else {
		    	clienteRepository.save(clienteRequest);
				return ResponseEntity.ok().headers(getHeaders()).build();
		    }
		
		}catch (Exception e) {
		 throw e;
		}
	}
	
	@GetMapping(value = "consultarCliente/{email}")
	public ResponseEntity consultarCliente(@PathVariable String email) {
		// validações
		if(email.isEmpty())  {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(getHeaders()).body("Email inválido");
		}
		
		try {
			Optional<Cliente> opt = clienteRepository.findByEmail(email);
				    
		    if(opt.isPresent()) {
		    	//return ResponseEntity.ok().headers(getHeaders()).build();
		    	Cliente cliente = opt.get();
		    	return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
		    }else {
		    	return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(getHeaders()).build();
			}
		
		}catch (Exception e) {
		 throw e;
		}
		
	}
	
	// Alterar Cliente
	//@GetMapping(value = "alterarCliente/{email}")
	//public ResponseEntity<Cliente> alterarCliente(@PathVariable String email) {
		//Optional<Cliente> optional = clienteRepository.findByEmail(email);
		
	//}
	
	
	// Excluir Cliente
	@DeleteMapping("excluirCliente/{clienteId}")
	public ResponseEntity<Cliente> excluirCliente(@PathVariable Long clienteId) {
		Optional<Cliente> optional = clienteRepository.findById(clienteId);
		if(optional.isPresent()) {
			clienteRepository.delete(optional.get());
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("versaoApi")
	public String versaoApi() {
		final String versao = "Versão 0.0.1";
		return versao;
	}
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Access-Control-Allow-Origin", "*");
		return headers;
	}
		
}

