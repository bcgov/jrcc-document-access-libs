package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.console;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Default prettifier, truncates the payload
 * @author alexjoybc
 * @since 0.3.1
 *
 */
@Service
@ConditionalOnProperty(
		value="bcgov.access.output.console.format",
		havingValue="default",
		matchIfMissing = true)
public class DefaultPrettifier implements Prettifier {

	@Override
	public String prettify(String input) {
		int max = 100;
		if(input.length() < max) max = input.length();
		return input.substring(0, max) + " ...";
	}

}
