package com.tracker.controller;

import com.tracker.dto.DailyExpenseDTO;
import com.tracker.service.DailyExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/daily-expenses")
@RequiredArgsConstructor
public class DailyExpenseController {

    private final DailyExpenseService dailyExpenseService;

    @PostMapping
    public ResponseEntity<DailyExpenseDTO> createExpense(@Valid @RequestBody DailyExpenseDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dailyExpenseService.createDailyExpense(dto));
    }

    @GetMapping
    public ResponseEntity<List<DailyExpenseDTO>> getExpensesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(dailyExpenseService.getDailyExpensesByDate(date));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailyExpenseDTO> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody DailyExpenseDTO dto) {
        return ResponseEntity.ok(dailyExpenseService.updateDailyExpense(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        dailyExpenseService.deleteDailyExpense(id);
        return ResponseEntity.noContent().build();
    }
}
