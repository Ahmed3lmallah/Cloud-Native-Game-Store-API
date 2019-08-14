package com.company.AdminAPI.util.feign;

import com.company.AdminAPI.views.input.LevelUpInputModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "level-up-service")
public interface LevelUpClient {

    @RequestMapping(value = "/levelup", method = RequestMethod.GET)
    public List<LevelUpInputModel> getAllLevelUps();

    @RequestMapping(value = "/levelup", method = RequestMethod.POST)
    public LevelUpInputModel createLevelUp(@RequestBody @Valid LevelUpInputModel levelUpInputModel);

    @RequestMapping(value = "/levelup/{id}", method = RequestMethod.GET)
    public LevelUpInputModel getLevelUp(@PathVariable int id);

    @RequestMapping(value = "/levelup/customer/{customerId}", method = RequestMethod.GET)
    public LevelUpInputModel getLevelUpByCustomerId(@PathVariable int customerId);

    @RequestMapping(value = "/levelup/{id}", method = RequestMethod.PUT)
    public LevelUpInputModel updateLevelUp(@RequestBody @Valid LevelUpInputModel levelUpInputModel, @PathVariable int id);

    @RequestMapping(value = "/levelup/{id}", method = RequestMethod.DELETE)
    public String deleteLevelUp(@PathVariable int id);
}
