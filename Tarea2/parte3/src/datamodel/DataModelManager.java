package datamodel;

import persistence.PersistenceManager;

import java.util.List;

public class DataModelManager {
    private PersistenceManager persistenceManager;

    public DataModelManager(String urlDB, String user, String pass) {
        persistenceManager = new PersistenceManager(urlDB, user, pass);
    }

//    // Parte 2. Actividad 10. Metodos para recuperar todos los actores.
//    public List<Actor> getAllActors() {
//        // No he entendido el enunciado, he supuesto que se tendria que crear un metodo en PersistenceManager que devuelva todos los actores.
//        // Yo lo tenia creado de antes y lo he reutilizado, porque estoy dejando el codigo generado previamente pero si no fuese el caso hubiera creado el metodo.
//        return persistenceManager.getActorWithoutCondition();
//    }
//
//    // Parte 2. Actividad 10. Metodos para recuperar los actores cuyo nombre empieza por E.
//    public List<Actor> getActorsFirstNameStartE() {
//        return persistenceManager.getActor("first_name LIKE 'E%'");
//    }

    public List<String> getFilmsForActor(int actor_id) {
        return persistenceManager.getFilmsForActor(actor_id);
    }
}
