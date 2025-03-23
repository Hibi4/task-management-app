package org.clipshare.task_management.web;

import jakarta.persistence.EntityNotFoundException;
import org.clipshare.task_management.service.ProjectService;

import org.clipshare.task_management.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.clipshare.task_management.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private TaskService taskService;

    @GetMapping("/projects")
    public String projects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "projects";
    }

    @PostMapping("/addProject")
    public ResponseEntity<String> addProject(@RequestBody Project project) {
        projectService.saveProject(project);
        return ResponseEntity.ok("Project added successfully");
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTask(@RequestBody Task task) {
        // task.setId(task.getProject().getProject_id());
        taskService.saveTask(task);
        return ResponseEntity.ok("Task added successfully");
    }

    @GetMapping("/task/getTaskById/{id}")
    public String getTaskById(@PathVariable Integer id, Model model) {
        Project project = projectService.getProjectById(id);
        if(project == null) {
            throw new NullPointerException("project is null");
            // throw new ResourceNotFoundException("Project non trouvé");
        }
        model.addAttribute("project", project); // Changement de "details" à "project"
        model.addAttribute("tasks", project.getTasks()); // Séparation des tâches
        return "project-details";
    }

    @PostMapping("/add/{projectId}")
    public ResponseEntity<String> addProject(@PathVariable Integer projectId, @RequestBody Task taskRequest) {
        try {
            Project project = projectService.getProjectById(projectId);
            if (project == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project non trouvé");
            }
            Task newTask = new Task();
            newTask.setTitle(taskRequest.getTitle());
            newTask.setDescription(taskRequest.getDescription());
            newTask.setStatus(taskRequest.getStatus());
            newTask.setPriority(taskRequest.getPriority());
            newTask.setDeadline(taskRequest.getDeadline());
            newTask.setProject(project);
            taskService.saveTask(newTask);
            return ResponseEntity.ok("Tâche ajoutée avec succès");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) {
            // Pour les autres erreurs 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur interne est survenue: " + e.getMessage());
        }
    }

    // why /deleteProject/{id}
    @DeleteMapping("/deleteProject/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Integer id) throws Exception {
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project deleted successfully");
    }

    @DeleteMapping("/deleteTask/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id) throws Exception {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }

    @PostMapping("/projects/{projectId}/update-deadline")
    public String updateProjectDeadline(@PathVariable Integer projectId,
                                        @RequestParam("newDeadline") String newDeadlin,
                                        Model model) { // Use @RequestParam instead of @PathVariable to receive the date as a form parameter.
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate newDeadline = LocalDate.parse(newDeadlin, dateFormatter);
            if(newDeadline.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("La date ne peut pas être dans le passé");
            }
            // Project updatedProject = projectService.updateDeadline(projectId, newDeadline.toString());
            projectService.updateDeadline(projectId, newDeadline.toString());
            return "redirect:/projects"; // Redirect on success

        } catch (DateTimeParseException e) {
            model.addAttribute("errorMessage", "Invalid date format. Please use yyyy-MM-dd.");
            return "error";  // Return the view where the error message can be displayed.
        }
        catch(Exception e){
            model.addAttribute("errorMessage", "An error occurred while updating deadline.");
            return "error";
        }

    }

    @PutMapping("/tasks/{taskId}/complete")
    public ResponseEntity<Map<String, Object>> completeTask(@PathVariable Integer taskId) {
        try {
            Task task = taskService.completeTask(taskId);
            int total = taskService.calculateProgress(task.getProject().getProject_id());
            task.getProject().setProgress(total);

            projectService.saveProject(task.getProject());

            Map<String, Object> response = new HashMap<>();
            response.put("completed", true);
            response.put("projectId", task.getProject().getProject_id());
            response.put("projectProgress", task.getProject().getProgress());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /*
Task task = taskService.completeTask(id);
            Integer projectId = task.getProject().getId();

            // Calculer et mettre à jour la progression
            int progress = taskService.calculateProjectProgress(projectId);
            Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

            project.setProgress(progress);
            projectRepository.save(project);

            return ResponseEntity.ok(Map.of(
                "taskId", id,
                "projectId", projectId,
                "progress", progress
            ));
     */
    /* @PutMapping("/tasks/{taskId}/complete")
    public ResponseEntity<Map<String, Object>> completeTask(@PathVariable Integer taskId) {
        try {
            Task task = taskService.completeTask(taskId);
            Map<String, Object> response = new HashMap<>();
            response.put("completed", true);
            response.put("projectId", task.getProject().getProject_id());
            response.put("projectProgress", task.getProject().getProgress());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    } */

    /*
    @PutMapping("/{taskId}/complete")
    public ResponseEntity<Map<String, Object>> completeTask(@PathVariable Long taskId) {
        Task updatedTask = taskService.completeTask(taskId);
        Project project = updatedTask.getProject();

        Map<String, Object> response = new HashMap<>();
        response.put("completed", updatedTask.isCompleted());
        response.put("taskId", updatedTask.getId());
        response.put("projectId", project.getProject_id());
        response.put("projectProgress", project.getProgress());

        return ResponseEntity.ok(response);
    } */



    /* @PostMapping("/addProject/{id}")
    public String addNewProject(@PathVariable Integer id) {
        Project project = projectService.getProjectById(id);
        if(project != null) {
            project.setProject_id(project.getProject_id());
            project.setTitle(project.getTitle());
            project.setDescription(project.getDescription());
            project.setDeadline(project.getDeadline());
            project.setTasks(project.getTasks());
            projectService.saveProject(project);
        }
        // return ResponseEntity.ok("Project added successfully");
        return "redirect:/projects";
    } */

    /* @PostMapping("/add/{id}")
    public String addNewTask(@PathVariable Integer id, @RequestBody Task task) {
        Project project = projectService.getProjectById(id);
        if(project != null) {
            Task newTask = new Task();
            newTask.setId(id);
            newTask.setTitle(task.getTitle());
            newTask.setDescription(task.getDescription());
            newTask.setStatus(task.getStatus());
            newTask.setPriority(task.getPriority());
            newTask.setDeadline(task.getDeadline());
            project.getTasks().add(newTask); // add the new task
            projectService.saveProject(project);
        }
        // return "redirect:/project-details"; // Redirect back to the books page
        return "redirect:/task/getTaskById/"+id;
        // /task/getTaskById/{id}
    } */


    // @PostMapping("/projects/{projectId}/tasks")
    /* @PostMapping("/add/{id}")
    public String addTaskToProject(
            @PathVariable Integer projectId,
            @RequestBody Task taskRequest) { // Utilisation de @RequestBody au lieu de @ModelAttribute

        Project project = projectService.getProjectById(projectId);
        if (project == null) {
           // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projet non trouvé");
        }
        // Création d'une nouvelle tâche avec les données reçues
        Task newTask = new Task();
        newTask.setTitle(taskRequest.getTitle());
        newTask.setDescription(taskRequest.getDescription());
        newTask.setStatus(taskRequest.getStatus());
        newTask.setPriority(taskRequest.getPriority());
        newTask.setDeadline(taskRequest.getDeadline());
        newTask.setProject(project); // Liaison au projet

        taskService.saveTask(newTask); // Utilisation du service de tâche

        return ResponseEntity.ok("Tâche ajoutée avec succès").toString();

    } */
        /* Project project = projectService.getProjectById(projectId);
        if(project != null) {
            task.setProject(project); // Liaison bidirectionnelle
            project.getTasks().add(task);
            projectService.saveProject(project);
        }
        return "redirect:/task/getTaskById/" + projectId; */


    /*
    // Handle adding a new book for an author
    @PostMapping("/{id}/books/add")
    public String addBookToAuthor(@PathVariable Long id, @RequestParam String title) {
        Author author = authorService.getAuthorById(id);
        if (author != null) {
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            author.getBooks().add(book); // Add the book to the author's list of books
            authorService.saveAuthor(author); // Save the updated author entity
        }
        return "redirect:/authors/" + id + "/books"; // Redirect back to the books page
    }
     */

    /* @GetMapping("/task/getTaskById/{id}")
    public List<Task> getTaskById(@PathVariable Integer id) {
        return projectService.getTaskById(id);
    } */ 

    /* @GetMapping("/task/getTaskById/{id}")
    public String getTaskById(@PathVariable Integer id, Model model) {
        // return projectService.getTaskById(id);
        model.addAttribute("details", projectService.getTaskById(id));
        return "project-details";
    } */

}
