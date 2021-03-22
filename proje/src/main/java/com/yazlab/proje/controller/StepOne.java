package com.yazlab.proje.controller;

import com.yazlab.proje.model.Frequency;
import com.yazlab.proje.service.FrequencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@CrossOrigin("*")
public class StepOne {

    @Autowired
    private FrequencyService frequency;

    @PostMapping("/stepOne")
    public List<Frequency> stepOne(@RequestParam String url) throws IOException {
        return frequency.frequencyCalculate(url);
    }

}
