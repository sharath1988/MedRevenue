/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.service;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.jboss.aesh.edit.EmacsEditMode;

import edu.ucsd.som.vchs.medgrp.revenue.data.CapDistributionRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.RevenueInpatientPercentagePsychRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.RevenueInpatientPercentageRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.RevenuePaymentInformationRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.RevenuePaymentInformationViewPsychRepository;
import edu.ucsd.som.vchs.medgrp.revenue.data.RevenuePaymentInformationViewRepository;
import edu.ucsd.som.vchs.medgrp.revenue.model.CapDistribution;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueInpatientPercentage;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenueInpatientPercentageBase;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenuePaymentInformation;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenuePaymentInformationView;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenuePaymentInformationViewBase;
import edu.ucsd.som.vchs.medgrp.revenue.model.RevenuePaymentInformationViewPsych;

/**
 * Service for populating and updating the Payment Information section on the revenue worksheet page
 * 
 * @author somdev5
 *
 */
@SuppressWarnings("cdi-ambiguous-dependency")
@Named("paymentInformationService")
public class PaymentInformationService implements Serializable {

	@Inject
	private Log log;
	
	//@Inject
	//private RevenuePaymentInformationViewRepository paymentInfoViewRepo;
	@Inject
	private EntityManager em;
	
	@Inject
	private RevenuePaymentInformationViewPsychRepository paymentInfoViewPsychRepo;
	
	@Inject
	private RevenuePaymentInformationRepository paymentInfoRepo;
	
	@Inject
	private RevenueInpatientPercentageRepository inpatientPercRepo;
	
	@Inject
	private RevenueInpatientPercentagePsychRepository inpatientPercPsychRepo;
	
	@Inject
	private CapDistributionRepository capDistributionRepository;
		
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -7593843450483294297L;

	/**
	 * Default constructor
	 */
	public PaymentInformationService() {
		super();
	}

	/**
	 * Returns collections to trvu rates and payment information for a given division
	 * 
	 * @param divisionId
	 * @return
	 */
	public RevenuePaymentInformationViewBase getPaymentInformation(Integer divisionId) {
		//return paymentInfoViewRepo.findOptionalByDivisionId(divisionId);
		StoredProcedureQuery sproc = em.createNamedStoredProcedureQuery(RevenuePaymentInformationView.GET_PAYMENT_INFORMATION_BY_DIV_PROC);
		sproc.setParameter("pDivisionId", divisionId);
		return (RevenuePaymentInformationViewBase) sproc.getSingleResult();
	}
	
	/**
	 * Method used by Psychiatry division to filter based on SOS
	 * 
	 * @param divisionId
	 * @param sos
	 * @return
	 */
	public RevenuePaymentInformationViewBase getPaymentInformationBySos(Integer divisionId, String sos) {
		return paymentInfoViewPsychRepo.findOptionalByDivisionIdAndSos(divisionId, sos);
	}
	
	/**
	 * Updates payment information rate percentage inc/dec in the payment information section
	 * 
	 * @param paymentInformation
	 */
	public void savePaymentInformation(RevenuePaymentInformationViewBase paymentInformation) {
		
		RevenuePaymentInformation entity = new RevenuePaymentInformation();
		
		try {
			BeanUtils.copyProperties(entity, paymentInformation);
			paymentInfoRepo.saveAndFlush(entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * Returns prior year in-patient percentage information for this division
	 * 
	 * @param divisionId
	 * @return
	 */
	public RevenueInpatientPercentageBase getInpatientPercentage(Integer divisionId) {
		return inpatientPercRepo.findBy(divisionId);
	}
	
	/**
	 * Returns inpatient percentage for psych
	 * @return
	 */
	public RevenueInpatientPercentageBase getInpatientPercentageForPsych() {
		return inpatientPercPsychRepo.findBy(22);
	}
	
	/**
	 * Persists inpatient percentage
	 * 
	 * @param inpatientPercentage
	 */
	public void saveProjInpatientPercentage(RevenueInpatientPercentageBase inpatientPercentage) {
		inpatientPercRepo.saveAndFlush((RevenueInpatientPercentage) inpatientPercentage);
	}
	
	/**
	 * Refresh payment information
	 * @param paymentInformation
	 */
	public void refreshPaymentInformationView(RevenuePaymentInformationViewBase paymentInformation) {
		//paymentInfoViewRepo.refresh((RevenuePaymentInformationView)paymentInformation);
		paymentInformation = getPaymentInformation(paymentInformation.getDivisionId());
	}
	
	/**
	 * Refresh payment information
	 * @param paymentInformation
	 */
	public void refreshPaymentInformationViewPsych(RevenuePaymentInformationViewBase paymentInformation) {
		paymentInfoViewPsychRepo.refresh((RevenuePaymentInformationViewPsych)paymentInformation);
	}
	
	/**
	 * Persists cap distribution
	 * 
	 * @param capDistribution
	 */
	public void saveCapDistribution(CapDistribution capDistribution) {
		capDistributionRepository.saveAndFlush(capDistribution);
	}
	
	/**
	 * Returns cap distribution by id
	 * @param divisionId
	 */
	public CapDistribution getCapDistributionByDivision(Integer divisionId) {
		CapDistribution capDistribution = capDistributionRepository.findOptionalByDivisionId(divisionId);
		if (capDistribution == null) {
			capDistribution = new CapDistribution();
			capDistribution.setDivisionId(divisionId);
			capDistribution.setAmount(BigDecimal.ZERO);				
		}
		return capDistribution;
	}
}