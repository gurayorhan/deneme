package com.yazlab.proje.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Similarity {

    private String urlOne;

    private String urlTwo;

    private Double score;

    private List<Keyword> keywordsUrlOne;

    private List<Frequency> frequenciesUrlOne;

    private List<Keyword> keywordsUrlTwo;

    private List<Frequency> frequenciesUrlTwo;

}
