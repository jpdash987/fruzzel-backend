package com.tracker.controller;

import com.tracker.dto.FrameSellerDTO;
import com.tracker.service.FrameSellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/frame-sellers")
@RequiredArgsConstructor
public class FrameSellerController {

    private final FrameSellerService frameSellerService;

    @PostMapping
    public ResponseEntity<FrameSellerDTO> createFrameSeller(@Valid @RequestBody FrameSellerDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(frameSellerService.createFrameSeller(dto));
    }

    @GetMapping
    public ResponseEntity<List<FrameSellerDTO>> getAllFrameSellers() {
        return ResponseEntity.ok(frameSellerService.getAllFrameSellers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FrameSellerDTO> getFrameSellerById(@PathVariable Long id) {
        return ResponseEntity.ok(frameSellerService.getFrameSellerById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FrameSellerDTO> updateFrameSeller(
            @PathVariable Long id,
            @Valid @RequestBody FrameSellerDTO dto) {
        return ResponseEntity.ok(frameSellerService.updateFrameSeller(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFrameSeller(@PathVariable Long id) {
        frameSellerService.deleteFrameSeller(id);
        return ResponseEntity.noContent().build();
    }
}
