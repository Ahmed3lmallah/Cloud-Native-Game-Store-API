package com.company.levelUpservice.service;

import com.company.levelUpservice.dao.LevelUpDao;
import com.company.levelUpservice.dao.LevelUpDaoJdbcTemplateImpl;
import com.company.levelUpservice.dto.LevelUp;
import com.company.levelUpservice.exception.NotFoundException;
import com.company.levelUpservice.views.LevelUpViewModel;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ServiceLayerTest {

    private ServiceLayer serviceLayer;
    private LevelUpDao levelUpDao;

    @Before
    public void setUp() throws Exception {
        setUpLevelUpDaoMock();
        serviceLayer = new ServiceLayer(levelUpDao);
    }

    @Test
    public void saveLevelUp() {
        //Input
        LevelUpViewModel input = new LevelUpViewModel();
        input.setCustomerId(1);
        input.setMemberDate(LocalDate.of(2019,10,05));
        input.setPoints(25);

        //From Service
        LevelUpViewModel fromService = serviceLayer.saveLevelUp(input);

        //expected
        input.setLevelUpId(1);

        //Asserting
        assertEquals(input, fromService);
    }

    @Test
    public void findLevelUp() {
        //expected
        LevelUpViewModel expectedOutput = new LevelUpViewModel();
        expectedOutput.setLevelUpId(1);
        expectedOutput.setCustomerId(1);
        expectedOutput.setMemberDate(LocalDate.of(2019,10,05));
        expectedOutput.setPoints(25);

        //Asserting
        assertEquals(expectedOutput, serviceLayer.findLevelUp(1));
    }

    @Test
    public void findAllLevelUps() {
        //expected
        LevelUpViewModel expectedOutput = new LevelUpViewModel();
        expectedOutput.setLevelUpId(1);
        expectedOutput.setCustomerId(1);
        expectedOutput.setMemberDate(LocalDate.of(2019,10,05));
        expectedOutput.setPoints(25);

        //Asserting
        assertEquals(1,serviceLayer.findAllLevelUps().size());
        assertEquals(expectedOutput, serviceLayer.findAllLevelUps().get(0));
    }

    @Test(expected = NotFoundException.class)
    public void updateLevelUp() {
        //Updating LevelUp!
        //expected & Input
        LevelUpViewModel expectedOutput = new LevelUpViewModel();
        expectedOutput.setLevelUpId(1);
        expectedOutput.setCustomerId(1);
        expectedOutput.setMemberDate(LocalDate.of(2019,10,05));
        expectedOutput.setPoints(25);

        assertEquals(expectedOutput, serviceLayer.updateLevelUp(expectedOutput));

        //A Product that doesn't exist in DB
        LevelUpViewModel fakeLevelUp = new LevelUpViewModel();
        fakeLevelUp.setLevelUpId(2);
        fakeLevelUp.setCustomerId(1);
        fakeLevelUp.setMemberDate(LocalDate.of(2019,10,05));
        fakeLevelUp.setPoints(25);

        serviceLayer.updateLevelUp(fakeLevelUp);
    }

    @Test(expected = NotFoundException.class)
    public void removeLevelUp() {
        assertEquals(serviceLayer.removeLevelUp(1), "LevelUp [1] deleted successfully!");

        //A product that doesn't exist in DB
        serviceLayer.removeLevelUp(2);
    }

    private void setUpLevelUpDaoMock() {

        levelUpDao = mock(LevelUpDaoJdbcTemplateImpl.class);

        // Output
        LevelUp output = new LevelUp();
        output.setLevelUpId(1);
        output.setCustomerId(1);
        output.setMemberDate(LocalDate.of(2019,10,05));
        output.setPoints(25);

        // Input
        LevelUp input = new LevelUp();
        input.setCustomerId(1);
        input.setMemberDate(LocalDate.of(2019,10,05));
        input.setPoints(25);

        // All Customers
        List<LevelUp> levelUps = new ArrayList<>();
        levelUps.add(output);

        doReturn(output).when(levelUpDao).addLevelUp(input);
        doReturn(output).when(levelUpDao).getLevelUp(1);
        doReturn(levelUps).when(levelUpDao).getAllLevelUp();
        doNothing().when(levelUpDao).updateLevelUp(input);
    }
}