package br.com.fiap.ms_pedidos.service;

import br.com.fiap.ms_pedidos.DTO.PedidoDTO;
import br.com.fiap.ms_pedidos.entities.Pedido;
import br.com.fiap.ms_pedidos.exceptions.ResourceNotFoundException;
import br.com.fiap.ms_pedidos.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional(readOnly = true)
    public List<PedidoDTO> findAllPedidos(){
        return pedidoRepository.findAll().stream().map(PedidoDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public PedidoDTO findPedidoById(Long id){
        Pedido pedido = pedidoRepository.findById(id).orElseThrow( ()-> new ResourceNotFoundException("Recurso não encontrado. ID:"+id));
        return new PedidoDTO(pedido);
    }
}
