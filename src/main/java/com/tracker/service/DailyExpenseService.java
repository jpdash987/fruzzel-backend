package com.tracker.service;

import com.tracker.dto.DailyExpenseDTO;
import com.tracker.entity.DailyExpense;
import com.tracker.repository.DailyExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyExpenseService {

    private final DailyExpenseRepository dailyExpenseRepository;

    @Transactional
    public DailyExpenseDTO createDailyExpense(DailyExpenseDTO dto) {
        DailyExpense expense = DailyExpense.builder()
                .date(dto.getDate())
                .expenseName(dto.getExpenseName())
                .amount(dto.getAmount())
                .build();
        
        DailyExpense saved = dailyExpenseRepository.save(expense);
        return mapToDTO(saved);
    }

    public List<DailyExpenseDTO> getDailyExpensesByDate(LocalDate date) {
        return dailyExpenseRepository.findByDate(date).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DailyExpenseDTO updateDailyExpense(Long id, DailyExpenseDTO dto) {
        DailyExpense expense = dailyExpenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        
        expense.setDate(dto.getDate());
        expense.setExpenseName(dto.getExpenseName());
        expense.setAmount(dto.getAmount());
        
        DailyExpense saved = dailyExpenseRepository.save(expense);
        return mapToDTO(saved);
    }

    @Transactional
    public void deleteDailyExpense(Long id) {
        dailyExpenseRepository.deleteById(id);
    }

    private DailyExpenseDTO mapToDTO(DailyExpense expense) {
        return DailyExpenseDTO.builder()
                .id(expense.getId())
                .date(expense.getDate())
                .expenseName(expense.getExpenseName())
                .amount(expense.getAmount())
                .build();
    }
}
