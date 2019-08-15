package com.company.levelUpservice.controller;

import com.company.levelUpservice.service.ServiceLayer;
import com.company.levelUpservice.views.LevelUpViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
@CacheConfig(cacheNames = {"levelup"})
@RequestMapping(value = "/levelup")
public class LevelUpController {

    @Autowired
    ServiceLayer serviceLayer;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<LevelUpViewModel> getAllLevelUps(){
        return serviceLayer.findAllLevelUps();
    }

    @CachePut(key = "#result.getLevelUpId()")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public LevelUpViewModel createLevelUp(@RequestBody @Valid LevelUpViewModel levelUpViewModel){
        return serviceLayer.saveLevelUp(levelUpViewModel);
    }

    @Cacheable
    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public LevelUpViewModel getLevelUp(@PathVariable int id) {
        System.out.println("fetching from DB...");
        return serviceLayer.findLevelUp(id);
    }

    @GetMapping(value = "/customer/{customerId}")
    @ResponseStatus(value = HttpStatus.OK)
    public LevelUpViewModel getLevelUpByCustomerId(@PathVariable int customerId) {
        return serviceLayer.findLevelUpByCustomerId(customerId);
    }

    @CacheEvict(key = "#id")
    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public LevelUpViewModel updateLevelUp(@RequestBody @Valid LevelUpViewModel levelUpViewModel, @PathVariable int id) {
        if(id!=levelUpViewModel.getLevelUpId()){
            throw new IllegalArgumentException("Level Up! ID in path must match with request body!");
        }
        return serviceLayer.updateLevelUp(levelUpViewModel);
    }

    @CacheEvict
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteLevelUp(@PathVariable int id) {
        return serviceLayer.removeLevelUp(id);
    }
}