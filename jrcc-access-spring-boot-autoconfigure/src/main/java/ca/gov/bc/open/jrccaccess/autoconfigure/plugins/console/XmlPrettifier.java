package ca.gov.bc.open.jrccaccess.autoconfigure.plugins.console;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Prettify xml source
 * @author alexjoybc
 * @since 0.3.1
 */
@Service
@ConditionalOnProperty(
		value="bcgov.access.output.console.format",
		havingValue="xml")
public class XmlPrettifier implements Prettifier {

	private Logger logger= LoggerFactory.getLogger(XmlPrettifier.class);
	
	/**
	 * prettify xml
	 */
	@Override
	public String prettify(String input) {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			StreamSource source = new StreamSource(new StringReader(input));
			StreamResult result = new StreamResult(new StringWriter());
			transformer.transform(source, result);
			return result.getWriter().toString();
		} catch (Exception e) {
			logger.info(e.getMessage());
			return input;
		}
		
	}

}
