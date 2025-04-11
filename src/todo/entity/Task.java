package todo.entity;

import db.Entity;
import db.Trackable;
import java.util.Date;

public class Task extends Entity implements Trackable {
    public enum Status { NotStarted, InProgress, Completed }

    private Date creationDate;
    private Date lastModificationDate;
    public String title;
    public String description;
    public Date dueDate;
    public Status status;
    
    @Override
    public Task copy() {
        Task copy = new Task();
        copy.id = this.id;
        copy.title = this.title;
        copy.description = this.description;
        copy.dueDate = this.dueDate != null ? new Date(this.dueDate.getTime()) : null;
        copy.status = this.status;
        copy.creationDate = this.creationDate != null ? new Date(this.creationDate.getTime()) : null;
        copy.lastModificationDate = this.lastModificationDate != null ? new Date(this.lastModificationDate.getTime()) : null;
        return copy;
    }

    @Override
    public int getEntityCode() {
        return 1;
    }

    // Trackable interface implementation
    @Override
    public void setCreationDate(Date date) {
        this.creationDate = date;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setLastModificationDate(Date date) {
        this.lastModificationDate = date;
    }

    @Override
    public Date getLastModificationDate() {
        return lastModificationDate;
    }
}