package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
	drop table tblLJMRI_TRVU_meta
	/
	-- populate metadata for table
	create table tblLJMRI_TRVU_meta as
	select distinct concat(dv.somDivisionId, '-', capType) as id, t.indx, dv.somDivisionId as divisionId, dv.somDivisionName as division, dv.somDepartmentName as department, dv.somMajorGroupId as somMajorGroupId, dv.somMajorGroupName as somMajorGroup, capType
	from tblLJMRI_TRVU t
	join somMgDivisionView dv on dv.somDivisionId = t.divisionId --or dv.somDivisionId in (247, 248, 249)
	where (dv.somDivisionId not in (107) or indx is not null)
	
	union all
	
	select distinct concat(dv.somDivisionId, '-', 'COMMERCIAL') as id, 
	    (CASE 
	        WHEN dv.somDivisionId = 101 then 'MSCRD09'
	        WHEN dv.somDivisionId = 247 then 'MSCRCTB'
	        WHEN dv.somDivisionId = 248 then 'MSCROMB'
	        WHEN dv.somDivisionId = 249 then 'MSCRILB' 
	    END) as indx,
	    dv.somDivisionId as divisionId, dv.somDivisionName as division, dv.somDepartmentName as department, dv.somMajorGroupId as somMajorGroupId, dv.somMajorGroupName as somMajorGroup, 'COMMERCIAL' as capType
	from tblLJMRI_TRVU t
	join somMgDivisionView dv on dv.somDivisionId in (101, 247, 248, 249)
	order by divisionId
 *
 * Questions:
 * 	1) What capTypes do we need for the new divisions
 * 	2) We also need to add 2 new divisions - submitted to Glenn on 6/28
 * 
 * @author myebba
 *
 */
@Entity
@Table(name = "tblLJMRI_TRVU_meta", catalog = "mgfs")
public class LjmriTrvuMeta implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	private String id;
	
	@Column(name="indx")
	private String indx;
			
	@Column(name="department")
	private String department;
	
	@Column(name="division")
	private String division;
	
	@Column(name="divisionId", columnDefinition="smallint")
	private Integer divisionId;
	
	@Column(name="somMajorGroup")
	private String somMajorGroup;
	
	@Column(name="somMajorGroupId")
	private Integer somMajorGroupId;
	
	@Column(name="capType")
	private String capType;	

	public LjmriTrvuMeta() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIndx() {
		return indx;
	}

	public void setIndx(String indx) {
		this.indx = indx;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public Integer getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Integer divisionId) {
		this.divisionId = divisionId;
	}

	public String getSomMajorGroup() {
		return somMajorGroup;
	}

	public void setSomMajorGroup(String somMajorGroup) {
		this.somMajorGroup = somMajorGroup;
	}

	public Integer getSomMajorGroupId() {
		return somMajorGroupId;
	}

	public void setSomMajorGroupId(Integer somMajorGroupId) {
		this.somMajorGroupId = somMajorGroupId;
	}

	public String getCapType() {
		return capType;
	}

	public void setCapType(String capType) {
		this.capType = capType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LjmriTrvuMeta other = (LjmriTrvuMeta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		//return getDivision() + " : " + getIndx() + " : " + getCapType();
		return getDivision() + " : " + getIndx();
	}
}