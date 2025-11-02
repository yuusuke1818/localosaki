package jp.skygroup.enl.webap.base;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author take_suzuki
 */
public class BaseSftpFileTest {

    public BaseSftpFileTest() {
    }

    private final String hostName = "xx.xxx.xxx.xx";
    private final int portNo = 22;
    private final String user = "user";
    private final String password = "password";
    private final String fileNameEncoding = "UTF-8";

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getFileList method, of class BaseSftpFile.
     */
//    @Test
    public void testGetFileList() {

        String remoteDir = "/";
        List<String> result = BaseSftpFile.getFileList(hostName, portNo, user, password, fileNameEncoding, remoteDir);
        for (String a : result){
            System.out.println(a);
        }
        assertNotNull(result);
    }

    /**
     * Test of getFile method, of class BaseSftpFile.
     */
//    @Test
    public void testGetFile() {

        String remotePath = "/test.zip";
        String localPath = "C:/test.zip";
        boolean expResult = true;
        boolean result = BaseSftpFile.getFile(hostName, portNo, user, password, fileNameEncoding, remotePath, localPath);
        assertEquals(expResult, result);
    }

    /**
     * Test of delFile method, of class BaseSftpFile.
     */
//    @Test
    public void testDelFile() {

        String remotePath = "/test.zip";
        boolean expResult = true;
        boolean result = BaseSftpFile.delFile(hostName, portNo, user, password, fileNameEncoding, remotePath);
        assertEquals(expResult, result);
    }

}
