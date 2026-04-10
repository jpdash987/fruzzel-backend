package com.tracker.service;

import com.tracker.dto.FrameEntryDTO;
import com.tracker.entity.Frame;
import com.tracker.entity.FrameEntry;
import com.tracker.entity.FrameSeller;
import com.tracker.exception.ResourceNotFoundException;
import com.tracker.repository.FrameEntryRepository;
import com.tracker.repository.FrameRepository;
import com.tracker.repository.FrameSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FrameEntryService {

    private final FrameEntryRepository frameEntryRepository;
    private final FrameSellerRepository frameSellerRepository;
    private final FrameRepository frameRepository;

    /**
     * Save (create or update) frame entries for a given date + frame seller.
     * The payload is a list of entries (one per frame). Existing entries for the
     * same date + seller + frame are updated; new ones are created.
     */
    public List<FrameEntryDTO> saveFrameEntries(LocalDate date, Long frameSellerId, List<FrameEntryDTO> dtos) {
        FrameSeller seller = frameSellerRepository.findById(frameSellerId)
                .orElseThrow(() -> new ResourceNotFoundException("FrameSeller", frameSellerId));

        List<FrameEntry> saved = dtos.stream().map(dto -> {
            Frame frame = frameRepository.findById(dto.getFrameId())
                    .orElseThrow(() -> new ResourceNotFoundException("Frame", dto.getFrameId()));

            Optional<FrameEntry> existing = frameEntryRepository
                    .findByDateAndFrameSellerIdAndFrameId(date, frameSellerId, dto.getFrameId());

            FrameEntry entry = existing.orElse(FrameEntry.builder()
                    .date(date)
                    .frameSeller(seller)
                    .frame(frame)
                    .mrp(0.0)
                    .build());

            entry.setFrameCount(dto.getFrameCount() != null ? dto.getFrameCount() : 0);
            entry.calculateMrp();

            return frameEntryRepository.save(entry);
        }).collect(Collectors.toList());

        return saved.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FrameEntryDTO> getFrameEntriesByDateAndSeller(LocalDate date, Long frameSellerId) {
        return frameEntryRepository.findByDateAndFrameSellerId(date, frameSellerId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FrameEntryDTO> getFrameEntriesByDate(LocalDate date) {
        return frameEntryRepository.findByDate(date)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private FrameEntryDTO toDTO(FrameEntry entry) {
        return FrameEntryDTO.builder()
                .id(entry.getId())
                .date(entry.getDate())
                .frameSellerId(entry.getFrameSeller().getId())
                .frameSellerName(entry.getFrameSeller().getName())
                .frameId(entry.getFrame().getId())
                .frameName(entry.getFrame().getName())
                .frameRate(entry.getFrame().getRate())
                .frameCount(entry.getFrameCount())
                .mrp(entry.getMrp())
                .build();
    }
}
