package ca.bc.gov.open.jrccaccessspringbootsampleapp;

import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;
import ca.bc.gov.open.jrccaccess.libs.processing.DocumentProcessor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DocumentProcessorExample implements DocumentProcessor {

	@Override
	public String processDocument(String content, TransactionInfo transactionInfo) {
		return content.toUpperCase(Locale.CANADA);
	}

}
