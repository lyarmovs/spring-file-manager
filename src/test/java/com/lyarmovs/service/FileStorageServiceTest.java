package com.lyarmovs.service;

import com.lyarmovs.SpringFileManagerBaseTest;
import com.lyarmovs.conf.DocumentProperties;
import com.lyarmovs.logic.FileStorageService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author Lev Yarmovsky (lev.yarmovsky@cgifederal.com)
 * @version $Id: FileStorageServiceTest.java,v 1.0 4/2/2017 11:26 AM lyarmovs Exp $
 *
 * Set of SpringBootTest {@link SpringBootTest @SpringBootTest} test cases
 * for file storage service {@link FileStorageService @FileStorageService}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileStorageServiceTest extends SpringFileManagerBaseTest {
    protected final Logger logger = LoggerFactory.getLogger(FileStorageServiceTest.class);
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private DocumentProperties documentProperties;

    @Test
    public void storeDocumentTest() {
        try {
            String absolutePathStr = storeFileByFileService();
            logger.info("File stored into:" + absolutePathStr);
            File storedFile = new File(absolutePathStr);
            assertThat(storedFile.exists()).isEqualTo(true);
        } catch (Throwable t) {
            logger.error("Error adding file", t);
            fail("Error adding file", t);
        }
    }

    private String storeFileByFileService() throws IOException {
        ClassPathResource uploadFileResource = new ClassPathResource("/testDocument1.txt", getClass());
        MockMultipartFile uploadFile = new MockMultipartFile("test.txt", "test.txt", "", new FileInputStream(uploadFileResource.getFile()));
        return fileStorageService.storeDocument(uploadFile);
    }

    @Test
    public void deleteFileTest() {
        try {
            ClassPathResource uploadFileResource = new ClassPathResource("/testDocument1.txt", getClass());
            File tmpFile = File.createTempFile("testFile", "txt");
            FileUtils.copyFile(uploadFileResource.getFile(), tmpFile);
            assertThat(tmpFile.exists()).isEqualTo(true);
            fileStorageService.deleteFile(tmpFile.getAbsolutePath());
            assertThat(tmpFile.exists()).isEqualTo(false);
        } catch (Throwable t) {
            logger.error("Error deleting file", t);
            fail("Error deleting file", t);
        }
    }

    @Test
    public void getFileInputStreamTest() {
        InputStream is = null;
        try {
            String absolutePathStr = storeFileByFileService();
            is = fileStorageService.getFileInputStream(absolutePathStr);
            String testString = IOUtils.toString(is, "UTF-8");
            assertThat(testString).isEqualTo("This is a test document 1.");
        } catch (Throwable t) {
            logger.error("Error reading file", t);
            fail("Error reading file", t);
        } finally {
            if(is != null) {
                IOUtils.closeQuietly(is);
            }
        }
    }

}
