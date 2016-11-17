/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author somdev5
 *
 */
public class PLLineItemAmountRecalculateTest {

	// http://localhost:8080/vchs-budget-services/rest/lineitems/refresh-amount/medgrp-gross-charges?divisionId=22
	private static final String REQUEST_BASE_URL = "http://localhost:8080/vchs-budget-services/rest/lineitems/refresh-amount";
	
	@Test
	public void refreshAmountFlagForMedGrpGrossChargesShouldBeCalled() {
		try {
			String requestUrl = REQUEST_BASE_URL + "/medgrp-gross-charges";
			Integer divisionId = 22;
			Client client = ClientBuilder.newClient();
			String id = client.target(requestUrl)
							   .queryParam("divisionId", divisionId)
							   .request().get(String.class);
			System.out.println("id=" + id);
			//Assert.assertTrue(id > 0);
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
