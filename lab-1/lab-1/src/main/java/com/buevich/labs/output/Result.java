package com.buevich.labs.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class Result {
    public static void result(Set<String> types, String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (String type : types) {
                writer.write(type == null ? "" : type);
                writer.newLine();
            }
            System.out.println("Результат сохранен в: " + outputPath);
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}


