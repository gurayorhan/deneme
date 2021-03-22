package com.yazlab.proje.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Deep {

    private String url;

    private List<Keyword> keywords = new ArrayList<>();

    private List<DeepSimilarity> deepSimilarities = new ArrayList<>();

}
