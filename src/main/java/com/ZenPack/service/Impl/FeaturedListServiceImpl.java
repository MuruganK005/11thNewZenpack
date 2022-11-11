package com.ZenPack.service.Impl;

import com.ZenPack.Dto.FeatureDto;
import com.ZenPack.model.FeaturedList;
import com.ZenPack.repository.FeaturedListRepository;
import com.ZenPack.service.Services.FeaturedListService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeaturedListServiceImpl implements FeaturedListService {


    @Autowired
    private FeaturedListRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(FeaturedListServiceImpl.class);

    @Override
    public FeaturedList save(FeaturedList featuredList) {
        return repository.save(featuredList);
    }

    @Override
    public List<FeaturedList> findAllList() {
        return repository.findAll();
    }

    @Override
    public List<FeaturedList> findByKeyword(String keyword){
        return repository.findByKeyword(keyword);
    }

    @Override
    public FeaturedList updatedList(FeaturedList updatedList) {
        return repository.save(updatedList);
    }

    @Override
    public Optional<FeaturedList> getListById(int listId) {
        return repository.findById(listId);
    }

    @Override
    public String deleteList(int id) {
        FeaturedList existingList=repository.findById(id).get();
        repository.delete(existingList);
        return " Id "+id+" Deleted SuccessFully";
    }


}
