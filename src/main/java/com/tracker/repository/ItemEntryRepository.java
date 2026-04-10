package com.tracker.repository;

import com.tracker.entity.ItemEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemEntryRepository extends JpaRepository<ItemEntry, Long> {

    List<ItemEntry> findByDailyEntryId(Long dailyEntryId);
}
