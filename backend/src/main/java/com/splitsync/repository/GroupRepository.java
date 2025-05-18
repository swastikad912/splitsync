package com.splitsync.repository;

import com.splitsync.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByCreatedAtBefore(LocalDateTime cutoff);

}