package com.company.AdminAPI.util.feign;

import com.company.AdminAPI.util.messages.LevelUp;
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
    public List<LevelUp> getAllLevelUps();

    @RequestMapping(value = "/levelup", method = RequestMethod.POST)
    public LevelUp createLevelUp(@RequestBody @Valid LevelUp levelUp);

    @RequestMapping(value = "/levelup/{id}", method = RequestMethod.GET)
    public LevelUp getLevelUp(@PathVariable int id);

    @RequestMapping(value = "/levelup/{id}", method = RequestMethod.PUT)
    public LevelUp updateLevelUp(@RequestBody @Valid LevelUp levelUp, @PathVariable int id);

    @RequestMapping(value = "/levelup/{id}", method = RequestMethod.DELETE)
    public String deleteLevelUp(@PathVariable int id);
}
