package org.clipshare.task_management.repository;

import org.clipshare.task_management.model.Project;
import org.clipshare.task_management.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {
    // int findTaskByAssigneeTaskIsTrueAndCompleted(Integer id,boolean completed);
    //int countCompletedTasks(Project project, Boolean completed);
    //
    // int countByProjectAndCompleted(Integer projectId, boolean completed);
    //
    // int countByProject(Project project);
    // Integer countByProjectAndCompleted(Integer projectId, boolean completed);
    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.project_id = :projectId AND t.completed = :completed")
    Integer countByProjectAndCompleted(@Param("projectId") Integer projectId,
                                         @Param("completed") boolean completed);
    // Integer countByProject(Project project);
    @Query("SELECT COUNT(*) FROM Task t WHERE t.project.project_id = :projectId")
    Integer countByProject(Integer projectId);
    // Integer countByProject(Integer projectId);



}
