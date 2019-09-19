package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.http;

import ca.bc.gov.open.api.DocumentApi;
import ca.bc.gov.open.api.model.DocumentReceivedResponse;
import ca.bc.gov.open.api.model.Error;
import ca.bc.gov.open.jrccaccess.autoconfigure.AccessProperties.PluginConfig;
import ca.bc.gov.open.jrccaccess.autoconfigure.common.Constants;
import ca.bc.gov.open.jrccaccess.autoconfigure.services.DocumentReadyHandler;
import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.DocumentMessageException;
import ca.bc.gov.open.jrccaccess.libs.services.exceptions.ServiceUnavailableException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The document controller provides an endpoint to submit a document.
 * @author alexjoybc
 * @since 0.2.0
 *
 */
@RestController
@ConditionalOnProperty(
	value="bcgov.access.input.plugin",
	havingValue = "http"
)
public class DocumentController implements DocumentApi {
	
	private DocumentReadyHandler documentReadyHandler;
	private PluginConfig inputConfig;

	private Logger logger = LoggerFactory.getLogger(DocumentController.class);
	
	/**
	 * Creates a new document controller
	 * @param documentReadyHandler the service that will handle the document
	 */
	public DocumentController(DocumentReadyHandler documentReadyHandler, @Qualifier("inputConfig") PluginConfig inputConfig) {
		
		this.documentReadyHandler = documentReadyHandler;
		this.inputConfig = inputConfig;
	}

	@Override
	public ResponseEntity<DocumentReceivedResponse> postDocument(UUID xRequestID,
																 UUID xB3TraceId,
																 UUID xB3ParentSpanId,
																 UUID xB3SpanId,
																 String xB3Sampled,
																 @Valid String sender,
																 @Valid MultipartFile fileInfo) {

		DocumentReceivedResponse response = new DocumentReceivedResponse();
		response.setAcknowledge(true);

		if (StringUtils.isBlank(sender) && inputConfig.getSender().equals(Constants.UNKNOWN_SENDER)) {
			logger.warn("Sender not specified in application.yml, using default value of unknown.");
		}

		try {
			TransactionInfo transactionInfo = new TransactionInfo(fileInfo.getOriginalFilename(), inputConfig.getSender(), LocalDateTime.now());
			documentReadyHandler.handle(getContent(fileInfo.getInputStream()), transactionInfo);
		} catch (ServiceUnavailableException e) {
			Error error = new Error();
			error.setCode(Integer.toString(HttpStatus.SERVICE_UNAVAILABLE.value()));
			error.setMessage(e.getMessage());
			return new ResponseEntity(error, HttpStatus.SERVICE_UNAVAILABLE);

		} catch (IOException | DocumentMessageException e) {
			Error error = new Error();
			error.setCode(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			error.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.ok(response);

	}

	private String getContent(InputStream inputStream) throws IOException {

		StringBuilder stringBuilder = new StringBuilder();
		String line = null;

		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
		}

		return stringBuilder.toString();
	}

}
