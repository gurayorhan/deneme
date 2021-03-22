package com.yazlab.proje.service;

import com.yazlab.proje.util.StaticVariables;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentExtraction{

    public List<String> getH(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        List<String> priority = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            Elements elements = document.select("h"+i);
            for (Element element : elements) {
                if(element.text().toLowerCase().trim().length() > 20){
                    String[] text = replace(element.text().toLowerCase().trim()).trim().split("\\.");
                    for (int j = 0; j < text.length; j++) {
                        if(text[j].length() > 20){
                            String[] strings = stopWords(text[j].trim()).split(" ");
                            for (int k = 0; k < strings.length; k++) {
                                priority.add(strings[k]);
                            }
                        }
                    }
                }
            }
        }
        return priority;
    }

    public List<String> getP(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        List<String> words = new ArrayList<>();
        Elements elements = document.select("p");
        for (Element element : elements) {
            if(element.text().toLowerCase().trim().length() > 20){
                String[] text = replace(element.text().toLowerCase().trim()).trim().split("\\.");
                for (int j = 0; j < text.length; j++) {
                    if(text[j].length() > 20){
                        String[] strings = stopWords(text[j].trim()).split(" ");
                        for (int k = 0; k < strings.length; k++) {
                            words.add(strings[k]);
                        }
                    }
                }
            }
        }
        return words;
    }

    public String replace(String text){
        text = text.toLowerCase();
        text = text.replace("\n","");
        String str = "/*-+,;:'%&{[()]}-_?=!#£<|>~@`\"1234567890▲▼–";
        for (int i = 0;i<str.length();i++){
            text = text.replace(str.charAt(i), ' ');
        }
        return text;
    }

    public String stopWords(String text){
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
