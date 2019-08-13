package com.company.levelUpservice.dao;

import com.company.levelUpservice.dto.LevelUp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LevelUpDaoTest {

    @Autowired
    private LevelUpDao dao;

    @Before
    public void setUp() throws Exception {
        List<LevelUp> levelUps = dao.getAllLevelUp();
        levelUps.forEach(levelUp -> dao.deleteLevelUp(levelUp.getLevelUpId()));
    }

    @Test
    public void addGetLevelUp() {
        //Arranging
        LevelUp levelUp = new LevelUp();
        levelUp.setLevelUpId(1);
        levelUp.setCustomerId(1);
        levelUp.setMemberDate(LocalDate.of(2019,10,05));
        levelUp.setPoints(25);

        //Adding LevelUp
        LevelUp fromAdd = dao.addLevelUp(levelUp);
        //Getting LevelUp
        LevelUp fromGet = dao.getLevelUp(fromAdd.getLevelUpId());

        //Asserting
        assertEquals(fromAdd, fromGet);
    }

    @Test
    public void getAllLevelUp() {
        //Arranging
        LevelUp levelUp = new LevelUp();
        levelUp.setLevelUpId(1);
        levelUp.setCustomerId(1);
        levelUp.setMemberDate(LocalDate.of(2019,10,05));
        levelUp.setPoints(25);
        levelUp = dao.addLevelUp(levelUp);

        //Asserting
        assertEquals(dao.getAllLevelUp().size(),1);
        assertEquals(dao.getAllLevelUp().get(0),levelUp);
    }

    @Test
    public void updateLevelUp() {
        //Arranging
        LevelUp levelUp = new LevelUp();
        levelUp.setLevelUpId(1);
        levelUp.setCustomerId(1);
        levelUp.setMemberDate(LocalDate.of(2019,10,05));
        levelUp.setPoints(25);
        levelUp = dao.addLevelUp(levelUp);

        //Updating LevelUp
        levelUp.setPoints(35);
        dao.updateLevelUp(levelUp);

        //Asserting
        assertEquals(dao.getLevelUp(levelUp.getLevelUpId()),levelUp);
    }

    @Test
    public void deleteLevelUp() {
        //Arranging
        LevelUp levelUp = new LevelUp();
        levelUp.setLevelUpId(1);
        levelUp.setCustomerId(1);
        levelUp.setMemberDate(LocalDate.of(2019,10,05));
        levelUp.setPoints(25);
        levelUp = dao.addLevelUp(levelUp);

        //Deleting
        dao.deleteLevelUp(levelUp.getLevelUpId());

        //Asserting
        assertNull(dao.getLevelUp(levelUp.getLevelUpId()));
    }
}