package com.noobprogrammer.scoutspace.repository;

import com.noobprogrammer.scoutspace.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByEmailOrUsername(String username, Object password);
}
