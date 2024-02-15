package com.ssafy.mokkoji.member.administrator.service;

import com.ssafy.mokkoji.member.administrator.dto.TalkBodyDto;
import com.ssafy.mokkoji.member.administrator.repository.AdminBodyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final AdminBodyRepository talkkBodyRepository;

    @Autowired
    public AdminService(AdminBodyRepository talkkBodyRepository) {
        this.talkkBodyRepository = talkkBodyRepository;
    }

    public List<TalkBodyDto> getAllSubject(){
        return talkkBodyRepository.findAll();
    }
    public void deleteBySubject(String subject){ talkkBodyRepository.deleteById(subject);}

    public TalkBodyDto save(TalkBodyDto dto){ return talkkBodyRepository.save(dto);}
}