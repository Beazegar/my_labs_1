package com.buevich.labs.read;

import com.buevich.labs.entity.character.Character;
import com.buevich.labs.parse.csvParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class read {
    public static List<Character> Read(BufferedReader reader) {
        List<Character> allCharacters = new ArrayList<>(); // пустой список для всего файла
        String line;
        boolean firstLine = true;

        try {
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                Character character = csvParser.parseLine(line); // парсинг строки
                if (character != null) allCharacters.add(character); // добавляем в общую кашу после проверки в парс
            }
        } catch (IOException e) {
            System.err.println("Просчитались, и вот где: " + e.getMessage());
        }
        return allCharacters;
    }
}
