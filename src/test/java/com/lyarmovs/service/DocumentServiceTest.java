package com.lyarmovs.service;

import com.lyarmovs.SpringFileManagerBaseTest;
import com.lyarmovs.logic.DocumentService;
import com.lyarmovs.model.DocumentMetadata;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author Lev Yarmovsky (lev.yarmovsky@cgifederal.com)
 * @version $Id: DocumentServiceTest.java,v 1.0 4/2/2017 11:26 AM lyarmovs Exp $
 *
 * Set of SpringBootTest {@link SpringBootTest @SpringBootTest} test cases
 * for Document service {@link DocumentService @DocumentService}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DocumentServiceTest  extends SpringFileManagerBaseTest {
    protected final Logger logger = LoggerFactory.getLogger(DocumentServiceTest.class);

    @Test
    public void getDocumentMetadataListTest() {
        List<DocumentMetadata> documentMetadataList = getDocumentService().getDocumentMetadataList();
                assertThat(documentMetadataList.size()).isGreaterThanOrEqualTo(3);
        assertThat(documentMetadataList.get(0).getName()).isEqualTo("testDocument1.txt");
    }

    @Test
    public void findByNameTest() {
        List<DocumentMetadata> documentMetadataList = getDocumentService().findByName("testDocument1.txt");
        assertThat(documentMetadataList.size()).isGreaterThanOrEqualTo(1);
        assertThat(documentMetadataList.get(0).getName()).isEqualTo("testDocument1.txt");
    }

    @Test
    public void findByExtensionTest() {
        List<DocumentMetadata> documentMetadataList = getDocumentService().findByExtension("png");
        assertThat(documentMetadataList.size()).isGreaterThanOrEqualTo(1);
        assertThat(documentMetadataList.get(0).getName()).isEqualTo("martian-32.png");
    }

    @Test
    public void getDocumentStreamTest() {
        InputStream is = null;
        try {
            List<DocumentMetadata> documentMetadataList = getDocumentService().findByName("testDocument1.txt");
            is = getDocumentService().getDocumentStream(documentMetadataList.get(0));
            String testString = IOUtils.toString(is, "UTF-8");
            assertThat(testString).isEqualTo("This is a test document 1.");
        } catch (Throwable t) {
            logger.error("Error reading IS", t);
            fail("Error reading IS", t);
        } finally {
            if(is != null) {
                IOUtils.closeQuietly(is);
            }
        }
    }

    @Test
    public void getDocumentStreamErrorTest() {
        InputStream is = null;
        try {
            DocumentMetadata documentMetadata = new DocumentMetadata();
            is = getDocumentService().getDocumentStream(documentMetadata);
            assertThat(is).isEqualTo(null);
        } catch (Throwable t) {
            logger.error("Error reading IS", t);
            fail("Error reading IS", t);
        } finally {
            if(is != null) {
                IOUtils.closeQuietly(is);
            }
        }
    }


    @Test
    public void findByIdTest() {
        DocumentMetadata documentMetadata = getDocumentService().findById(1L);
        assertThat(documentMetadata.getDescription()).isEqualTo("Setup Test File 1");
        assertThat(documentMetadata.getName()).isEqualTo("testDocument1.txt");
        assertThat(documentMetadata.getSize()).isEqualTo(26L);
        assertThat(documentMetadata.getExtension()).isEqualTo("txt");
    }

    @Test
    public void deleteDocumentTest() {
        getDocumentService().deleteDocument(2L);
        DocumentMetadata documentMetadata = getDocumentService().findById(2L);
        assertThat(documentMetadata).isEqualTo(null);
    }

    @Test
    public void addDocumentTest() {
        try {
            ClassPathResource uploadFileResource = new ClassPathResource("/testDocument1.txt", getClass());
            MockMultipartFile uploadFile = new MockMultipartFile("test.txt", "test.txt", "", new FileInputStream(uploadFileResource.getFile()));
            DocumentMetadata documentMetadata = getDocumentService().addDocument(uploadFile, "Test Desctiption");
            assertThat(documentMetadata.getName()).isEqualTo("test.txt");
            assertThat(documentMetadata.getDescription()).isEqualTo("Test Desctiption");
            assertThat(documentMetadata.getSize()).isEqualTo(26L);
            assertThat(documentMetadata.getExtension()).isEqualTo("txt");
        } catch (Throwable t) {
            logger.error("Error adding file", t);
            fail("Error adding file", t);
        }
    }

    @Test
    public void findByDescriptionContainingTest() {
        List<DocumentMetadata> documentMetadataList = getDocumentService().findByDescriptionContaining("Test File");
        assertThat(documentMetadataList.size()).isGreaterThanOrEqualTo(2);
        assertThat(documentMetadataList.get(0).getName()).isEqualTo("testDocument1.txt");
    }

    @Test
    public void lastHourUploadsNotificationTest() {
        try {
            getDocumentService().lastHourUploadsNotification();
        }catch (Throwable t) {
            logger.error("Error sending notifications", t);
            fail("Error sending notifications", t);
        }
    }

}
