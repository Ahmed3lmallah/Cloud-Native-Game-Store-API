package com.company.RetailAPI.util.feign;

import com.company.RetailAPI.views.input.LevelUpInputModel;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class LevelUpClientFallback implements LevelUpClient {

    @Override
    public LevelUpInputModel getLevelUpByCustomerId(int customerId) {
        LevelUpInputModel levelUp = new LevelUpInputModel();
        levelUp.setCustomerId(customerId);
        levelUp.setLevelUpId(0);
        levelUp.setMemberDate(LocalDate.now());
        levelUp.setPoints(0);
        return levelUp;
    }

}
