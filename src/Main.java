import db.Database;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Task;
import todo.entity.Step;
import todo.service.TaskService;
import todo.service.StepService;
import todo.validator.TaskValidator;
import todo.validator.StepValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {

        Database.registerValidator(1, new TaskValidator());
        Database.registerValidator(2, new StepValidator());

        System.out.println("To-Do List Application");
        printHelp();

        while (true) {
            System.out.print("\nEnter command: ");
            String command = scanner.nextLine().trim().toLowerCase();

            try {
                switch (command) {
                    case "add task":
                        handleAddTask();
                        break;
                    case "add step":
                        handleAddStep();
                        break;
                    case "delete":
                        handleDelete();
                        break;
                    case "update task":
                        handleUpdateTask();
                        break;
                    case "update step":
                        handleUpdateStep();
                        break;
                    case "get task-by-id":
                        handleGetTaskById();
                        break;
                    case "get all-tasks":
                        handleGetAllTasks();
                        break;
                    case "get incomplete-tasks":
                        handleGetIncompleteTasks();
                        break;
                    case "help":
                        printHelp();
                        break;
                    case "exit":
                        System.out.println("Exiting application...");
                        return;
                    default:
                        System.out.println("Invalid command. Type 'help' for available commands.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void printHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("  add task       - Add a new task");
        System.out.println("  add step       - Add a new step to a task");
        System.out.println("  delete         - Delete a task or step");
        System.out.println("  update task    - Update a task");
        System.out.println("  update step    - Update a step");
        System.out.println("  get task-by-id - View a specific task");
        System.out.println("  get all-tasks  - View all tasks");
        System.out.println("  get incomplete-tasks - View incomplete tasks");
        System.out.println("  help           - Show this help message");
        System.out.println("  exit           - Exit the application");
    }

    private static void handleAddTask() throws ParseException, InvalidEntityException {
        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Due date (yyyy-MM-dd): ");
        Date dueDate = dateFormat.parse(scanner.nextLine());

        TaskService.createTask(title, description, dueDate);
        System.out.println("Task saved successfully.");
    }

    private static void handleAddStep() throws InvalidEntityException, EntityNotFoundException {
        System.out.print("Task ID: ");
        int taskId = Integer.parseInt(scanner.nextLine());

        System.out.print("Step Title: ");
        String title = scanner.nextLine();

        StepService.createStep(taskId, title);
        System.out.println("Step saved successfully.");
    }

    private static void handleDelete() throws EntityNotFoundException {
        System.out.print("ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            Task task = (Task) Database.get(id);
            TaskService.deleteTask(id);
            System.out.println("Task and its steps deleted successfully.");
        } catch (EntityNotFoundException e) {

            Database.delete(id);
            System.out.println("Step deleted successfully.");
        }
    }

    private static void handleUpdateTask() throws EntityNotFoundException, InvalidEntityException {
        System.out.print("Task ID: ");
        int taskId = Integer.parseInt(scanner.nextLine());

        System.out.print("Field to update (title/description/dueDate/status): ");
        String field = scanner.nextLine();

        System.out.print("New Value: ");
        String value = scanner.nextLine();

        Task task = TaskService.getTask(taskId);
        Task oldTask = task.copy();

        switch (field.toLowerCase()) {
            case "title":
                task.title = value;
                break;
            case "description":
                task.description = value;
                break;
            case "duedate":
                try {
                    task.dueDate = dateFormat.parse(value);
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd");
                }
                break;
            case "status":
                task.status = Task.Status.valueOf(value);
                if (task.status == Task.Status.Completed) {
                    TaskService.setAsCompleted(taskId);
                    return;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid field name");
        }

        Database.update(task);
        System.out.println("Successfully updated the task.");
        System.out.println("Field: " + field);
        System.out.println("Old Value: " + getFieldValue(oldTask, field));
        System.out.println("New Value: " + value);
    }

    private static void handleUpdateStep() throws EntityNotFoundException, InvalidEntityException {
        System.out.print("Step ID: ");
        int stepId = Integer.parseInt(scanner.nextLine());

        System.out.print("Field to update (title/status): ");
        String field = scanner.nextLine();

        System.out.print("New Value: ");
        String value = scanner.nextLine();

        Step step = (Step) Database.get(stepId);
        Step oldStep = step.copy();

        switch (field.toLowerCase()) {
            case "title":
                step.title = value;
                break;
            case "status":
                step.status = Step.Status.valueOf(value);
                if (step.status == Step.Status.Completed) {
                    StepService.completeStep(stepId);
                    return;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid field name");
        }

        Database.update(step);
        System.out.println("Successfully updated the step.");
        System.out.println("Field: " + field);
        System.out.println("Old Value: " + getFieldValue(oldStep, field));
        System.out.println("New Value: " + value);
    }

    private static void handleGetTaskById() throws EntityNotFoundException {
        System.out.print("Task ID: ");
        int taskId = Integer.parseInt(scanner.nextLine());

        Task task = TaskService.getTask(taskId);
        printTaskDetails(task);
    }

    private static void handleGetAllTasks() {
        List<Task> tasks = TaskService.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        for (Task task : tasks) {
            printTaskDetails(task);
            System.out.println();
        }
    }

    private static void handleGetIncompleteTasks() {
        List<Task> tasks = TaskService.getIncompleteTasks();
        if (tasks.isEmpty()) {
            System.out.println("No incomplete tasks found.");
            return;
        }

        for (Task task : tasks) {
            printTaskDetails(task);
            System.out.println();
        }
    }

    private static void printTaskDetails(Task task) {
        System.out.println("ID: " + task.id);
        System.out.println("Title: " + task.title);
        System.out.println("Description: " + task.description);
        System.out.println("Due Date: " + dateFormat.format(task.dueDate));
        System.out.println("Status: " + task.status);
        System.out.println("Created: " + task.getCreationDate());
        System.out.println("Last Modified: " + task.getLastModificationDate());

        List<Step> steps = StepService.getStepsForTask(task.id);
        if (!steps.isEmpty()) {
            System.out.println("Steps:");
            for (Step step : steps) {
                System.out.println("  + " + step.title + ":");
                System.out.println("    ID: " + step.id);
                System.out.println("    Status: " + step.status);
            }
        }
    }

    private static String getFieldValue(Task task, String field) {
        switch (field.toLowerCase()) {
            case "title": return task.title;
            case "description": return task.description;
            case "duedate": return dateFormat.format(task.dueDate);
            case "status": return task.status.toString();
            default: return "";
        }
    }

    private static String getFieldValue(Step step, String field) {
        switch (field.toLowerCase()) {
            case "title": return step.title;
            case "status": return step.status.toString();
            default: return "";
        }
    }
}