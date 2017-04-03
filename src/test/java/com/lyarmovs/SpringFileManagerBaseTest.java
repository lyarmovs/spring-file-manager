package com.lyarmovs;

import com.lyarmovs.logic.DocumentService;
import com.lyarmovs.logic.FileStorageService;
import com.lyarmovs.model.DocumentMetadata;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Lev Yarmovsky (lev.yarmovsky@cgifederal.com)
 * @version $Id: SpringFileManagerBaseTest.java,v 1.0 4/2/2017 11:40 AM lyarmovs Exp $
 *
 * Base class for SpringBootTest {@link SpringBootTest @SpringBootTest} test cases
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringFileManagerBaseTest {
    private final Logger logger = LoggerFactory.getLogger(SpringFileManagerBaseTest.class);
    @Autowired
    private DocumentService documentService;

    public DocumentService getDocumentService() {
        return documentService;
    }


    /**
     * Setup files in a database and on a file storage before executing test
     * @throws Exception should not have errors if correctly setup
     */
    @Before
    public void setup() throws Exception {
        populateTestFiles("testDocument1.txt", "Setup Test File 1", "text/plain");
        populateTestFiles("testDocument2.txt", "Setup Test File 2", "text/plain");
        populateTestFiles("testDocument3.txt", "Setup Test File 3", "text/plain");
        populateTestFiles("martian-32.png", "Setup Image Test File", "image/png");
    }

    private void populateTestFiles(String fileName, String description, String contentType) throws IOException {
        ClassPathResource uploadFileResource = new ClassPathResource("/" + fileName, getClass());
        MockMultipartFile uploadFile = new MockMultipartFile(fileName, uploadFileResource.getFilename(), contentType, new FileInputStream(uploadFileResource.getFile()));
        DocumentMetadata documentMetadata = documentService.addDocument(uploadFile, description);
        logger.info(documentMetadata.toString());
    }

    /**
     * Delete all files from the file storage after the test were executed
     */
    @AfterClass
    public static void cleanup() {
        FileStorageService.deleteAllStoredDocuments();
    }
}
