package org.arb_tech.web.service;

import java.util.List;
import java.util.Optional;

import org.arb_tech.web.dao.IProjectRepo;
import org.arb_tech.web.entity.Project;
import org.arb_tech.web.exception.ProjectPortalException;
import org.arb_tech.web.vo.ProjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * service layer which contains the business logic for projects
 * @author Aniket.Bharsakale
 */
@Service
public class ProjectServiceImpl implements IProjectService {

	@Autowired
	private IProjectRepo projectRepo;

	public List<Project> getAllProjects() {
		return projectRepo.findAll();
	}

	@Override
	public String createProject(ProjectVO projectVO) throws ProjectPortalException {
		Project project = null;
		if (null != projectVO) {
			project = new Project();
			project.setName(projectVO.getName());
			project.setResourceStrength(projectVO.getResourceStrength());
			project.setStartDate(projectVO.getStartDate());
			project.setEndDate(projectVO.getEndDate());
			project.setProjectCode(projectVO.getProjectCode());
			project = projectRepo.save(project);
		} else {
			throw new ProjectPortalException("Project to be created cannot be null.");
		}

		if (null != project)
			return "Project: " + project.getName() + " created & saved in database.";
		else
			return "Project not created in database.";
	}

	@Override
	public ProjectVO getProjectByIdOrCode(Integer projectId, String projectCode) throws ProjectPortalException {
		ProjectVO projectVO = null;
		
		// If we receive projectId from UI
		if (null != projectId) {
			Optional<Project> object = projectRepo.findById(projectId);

			if (object.isPresent()) {
				projectVO = new ProjectVO();
				projectVO.setName(object.get().getName());
				projectVO.setResourceStrength(object.get().getResourceStrength());
				projectVO.setStartDate(object.get().getStartDate());
				projectVO.setEndDate(object.get().getEndDate());
				projectVO.setProjectCode(object.get().getProjectCode());
				return projectVO;
			} else {
				throw new ProjectPortalException("Project not found in database with project id: "+projectId);
			}
		
		// if we receive projectCode from UI
		} else if(null != projectCode && !projectCode.isEmpty()) {
		
			Project project = projectRepo.getProjectByProjectCode(projectCode);
			if(null != project) {
				projectVO = new ProjectVO();
				projectVO.setName(project.getName());
				projectVO.setResourceStrength(project.getResourceStrength());
				projectVO.setStartDate(project.getStartDate());
				projectVO.setEndDate(project.getEndDate());
				projectVO.setProjectCode(project.getProjectCode());
				return projectVO;
			} else {
				throw new ProjectPortalException("Project with proejct code:" + projectCode + " not found in database.");
			}
		} else {
			throw new ProjectPortalException("Project ID and ProjectCode both received as NULL.");
		}
	}

	@Override
	public String updateProjectById(Integer projectId, ProjectVO projectVO) throws ProjectPortalException {
		if (null != projectId && null != projectVO) {
			Optional<Project> object = projectRepo.findById(projectId);
			if (object.isPresent()) {
				Project project = object.get();
				if (null != projectVO.getName())
					project.setName(projectVO.getName());
				if (null != projectVO.getStartDate())
					project.setStartDate(projectVO.getStartDate());
				if (null != projectVO.getEndDate())
					project.setEndDate(projectVO.getEndDate());
				if (null != projectVO.getResourceStrength())
					project.setResourceStrength(projectVO.getResourceStrength());
				if(null != projectVO.getProjectCode())
					project.setProjectCode(projectVO.getProjectCode());					
				
				projectRepo.save(project);
				
				return "Project Info. updated successfully in database.";
			} else
				return "Project with ID:" + projectId + " is not present/is empty in database. ";
		} else {
			throw new ProjectPortalException("ProjecId or the Project tobe updated cannot be NULL.");
		}
	}

	@Override
	public String deleteProjectById(Integer projectId) throws ProjectPortalException {
		if (null != projectId) {
			Optional<Project> object = projectRepo.findById(projectId);
			if (object.isPresent()) {
				projectRepo.delete(object.get());
				return "Project: " + object.get().getName() + "has been deleted from database.";
			} else {
				return "Any project with an ID:" + projectId + " not found in database.";
			}
		} else {
			throw new ProjectPortalException("Project ID cannot be null.");
		}
	}

	@Override
	public Project getProjectByCode(String projectCode) throws ProjectPortalException {
		if(null != projectCode && !projectCode.isEmpty()) {
			Project project = projectRepo.getProjectByProjectCode(projectCode);
			if(null != project) {
				return project;
			} else {
				throw new ProjectPortalException("Project with proejct code:" + projectCode + " not found in database.");
			}
		} else throw new ProjectPortalException("Project Code cannot be null or empty");
	}
}