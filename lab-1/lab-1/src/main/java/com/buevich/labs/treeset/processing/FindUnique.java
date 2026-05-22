package com.buevich.labs.treeset.processing;

import com.buevich.labs.entity.character.Character;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class FindUnique {
    public static Set<String> findUnique(List<Character> characters){
        Set<String> unique = new TreeSet<>();
        for (Character i: characters) {
            String uniqueStatus =i.getStatus();
            unique.add(uniqueStatus);
        }
        return unique;
    }
}
