package com.buevich.labs;


import com.buevich.labs.database.entity.Team;
import com.buevich.labs.database.factory.ConnectionFactory;
import com.buevich.labs.database.repository.TeamRepository;
import com.buevich.labs.database.repository.TeamRepositoryImpl;
import com.buevich.labs.database.errors.LabotoryRuntimeException;

public class Main {
    public static void main(String[] args) throws LabotoryRuntimeException {
        ConnectionFactory factory = new ConnectionFactory(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "postgres",
                "public"
        );

        try {
            TeamRepository repository = new TeamRepositoryImpl(factory, "public", "teams");

            // 1. Save (Create)
            System.out.println("=== SAVE ===");
            Team team1 = new Team("Команда 1", 100);
            Team team2 = new Team("Команда 2", 85);
            int id1 = repository.save(team1);
            int id2 = repository.save(team2);
            System.out.println("Команда сохранена, : " + id1);
            System.out.println("Команда сохранена: " + id2);

            // 2. FindById (Read)
            System.out.println("\n=== Поиск по ID ===");
            Team found = repository.findById(id1);
            System.out.println("Found: " + found);

            // 3. FindByName (Read by second field)
            System.out.println("\n=== Поиск по названию ===");
            Team foundByName = repository.findByName("Tigers");
            System.out.println("Found by name: " + foundByName);

            // 4. FindAll
            System.out.println("\n=== Общий поиск ===");
            System.out.println("Все команды: " + repository.findAll());

            // 5. Update
            System.out.println("\n=== Обновление ===");
            Team updatedTeam = new Team(id1, "Супер пупер команда 1", 150);
            boolean updated = repository.update(updatedTeam);
            System.out.println("Успешно обновлено: " + updated);
//            System.out.println("Новая запись: " + repository.findById(id1));

            System.out.println("\n=== Удаление ===");
            repository.deleteById(id2);
            System.out.println("После удаления: " + repository.findAll());

            System.out.println("\n=== ИДЕМПОТЕНТНОСТЬ ===");
            System.out.println("findById(999) returns: " + repository.findById(999)); // null
            System.out.println("несуществующей записи: " + repository.update(new Team(999, "фЛузеры", 0))); // false

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}