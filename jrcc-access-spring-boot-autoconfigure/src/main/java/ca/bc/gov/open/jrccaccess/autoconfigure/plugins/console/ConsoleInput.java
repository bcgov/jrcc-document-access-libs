package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.console;

import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties;
import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties.PluginConfig;
import ca.bc.gov.open.jrccaccess.autoconfigure.common.Constants;
import ca.bc.gov.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * The console input reads message from standard input
 * @author 177226
 *
 */
@Component
@ConditionalOnProperty(
		value="bcgov.access.input.plugin",
		havingValue="console"
	)
public class ConsoleInput implements CommandLineRunner {

	@Value("${spring.application.name:unknown}")
	private String appName;

	private static final String CONSOLE_FILENAME="console.txt";
	
	private DocumentReadyHandler documentReadyHandler;
	private AccessProperties accessProperties;

	/**
	 * Constructs a new ConsoleInput with the specified DocumentReadyHandler.
	 * @param documentReadyHandler
	 * @param accessProperties
	 */
	public ConsoleInput(DocumentReadyHandler documentReadyHandler, AccessProperties accessProperties) {
		this.documentReadyHandler = documentReadyHandler;
		this.accessProperties = accessProperties;
	}
	
	/**
	 * sends any message from standard input to the {@link DocumentReadyHandler}
	 */
	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);

		PluginConfig inputConfig = new PluginConfig();

		inputConfig = this.accessProperties.getInput();

		if (inputConfig == null){
			throw new Exception("Input config not present");
		}

		if (inputConfig.getSender() == null) inputConfig.setSender("console");
		
		while(scanner.hasNext()) {
			MDC.put(Constants.MDC_KEY_FILENAME, CONSOLE_FILENAME);
			TransactionInfo transactionInfo = new TransactionInfo(CONSOLE_FILENAME, inputConfig.getSender(), LocalDateTime.now());

			documentReadyHandler.handle(scanner.nextLine(), transactionInfo);
		}	

		MDC.clear();
		scanner.close();
	}

	
	
	
	
}
