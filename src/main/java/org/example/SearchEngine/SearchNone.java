package org.example.SearchEngine;

import java.util.ArrayList;
import java.util.HashSet;

public class SearchNone implements SearchInterface {
    @Override
    public void search(String query, Search search) {
        String[] words = query.toLowerCase().split(" ");
        HashSet<Integer> indexes = new HashSet<>();
        for (String word : words) {
            ArrayList<Integer> index = search.wordMap.getOrDefault(word, null);
            if (index != null) {
                indexes.addAll(index);
            }
        }
        int n = search.mp.size();
        int numberOfPeople = n - indexes.size();
        System.out.println(numberOfPeople + " persons found:");
        for (java.util.Map.Entry<Integer, String> index : search.mp.entrySet()) {
            if (!indexes.contains(index.getKey())) System.out.println(index.getValue());
        }
        System.out.println();
    }
}

