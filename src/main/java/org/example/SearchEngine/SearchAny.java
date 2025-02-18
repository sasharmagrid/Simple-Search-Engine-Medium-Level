package org.example.SearchEngine;

import java.util.ArrayList;
import java.util.HashSet;

public class SearchAny implements SearchInterface {
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
        int numberOfPeople = indexes.size();
        System.out.println(numberOfPeople + " persons found:");
        for (int index : indexes) {
            System.out.println(search.mp.get(index));
        }
        System.out.println();
    }
}

