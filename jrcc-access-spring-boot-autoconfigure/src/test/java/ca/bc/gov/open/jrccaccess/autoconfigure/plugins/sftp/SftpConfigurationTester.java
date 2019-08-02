package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.sftp;

import ca.bc.gov.open.jrccaccess.autoconfigure.AccessApplication;
import com.jcraft.jsch.ChannelSftp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = AccessApplication.class,
        properties = {
                "bcgov.access.input.plugin=sftp",
                "bcgov.access.input.sfpt.host=localhost",
                "bcgov.access.input.sftp.port=2222",
                "bcgov.access.input.sftp.username=master",
                "bcgov.access.input.sftp.password=master",
                "bcgov.access.input.sftp.local-directory=local",
                "bcgov.access.input.sftp.remote-directory=remote",
                "bcgov.access.output.plugin=console"
        })
@ContextConfiguration
public class SftpConfigurationTester {


    @Autowired
    @Qualifier("sftpSessionFactory")
    private SessionFactory<ChannelSftp.LsEntry> sftpSessionFactorySut;

    @Autowired
    @Qualifier("sftpInboundFileSynchronizer")
    private SftpInboundFileSynchronizer sftpInboundFileSynchronizerSut;

    @Test
    public void with_valid_config_should_create_sftpSessionFactory() {

        Assert.assertNotNull(sftpSessionFactorySut);
    }

    @Test
    public void with_valid_config_should_create_SftpInboundFileSynchronizer() {
        Assert.assertNotNull(sftpInboundFileSynchronizerSut);
    }

}
