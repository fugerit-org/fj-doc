package org.fugerit.java.doc.playground.facade;

import lombok.Getter;
import lombok.Setter;
import org.fugerit.java.core.lang.helpers.CollectionUtils;
import org.fugerit.java.emp.sm.service.ServiceMessage;
import org.fugerit.java.emp.sm.service.ServiceResponse;

public class BasicOutput extends ServiceResponse {

	public void setMessage( String message ) {
		this.addAllBySeverity( ServiceMessage.newMessage( ServiceMessage.Severity.INFO, message ) );
	}

	public String getMessage() {
		return CollectionUtils.isEmpty( this.getInfos() ) ? null : this.getInfos().get( 0 ).getText();
	}
	
}
