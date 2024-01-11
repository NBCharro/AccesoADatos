package application;

import datamodel.Actor;
import datamodel.DataModelManager;
import datamodel.Film;
import datamodel.Rating;
import utils.MyConnection;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        DataModelManager dataModelManager = new DataModelManager("jdbc:mysql://localhost:3306/sakila", "root", "root");

        DataModelManager dataModelManager = new DataModelManager();

//        actividad3(dataModelManager);
//        actividad4(dataModelManager);
//        actividad6(dataModelManager);

        List<Actor> actors = dataModelManager.getActorByName("ED");
        for (Actor actor : actors) {
            System.out.println(actor.getActor_id());
        }

    }

    private static void actividad3(DataModelManager dataModelManager) {
        Actor actor = new Actor("Antonio", "Lopez");
        Boolean actorGuardadoDB = dataModelManager.newActor(actor);
        System.out.println("");
        System.out.println("Actividad 3. Implementa un método que te permita incluir un nuevo Actor en la base de datos");
        if (actorGuardadoDB) {
            System.out.println("Se ha insertado el actor");
        } else {
            System.out.println("No se ha podido insertar el actor");
        }
    }

    private static void actividad4(DataModelManager dataModelManager) {
        Film film = new Film("Tibuton", "Pelicula tiburon", (short) 1975, (byte) 1, (Integer) null, (byte) 6, 6.0, (short) 90, 21, "G", "Deleted Scenes");
        Boolean filmGuardadoDB = dataModelManager.newFilm(film);
        System.out.println("");
        System.out.println("Actividad 4. Implementa un método que te permita incluir una nueva pelicula en la base de datos");
        if (filmGuardadoDB) {
            System.out.println("Se ha insertado la pelicula");
        } else {
            System.out.println("No se ha podido insertar la pelicula");
        }
    }

    private static void actividad6(DataModelManager dataModelManager) {
        Actor actor = new Actor("Antonio", "Lopez");
        Film film = new Film("Tiburon", "Pelicula tiburon", (short) 1975, (byte) 1, (Integer) null, (byte) 6, 6.0, (short) 90, 21, "G", "Deleted Scenes");
        Film film2 = new Film("Tiburon2", "Pelicula tiburon 2", (short) 1975, (byte) 1, (Integer) null, (byte) 6, 6.0, (short) 90, 21, "G", "Deleted Scenes");
        List<Film> arrayfilm = new ArrayList<>();
        arrayfilm.add(film);
        arrayfilm.add(film2);
        actor.setFilms(arrayfilm);
        Boolean actorGuardadoDB = dataModelManager.newActor(actor);
        System.out.println("");
        System.out.println("Actividad 6. Modifica el método newActor de forma que se introduzcan los valores del nuevo\n" +
                "Actor y de todas sus Film en las que haya participado");
        if (actorGuardadoDB) {
            System.out.println("Se ha insertado el actor y sus peliculas");
        } else {
            System.out.println("No se ha podido insertar el actor ni sus peliculas");
        }
    }
}