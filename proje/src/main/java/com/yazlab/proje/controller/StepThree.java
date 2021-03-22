package com.yazlab.proje.controller;

import com.yazlab.proje.model.Similarity;
import com.yazlab.proje.service.SimilarityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin("*")
@RestController
public class StepThree {

    @Autowired
    private SimilarityService similarityService;

    @PostMapping("/stepThree")
    public Similarity stepThree(@RequestParam String url1,@RequestParam String url2) throws IOException {
        return similarityService.similarity(url1,url2);
    }

}
