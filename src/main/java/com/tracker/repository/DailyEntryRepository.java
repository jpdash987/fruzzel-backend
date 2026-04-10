package com.tracker.repository;

import com.tracker.entity.DailyEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyEntryRepository extends JpaRepository<DailyEntry, Long> {

    Optional<DailyEntry> findByDateAndCustomerId(LocalDate date, Long customerId);

    List<DailyEntry> findByDate(LocalDate date);

    List<DailyEntry> findByCustomerId(Long customerId);

    List<DailyEntry> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
