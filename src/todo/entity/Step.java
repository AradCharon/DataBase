package todo.entity;

import db.Entity;
import java.util.Date;

public class Step extends Entity {
    public enum Status { NotStarted, Completed }

    public String title;
    public Status status;
    public int taskRef;

    @Override
    public Step copy() {
        Step copy = new Step();
        copy.id = this.id;
        copy.title = this.title;
        copy.status = this.status;
        copy.taskRef = this.taskRef;
        return copy;
    }

    @Override
    public int getEntityCode() {
        return 2; // Unique code for Step
    }
}