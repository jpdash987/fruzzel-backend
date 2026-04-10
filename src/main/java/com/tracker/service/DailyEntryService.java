package com.tracker.service;

import com.tracker.dto.DailyEntryDTO;
import com.tracker.dto.ItemEntryDTO;
import com.tracker.entity.*;
import com.tracker.exception.ResourceNotFoundException;
import com.tracker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyEntryService {

    private final DailyEntryRepository dailyEntryRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final ItemEntryRepository itemEntryRepository;

    /**
     * Create a daily entry with item entries.
     * If item entries are not provided, auto-loads all items for the customer
     * with default rates pre-filled and zero quantities.
     */
    public DailyEntryDTO createDailyEntry(DailyEntryDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", dto.getCustomerId()));

        // Check if entry already exists for this date + customer
        dailyEntryRepository.findByDateAndCustomerId(dto.getDate(), dto.getCustomerId())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "Daily entry already exists for customer '" + customer.getName()
                                    + "' on date " + dto.getDate());
                });

        // Create the daily entry
        DailyEntry dailyEntry = DailyEntry.builder()
                .date(dto.getDate())
                .customer(customer)
                .build();
        dailyEntry = dailyEntryRepository.save(dailyEntry);

        // Build item entries
        List<ItemEntry> itemEntries;

        if (dto.getItemEntries() != null && !dto.getItemEntries().isEmpty()) {
            // User provided item entries — use them
            itemEntries = buildItemEntriesFromDTO(dto.getItemEntries(), dailyEntry);
        } else {
            // Auto-load all items for this customer with defaults
            itemEntries = autoLoadItemEntries(customer, dailyEntry);
        }

        itemEntryRepository.saveAll(itemEntries);
        dailyEntry.setItemEntries(itemEntries);

        return toDTO(dailyEntry);
    }

    /**
     * Update an existing daily entry's item entries.
     */
    public DailyEntryDTO updateDailyEntry(Long id, DailyEntryDTO dto) {
        DailyEntry dailyEntry = dailyEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DailyEntry", id));

        // Remove old item entries
        itemEntryRepository.deleteAll(dailyEntry.getItemEntries());
        dailyEntry.getItemEntries().clear();

        // Build new item entries
        List<ItemEntry> itemEntries;
        if (dto.getItemEntries() != null && !dto.getItemEntries().isEmpty()) {
            itemEntries = buildItemEntriesFromDTO(dto.getItemEntries(), dailyEntry);
        } else {
            Customer customer = dailyEntry.getCustomer();
            itemEntries = autoLoadItemEntries(customer, dailyEntry);
        }

        itemEntryRepository.saveAll(itemEntries);
        dailyEntry.setItemEntries(itemEntries);

        return toDTO(dailyEntry);
    }

    /**
     * Get daily entry by date and customer ID.
     */
    @Transactional(readOnly = true)
    public DailyEntryDTO getDailyEntry(LocalDate date, Long customerId) {
        DailyEntry dailyEntry = dailyEntryRepository.findByDateAndCustomerId(date, customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Daily entry not found for customer " + customerId + " on date " + date));
        return toDTO(dailyEntry);
    }

    /**
     * Get all daily entries for a specific date.
     */
    @Transactional(readOnly = true)
    public List<DailyEntryDTO> getDailyEntriesByDate(LocalDate date) {
        return dailyEntryRepository.findByDate(date)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all daily entries (recent first, limited).
     */
    @Transactional(readOnly = true)
    public List<DailyEntryDTO> getAllDailyEntries() {
        return dailyEntryRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Delete a daily entry.
     */
    public void deleteDailyEntry(Long id) {
        if (!dailyEntryRepository.existsById(id)) {
            throw new ResourceNotFoundException("DailyEntry", id);
        }
        dailyEntryRepository.deleteById(id);
    }

    // ──────────────────────────────────────────────
    // Private helpers
    // ──────────────────────────────────────────────

    private List<ItemEntry> buildItemEntriesFromDTO(List<ItemEntryDTO> dtos, DailyEntry dailyEntry) {
        List<ItemEntry> entries = new ArrayList<>();

        for (ItemEntryDTO entryDTO : dtos) {
            Item item = itemRepository.findById(entryDTO.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item", entryDTO.getItemId()));

            // Use provided rate/profitRate or fall back to item defaults
            Double rate = entryDTO.getRate() != null ? entryDTO.getRate() : item.getDefaultRate();
            Double profitRate = entryDTO.getProfitRate() != null ? entryDTO.getProfitRate() : item.getDefaultProfitRate();

            ItemEntry entry = ItemEntry.builder()
                    .dailyEntry(dailyEntry)
                    .item(item)
                    .qty(entryDTO.getQty())
                    .returnQty(entryDTO.getReturnQty())
                    .rate(rate)
                    .profitRate(profitRate)
                    .online(entryDTO.getOnline() != null ? entryDTO.getOnline() : 0.0)
                    .build();

            // Trigger calculation
            entry.calculateFields();
            entries.add(entry);
        }

        return entries;
    }

    private List<ItemEntry> autoLoadItemEntries(Customer customer, DailyEntry dailyEntry) {
        List<Item> items = itemRepository.findByCustomerId(customer.getId());
        List<ItemEntry> entries = new ArrayList<>();

        for (Item item : items) {
            ItemEntry entry = ItemEntry.builder()
                    .dailyEntry(dailyEntry)
                    .item(item)
                    .qty(0)
                    .returnQty(0)
                    .rate(item.getDefaultRate())
                    .profitRate(item.getDefaultProfitRate())
                    .online(0.0)
                    .build();
            entry.calculateFields();
            entries.add(entry);
        }

        return entries;
    }

    private DailyEntryDTO toDTO(DailyEntry dailyEntry) {
        List<ItemEntryDTO> itemEntryDTOs = dailyEntry.getItemEntries()
                .stream()
                .map(this::toItemEntryDTO)
                .collect(Collectors.toList());

        double totalPrice = itemEntryDTOs.stream()
                .mapToDouble(e -> e.getPrice() != null ? e.getPrice() : 0.0)
                .sum();
        double totalProfit = itemEntryDTOs.stream()
                .mapToDouble(e -> e.getProfit() != null ? e.getProfit() : 0.0)
                .sum();
        double totalOnline = itemEntryDTOs.stream()
                .mapToDouble(e -> e.getOnline() != null ? e.getOnline() : 0.0)
                .sum();

        return DailyEntryDTO.builder()
                .id(dailyEntry.getId())
                .date(dailyEntry.getDate())
                .customerId(dailyEntry.getCustomer().getId())
                .customerName(dailyEntry.getCustomer().getName())
                .itemEntries(itemEntryDTOs)
                .totalPrice(totalPrice)
                .totalProfit(totalProfit)
                .totalOnline(totalOnline)
                .build();
    }

    private ItemEntryDTO toItemEntryDTO(ItemEntry entry) {
        return ItemEntryDTO.builder()
                .id(entry.getId())
                .itemId(entry.getItem().getId())
                .itemName(entry.getItem().getName())
                .qty(entry.getQty())
                .returnQty(entry.getReturnQty())
                .rate(entry.getRate())
                .profitRate(entry.getProfitRate())
                .online(entry.getOnline())
                .finalQty(entry.getFinalQty())
                .price(entry.getPrice())
                .profit(entry.getProfit())
                .build();
    }
}
