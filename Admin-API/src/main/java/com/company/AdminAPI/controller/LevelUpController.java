package com.company.AdminAPI.controller;

import com.company.AdminAPI.service.ServiceLayer;
import com.company.AdminAPI.views.LevelUpViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
@RequestMapping(value = "/levelup")
public class LevelUpController {

    @Autowired
    ServiceLayer serviceLayer;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<LevelUpViewModel> getAllLevelUps(){
        return serviceLayer.findAllLevelUps();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public LevelUpViewModel createLevelUp(@RequestBody @Valid LevelUpViewModel levelUpViewModel){
        return serviceLayer.saveLevelUp(levelUpViewModel);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public LevelUpViewModel getLevelUp(@PathVariable int id) {
        return serviceLayer.findLevelUp(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public LevelUpViewModel updateLevelUp(@RequestBody @Valid LevelUpViewModel levelUpViewModel, @PathVariable int id) {
        if(id!=levelUpViewModel.getLevelUpId()){
            throw new IllegalArgumentException("Level Up! ID in path must match with request body!");
        }
        return serviceLayer.updateLevelUp(levelUpViewModel);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteLevelUp(@PathVariable int id) {
        return serviceLayer.removeLevelUp(id);
    }
}