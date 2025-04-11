package example;

import db.Entity;
import db.Trackable;
import java.util.Date;

public class Document extends Entity implements Trackable {
    public String content;
    private Date creationDate;
    private Date lastModificationDate;

    public Document(String content) {
        this.content = content;
    }

    @Override
    public Document copy() {
        Document copy = new Document(this.content);
        copy.id = this.id;
        copy.creationDate = this.creationDate != null ? new Date(this.creationDate.getTime()) : null;
        copy.lastModificationDate = this.lastModificationDate != null ? new Date(this.lastModificationDate.getTime()) : null;
        return copy;
    }

    @Override
    public int getEntityCode() {
        return 15;
    }

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