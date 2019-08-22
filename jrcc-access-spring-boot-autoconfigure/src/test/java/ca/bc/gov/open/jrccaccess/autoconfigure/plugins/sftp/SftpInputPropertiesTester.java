package ca.bc.gov.open.jrccaccess.autoconfigure.plugins.sftp;

import org.junit.Assert;
import org.junit.Test;

public class SftpInputPropertiesTester {

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



}
