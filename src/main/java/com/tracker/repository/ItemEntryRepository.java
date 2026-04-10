package com.tracker.repository;

import com.tracker.entity.ItemEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemEntryRepository extends JpaRepository<ItemEntry, Long> {

    List<ItemEntry> findByDailyEntryId(Long dailyEntryId);

    @Modifying
    @Query("DELETE FROM ItemEntry i WHERE i.item.id = :itemId")
    void deleteByItemId(Long itemId);
}
