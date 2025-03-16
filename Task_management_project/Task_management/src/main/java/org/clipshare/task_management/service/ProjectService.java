package org.clipshare.task_management.service;

import jakarta.persistence.EntityNotFoundException;
import org.clipshare.task_management.model.Project;
import org.clipshare.task_management.model.Task;
import org.clipshare.task_management.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Iterable<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Transactional
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    /* public Project getProjectById(Integer id) {
        return projectRepository.findById(id).get();
    }*/
    public Project getProjectById(Integer id) {
        return projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
    }

    /* public void addTaskToProject(Integer projectId, Task task) {
        Project project = projectRepository.findById(projectId).get();
           /* .orElseThrow(() -> new ResourceNotFoundException("Project non trouvé")); */ 
        
    /*    task.setProject(project);
        project.getTasks().add(task);
        projectRepository.save(project);
    } */

    /* public List<Task> getTaskById(int id) {
        if(projectRepository.existsById(id)) {
            return projectRepository.findById(id).get().getTasks();
        }
        throw new IllegalArgumentException("Project with id " + id + " does not exist");
    } */

    public void deleteProject(int id) throws Exception {
        if(projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Project with id " + id + " does not exist");
        }
    }



    


}
