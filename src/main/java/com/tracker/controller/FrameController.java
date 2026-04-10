package com.tracker.controller;

import com.tracker.dto.FrameDTO;
import com.tracker.service.FrameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/frames")
@RequiredArgsConstructor
public class FrameController {

    private final FrameService frameService;

    @PostMapping
    public ResponseEntity<FrameDTO> createFrame(@Valid @RequestBody FrameDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(frameService.createFrame(dto));
    }

    @GetMapping
    public ResponseEntity<List<FrameDTO>> getAllFrames() {
        return ResponseEntity.ok(frameService.getAllFrames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FrameDTO> getFrameById(@PathVariable Long id) {
        return ResponseEntity.ok(frameService.getFrameById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FrameDTO> updateFrame(
            @PathVariable Long id,
            @Valid @RequestBody FrameDTO dto) {
        return ResponseEntity.ok(frameService.updateFrame(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFrame(@PathVariable Long id) {
        frameService.deleteFrame(id);
        return ResponseEntity.noContent().build();
    }
}
