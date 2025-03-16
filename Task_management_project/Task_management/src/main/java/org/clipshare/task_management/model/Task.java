package org.clipshare.task_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Status is required")
    private String status;
    @NotBlank(message = "Priority is required")
    private String priority;
    @NotBlank(message = "Deadline is required")
    private String deadline;
    /* @NotNull
    private Integer totalTasks; */
    private Boolean completed = false;
    private Integer assigneeTask = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    /* @NotBlank(message = "Created at is required")
    private String created_at;

    private Integer user_id; */

    /* public Stack<String> getAllTasks() {
        Stack<String> tasks = new Stack<>();
        tasks.add("Développement web ");
        tasks.add("Programmation Java");
        tasks.add("Programmation Python");
        return tasks;
    } */
}





