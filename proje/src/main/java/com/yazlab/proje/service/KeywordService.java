package com.yazlab.proje.service;

import com.yazlab.proje.model.Keyword;
import com.yazlab.proje.util.StaticVariables;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeywordService {

    public List<Keyword> keywordExtraction(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        List<String> sentences = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            if(i<7){
                Elements elements = document.select("h"+i);
                for (Element element : elements) {
                    if(element.text().toLowerCase().trim().length() > 20){
                        String[] text = replace(element.text().toLowerCase().trim()).trim().split("\\.");
                        for (int j = 0; j < text.length; j++) {
                            if(text[j].length() > 20){
                                sentences.add(stopWords(text[j].trim()));
                            }
                        }
                    }
                }
            }else{
                Elements elements = document.select("p");
                for (Element element : elements) {
                    if(element.text().toLowerCase().trim().length() > 20){
                        String[] text = replace(element.text().toLowerCase().trim()).trim().split("\\.");
                        for (int j = 0; j < text.length; j++) {
                            if(text[j].length() > 20){
                                sentences.add(stopWords(text[j].trim()));
                            }
                        }
                    }
                }
            }
        }
        List<String> priority = new ArrayList<>();
        Elements elements = document.select("h1");
        for (Element element : elements) {
            if(element.text().toLowerCase().trim().length() > 20){
                String[] text = replace(element.text().toLowerCase().trim()).trim().split("\\.");
                for (int j = 0; j < text.length; j++) {
                    if(text[j].length() > 20){
                        String[] strings = stopWords(text[j].trim()).split(" ");
                        for (int i = 0; i < strings.length; i++) {
                            priority.add(strings[i]);
                        }
                    }
                }
            }
        }
        List<String> list = new ArrayList<>();
        Map<String,Integer> wordCountMap = new HashMap<>();
        Map<String,Integer> sentencesCountMap = new HashMap<>();
        for (String s:sentences) {
            String[] text = s.split(" ");
            for (int i = 0; i < text.length; i++) {
                if(wordCountMap.isEmpty()){
                    list.add(text[i]);
                    wordCountMap.put(text[i],1);
                    sentencesCountMap.put(text[i],0);
                }else{
                    if(wordCountMap.containsKey(text[i])){
                        wordCountMap.put(text[i],wordCountMap.get(text[i])+1);
                    }else{
                        list.add(text[i]);
                        wordCountMap.put(text[i],1);
                        sentencesCountMap.put(text[i],0);
                    }
                }
            }
        }
        for (String s1:list) {
            for (String s2:sentences) {
                String[] text = s2.split(" ");
                for (int i = 0; i < text.length; i++) {
                    if(s1.equals(text[i])){
                        sentencesCountMap.put(s1,sentencesCountMap.get(s1)+1);
                    }
                }
            }
        }

        List<Keyword> keywords = new ArrayList<>();
        for (String s: list) {
            Keyword keyword = new Keyword(s, ((wordCountMap.get(s)/(list.size()*1.0))*(Math.log10(sentences.size()/sentencesCountMap.get(s)))));
            keywords.add(keyword);
        }

        keywords = keywords.stream().sorted(Comparator.comparingDouble(Keyword::getValue)
                .reversed())
                .collect(Collectors.toList());
        
        List<Keyword> keywordList = new ArrayList<>();
        for (String element : priority) {
            for (Keyword keyword:keywords) {
                if(element.equals(keyword.getName())){
                    keywordList.add(keyword);
                    break;
                }
            }
        }
        for (Keyword keyword1:keywords){
            if(keywordList.size()>19){
                break;
            }
            Boolean aBoolean = false;
            for (Keyword keyword:keywordList) {
                if (keyword.getName().equals(keyword1.getName())){
                    aBoolean = true;
                    break;
                }
            }
            if(!aBoolean){
                keywordList.add(keyword1);
            }
        }

        return keywordList;
    }

    public List<Keyword> td_idf(String text){
        text = replace(text.toLowerCase());
        String[] sentences = text.split("\\.");
        System.out.println("Sentence: "+sentences.length);
        String[] words = text.replace("\\.","").split(" ");
        System.out.println("Word: "+words.length);
        Set<String> set = new HashSet<>();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            set.add(words[i].replace('.',' ').trim());
        }
        for (String s:set) {
            if(s.length() > 3 && !StaticVariables.DELETE_WORDS.toLowerCase().equals(s)){
                list.add(s);
            }
        }
        System.out.println("1 Distinct Word: "+list.size());
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if(i < list.size() && j < list.size()){
                    if(!list.get(i).equals(list.get(j)) && list.get(i).startsWith(list.get(j))){
                        list.remove(i);
                    }
                }

            }
        }
        System.out.println("2 Distinct Word: "+list.size());
        List<Keyword> keywords = new ArrayList<>();
        for (String s: list) {
            float count = 0;
            for (int i = 0; i < sentences.length; i++) {
                if(sentences[i].contains(s)){
                    count++;
                }
            }
            if(count>0){
                Keyword keyword = new Keyword(s, ((count/list.size())*(Math.log10(sentences.length/count))));
                keywords.add(keyword);
            }
        }
        keywords = keywords.stream().sorted(Comparator.comparingDouble(Keyword::getValue)
                .reversed()).limit(20)
                .collect(Collectors.toList());

        return keywords;
    }

    public List<Keyword> brakeAlgorithm(String text){
        text = replace(text.toLowerCase());
        String[] sentences = text.split("\\.");
        System.out.println("Sentence: "+sentences.length);
        String[] words = text.split(" ");
        System.out.println("Word: "+words.length);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            if(words[i].length() > 3 && !StaticVariables.DELETE_WORDS.toLowerCase().contains(words[i].trim())){
                list.add(words[i].replace('.',' ').trim());
            }
        }
        List<String> stringList = new ArrayList<>();
        List<Integer> integerList = new ArrayList<>();
        List<Integer> integerDegreeList = new ArrayList<>();
        for (String s:list) {
            if(stringList.size()<1){
                stringList.add(s);
                integerList.add(1);
                integerDegreeList.add(0);
            }else{
                Boolean no = true;
                for (int i = 0; i < stringList.size(); i++) {
                    if(s.startsWith(stringList.get(i)) || stringList.get(i).startsWith(s)){
                        integerList.set(i,integerList.get(i)+1);
                        no = false;
                        break;
                    }
                }
                if(no){
                    stringList.add(s);
                    integerList.add(1);
                    integerDegreeList.add(0);
                }
            }
        }
        for (int i = 0; i < stringList.size(); i++) {
            for (int j = 0; j < sentences.length; j++) {
                if(sentences[j].contains(stringList.get(i))){
                    String[] sent = sentences[j].replace("\\.","").split(" ");
                    if(sent.length<20){
                        for (int k = 0; k < sent.length; k++) {
                            if(sent[k].contains(stringList.get(i))){
                                break;
                            }
                            integerDegreeList.set(i,integerDegreeList.get(i)+1);
                        }
                    }
                }
            }
        }
        List<Keyword> keywords = new ArrayList<>();
        for (int i = 0; i < stringList.size(); i++) {
            System.out.println(stringList.get(i)+"-"+integerList.get(i)+"-"+integerDegreeList.get(i));
            if(integerDegreeList.get(i) == 0){
                integerDegreeList.set(i,1);
            }
            keywords.add(new Keyword(stringList.get(i),(integerDegreeList.get(i)*1.0)/(integerList.get(i)*1.0)));
        }
        keywords = keywords.stream().sorted(Comparator.comparingDouble(Keyword::getValue)
                .reversed()).limit(10)
                .collect(Collectors.toList());
        return keywords;

    }

    private String replace(String text){
        text = text.toLowerCase();
        text = text.replace("\n","");
        String str = "/*-+,;:'%&{[()]}-_?=!#£<|>~@`\"1234567890▲▼–";
        for (int i = 0;i<str.length();i++){
            text = text.replace(str.charAt(i), ' ');
        }
        return text;
    }

    private String stopWords(String text){
        String[] stopWords = StaticVariables.DELETE_WORDS.split(" ");
        String[] textWords = text.split(" ");
        for (int i = 0; i < textWords.length; i++) {
            for (int j = 0; j < stopWords.length; j++) {
                if(textWords[i].length()<3){
                    textWords[i] = "";
                    break;
                }
                if(textWords[i].equals(stopWords[j])){
                    textWords[i] = "";
                    break;
                }
            }
        }
        text = "";
        for (int i = 0; i < textWords.length; i++) {
            text = text + " " + textWords[i];
            text = text.trim();
        }
        return text.trim();
    }

}