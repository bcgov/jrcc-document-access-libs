package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.console;

import java.util.Scanner;

import ca.bc.gov.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

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
	
	private DocumentReadyHandler documentReadyHandler;
	
	public ConsoleInput(DocumentReadyHandler documentReadyHandler) {
		this.documentReadyHandler = documentReadyHandler;
	}
	
	/**
	 * sends any message from standard input to the {@link DocumentReadyHandler}
	 */
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub	
		
		Scanner scanner = new Scanner(System.in);
		
		while(scanner.hasNext()) {
			documentReadyHandler.handle(scanner.nextLine(), appName);
		}	
		
		scanner.close();
	}

	
	
	
	
}
