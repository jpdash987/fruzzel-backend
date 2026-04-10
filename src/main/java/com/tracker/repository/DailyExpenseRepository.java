package com.tracker.repository;

import com.tracker.entity.DailyExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyExpenseRepository extends JpaRepository<DailyExpense, Long> {
    List<DailyExpense> findByDate(LocalDate date);
}
