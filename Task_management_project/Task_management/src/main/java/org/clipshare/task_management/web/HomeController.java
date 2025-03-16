package org.clipshare.task_management.web;

import org.clipshare.task_management.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.clipshare.task_management.model.Task;

@Controller
public class HomeController {
    @Autowired
    private TaskService taskService;

    /* @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "home";
    } */ 

    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("settings", taskService.getAllTasks());
        return "settings";
    }

    @GetMapping("/calendar")
    public String calendar(Model model) {
        model.addAttribute("calendar", taskService.getAllTasks());
        return "calendar";
    }

    /* @PostMapping("/add")
    public ResponseEntity<String> addTask(@RequestBody Task task) {
        // task.setId(task.getProject().getProject_id());
        taskService.saveTask(task);
        return ResponseEntity.ok("Task added successfully");
    } */





}
