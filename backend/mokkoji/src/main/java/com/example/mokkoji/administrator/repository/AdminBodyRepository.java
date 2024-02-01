package com.example.mokkoji.administrator.repository;

import com.example.mokkoji.administrator.dto.TalkBodyDto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminBodyRepository extends MongoRepository<TalkBodyDto, String> {

}
