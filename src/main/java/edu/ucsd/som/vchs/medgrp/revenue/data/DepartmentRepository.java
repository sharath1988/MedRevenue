package edu.ucsd.som.vchs.medgrp.revenue.data;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.MaxResults;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import edu.ucsd.som.vchs.medgrp.revenue.model.Department;

@Repository
public interface DepartmentRepository extends EntityRepository<Department, String> {
	@Query("select d from Department d where d.departmentName like concat(?1,'%')")
	List<Department> findByDepartmentNameLike(String query, @MaxResults Integer maxResults);	
}
