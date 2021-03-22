package com.yazlab.proje.controller;

import com.yazlab.proje.model.Keyword;
import com.yazlab.proje.service.KeywordService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
public class StepTwo {

    @Autowired
    private KeywordService operation;

    @PostMapping("/stepTwoTD-IDF")
    public List<Keyword> stepTwoTDIDF(@RequestParam String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return operation.td_idf(document.text());
    }

    @PostMapping("/stepTwoBrake")
    public List<Keyword> stepTwoBrake(@RequestParam String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return operation.brakeAlgorithm(document.text());
    }

    @PostMapping("/stepTwo")
    public List<Keyword> keywordExtraction(@RequestParam String url) throws IOException {
        return operation.keywordExtraction(url);
    }

}
