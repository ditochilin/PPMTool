package com.myppm.ppmtool.services;

import com.myppm.ppmtool.domain.Backlog;
import com.myppm.ppmtool.domain.Project;
import com.myppm.ppmtool.domain.ProjectTask;
import com.myppm.ppmtool.exceptions.ProjectException;
import com.myppm.ppmtool.exceptions.ProjectNotFoundException;
import com.myppm.ppmtool.exceptions.ProjectNotFoundExceptionResponse;
import com.myppm.ppmtool.repositories.BacklogRepository;
import com.myppm.ppmtool.repositories.ProjectRepository;
import com.myppm.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
        try {
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            projectTask.setBacklog(backlog);
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);

            // format : sdfjs-1, sdfjs-2...
            projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            // initial priority
            if (projectTask.getPriority() == null) {
                projectTask.setPriority(3);
            }

            // initial status
            if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);
        } catch(Exception ex){
            throw new ProjectNotFoundException("Project not found");
        }
    }

    public Iterable<ProjectTask> findBacklogById(String id) {

        Project project = projectRepository.findByProjectIdentifier(id);
        if(project==null){
            throw new ProjectNotFoundException("Project with ID '"+id+"' does not exist.");
        }

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String sequence){
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if(backlog==null){
            throw new ProjectNotFoundException("Project with ID '"+backlog_id+"' does not exist.");
        }
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(sequence);
        if(projectTask==null){
            throw new ProjectNotFoundException("Project Task '"+sequence+"' not found.");
        }
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '"+sequence+"' does not exist in project: '"+backlog_id+"'");
        }

        return projectTask;
    }
}
