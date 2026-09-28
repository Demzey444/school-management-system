package com.niit.sms.repository;

import com.niit.sms.model.SchoolInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SchoolInfoRepository extends MongoRepository<SchoolInfo, String> {
}
