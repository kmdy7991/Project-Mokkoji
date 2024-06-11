package com.ssafy.mokkoji.member.administrator.repository;

import com.ssafy.mokkoji.member.administrator.dto.TalkBodyDto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminBodyRepository extends MongoRepository<TalkBodyDto, String> {

}
