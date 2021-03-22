package com.yazlab.proje.service;

import com.yazlab.proje.model.Frequency;
import com.yazlab.proje.model.Keyword;
import com.yazlab.proje.model.Similarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SimilarityService {

    @Autowired
    private DocumentExtraction documentExtraction;

    @Autowired
    private FrequencyService frequencyService;

    @Autowired
    private KeywordService keywordService;

    public Similarity similarity(String url1,String url2) throws IOException {
        List<Keyword> keywordList1 = keywordService.keywordExtraction(url1);
        List<Keyword> keywordList2 = keywordService.keywordExtraction(url2);
        List<Frequency> frequencyList1 = frequencyService.frequencyCalculate(url1);
        List<Frequency> frequencyList2 = frequencyService.frequencyCalculate(url2);
        List<String> h1 = documentExtraction.getH(url1);
        List<String> h2 = documentExtraction.getH(url2);
        List<String> p1 = documentExtraction.getP(url1);
        List<String> p2 = documentExtraction.getP(url2);
        Integer keywordSimilarity = keywordSimilarity(keywordList1,keywordList2);
        Integer titleSimilarity = titleSimilarity(h1,h2);
        Integer similarityOfKeywordsToTitle = similarityOfKeywordsToTitle(keywordList1,keywordList2,h1,h2);
        Integer similarityOfKeywordsToText = SimilarityOfKeywordsToText(keywordList1,keywordList2,p1,p2);
        Double aDouble = keywordSimilarity*0.35+similarityOfKeywordsToTitle*0.3+titleSimilarity*0.2+similarityOfKeywordsToText*0.15;
        return new Similarity(url1,url2,aDouble,keywordList1,frequencyList1,keywordList2,frequencyList2);
    }

    public Integer keywordSimilarity(List<Keyword> keywordList1,List<Keyword> keywordList2){
        Integer count = 0;
        for (Keyword keyword1:keywordList1) {
            for (Keyword keyword2:keywordList2) {
                if(keyword1.getName().equals(keyword2.getName())){
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    public Integer titleSimilarity(List<String> h1,List<String> h2){
        Integer count = 0;
        for (String s1:h1) {
            for (String s2:h2) {
                if(s1.equals(s2)){
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    public Integer similarityOfKeywordsToTitle(List<Keyword> keywordList1,List<Keyword> keywordList2,List<String> h1,List<String> h2){
        Set<String> set = new HashSet<>();
        for (Keyword keyword:keywordList1) {
            for (String s:h1) {
                if(keyword.getName().equals(s)){
                    set.add(keyword.getName());
                    break;
                }
            }
        }
        for (Keyword keyword:keywordList2) {
            for (String s:h2) {
                if(keyword.getName().equals(s)){
                    set.add(keyword.getName());
                    break;
                }
            }
        }
        return set.size();
    }

    public Integer SimilarityOfKeywordsToText(List<Keyword> keywordList1,List<Keyword> keywordList2,List<String> p1,List<String> p2){
        Set<String> set = new HashSet<>();
        for (Keyword keyword:keywordList1) {
            for (String s:p1) {
                if(keyword.getName().equals(s)){
                    set.add(keyword.getName());
                    break;
                }
            }
        }
        for (Keyword keyword:keywordList2) {
            for (String s:p2) {
                if(keyword.getName().equals(s)){
                    set.add(keyword.getName());
                    break;
                }
            }
        }
        return set.size();
    }

}
