package com.tracker.service;

import com.tracker.dto.ItemDTO;
import com.tracker.entity.Customer;
import com.tracker.entity.Item;
import com.tracker.exception.ResourceNotFoundException;
import com.tracker.repository.CustomerRepository;
import com.tracker.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;

    public ItemDTO createItem(ItemDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", dto.getCustomerId()));

        Item item = Item.builder()
                .name(dto.getName())
                .defaultRate(dto.getDefaultRate())
                .defaultProfitRate(dto.getDefaultProfitRate())
                .customer(customer)
                .build();

        item = itemRepository.save(item);
        return toDTO(item);
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer", customerId);
        }
        return itemRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemDTO getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item", id));
        return toDTO(item);
    }

    public ItemDTO updateItem(Long id, ItemDTO dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item", id));

        item.setName(dto.getName());
        item.setDefaultRate(dto.getDefaultRate());
        item.setDefaultProfitRate(dto.getDefaultProfitRate());

        // Allow moving item to a different customer
        if (dto.getCustomerId() != null && !dto.getCustomerId().equals(item.getCustomer().getId())) {
            Customer newCustomer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", dto.getCustomerId()));
            item.setCustomer(newCustomer);
        }

        item = itemRepository.save(item);
        return toDTO(item);
    }

    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item", id);
        }
        itemRepository.deleteById(id);
    }

    private ItemDTO toDTO(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .defaultRate(item.getDefaultRate())
                .defaultProfitRate(item.getDefaultProfitRate())
                .customerId(item.getCustomer().getId())
                .customerName(item.getCustomer().getName())
                .build();
    }
}
