package com.ZenPack.service.Services;

import com.ZenPack.Dto.FeatureDto;
import com.ZenPack.model.FeaturedList;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeaturedListService {

    FeaturedList save(FeaturedList featuredList);

    List<FeaturedList> findAllList();

    //Get Feature_list by keyword
    List<FeaturedList> findByKeyword(String keyword);


    FeaturedList updatedList(FeaturedList updatedList);

    Optional<FeaturedList> getListById(int id);

    String deleteList(int id);
}
