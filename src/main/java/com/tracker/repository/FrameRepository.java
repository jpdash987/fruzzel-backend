package com.tracker.repository;

import com.tracker.entity.Frame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrameRepository extends JpaRepository<Frame, Long> {
}
