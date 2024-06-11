package com.ssafy.mokkoji.administrator.repository;

import com.ssafy.mokkoji.administrator.dto.TalkBodyDto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminBodyRepository extends MongoRepository<TalkBodyDto, String> {

}
