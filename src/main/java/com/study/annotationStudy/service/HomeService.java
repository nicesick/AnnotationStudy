package com.study.annotationStudy.service;

import com.study.annotationStudy.dto.Guest;
import com.study.annotationStudy.repository.HomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HomeService {
    private HomeRepository homeRepository;

    @Autowired
    public HomeService(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    @Transactional
    public List<Guest> getGuests() {
        return homeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Guest> getGuestsReadOnly() {
        return homeRepository.findAll();
    }
}
