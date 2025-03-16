package org.clipshare.task_management.service;

import jakarta.persistence.EntityNotFoundException;
import org.clipshare.task_management.model.Project;
import org.clipshare.task_management.model.Task;
import org.clipshare.task_management.repository.ProjectRepository;
import org.clipshare.task_management.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Iterable<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Integer id) {
        if(taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Task with id " + id + " does not exist");
        }
    }

    public Task completeTask(Integer id) {
        Task task = taskRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Task with id " + id + " does not exist"));

        task.setCompleted(true);
        // Task updatedTask = taskRepository.save(task);

        // update the progress bar
        // updateProjectProgress(updatedTask.getProject());
        return taskRepository.save(task);
    }

    public int calculateProgress(Integer projectId) {
        Integer completed = taskRepository.countByProjectAndCompleted(projectId, true);
        Integer total = taskRepository.countByProject(projectId);
        return ((completed * 100) / total);
    }

    /*
    private void updateProjectProgress(Project project) {
        // 1. Compter les tâches complétées
        long completedTasks = taskRepository.countByProjectAndCompleted(project, true);
        long totalTasks = taskRepository.countByProject(project);

        // 2. Calculer la progression
        int progress = calculateProgress(completedTasks, totalTasks);

        // 3. Mettre à jour le projet
        project.setProgress(progress);
        projectRepository.save(project);
    }

    private int calculateProgress(long completed, long total) {
        if (total == 0) return 0;
        return (int) Math.round((completed * 100.0) / total);
    }
     */
    /*
    public int calculateProjectProgress(Integer projectId) {
        long completed = taskRepository.countByProjectIdAndCompleted(projectId, true);
        long total = taskRepository.countByProjectId(projectId);
        return (int) ((completed * 100) / total);
    }
     */

    /*
    public Task completeTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

        task.setCompleted(true);
        Task updatedTask = taskRepository.save(task);

        // Mettre à jour la progression du projet
        Project project = updatedTask.getProject();
        updateProjectProgress(project);

        return updatedTask;
    }

    private void updateProjectProgress(Project project) {
        List<Task> tasks = taskRepository.findByProject(project);
        long completedCount = tasks.stream().filter(Task::isCompleted).count();

        int progress = tasks.isEmpty() ? 0 : (int) ((completedCount * 100) / tasks.size());
        project.setProgress(progress);

        projectRepository.save(project);
    }
     */

    /*
    * public Task completeTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

        task.setCompleted(true);
        Task updatedTask = taskRepository.save(task);

        // Mettre à jour la progression du projet
        Project project = task.getProject();
        int completedTasks = taskRepository.countByProjectAndCompleted(project, true);
        int totalTasks = taskRepository.countByProject(project);
        int progress = (int) ((completedTasks * 100.0) / totalTasks);

        project.setProgress(progress);
        projectService.updateProject(project);

        return updatedTask;
    } */

    /*
    public void deleteProject(int id) throws Exception {
        if(projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Project with id " + id + " does not exist");
        }
    }
     */

    public Task getTaskById(Integer id) {
        return taskRepository.findById(id).orElse(null);
    }
}
