package com.example.Aniruddha.journalApp.repository;


import com.example.Aniruddha.journalApp.entity.JournalEntry;
import com.example.Aniruddha.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, ObjectId> {
    User findByUserName(String userName);

    void deleteByUserName(String userName);
}
