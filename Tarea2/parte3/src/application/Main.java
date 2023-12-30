package application;

import datamodel.Actor;
import datamodel.DataModelManager;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataModelManager dataModelManager = new DataModelManager("jdbc:mysql://localhost:3306/sakila", "root", "root");
//        List<Actor> allActors = dataModelManager.getAllActors();
//        System.out.println("Todos los actores");
//        for (Actor actor : allActors) {
//            System.out.println(actor);
//        }
//        List<Actor> actorsFirstNameStartE = dataModelManager.getActorsFirstNameStartE();
//        System.out.println("Actores cuyo nombre empieza por E");
//        for (Actor actor : actorsFirstNameStartE) {
//            System.out.println(actor);
//        }
        // PArte 3. Actividad 5. Implementar un metodo que dado el ID de un actor obtenga sus peliculas
        List<String> peliculas = dataModelManager.getFilmsForActor(2);
        for (String pelicula : peliculas) {
            System.out.println(pelicula);
        }

    }
}