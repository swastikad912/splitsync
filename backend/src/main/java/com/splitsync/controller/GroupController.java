package com.splitsync.controller;

import com.splitsync.model.Group;
import com.splitsync.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @PostMapping
    public Group createGroup(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        Number totalMembersNum = (Number) body.get("totalMembers"); // safer cast
        int totalMembers = totalMembersNum.intValue(); // convert Number to int
        return groupService.createGroup(name, totalMembers);
//        return groupService.createGroup((String) body.get("name"), (int) body.get("totalMembers"));
    }

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable Long id) {
        return groupService.getGroup(id);
    }

    @GetMapping("/{id}/settlements")
    public Map<String, Map<String, Double>> calculateSettlements(@PathVariable Long id) {
        Group group = groupService.getGroup(id);
        return groupService.calculateSettlements(group);
    }
}