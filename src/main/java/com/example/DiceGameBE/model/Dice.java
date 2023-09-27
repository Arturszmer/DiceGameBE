package com.example.DiceGameBE.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;

@Data
@RedisHash
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Dice implements Serializable {

    private Integer value;
    @JsonProperty(value = "isGoodNumber")
    private boolean isGoodNumber;
    @JsonProperty(value = "isChecked")
    private boolean isChecked;
    @JsonProperty(value = "isMultiple")
    private boolean isMultiple;
    @JsonProperty(value = "isImmutable")
    private boolean isImmutable;

    public Dice(Integer value) {
        this.value = value;
    }

    public void insertNewValueFromRoll(int value){
        this.value = value;
        this.isGoodNumber = false;
        this.isChecked = false;
        this.isMultiple = false;
        this.isImmutable = false;
    }
}
