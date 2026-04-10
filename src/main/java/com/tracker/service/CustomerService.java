package com.tracker.service;

import com.tracker.dto.CustomerDTO;
import com.tracker.entity.Customer;
import com.tracker.entity.DailyEntry;
import com.tracker.exception.ResourceNotFoundException;
import com.tracker.repository.CustomerRepository;
import com.tracker.repository.DailyEntryRepository;
import com.tracker.repository.ItemEntryRepository;
import com.tracker.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final DailyEntryRepository dailyEntryRepository;
    private final ItemEntryRepository itemEntryRepository;
    private final ItemRepository itemRepository;

    public CustomerDTO createCustomer(CustomerDTO dto) {
        Customer customer = Customer.builder()
                .name(dto.getName())
                .build();
        customer = customerRepository.save(customer);
        return toDTO(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        return toDTO(customer);
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        customer.setName(dto.getName());
        customer = customerRepository.save(customer);
        return toDTO(customer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));

        // Bypass JPA cascading errors by dropping constraint branches expressly
        List<DailyEntry> dailyEntries = dailyEntryRepository.findByCustomerId(id);
        for (DailyEntry de : dailyEntries) {
            itemEntryRepository.deleteAll(de.getItemEntries());
        }
        itemEntryRepository.flush();

        dailyEntryRepository.deleteAll(dailyEntries);
        dailyEntryRepository.flush();
        
        itemRepository.deleteAll(customer.getItems());
        itemRepository.flush();
        
        customerRepository.deleteById(id);
    }

    private CustomerDTO toDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .build();
    }
}
