package com.company.queueservice.util.feign;

import com.company.queueservice.util.messages.LevelUpMsg;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "level-up-service")
public interface LevelUpClient {

    @RequestMapping(value = "/levelup/{id}", method = RequestMethod.PUT)
    LevelUpMsg updateLevelUp(@RequestBody LevelUpMsg levelUpMsg, @PathVariable int id);

    @RequestMapping(value = "/levelup/customer/{customerId}", method = RequestMethod.GET)
    LevelUpMsg getLevelUpByCustomerId(@PathVariable int customerId);

}
