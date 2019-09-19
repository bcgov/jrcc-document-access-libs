package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.console;

import ca.bc.gov.open.jrccaccess.libs.DocumentOutput;
import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * The console output prints document properties to the standard output
 * @author alexjoybc
 * @since 0.2.0
 */
@Service
@ConditionalOnProperty(
		value="bcgov.access.output.plugin",
		havingValue="console"
	)
public class ConsoleOutput implements DocumentOutput {

	private Logger logger = LoggerFactory.getLogger(ConsoleOutput.class);
	
	private Prettifier prettifier;
	
	public ConsoleOutput(Prettifier prettifier) {
		this.prettifier = prettifier;
	}
	
	/**
	 * Sends the document to the console stdout.
	 */
	@Override
	public void send(String content, TransactionInfo transactionInfo) {
		logger.info("transactionInfo: {}", transactionInfo);
		String result = this.prettifier.prettify(content);
		logger.info("Content: " + result);
	}

}
