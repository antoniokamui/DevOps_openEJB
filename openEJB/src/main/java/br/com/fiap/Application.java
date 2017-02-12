package br.com.fiap;

import static org.junit.Assert.assertNotNull;
import static junit.framework.Assert.assertEquals;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.apache.cxf.jaxrs.client.WebClient;

public class Application {

	public static void main(String[] args) throws Exception {
		Properties proContainer  = new Properties();
		
		proContainer.put("httpejbd", "cfx-rs");
		proContainer.put("openejb.embedded.remotable", "true");
		
		proContainer.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.core.LocalInitialContextFactory");
		proContainer.put("dbUnit", "new://Resource?type=DataSource");
		proContainer.put("dbUnit.JdbcDriver", "org.hsqldb.jdbcDriver");
		proContainer.put("dbUnit.JdbcUrl", "jdbc:hsqldb:mem:moviedb");
		
		proContainer.put("dbUnit.hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		
		
		EJBContainer container = EJBContainer.createEJBContainer(proContainer);
		
		Context context = container.getContext();
		SimpleRestEJB object = (SimpleRestEJB) context.lookup("java:global/openEJB/SimpleRestEJB");
		assertNotNull(object);
		
		String messageFromEjb = object.ejb();
		assertNotNull(messageFromEjb);
		System.out.println(messageFromEjb);
		
		String messageFromRESTService = WebClient.create("http://localhost:4204").path("/openEJB/ejb").get(String.class);
		
		assertNotNull(messageFromEjb, messageFromRESTService);
		
		System.out.println(messageFromRESTService);
		
		

		object.addMovie(new Movie("Tekken ","Tekken o filme",2010));
		
		String tituloFilmeRS = WebClient.create("http://localhost:4204").path("/openEJB/ejb/db/movie/0").get(String.class);
		String tituloFilmeEJB = object.getMovieTitle("0");

		assertEquals(tituloFilmeEJB, tituloFilmeRS);
		
		System.out.println(tituloFilmeRS);
		System.out.println(tituloFilmeEJB);
		
		
	}
	
}