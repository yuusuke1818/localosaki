package jp.skygroup.enl.webap.base;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author take_suzuki
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BaseFileZipArchiveTest {

    private static void delete(File f) {

        if (!f.exists()) {
            return;
        }

        if (f.isFile()) {
            f.delete();
        }

        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                delete(files[i]);
            }
            f.delete();
        }
    }

    private static void mkdirs(File f) {

        if (!f.exists()) {
            f.mkdirs();
        }

    }

    public BaseFileZipArchiveTest() {
    }

    @BeforeClass
    public static void setUpClass() {

        delete(new File("C:\\proj\\base\\trunk\\BaseFileZipArchiveTest\\output\\zip"));
        mkdirs(new File("C:\\proj\\base\\trunk\\BaseFileZipArchiveTest\\output\\zip"));
        delete(new File("C:\\proj\\base\\trunk\\BaseFileZipArchiveTest\\output\\targz"));
        mkdirs(new File("C:\\proj\\base\\trunk\\BaseFileZipArchiveTest\\output\\targz"));
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
     * Test of compressDirectory method, of class BaseFileZipArchive.
     */
    @Test
    public void testCompressDirectory() {
        String inputDirectoryPath = "C:\\proj\\base\\trunk\\BaseFileZipArchiveTest\\input\\zip\\100";
        String zipFilePath = "C:\\proj\\base\\trunk\\BaseFileZipArchiveTest\\output\\zip\\100.zip";
        Charset charset = StandardCharsets.UTF_8;
        int compressionLevel = 5;
        boolean expResult = true;
        boolean result = BaseFileZipArchive.compressDirectory(inputDirectoryPath, zipFilePath, charset, compressionLevel);
        assertEquals(expResult, result);
    }

    /**
     * Test of compressFileList method, of class BaseFileZipArchive.
     */
    @Test
    public void testCompressFileList() {
        List<String> inputFileList = new ArrayList<>();
        inputFileList.add("C:\\proj\\base\\trunk\\BaseFileZipArchiveTest\\input\\zip\\001.txt");
        inputFileList.add("C:\\proj\\base\\trunk\\BaseFileZipArchiveTest\\input\\zip\\002.txt");
        inputFileList.add("C:\\proj\\base\\trunk\\BaseFileZipArchiveTest\\input\\zip\\003.txt");
        inputFileList.add("C:\\proj\\base\\trunk\\BaseFileZipArchiveTest\\input\\zip\\004.txt");
        String zipFilePath = "C:\\proj\\base\\trunk\\BaseFileZipArchiveTest\\output\\zip\\txt.zip";
        Charset charset = StandardCharsets.UTF_8;
        int compressionLevel = 5;
        boolean expResult = true;
        boolean result = BaseFileZipArchive.compressFileList(inputFileList, zipFilePath, charset, compressionLevel);
        assertEquals(expResult, result);
    }

    /**
     * Test of unZip method, of class BaseFileZipArchive.
     */
    @Test
    public void testUnZip() {
        String zipFilePath = "C:\\proj\\base\\trunk\\BaseFileZipArchiveTest\\output\\zip\\100.zip";
        String outputDirectoryPath = "C:\\proj\\base\\trunk\\BaseFileZipArchiveTest\\output\\zip";
        boolean expResult = true;
        boolean result = BaseFileZipArchive.unZip(zipFilePath, outputDirectoryPath);
        assertEquals(expResult, result);
    }

    /**
     * Test of unTargz method, of class BaseFileZipArchive.
     */
    @Test
    public void testUnTargz() {
        String targzFilePath = "C:\\proj\\base\\trunk\\BaseFileZipArchiveTest\\input\\targz\\100.tar.gz";
        String outputDirPath = "C:\\proj\\base\\trunk\\BaseFileZipArchiveTest\\output\\targz";
        boolean expResult = true;
        boolean result = BaseFileZipArchive.unTargz(targzFilePath, outputDirPath);
        assertEquals(expResult, result);
    }
}
