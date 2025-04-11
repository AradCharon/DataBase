package todo.service;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;
import java.util.ArrayList;
import java.util.List;

public class StepService {
    public static void createStep(int taskRef, String title)
            throws InvalidEntityException, EntityNotFoundException {
        Step step = new Step();
        step.title = title;
        step.status = Step.Status.NotStarted;
        step.taskRef = taskRef;
        Database.add(step);

        Task task = (Task) Database.get(taskRef);
        if (task.status == Task.Status.NotStarted) {
            task.status = Task.Status.InProgress;
            Database.update(task);
        }
    }

    public static void completeStep(int stepId)
            throws EntityNotFoundException, InvalidEntityException {
        Step step = (Step) Database.get(stepId);
        step.status = Step.Status.Completed;
        Database.update(step);

        Task task = (Task) Database.get(step.taskRef);
        boolean allCompleted = true;
        for (Entity entity : Database.getAll(2)) {
            Step s = (Step) entity;
            if (s.taskRef == task.id && s.status != Step.Status.Completed) {
                allCompleted = false;
                break;
            }
        }

        if (allCompleted) {
            task.status = Task.Status.Completed;
        } else {
            task.status = Task.Status.InProgress;
        }
        Database.update(task);
    }

    public static List<Step> getStepsForTask(int taskId) {
        List<Step> steps = new ArrayList<>();
        for (Entity entity : Database.getAll(2)) {
            Step step = (Step) entity;
            if (step.taskRef == taskId) {
                steps.add(step);
            }
        }
        return steps;
    }
}