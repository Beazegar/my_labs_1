package com.buevich.labs;

import com.buevich.labs.entity.character.Character;
import com.buevich.labs.treeset.processing.FindUnique;
import com.buevich.labs.read.read;
import com.buevich.labs.crud.Crud;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Main {

    private static final String inputFile = "lab-1/src/main/resources/characters.csv";
    private static final String outputFile = "lab-1/src/main/resources/new_characters.csv";

    public static void main(String[] args) {

        List<Character> allCharacters = readFrom();
        if (allCharacters == null || allCharacters.isEmpty()) {
            System.err.println("Пустой файл");
            return;
        }
        allCharacters.getFirst().hashCode();

        Set<String> uniqueStatuses = FindUnique.findUnique(allCharacters);

        System.out.println("Уникальные status:");
        for (String status : uniqueStatuses) {
            System.out.println(status);
        }

        // Сохранение в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String type : uniqueStatuses) {
                writer.write(type);
                writer.newLine();
            }
            System.out.println("\nРезультат сохранён в " + outputFile);
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }

        // CRUD

        Crud crudService = new Crud(inputFile);

        // CREATE
        Character newChar = new Character();
        newChar.setId(77);
        newChar.setName("Alya");
        newChar.setStatus("Alive");
        newChar.setSpecies("Human");
        newChar.setType("NO INFO");
        newChar.setGender("Female");
        newChar.setOriginName("Earth (C-137)");
        newChar.setLocationName("New York");
        newChar.setCreated(LocalDateTime.now());
        crudService.create(newChar);

        // READ
        Character found = crudService.findById(77);
        if (found != null) {
            System.out.println("Найден персонаж: " + found.getName() + " (ID: " + found.getId() + ")");
        } else {
            System.out.println("Персонаж с этим ID не найден");
        }

        // UPDATE
        Character updatedChar = new Character();
        updatedChar.setName("Morty Jr. Updated");
        updatedChar.setStatus("Dead");
        updatedChar.setSpecies("Alien");
        updatedChar.setType("Cyborg");
        updatedChar.setGender("Male");
        updatedChar.setOriginName("Earth (C-137)");
        updatedChar.setLocationName("Citadel of New Jersey");
        updatedChar.setCreated(LocalDateTime.now());
        crudService.update(77, updatedChar);

        Character afterUpdate = crudService.findById(77);
        if (afterUpdate != null) System.out.println("После обновления: " + afterUpdate.getName() + ", статус: " + afterUpdate.getStatus());

        // READ ALL
        List<Character> all = crudService.findAll();
        System.out.println("Всего персонажей: " + all.size());

        // DELETE
        crudService.delete(77);
        Character afterDelete = crudService.findById(77);
        if (afterDelete == null) System.out.println("Персонаж с указанным ID успешно удалён");
    }

    private static List<Character> readFrom() {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            return read.Read(br);
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
            return null;
        }
    }
}