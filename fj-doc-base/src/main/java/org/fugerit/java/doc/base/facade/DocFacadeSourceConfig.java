package org.fugerit.java.doc.base.facade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocFacadeSourceConfig {

	public static final DocFacadeSourceConfig DEFAULT_CONFIG = new DocFacadeSourceConfig().withFailOnSourceModuleNotFound(false);
	
	@Getter @With private boolean failOnSourceModuleNotFound; 

}
