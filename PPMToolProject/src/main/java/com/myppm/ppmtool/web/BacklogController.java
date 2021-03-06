package com.myppm.ppmtool.web;

import com.myppm.ppmtool.domain.ProjectTask;
import com.myppm.ppmtool.services.MapValidationErrorService;
import com.myppm.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addPTtoBacklog(@Valid  @RequestBody ProjectTask projectTask,
                                            BindingResult result, @PathVariable String backlog_id){
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidation(result);
        if(errorMap != null){
            return errorMap;
        }

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id, projectTask);
        new ArrayList<ProjectTask>();
        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlog_id}")
    public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlog_id){
        return projectTaskService.findBacklogById(backlog_id);
    }

    @GetMapping("/{backlog_id}/{task_id}")
    public ResponseEntity<?> getPTByProjectSequence(@PathVariable String backlog_id,
                                                    @PathVariable String task_id){
        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlog_id, task_id);
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{backlog_id}/{task_id}")
    public ResponseEntity<?> updateByProjectSequence(@Valid  @RequestBody ProjectTask updatedTask,
                                               BindingResult result,
                                                @PathVariable String backlog_id,
                                                @PathVariable String task_id){
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidation(result);
        if(errorMap != null){
            return errorMap;
        }

        ProjectTask newProjectTask = projectTaskService.updateByProjectSequence(updatedTask, backlog_id, task_id);

        return new ResponseEntity<ProjectTask>(newProjectTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlog_id}/{task_id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String task_id){
        projectTaskService.deletePTByProjectSequence(backlog_id, task_id);
        return new ResponseEntity<String>("Project Task "+task_id+" was deleted successfully", HttpStatus.OK);
    }
}
