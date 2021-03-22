package com.yazlab.proje.service;

import com.yazlab.proje.model.Frequency;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FrequencyService {

    @Autowired
    private DocumentExtraction documentExtraction;


    public List<Frequency> frequencyCalculate(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        List<String> sentences = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            if(i<7){
                Elements elements = document.select("h"+i);
                for (Element element : elements) {
                    if(element.text().toLowerCase().trim().length() > 20){
                        String[] text = documentExtraction.replace(element.text().toLowerCase().trim()).trim().split("\\.");
                        for (int j = 0; j < text.length; j++) {
                            if(text[j].length() > 20){
                                sentences.add(text[j].trim());
                            }
                        }
                    }
                }
            }else{
                Elements elements = document.select("p");
                for (Element element : elements) {
                    if(element.text().toLowerCase().trim().length() > 20){
                        String[] text = documentExtraction.replace(element.text().toLowerCase().trim()).trim().split("\\.");
                        for (int j = 0; j < text.length; j++) {
                            if(text[j].length() > 20){
                                sentences.add(text[j].trim());
                            }
                        }
                    }
                }
            }
        }
        Integer count = 0;
        List<String> list = new ArrayList<>();
        Map<String,Integer> wordCountMap = new HashMap<>();
        for (String s:sentences) {
            String[] text = s.split(" ");
            for (int i = 0; i < text.length; i++) {
                if(wordCountMap.isEmpty() && text[i].length()>1){
                    list.add(text[i]);
                    wordCountMap.put(text[i],1);
                    count++;
                }else if(text[i].length()>1){
                    if(wordCountMap.containsKey(text[i])){
                        wordCountMap.put(text[i],wordCountMap.get(text[i])+1);
                    }else{
                        list.add(text[i]);
                        wordCountMap.put(text[i],1);
                    }
                    count++;
                }
            }
        }
        List<Frequency> frequencies = new ArrayList<>();
        for (String s:list) {
            frequencies.add(new Frequency(s,wordCountMap.get(s), (wordCountMap.get(s)*1.0)/list.size()));
        }

        frequencies = frequencies.stream().sorted(Comparator.comparingDouble(Frequency::getFrequency)
                .reversed())
                .collect(Collectors.toList());

        return frequencies;
    }

}
