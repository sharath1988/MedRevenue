/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author somdev5
 *
 */
@Entity
@Table(catalog="budget", name="medgrp_revenue_employee_merge")
public class RevenueProviderMerge implements Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -2260869996964595694L;
	
	@EmbeddedId
	private RevenueProviderMergePK id;
	
	/**
	 * Default constructor
	 */
	public RevenueProviderMerge() {
		super();
	}

	/**
	 * @return the id
	 */
	public RevenueProviderMergePK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(RevenueProviderMergePK id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RevenueProviderMerge other = (RevenueProviderMerge) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RevenueProviderMerge [id=" + id + "]";
	}
}
