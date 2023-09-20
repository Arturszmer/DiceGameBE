package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Dice;


public class DiceBuilder {

    private Integer value;
    private boolean isGoodNumber;
    private boolean isChecked;
    private boolean isMultiple;
    private boolean isImmutable;

    private DiceBuilder() {
    }

    public static DiceBuilder aDiceBuilder() {
        return new DiceBuilder();
    }

    public DiceBuilder withValue(Integer value) {
        this.value = value;
        return this;
    }

    public DiceBuilder withGoodNumber() {
        this.isGoodNumber = true;
        return this;
    }

    public DiceBuilder withChecked() {
        this.isChecked = true;
        return this;
    }

    public DiceBuilder withMultiple() {
        this.isMultiple = true;
        return this;
    }

    public DiceBuilder withImmutable() {
        this.isImmutable = true;
        return this;
    }

    public Dice build() {
        return new Dice(value, isGoodNumber, isChecked, isMultiple, isImmutable);
    }
}
