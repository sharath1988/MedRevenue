/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.test;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.runner.RunWith;

import edu.ucsd.som.vchs.medgrp.revenue.test.util.TestResources;
import edu.ucsd.som.vchs.medgrp.revenue.util.Resources;

/**
 * @author somdev5
 *
 */

@RunWith(Arquillian.class)
public abstract class ShrinkWrapContainerTests {
		
	@Deployment
    public static Archive<?> createTestArchive() {
		
		// Load maven dependencies from this project's pom.xml
		File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeDependencies().resolve().withTransitivity().asFile(); // VERY IMPORTANT!!
			
		// You can load individual libs like this
		/*String[] slibs = {
				"org.apache.deltaspike.modules:deltaspike-data-module-api",
				"org.apache.deltaspike.modules:deltaspike-data-module-impl",
				"org.apache.deltaspike.modules:deltaspike-jpa-module-api",
				"org.apache.deltaspike.modules:deltaspike-jpa-module-impl",
				"org.primefaces:primefaces"
		};
		File[] libs = Maven.resolver().loadPomFromFile("pom.xml").resolve(slibs).withTransitivity().asFile();*/

        return ShrinkWrap.create(WebArchive.class, "test.war")
        		.addClasses(TestResources.class) // Add producer
        		// All classes in this package will be scanned.  Filter the app Resources class
        		.addPackages(true, Filters.exclude(Resources.class), "edu.ucsd.som.vchs", "javax.faces.context") 
                .addAsLibraries(libs)
                .addAsResource("META-INF/test-apache-deltaspike.properties", "META-INF/apache-deltaspike.properties")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml") // Check hibernate.hbm2ddl.auto property!!
                .addAsWebInfResource("test-ds.xml", "test-ds.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml"); // Need this for CDI to work
    }
}
