package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.sftp;

import org.junit.Assert;
import org.junit.Test;

public class SftpInputPropertiesTests {

    @Test
    public void with_null_ssh_key_should_return_null() {
        SftpInputProperties sut = new SftpInputProperties();
        Assert.assertNull(sut.getSshPrivateKey());
    }

    @Test
    public void with_ssh_key_should_return_ssh_key() {
        SftpInputProperties sut = new SftpInputProperties();
        sut.setSshPrivateKey("---BEGIN RSA PRIVATE KEY ----\n1111");
        Assert.assertNotNull(sut.getSshPrivateKey());
    }


    @Test
    public void default_allow_unknown_key_return_false() {
        SftpInputProperties sut = new SftpInputProperties();
        Assert.assertFalse(sut.isAllowUnknownKeys());
    }

    @Test
    public void set_allow_unknown_key_should_succeed() {
        SftpInputProperties sut = new SftpInputProperties();
        sut.setAllowUnknownKeys(true);
        Assert.assertTrue(sut.isAllowUnknownKeys());
    }

    @Test
    public void default_get_known_hosts_return_null() {
        SftpInputProperties sut = new SftpInputProperties();
        Assert.assertNull(sut.getKnownHostFile());
    }

    @Test
    public void set_known_hosts_should_succeed() {
        SftpInputProperties sut = new SftpInputProperties();
        sut.setKnownHostFile("c://test//known_hosts");
        Assert.assertEquals(sut.getKnownHostFile(), "c://test//known_hosts");
    }
}
