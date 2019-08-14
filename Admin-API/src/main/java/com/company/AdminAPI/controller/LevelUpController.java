package com.company.AdminAPI.controller;

import com.company.AdminAPI.service.ServiceLayer;
import com.company.AdminAPI.views.input.LevelUpInputModel;
import com.company.AdminAPI.views.output.LevelUpViewModel;
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
    public LevelUpViewModel createLevelUp(@RequestBody @Valid LevelUpInputModel levelUpInputModel){
        return serviceLayer.saveLevelUp(levelUpInputModel);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public LevelUpViewModel getLevelUp(@PathVariable int id) {
        return serviceLayer.findLevelUp(id);
    }

    @GetMapping(value = "/customer/{customerId}")
    @ResponseStatus(value = HttpStatus.OK)
    public LevelUpViewModel getLevelUpByCustomerId(@PathVariable int customerId) {
        return serviceLayer.findLevelUpByCustomerId(customerId);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public LevelUpViewModel updateLevelUp(@RequestBody @Valid LevelUpInputModel levelUpInputModel, @PathVariable int id) {
        if(id!=levelUpInputModel.getLevelUpId()){
            throw new IllegalArgumentException("Level Up! ID in path must match with request body!");
        }
        return serviceLayer.updateLevelUp(levelUpInputModel);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteLevelUp(@PathVariable int id) {
        return serviceLayer.removeLevelUp(id);
    }
}