package com.yazlab.proje.model;

import com.yazlab.proje.model.deep.LevelOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeepSimilarity {

    private Double score;

    private LevelOne levelOne;

}
