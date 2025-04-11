import db.Database;
import db.exception.InvalidEntityException;
import example.Document;
import example.Human;
import db.exception.EntityNotFoundException;
import example.HumanValidator;

public class Main {
    public static void main(String[] args) throws InvalidEntityException {
        System.setProperty("user.timezone", "GMT+03:30");

        Document doc = new Document("Eid Eid Eid");

        Database.add(doc);

        System.out.println("Document added");
        System.out.println("id: " + doc.id);
        System.out.println("content: " + doc.content);
        System.out.println("creation date: " + doc.getCreationDate());
        System.out.println("last modification date: " + doc.getLastModificationDate());
        System.out.println();

        try {
            System.out.println("Waiting for 30 seconds...");
            Thread.sleep(30_000);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted!");
        }

        doc.content = "This is the new content";

        Database.update(doc);

        System.out.println("Document updated");
        System.out.println("id: " + doc.id);
        System.out.println("content: " + doc.content);
        System.out.println("creation date: " + doc.getCreationDate());
        System.out.println("last modification date: " + doc.getLastModificationDate());
    }
}