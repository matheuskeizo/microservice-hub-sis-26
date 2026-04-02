package br.com.fiap.ms_pedidos.controller;

import br.com.fiap.ms_pedidos.DTO.PedidoDTO;
import br.com.fiap.ms_pedidos.service.PedidoService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> getAllPedidos(){
        List<PedidoDTO> dto = pedidoService.findAllPedidos();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> getPedido(@PathVariable Long id){
        PedidoDTO pedidoDTO = pedidoService.findPedidoById(id);
        return ResponseEntity.ok(pedidoDTO);
    }

    @PostMapping
    public  ResponseEntity<PedidoDTO> createPedido(@RequestBody @Valid PedidoDTO pedidoDTO){
        pedidoDTO= pedidoService.savePedido(pedidoDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(pedidoDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(pedidoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> updatePedido(@PathVariable Long id, @RequestBody @Valid PedidoDTO pedidoDTO){
        pedidoDTO=pedidoService.updatePedido(id,pedidoDTO);
        return ResponseEntity.ok(pedidoDTO);
    }
}

