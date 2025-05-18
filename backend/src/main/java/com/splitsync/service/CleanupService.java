package com.splitsync.service;

import com.splitsync.model.Group;
import com.splitsync.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CleanupService {

    @Autowired
    private GroupRepository groupRepo;

    @Scheduled(cron = "0 0 3 * * ?") // every day at 3 AM
    public void deleteOldGroups() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(3);
        List<Group> oldGroups = groupRepo.findByCreatedAtBefore(cutoff);
        groupRepo.deleteAll(oldGroups);
    }
}