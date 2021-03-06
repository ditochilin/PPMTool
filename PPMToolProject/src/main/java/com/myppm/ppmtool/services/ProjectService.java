package com.myppm.ppmtool.services;

import com.myppm.ppmtool.domain.Backlog;
import com.myppm.ppmtool.domain.Project;
import com.myppm.ppmtool.exceptions.ProjectException;
import com.myppm.ppmtool.repositories.BacklogRepository;
import com.myppm.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project){
        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if(project.getId()==null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId()!=null){
                project.setBacklog(
                        backlogRepository.findByProjectIdentifier(
                                project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);
        } catch (Exception e){
            throw new ProjectException("Project ID: "+project.getProjectIdentifier()
                    +" already exists!");
        }
    }

    public Project findProjectByIdentifier(String projectId){

        Project project = projectRepository.findByProjectIdentifier(projectId);
        if(project==null){
            throw new ProjectException("Project ID: "+projectId
                    +" does not exist!");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId){
        Project project = findProjectByIdentifier(projectId);
        projectRepository.delete(project);
    }
}
