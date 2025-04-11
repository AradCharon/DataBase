package db;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Database {
    private static final List<Entity> entities = new ArrayList<>();
    private static int nextId = 0;
    private static final Map<Integer, Validator> validators = new HashMap<>();

    private Database() {}

    public static void registerValidator(int entityCode, Validator validator) {
        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator for entity code " + entityCode + " already exists");
        }
        validators.put(entityCode, validator);
    }

    public static void add(Entity e) throws InvalidEntityException {
        validateEntity(e);
        Entity copy = e.copy();
        copy.id = nextId++;

        if (copy instanceof Trackable) {
            Trackable trackable = (Trackable) copy;
            Date now = new Date();
            trackable.setCreationDate(now);
            trackable.setLastModificationDate(now);
        }

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

    public static void update(Entity e) throws EntityNotFoundException, InvalidEntityException {
        validateEntity(e);
        Entity copy = e.copy();

        if (copy instanceof Trackable) {
            Trackable trackable = (Trackable) copy;
            trackable.setLastModificationDate(new Date());
        }

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == copy.id) {
                entities.set(i, copy);
                return;
            }
        }
        throw new EntityNotFoundException(copy.id);
    }

    private static void validateEntity(Entity entity) throws InvalidEntityException {
        Validator validator = validators.get(entity.getEntityCode());
        if (validator != null) {
            validator.validate(entity);
        }
    }
}