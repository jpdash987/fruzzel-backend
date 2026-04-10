package com.tracker.controller;

import com.tracker.dto.FrameEntryDTO;
import com.tracker.service.FrameEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/frame-entries")
@RequiredArgsConstructor
public class FrameEntryController {

    private final FrameEntryService frameEntryService;

    /**
     * Save (upsert) a batch of frame entries for a date + seller.
     * POST /api/frame-entries?date=2026-04-10&frameSellerId=1
     */
    @PostMapping
    public ResponseEntity<List<FrameEntryDTO>> saveFrameEntries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long frameSellerId,
            @RequestBody List<FrameEntryDTO> entries) {
        List<FrameEntryDTO> result = frameEntryService.saveFrameEntries(date, frameSellerId, entries);
        return ResponseEntity.ok(result);
    }

    /**
     * Get frame entries by date and seller.
     * GET /api/frame-entries?date=2026-04-10&frameSellerId=1
     */
    @GetMapping
    public ResponseEntity<List<FrameEntryDTO>> getFrameEntries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long frameSellerId) {
        if (frameSellerId != null) {
            return ResponseEntity.ok(frameEntryService.getFrameEntriesByDateAndSeller(date, frameSellerId));
        }
        return ResponseEntity.ok(frameEntryService.getFrameEntriesByDate(date));
    }
}
