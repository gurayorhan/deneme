package com.yazlab.proje.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Frequency {

    private String name;

    private Integer count;

    private Double frequency;

}
