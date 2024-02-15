package com.ssafy.mokkoji.game.talkbody.repository;

import com.ssafy.mokkoji.game.talkbody.dto.TalkBodyDto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TalkBodyRepository extends MongoRepository<TalkBodyDto, String> {

}
