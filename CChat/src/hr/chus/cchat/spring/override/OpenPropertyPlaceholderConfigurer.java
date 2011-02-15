package hr.chus.cchat.spring.override;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Custom extension of PropertyPlaceholderConfigurer so I can get properties later on in the application.
 * 
 * @author Jan Čustović (jan.custovic@yahoo.com)
 *
 */
public class OpenPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private Properties mergedProperties;

	public Properties getMergedProperties() throws IOException {
		if (mergedProperties == null) {
			mergedProperties = mergeProperties();
		}
		return mergedProperties;
	}
	
}