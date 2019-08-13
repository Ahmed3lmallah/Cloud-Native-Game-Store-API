package com.company.levelUpservice.dao;

import com.company.levelUpservice.dto.LevelUp;

import java.util.List;

public interface LevelUpDao {

    LevelUp addLevelUp(LevelUp levelUp);
    LevelUp getLevelUp(int levelUpId);
    List<LevelUp> getAllLevelUp();
    void updateLevelUp(LevelUp levelUp);
    void deleteLevelUp(int levelUpId);
}
