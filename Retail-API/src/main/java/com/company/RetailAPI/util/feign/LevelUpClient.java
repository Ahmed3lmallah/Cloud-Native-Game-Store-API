package com.company.RetailAPI.util.feign;

import com.company.RetailAPI.views.input.LevelUpInputModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "level-up-service", fallback = LevelUpClientFallback.class)
public interface LevelUpClient {

    @RequestMapping(value = "/levelup/customer/{customerId}", method = RequestMethod.GET)
    LevelUpInputModel getLevelUpByCustomerId(@PathVariable int customerId);

}
