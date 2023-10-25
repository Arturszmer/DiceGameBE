package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Dice;

class DiceBuilder {

    private Integer value;
    private boolean isGoodNumber;
    private boolean isChecked;
    private boolean isMultiple;
    private boolean isImmutable;

    public static DiceBuilder aDiceBuilder(){
        return new DiceBuilder();
    }

    public DiceBuilder withValue(int value){
        this.value = value;
        return this;
    }

    public DiceBuilder withGoodNumber(){
        isGoodNumber = true;
        return this;
    }

    public DiceBuilder withChecked(){
        isChecked = true;
        return this;
    }

    public DiceBuilder withMultiple(){
        isMultiple = true;
        return this;
    }

    public DiceBuilder withImmutable(){
        isImmutable = true;
        return this;
    }

    public Dice build(){
        Dice dice = new Dice();
        dice.setValue(value);
        dice.setGoodNumber(isGoodNumber);
        dice.setChecked(isChecked);
        dice.setMultiple(isMultiple);
        dice.setImmutable(isImmutable);
        return dice;
    }
}
