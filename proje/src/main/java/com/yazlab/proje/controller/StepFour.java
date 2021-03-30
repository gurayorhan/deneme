package com.yazlab.proje.controller;

import com.yazlab.proje.dto.StepFourDto;
import com.yazlab.proje.model.Deep;
import com.yazlab.proje.service.DeepExtraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin("*")
@RestController
public class StepFour {

    @Autowired
    private DeepExtraction deepExtraction;

    @PostMapping("/stepFour")
    public Deep stepFour(@RequestBody StepFourDto stepFourDto) throws IOException {
        return deepExtraction.deepSimilarity(stepFourDto);
    }

}
