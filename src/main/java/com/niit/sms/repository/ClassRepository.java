package com.niit.sms.repository;

import com.niit.sms.model.SchoolClass;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ClassRepository extends MongoRepository<SchoolClass, String> {
    Optional<SchoolClass> findByName(String name);
    boolean existsByName(String name);
}
