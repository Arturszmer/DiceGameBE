package com.example.DiceGameBE.model;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;

@Data
@RedisHash
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Dice implements Serializable {

    private Integer Value;
    private boolean isGoodNumber;
    private boolean isChecked;
    private boolean isMultiple;
    private boolean isImmutable;

    public Dice(Integer value) {
        Value = value;
    }
}
