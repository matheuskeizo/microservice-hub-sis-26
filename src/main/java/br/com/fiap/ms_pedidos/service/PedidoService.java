package br.com.fiap.ms_pedidos.service;

import br.com.fiap.ms_pedidos.DTO.ItemDoPedidoDTO;
import br.com.fiap.ms_pedidos.DTO.PedidoDTO;
import br.com.fiap.ms_pedidos.entities.ItemDoPedido;
import br.com.fiap.ms_pedidos.entities.Pedido;
import br.com.fiap.ms_pedidos.entities.Status;
import br.com.fiap.ms_pedidos.exceptions.ResourceNotFoundException;
import br.com.fiap.ms_pedidos.repositories.ItemDoPedidoRepository;
import br.com.fiap.ms_pedidos.repositories.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemDoPedidoRepository itemDoPedidoRepository;

    @Transactional(readOnly = true)
    public List<PedidoDTO> findAllPedidos(){
        return pedidoRepository.findAll().stream().map(PedidoDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public PedidoDTO findPedidoById(Long id){
        Pedido pedido = pedidoRepository.findById(id).orElseThrow( ()-> new ResourceNotFoundException("Recurso não encontrado. ID:"+id));
        return new PedidoDTO(pedido);
    }

    @Transactional
    public PedidoDTO savePedido(PedidoDTO pedidoDTO){

        Pedido pedido = new Pedido();
        pedido.setData(LocalDate.now());
        pedido.setStatus(Status.CRIADO);
        mapDtoToPedido(pedidoDTO,pedido);
        pedido.calcularValorTotalDoPedido();
        pedido = pedidoRepository.save(pedido);
        return new PedidoDTO(pedido);
    }

    private void mapDtoToPedido (PedidoDTO pedidoDTO, Pedido pedido){

        pedido.setNome(pedido.getNome());
        pedido.setCpf(pedido.getCpf());

        for(ItemDoPedidoDTO itemDto : pedidoDTO.getItens()){
            ItemDoPedido itemDoPedido= new ItemDoPedido();
            itemDoPedido.setQuantidade(itemDto.getQuantidade());
            itemDoPedido.setDescricao(itemDto.getDescricao());
            itemDoPedido.setPrecoUnitario(itemDto.getPrecoUnitario());
            itemDoPedido.setPedido(pedido);
            pedido.getItens().add(itemDoPedido);
        }
    }

    @Transactional
    public PedidoDTO updatePedido(Long id, PedidoDTO pedidoDTO){
        try {
            Pedido pedido = pedidoRepository.getReferenceById(id);
            pedido.getItens().clear();
            pedido.setData(LocalDate.now());
            pedido.setStatus(Status.CRIADO);
            mapDtoToPedido(pedidoDTO,pedido);
            pedido.calcularValorTotalDoPedido();
            pedido = pedidoRepository.save(pedido);
            return new PedidoDTO(pedido);

        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Recurso não encontrado. ID:"+id);
        }
    }


}
