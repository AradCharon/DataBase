package todo.service;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Task;
import todo.entity.Step;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskService {
    public static void createTask(String title, String description, Date dueDate)
            throws InvalidEntityException {
        Task task = new Task();
        task.title = title;
        task.description = description;
        task.dueDate = dueDate;
        task.status = Task.Status.NotStarted;
        Database.add(task);
    }

    public static void setAsCompleted(int taskId)
            throws EntityNotFoundException, InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.status = Task.Status.Completed;
        Database.update(task);

        for (Entity entity : Database.getAll(2)) {
            Step step = (Step) entity;
            if (step.taskRef == taskId && step.status != Step.Status.Completed) {
                step.status = Step.Status.Completed;
                Database.update(step);
            }
        }
    }

    public static List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Entity entity : Database.getAll(2)) {
            tasks.add((Task) entity);
        }
        tasks.sort((t1, t2) -> t1.dueDate.compareTo(t2.dueDate));
        return tasks;
    }

    public static List<Task> getIncompleteTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Entity entity : Database.getAll(2)) {
            Task task = (Task) entity;
            if (task.status != Task.Status.Completed) {
                tasks.add(task);
            }
        }
        tasks.sort((t1, t2) -> t1.dueDate.compareTo(t2.dueDate));
        return tasks;
    }

    public static Task getTask(int taskId) throws EntityNotFoundException {
        return (Task) Database.get(taskId);
    }

    public static void deleteTask(int taskId) throws EntityNotFoundException {
        for (Entity entity : Database.getAll(2)) {
            Step step = (Step) entity;
            if (step.taskRef == taskId) {
                Database.delete(step.id);
            }
        }

        Database.delete(taskId);
    }
}