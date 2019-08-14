package com.company.levelUpservice.service;

import com.company.levelUpservice.dao.LevelUpDao;
import com.company.levelUpservice.dto.LevelUp;
import com.company.levelUpservice.exception.NotFoundException;
import com.company.levelUpservice.views.LevelUpViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceLayer {
    private LevelUpDao dao;

    @Autowired
    public ServiceLayer(LevelUpDao dao) {
        this.dao = dao;
    }

    //
    // Service Layer Methods
    // --------------------- //
    public LevelUpViewModel saveLevelUp(LevelUpViewModel levelUpViewModel){
        LevelUp levelUp = viewModelToModel(levelUpViewModel);

        levelUp = dao.addLevelUp(levelUp);

        levelUpViewModel.setLevelUpId(levelUp.getLevelUpId());

        return levelUpViewModel;
    }

    public LevelUpViewModel findLevelUp(int levelUpId){
        LevelUp levelUp = dao.getLevelUp(levelUpId);
        if(levelUp==null){
            throw new NotFoundException("LevelUp ID cannot be found in DB!");
        } else {
            return buildLevelUpViewModel(levelUp);
        }
    }

    public LevelUpViewModel findLevelUpByCustomerId(int customerId){
        LevelUp levelUp = dao.getLevelUpByCustomerId(customerId);
        if(levelUp==null){
            throw new NotFoundException("LevelUp membership cannot be found in DB!");
        } else {
            return buildLevelUpViewModel(levelUp);
        }
    }

    public List<LevelUpViewModel> findAllLevelUps(){
        List<LevelUp> levelUps = dao.getAllLevelUp();
        List<LevelUpViewModel> levelUpViewModels = new ArrayList<>();

        levelUps.forEach(levelUp -> levelUpViewModels.add(buildLevelUpViewModel(levelUp)));

        return levelUpViewModels;
    }

    @Transactional
    public LevelUpViewModel updateLevelUp(LevelUpViewModel levelUpViewModel){
        //Checking if LevelUp exists
        findLevelUp(levelUpViewModel.getLevelUpId());

        //Updating
        dao.updateLevelUp(viewModelToModel(levelUpViewModel));

        //Retrieving
        return findLevelUp(levelUpViewModel.getLevelUpId());
    }

    @Transactional
    public String removeLevelUp(int levelUpId){
        //Checking if levelUp exists
        findLevelUp(levelUpId);

        //Deleting
        dao.deleteLevelUp(levelUpId);

        return "LevelUp ["+levelUpId+"] deleted successfully!";
    }

    //
    // Helper Methods
    // --------------------- //
    private LevelUp viewModelToModel(LevelUpViewModel levelUpViewModel){
        LevelUp levelUp = new LevelUp();
        levelUp.setLevelUpId(levelUpViewModel.getLevelUpId());
        levelUp.setCustomerId(levelUpViewModel.getCustomerId());
        levelUp.setMemberDate(levelUpViewModel.getMemberDate());
        levelUp.setPoints(levelUpViewModel.getPoints());
        return levelUp;
    }

    private LevelUpViewModel buildLevelUpViewModel(LevelUp levelUp){
        LevelUpViewModel levelUpViewModel = new LevelUpViewModel();
        levelUpViewModel.setLevelUpId(levelUp.getLevelUpId());
        levelUpViewModel.setCustomerId(levelUp.getCustomerId());
        levelUpViewModel.setMemberDate(levelUp.getMemberDate());
        levelUpViewModel.setPoints(levelUp.getPoints());
        return levelUpViewModel;
    }

}
