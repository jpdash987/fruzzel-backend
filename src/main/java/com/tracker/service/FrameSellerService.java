package com.tracker.service;

import com.tracker.dto.FrameSellerDTO;
import com.tracker.entity.FrameSeller;
import com.tracker.exception.ResourceNotFoundException;
import com.tracker.repository.FrameSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FrameSellerService {

    private final FrameSellerRepository frameSellerRepository;

    public FrameSellerDTO createFrameSeller(FrameSellerDTO dto) {
        FrameSeller seller = FrameSeller.builder()
                .name(dto.getName())
                .build();
        seller = frameSellerRepository.save(seller);
        return toDTO(seller);
    }

    @Transactional(readOnly = true)
    public List<FrameSellerDTO> getAllFrameSellers() {
        return frameSellerRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FrameSellerDTO getFrameSellerById(Long id) {
        FrameSeller seller = frameSellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FrameSeller", id));
        return toDTO(seller);
    }

    public FrameSellerDTO updateFrameSeller(Long id, FrameSellerDTO dto) {
        FrameSeller seller = frameSellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FrameSeller", id));
        seller.setName(dto.getName());
        seller = frameSellerRepository.save(seller);
        return toDTO(seller);
    }

    public void deleteFrameSeller(Long id) {
        if (!frameSellerRepository.existsById(id)) {
            throw new ResourceNotFoundException("FrameSeller", id);
        }
        frameSellerRepository.deleteById(id);
    }

    private FrameSellerDTO toDTO(FrameSeller seller) {
        return FrameSellerDTO.builder()
                .id(seller.getId())
                .name(seller.getName())
                .build();
    }
}
