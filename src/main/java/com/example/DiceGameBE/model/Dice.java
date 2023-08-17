package com.example.DiceGameBE.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
}
