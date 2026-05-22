package com.buevich.labs.parse;

import com.buevich.labs.entity.character.Character;

public class csvParser {
    public static Character parseLine(String line) {
        if (line == null) return null;

        String[] field = line.split(",");

        Character character = new Character();
        String status = field[2].trim();
        character.setStatus(status);
        return character;
    }
}
