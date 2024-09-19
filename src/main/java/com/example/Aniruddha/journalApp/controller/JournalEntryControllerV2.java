package com.example.Aniruddha.journalApp.controller;

import com.example.Aniruddha.journalApp.entity.JournalEntry;
import com.example.Aniruddha.journalApp.entity.User;
import com.example.Aniruddha.journalApp.service.JournalEntryService;
import com.example.Aniruddha.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournals()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry>all = user.getJournalEntries();
        if(all!=null && !all.isEmpty())
            return new ResponseEntity<>(all,HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry myJournal)
    {
        try{
            myJournal.setDate(LocalDateTime.now());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.saveJournalEntry(myJournal,userName);
            return new ResponseEntity<>(myJournal,HttpStatus.CREATED);
        }catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("get/id/{id}")
    public ResponseEntity<JournalEntry> getJournalById(@PathVariable ObjectId id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry>collect=user.getJournalEntries().stream().filter(x->x.getId().equals(id))
                        .collect(Collectors.toList());

        if(!collect.isEmpty())
        {
            Optional<JournalEntry>optionalJournalEntry=journalEntryService.findJournalById(id);
            if(optionalJournalEntry.isPresent())
            {
                return new ResponseEntity<>(optionalJournalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("delete/id/{id}")
    public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        if(journalEntryService.deleteById(id,userName))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("id/{id}")
    public ResponseEntity<JournalEntry> updateJournalById
            (@PathVariable ObjectId id,
             @RequestBody JournalEntry myJournal)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry>collect=user.getJournalEntries().stream().filter(x->x.getId().equals(id))
                .collect(Collectors.toList());

        if(!collect.isEmpty())
        {
            Optional<JournalEntry>journalEntry = journalEntryService.findJournalById(id);
            if(journalEntry.isPresent())
            {
                JournalEntry old = journalEntry.get();
                old.setContent(myJournal!=null && !myJournal.getContent().equals("")
                        ?myJournal.getContent():old.getContent());
                old.setTitle(myJournal!=null && !myJournal.getTitle().equals("")
                        ?myJournal.getTitle():old.getTitle());
                old.setDate(LocalDateTime.now());
                journalEntryService.saveJournalEntry(old);
                return new ResponseEntity<>(old,HttpStatus.OK);
        }

        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}