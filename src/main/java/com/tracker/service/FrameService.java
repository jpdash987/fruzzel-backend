package com.tracker.service;

import com.tracker.dto.FrameDTO;
import com.tracker.entity.Frame;
import com.tracker.exception.ResourceNotFoundException;
import com.tracker.repository.FrameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FrameService {

    private final FrameRepository frameRepository;

    public FrameDTO createFrame(FrameDTO dto) {
        Frame frame = Frame.builder()
                .name(dto.getName())
                .rate(dto.getRate())
                .build();
        frame = frameRepository.save(frame);
        return toDTO(frame);
    }

    @Transactional(readOnly = true)
    public List<FrameDTO> getAllFrames() {
        return frameRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FrameDTO getFrameById(Long id) {
        Frame frame = frameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Frame", id));
        return toDTO(frame);
    }

    public FrameDTO updateFrame(Long id, FrameDTO dto) {
        Frame frame = frameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Frame", id));
        frame.setName(dto.getName());
        frame.setRate(dto.getRate());
        frame = frameRepository.save(frame);
        return toDTO(frame);
    }

    public void deleteFrame(Long id) {
        if (!frameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Frame", id);
        }
        frameRepository.deleteById(id);
    }

    private FrameDTO toDTO(Frame frame) {
        return FrameDTO.builder()
                .id(frame.getId())
                .name(frame.getName())
                .rate(frame.getRate())
                .build();
    }
}
