package com.yazlab.proje.service;

import com.yazlab.proje.dto.StepFourDto;
import com.yazlab.proje.model.*;
import com.yazlab.proje.model.deep.LevelOne;
import com.yazlab.proje.model.deep.LevelThree;
import com.yazlab.proje.model.deep.LevelTwo;
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
public class DeepExtraction {

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private SimilarityService similarityService;

    public Deep deepSimilarity(StepFourDto stepFourDto) throws IOException {
        Deep deep = new Deep();
        deep.setUrl(stepFourDto.getUrl());
        List<Keyword> keywords = keywordService.keywordExtraction(stepFourDto.getUrl());
        deep.setKeywords(keywords);
        List<DeepSimilarity> deepSimilarities = new ArrayList<>();
        for (String url2:stepFourDto.getUrlList()) {
            Document document = Jsoup.connect(url2).get();
            Elements links = document.select("a[href]");
            Set<String> urls = new HashSet<>();
            for (Element link : links) {
                if(link.attr("abs:href").startsWith(url2+"/")){
                    String url = link.attr("abs:href").replace("#","").replace("index.html","").replace("../","");
                    urls.add(url);
                    document = null;
                    try{
                        document = Jsoup.connect(link.attr("abs:href").replace("#","").replace("index.html","").replace("../","")).get();
                    }catch (Exception e){

                    }
                    if(document != null){
                        Elements twoLinks = document.select("a[href]");
                        for (Element element:twoLinks) {
                            if(element.attr("abs:href").startsWith(url2+"/")){
                                url = element.attr("abs:href").replace("#","").replace("index.html","").replace("../","");
                                urls.add(url);
                            }
                        }
                    }
                }
            }
            List<String> list = new ArrayList<>(urls);
            list.sort( Comparator.comparing( String::toString ));
            LevelOne levelOne = new LevelOne();
            levelOne.setUri(url2);
            levelOne.setKeywords(keywordService.keywordExtraction(url2));
            List<LevelTwo> levelTwoList = new ArrayList<>();
            List<LevelThree> levelThreeList = new ArrayList<>();
            for (String s:list) {
                Integer integer = s.replace(url2,"").split("/").length;
                if(integer == 2){
                    try {
                        LevelTwo levelTwo = new LevelTwo();
                        levelTwo.setUri(s);
                        levelTwo.setKeywords(keywordService.keywordExtraction(s));
                        levelTwoList.add(levelTwo);
                        System.out.println(s.replace(url2,""));
                    }catch (Exception e){
                        System.out.println(s.replace(url2,"")+" - Error");
                    }
                }else if(integer == 3){
                    try {
                        LevelThree levelThree = new LevelThree();
                        levelThree.setUri(s);
                        levelThree.setKeywords(keywordService.keywordExtraction(s));
                        levelThreeList.add(levelThree);
                        System.out.println(s.replace(url2,""));
                    }catch (Exception e){
                        System.out.println(s.replace(url2,"")+" - Error");
                    }
                }
            }
            for (LevelThree levelThree:levelThreeList) {
                Boolean no = true;
                for (LevelTwo levelTwo:levelTwoList) {
                    if(levelThree.getUri().startsWith(levelTwo.getUri())){
                        no = false;
                        break;
                    }
                }
                if(no){
                    LevelTwo levelTwo = new LevelTwo();
                    levelTwo.setUri(url2+"/"+levelThree.getUri().replace(url2,"").split("/")[0]+levelThree.getUri().replace(url2,"").split("/")[1]);
                    levelTwoList.add(levelTwo);
                }
            }
            for (LevelTwo levelTwo:levelTwoList) {
                List<LevelThree> threes = new ArrayList<>();
                for (LevelThree levelThree:levelThreeList) {
                    if(levelThree.getUri().startsWith(levelTwo.getUri())){
                        threes.add(levelThree);
                    }
                }
                levelTwo.setLevelThreeList(threes);
            }
            levelOne.setLevelTwoList(levelTwoList);
            DeepSimilarity deepSimilarity = new DeepSimilarity(similarityCalculate(keywords,levelOne),levelOne);
            deepSimilarities.add(deepSimilarity);
        }
        deepSimilarities = deepSimilarities.stream().sorted(Comparator.comparingDouble(DeepSimilarity::getScore)
                .reversed())
                .collect(Collectors.toList());

        deep.setDeepSimilarities(deepSimilarities);
        return deep;
    }

    private Double similarityCalculate(List<Keyword> keywords,LevelOne levelOne){
        Integer levelOneCount,levelTwoCount = 0,levelThreeCount = 0;
        levelOneCount = similarityService.keywordSimilarity(keywords,levelOne.getKeywords());
        for (LevelTwo levelTwo:levelOne.getLevelTwoList()) {
            if(levelTwo.getKeywords().size()>0){
                levelTwoCount = levelTwoCount + similarityService.keywordSimilarity(keywords,levelTwo.getKeywords());
            }
            for (LevelThree levelThree:levelTwo.getLevelThreeList()) {
                if(levelThree.getKeywords().size()>0){
                    levelThreeCount = levelThreeCount + similarityService.keywordSimilarity(keywords,levelThree.getKeywords());
                }
            }
        }
        return (levelOneCount*0.5+levelTwoCount*0.3+levelThreeCount*0.2)*1;
    }

    public LevelOne test(String url1, String url2) throws IOException {
        LevelOne levelOne = new LevelOne();
        levelOne.setUri(url2);
        List<LevelTwo> levelTwoList = new ArrayList<>();
        Document doc = Jsoup.connect(url2).get();
        levelOne.setKeywords(keywordService.keywordExtraction(url2));
        Elements links = doc.select("a[href]");
        Set<String> urls = new HashSet<>();
        for (Element link : links) {
            if(link.attr("abs:href").startsWith(url2+"/") && link.attr("abs:href").replace(url2,"").split("/").length == 2){
                urls.add(link.attr("abs:href"));
            }
        }
        for (String s:urls){
            Document twoDocument = null;
            try {
                twoDocument = Jsoup.connect(s).get();
            }catch (Exception e){
            }
            if(twoDocument != null){
                LevelTwo levelTwo = new LevelTwo();
                levelTwo.setUri(s);
                levelTwo.setKeywords(keywordService.keywordExtraction(s));
                List<LevelThree> levelThreeList = new ArrayList<>();
                Elements twoLinks = twoDocument.select("a[href]");
                for (Element link : twoLinks) {
                    if(link.attr("abs:href").startsWith(s) && link.attr("abs:href").replace(s,"").split("/").length == 2){
                        Document threeDocument = null;
                        try {
                            threeDocument = Jsoup.connect(link.attr("abs:href")).get();
                        }catch (Exception e){
                        }
                        if(threeDocument != null){
                            LevelThree levelThree = new LevelThree();
                            levelThree.setUri(link.attr("abs:href"));
                            levelThree.setKeywords(keywordService.keywordExtraction(link.attr("abs:href")));
                            levelThreeList.add(levelThree);
                        }
                    }
                }
                levelTwo.setLevelThreeList(levelThreeList);
                levelTwoList.add(levelTwo);
            }
        }
        levelOne.setLevelTwoList(levelTwoList);
        /*
        System.out.println("1. "+levelOne.getUri());
        for (LevelTwo levelTwo:levelOne.getLevelTwoList()) {
            System.out.println("2. "+levelTwo.getUri());
            for (LevelThree levelThree:levelTwo.getLevelThreeList()) {
                System.out.println("3. "+levelThree.getUri());
            }
        }*/
        return levelOne;
    }
}
