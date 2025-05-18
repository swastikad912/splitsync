package com.splitsync.service;

import com.splitsync.model.Group;
import com.splitsync.model.User;
import com.splitsync.repository.GroupRepository;
import com.splitsync.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GroupRepository groupRepo;

    public User joinGroup(Long groupId , String name , String email , String upi)
    {
        Group g = groupRepo.findById(groupId).orElseThrow();
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setUpi(upi);
        u.setGroup(g);

        g.getMembers().add(u);
        groupRepo.save(g);


        return u;
    }

}