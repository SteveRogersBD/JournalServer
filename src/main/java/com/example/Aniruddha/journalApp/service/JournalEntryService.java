package com.example.Aniruddha.journalApp.service;

import com.example.Aniruddha.journalApp.entity.JournalEntry;
import com.example.Aniruddha.journalApp.entity.User;
import com.example.Aniruddha.journalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveJournalEntry(JournalEntry journalEntry, String userName)
    {
        try{
            User user = userService.findByUserName(userName);
            JournalEntry saved = journalEntryRepo.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        }catch(Exception e)
        {
            System.out.println(e);
            throw new RuntimeException("Exception: "+e);
        }
    }

    public List<JournalEntry> getAll()
    {
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> findJournalById(ObjectId id)
    {
        return journalEntryRepo.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName)
    {
        boolean removed = false;
        try{
            User user=userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(x-> x.getId().equals(String.valueOf(id)));
            if(removed)
            {
                userService.saveUser(user);
                journalEntryRepo.deleteById(id);
            }
        }catch (Exception e)
        {
            System.out.println(e);
            throw new RuntimeException("An error occurred while deleting the entry.",e);
        }
        return removed;

    }

    public void saveJournalEntry(JournalEntry journalEntry) {
        journalEntryRepo.save(journalEntry);
    }
}
