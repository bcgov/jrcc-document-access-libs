package ca.gov.bc.open.jrccaccessspringbootsampleapp;

import java.util.Locale;

import org.springframework.stereotype.Component;

import ca.bc.gov.open.jrccaccess.libs.TransactionInfo;
import ca.bc.gov.open.jrccaccess.libs.processing.DocumentProcessor;

@Component
public class DocumentProcessorExample implements DocumentProcessor {

	@Override
	public String processDocument(String content, TransactionInfo transactionInfo) {
		return content.toUpperCase(Locale.CANADA);
	}

}
