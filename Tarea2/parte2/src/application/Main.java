package application;

import datamodel.Actor;
import datamodel.DataModelManager;
import persistence.PersistenceManager;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // usoDelMetodoGetActorPrevioDataModelManager();

        // Parte 2. Actividad 11. Llamada de los metodos definidos en la actividad 10.
        DataModelManager dataModelManager = new DataModelManager("jdbc:mysql://localhost:3306/sakila", "root", "root");
        List<Actor> allActors = dataModelManager.getAllActors();
        System.out.println("Todos los actores");
        for (Actor actor : allActors) {
            System.out.println(actor);
        }
        List<Actor> actorsFirstNameStartE = dataModelManager.getActorsFirstNameStartE();
        System.out.println("Actores cuyo nombre empieza por E");
        for (Actor actor : actorsFirstNameStartE) {
            System.out.println(actor);
        }
    }

    private static void usoDelMetodoGetActorPrevioDataModelManager() {
        PersistenceManager persistenceManager = new PersistenceManager("jdbc:mysql://localhost:3306/sakila", "root", "root");
        // usoDelMetodoGetActorPrevioAlPatronSingleton(persistenceManager);

        // Patrón singleton para la creación de nuevas conexiones. Uso del metodo getActors actualizado con open() y close()
        List<Actor> actorsFirstNameStartE = persistenceManager.getActor("first_name LIKE 'E%'");
        System.out.println("Actores cuyo nombre empieza por E");
        for (Actor actor : actorsFirstNameStartE) {
            System.out.println(actor);
        }
    }

    private static void usoDelMetodoGetActorPrevioAlPatronSingleton(PersistenceManager persistenceManager) {
        // Capa de aplicación. Mostrar la información de cada actor
        List<Actor> actorsWithoutCondition = persistenceManager.getActorWithoutCondition();
        System.out.println("Todos los actores");
        for (Actor actor : actorsWithoutCondition) {
            System.out.println(actor);
        }
        // Parte 2. Actividad 5 y Actividad 6. Metodo getActors con condiciones
        List<Actor> actorsFirstNameStartE = persistenceManager.getActorWithCondition("first_name LIKE 'E%'");
        System.out.println("Actores cuyo nombre empieza por E");
        for (Actor actor : actorsFirstNameStartE) {
            System.out.println(actor);
        }
        List<Actor> actorsLasNameStartS = persistenceManager.getActorWithCondition("last_name LIKE 'S%'");
        System.out.println("Actores cuyo apellido empieza por S");
        for (Actor actor : actorsLasNameStartS) {
            System.out.println(actor);
        }
        List<Actor> actorsFirstNameEndN = persistenceManager.getActorWithCondition("first_name LIKE '%N'");
        System.out.println("Actores cuyo nombre acaba en N");
        for (Actor actor : actorsFirstNameEndN) {
            System.out.println(actor);
        }
    }
}