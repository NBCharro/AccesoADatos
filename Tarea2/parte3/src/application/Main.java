package application;

import datamodel.Actor;
import datamodel.DataModelManager;
import datamodel.Film;
import datamodel.Rating;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataModelManager dataModelManager = new DataModelManager("jdbc:mysql://localhost:3306/sakila", "root", "root");

        actividad8(dataModelManager);

        actividad9(dataModelManager);
        actividad10(dataModelManager);
        actividad11(dataModelManager);
        actividad12(dataModelManager);

        actividad13(dataModelManager);
        actividad14(dataModelManager);
        actividad15(dataModelManager);
        actividad16(dataModelManager);
        actividad17(dataModelManager);

        actividad19(dataModelManager);
        actividad20(dataModelManager);

    }

    private static void actividad8(DataModelManager dataModelManager) {
        // Parte 3. Actividad 8. Llamar al metodo getActorByName con el nombre "ED".
        List<Actor> actors = dataModelManager.getActorByName("ED");
        System.out.println("");
        System.out.println("Actividad 8. Actores que se llaman ED");
        for (Actor actor : actors) {
            System.out.println(actor);
        }
    }

    private static void actividad9(DataModelManager dataModelManager) {
        List<Film> FilmsByKeyword = dataModelManager.getFilmsByKeyword("DINOSAUR");
        System.out.println("");
        System.out.println("Actividad 9. Peliculas que contengan una palabra en su titulo");
        for (Film film : FilmsByKeyword) {
            System.out.println(film);
        }
    }

    private static void actividad10(DataModelManager dataModelManager) {
        List<Film> FilmsByKeyword2 = dataModelManager.getFilmsByRatingAndCost(Rating.G, 3);
        System.out.println("");
        System.out.println("Actividad 10. Peliculas con un rating dado y un replacement_cost superrior al dado");
        for (Film film : FilmsByKeyword2) {
            System.out.println(film);
        }
    }

    private static void actividad11(DataModelManager dataModelManager) {
        List<Actor> actorsByAverageRentalRate = dataModelManager.getActorsByAverageRentalRate(3.5);
        System.out.println("");
        System.out.println("Actividad 11. Actores con un rental_rate superior al valor dado");
        for (Actor actor : actorsByAverageRentalRate) {
            System.out.println(actor);
        }
    }

    private static void actividad12(DataModelManager dataModelManager) {
        Actor actorWithHighestAverageRentalRate = dataModelManager.getActorWithHighestAverageRentalRate();
        System.out.println("");
        System.out.println("Actividad 12. Actor cuyas peliculas, de media, tiene un mayor rental_rate");
        System.out.println(actorWithHighestAverageRentalRate);
    }

    private static void actividad13(DataModelManager dataModelManager) {
        int actorId = 3;
        List<String> actors = dataModelManager.getFilmsForActor(actorId);
        boolean existsActor = !actors.isEmpty();
        System.out.println("");
        System.out.println("Actividad 13. Utilizando los metodos definidos hasta ahora, determina si existe un actor mediante un ID");
        if (existsActor) {
            System.out.println("Existe al menos un actor cuyo ID es " + actorId);
        } else {
            System.out.println("No existe ningun actor cuyo ID es " + actorId);
        }
    }

    private static void actividad14(DataModelManager dataModelManager) {
        // Parte 3. Actividad 14. Utilizando el método getActorByName, determina si existe al menos un Actor cuyo nombre sea NICK
        String nombreActor = "NICK";
        List<Actor> actors = dataModelManager.getActorByName(nombreActor);
        boolean existsActor = !actors.isEmpty();
        System.out.println("");
        System.out.println("Actividad 14. Utilizando el método getActorByName, determina si existe al menos un Actor cuyo nombre sea NICK");
        if (existsActor) {
            System.out.println("Existe al menos un actor cuyo nombre es " + nombreActor);
        } else {
            System.out.println("No existe ningun actor cuyo nombre es " + nombreActor);
        }
    }

    private static void actividad15(DataModelManager dataModelManager) {
        int actorId = 3;
        boolean existsActor = dataModelManager.existsActor(actorId);
        System.out.println("");
        System.out.println("Actividad 15. Metodo que permite comprobar si existe un actor mediante un ID");
        if (existsActor) {
            System.out.println("Existe al menos un actor cuyo ID es " + actorId);
        } else {
            System.out.println("No existe ningun actor cuyo ID es " + actorId);
        }
    }

    private static void actividad16(DataModelManager dataModelManager) {
        int filmId = 3;
        boolean existsFilm = dataModelManager.existsFilm(filmId);
        System.out.println("");
        System.out.println("Actividad 16. Metodo que permite comprobar si existe una pelicula mediante un ID");
        if (existsFilm) {
            System.out.println("Existe al menos una pelicula cuyo ID es " + filmId);
        } else {
            System.out.println("No existe ninguna pelicula cuyo ID es " + filmId);
        }
    }

    private static void actividad17(DataModelManager dataModelManager) {
        int filmId = 3;
        int actorId = 5;
        boolean existsFilm = dataModelManager.existsById(filmId, "film_id", "film");
        boolean existsActor = dataModelManager.existsById(actorId, "actor_id", "actor");
        System.out.println("");
        System.out.println("Actividad 17. Metodo generico que permite saber si existe una pelicula o un actor");
        if (existsFilm) {
            System.out.println("Existe al menos una pelicula cuyo ID es " + filmId);
        } else {
            System.out.println("No existe ninguna pelicula cuyo ID es " + filmId);
        }
        if (existsActor) {
            System.out.println("Existe al menos un actor cuyo ID es " + actorId);
        } else {
            System.out.println("No existe ningun actor cuyo ID es " + actorId);
        }
    }

    private static void actividad19(DataModelManager dataModelManager) {
        List<Film> filmsByKeyword = dataModelManager.getFilmsByKeyword("DINOSAUR");
        System.out.println("");
        System.out.println("Actividad 19. Mostrar el lenguaje original de la película.");
        System.out.println(filmsByKeyword.getFirst());
        System.out.println("Original Languaje ID: " + filmsByKeyword.getFirst().getOriginalLanguageId());
    }

    private static void actividad20(DataModelManager dataModelManager) {
        List<Film> FilmsByKeyword2 = dataModelManager.getFilmsByRatingAndCost(Rating.G, 3);
        System.out.println("");
        System.out.println("Actividad 20. Asegúrate que los valores que almacenas en los objetos de las clases Film y Actor son los que realmente están en la base de datos");
        for (Film film : FilmsByKeyword2) {
            System.out.println(film);
        }
    }
}