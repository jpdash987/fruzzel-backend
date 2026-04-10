package com.tracker.repository;

import com.tracker.entity.FrameSeller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrameSellerRepository extends JpaRepository<FrameSeller, Long> {
}
