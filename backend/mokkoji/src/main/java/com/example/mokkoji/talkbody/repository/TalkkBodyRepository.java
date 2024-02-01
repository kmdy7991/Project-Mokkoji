package com.example.mokkoji.talkbody.repository;

import com.example.mokkoji.talkbody.dto.TalkBodyDto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TalkkBodyRepository extends MongoRepository<TalkBodyDto, String> {

}
