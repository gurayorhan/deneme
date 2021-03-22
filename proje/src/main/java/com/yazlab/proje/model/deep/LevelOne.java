package com.yazlab.proje.model.deep;

import com.yazlab.proje.model.Keyword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelOne {

    private String uri;

    private List<Keyword> keywords = new ArrayList<>();

    private List<LevelTwo> levelTwoList = new ArrayList<>();

}
