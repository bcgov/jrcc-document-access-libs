package ca.bc.gov.open.jrccaccessspringbootsampleapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
		properties = {
				"bcgov.access.input.plugin=http",
				"bcgov.access.output.plugin=console"
		}
)
public class JrccAccessSpringBootSampleAppApplicationTests {

	@Test
	public void contextLoads() {
	}

}
