package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import ca.gov.bc.open.jrccaccess.libs.DocumentOutput;
import ca.gov.bc.open.jrccaccess.libs.TransactionInfo;

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
		// TODO Auto-generated method stub
		logger.info("transactionInfo: {}", transactionInfo);
		String result = this.prettifier.prettify(content);
		System.out.println(result);

	}

}
