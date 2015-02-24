package com.redhat.bpms.examples.casemanagement;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.services.client.api.RemoteRestRuntimeFactory;

@WebService()
public class StartProcessService {
	
	private static RuntimeEngine runtimeEngine = null;

	@WebMethod()
	public void startProcess(String reporter, String location, String description, Boolean isFullInvestigation) {
	    System.out.println("Starting process: " + reporter);
	    
	    Incident incident = new Incident();
	    incident.setReporter(reporter);
	    incident.setLocation(location);
	    incident.setDescription(description);
	    
	    Map<String, Object> processVariables = new HashMap<String, Object>();
	    processVariables.put( "incident", incident );
	    processVariables.put( "isFullInvestigation", isFullInvestigation );
	    
	    KieSession kieSession = getRuntimeEngine().getKieSession();
	    kieSession.startProcess( "com.redhat.bpms.examples.casemanagement.CaseManagementProcess", processVariables );
	    
	}

	private RuntimeEngine getRuntimeEngine()
	{
		if (runtimeEngine == null) {
			try
			{
				String applicationContext = "http://localhost:8080/business-central";
				String deploymentId = "com.redhat.bpms.examples:CaseManagement:1.0";
				String userId = "erics";
				String password = "bpmsuite1!";
				
				URL jbpmURL = new URL( applicationContext );
				RemoteRestRuntimeFactory remoteRestSessionFactory = new RemoteRestRuntimeFactory( deploymentId, jbpmURL, userId, password );
				RuntimeEngine runtimeEngine = remoteRestSessionFactory.newRuntimeEngine();
				return runtimeEngine;
			}
			catch( MalformedURLException e )
			{
				throw new IllegalStateException( "This URL is always expected to be valid!", e );
			}
		}
		return runtimeEngine;
	}
}
