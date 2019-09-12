package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.console;

import brave.Tracer;
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
import org.springframework.beans.factory.annotation.Autowired;

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

	private final String CONSOLE_FILENAME="console.txt";
	
	private DocumentReadyHandler documentReadyHandler;

        @Autowired
	private Tracer tracer;

	/**
	 * Constructs a new ConsoleInput with the specified DocumentReadyHandler and Tracer.
	 * @param documentReadyHandler
	 * @param tracer
	 */
	public ConsoleInput(DocumentReadyHandler documentReadyHandler, Tracer tracer) {
		this.documentReadyHandler = documentReadyHandler;
		this.tracer = tracer;
	}
	
	/**
	 * sends any message from standard input to the {@link DocumentReadyHandler}
	 */
	@Override
	public void run(String... args) throws Exception {
                Scanner scanner = new Scanner(System.in);

                while(scanner.hasNext()) {
                        MDC.put(Constants.MDC_KEY_FILENAME, CONSOLE_FILENAME);
                        this.tracer.nextSpan().tag("filename", CONSOLE_FILENAME);
                        TransactionInfo transactionInfo = new TransactionInfo(CONSOLE_FILENAME,"console", LocalDateTime.now());

                        documentReadyHandler.handle(scanner.nextLine(), transactionInfo);
                }	

                MDC.clear();
                scanner.close();
        }
}
