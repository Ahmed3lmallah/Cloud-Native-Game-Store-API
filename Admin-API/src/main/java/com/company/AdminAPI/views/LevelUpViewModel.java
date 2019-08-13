package com.company.AdminAPI.views;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Objects;

public class LevelUpViewModel {

    private int levelUpId;
    @NotNull(message = "Must provide a Customer object (with only the customerId)!")
    private CustomerViewModel customer;
    @NotNull(message = "Must provide memberDate!")
    private LocalDate memberDate;
    @PositiveOrZero(message = "Must provide points (positive or zero)!")
    private int points;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LevelUpViewModel that = (LevelUpViewModel) o;
        return getLevelUpId() == that.getLevelUpId() &&
                getPoints() == that.getPoints() &&
                Objects.equals(getCustomer(), that.getCustomer()) &&
                Objects.equals(getMemberDate(), that.getMemberDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLevelUpId(), getCustomer(), getMemberDate(), getPoints());
    }

    public int getLevelUpId() {
        return levelUpId;
    }

    public void setLevelUpId(int levelUpId) {
        this.levelUpId = levelUpId;
    }

    public CustomerViewModel getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerViewModel customer) {
        this.customer = customer;
    }

    public LocalDate getMemberDate() {
        return memberDate;
    }

    public void setMemberDate(LocalDate memberDate) {
        this.memberDate = memberDate;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
