package com.tracker.controller;

import com.tracker.dto.DailyEntryDTO;
import com.tracker.service.DailyEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/daily-entry")
@RequiredArgsConstructor
public class DailyEntryController {

    private final DailyEntryService dailyEntryService;

    @PostMapping
    public ResponseEntity<DailyEntryDTO> createDailyEntry(@Valid @RequestBody DailyEntryDTO dto) {
        DailyEntryDTO created = dailyEntryService.createDailyEntry(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailyEntryDTO> updateDailyEntry(
            @PathVariable Long id,
            @Valid @RequestBody DailyEntryDTO dto) {
        return ResponseEntity.ok(dailyEntryService.updateDailyEntry(id, dto));
    }

    @GetMapping
    public ResponseEntity<?> getDailyEntry(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long customerId) {

        if (date != null && customerId != null) {
            // Specific entry for date + customer
            DailyEntryDTO entry = dailyEntryService.getDailyEntry(date, customerId);
            return ResponseEntity.ok(entry);
        } else if (date != null) {
            // All entries for a date
            List<DailyEntryDTO> entries = dailyEntryService.getDailyEntriesByDate(date);
            return ResponseEntity.ok(entries);
        } else {
            // All entries
            List<DailyEntryDTO> entries = dailyEntryService.getAllDailyEntries();
            return ResponseEntity.ok(entries);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDailyEntry(@PathVariable Long id) {
        dailyEntryService.deleteDailyEntry(id);
        return ResponseEntity.noContent().build();
    }
}
