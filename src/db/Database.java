package db;

import db.exception.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

public final class Database {
    private static final List<Entity> entities = new ArrayList<>();
    private static int nextId = 0;

    private Database() {}

    public static void add(Entity e) {
        Entity copy = e.copy();
        copy.id = nextId++;
        entities.add(copy);
    }

    public static Entity get(int id) throws EntityNotFoundException {
        for (Entity e : entities) {
            if (e.id == id) {
                return e.copy();
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) throws EntityNotFoundException {
        Entity toRemove = null;
        for (Entity e : entities) {
            if (e.id == id) {
                toRemove = e;
                break;
            }
        }

        if (toRemove == null) {
            throw new EntityNotFoundException(id);
        }

        entities.remove(toRemove);
    }

    public static void update(Entity e) throws EntityNotFoundException {
        Entity copy = e.copy();
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == copy.id) {
                entities.set(i, copy);
                return;
            }
        }
        throw new EntityNotFoundException(copy.id);
    }
}