package hr.chus.cchat.spring.override;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 
 * @author Jan Čustović
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