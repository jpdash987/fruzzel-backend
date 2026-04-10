package com.tracker.repository;

import com.tracker.entity.FrameEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FrameEntryRepository extends JpaRepository<FrameEntry, Long> {

    List<FrameEntry> findByDateAndFrameSellerId(LocalDate date, Long frameSellerId);

    List<FrameEntry> findByDate(LocalDate date);

    Optional<FrameEntry> findByDateAndFrameSellerIdAndFrameId(LocalDate date, Long frameSellerId, Long frameId);
}
