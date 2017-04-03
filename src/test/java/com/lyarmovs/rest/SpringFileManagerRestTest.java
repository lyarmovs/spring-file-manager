package com.lyarmovs.rest;

import com.lyarmovs.SpringFileManagerBaseTest;
import com.lyarmovs.model.DocumentMetadata;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author Lev Yarmovsky
 * @version $Id: SpringFileManagerRestTest.java,v 1.0 3/31/2017 6:40 PM lyarmovs Exp $
 *
 * Set of SpringBootTest {@link SpringBootTest @SpringBootTest} test cases
 * for REST services
 */
public class SpringFileManagerRestTest extends SpringFileManagerBaseTest {
    protected final Logger logger = LoggerFactory.getLogger(SpringFileManagerRestTest.class);
    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void findAllTest() {
        ResponseEntity<List<DocumentMetadata>> response = restTemplate.exchange("/documents/findAll/",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<DocumentMetadata>>() {
                });
        List<DocumentMetadata> documentMetadataList = response.getBody();
        logger.info("Document Metadata:" + documentMetadataList);
        assertThat(documentMetadataList.size()).isGreaterThanOrEqualTo(4);
        assertThat(documentMetadataList.get(0).getName()).isEqualTo("testDocument1.txt");
    }

    @Test
    public void findByIdTest() {
        ResponseEntity<DocumentMetadata> response = restTemplate.getForEntity("/documents/findById/1", DocumentMetadata.class);
        DocumentMetadata documentMetadata = response.getBody();
        logger.info("Document Metadata:" + documentMetadata);
        assertThat(documentMetadata.getName()).isEqualTo("testDocument1.txt");
    }

    @Test
    public void findByNameTest() {
        ResponseEntity<List<DocumentMetadata>> response = restTemplate.exchange("/documents/findByName/testDocument1.txt",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<DocumentMetadata>>() {
                });
        List<DocumentMetadata> documentMetadataList = response.getBody();
        logger.info("Document Metadata:" + documentMetadataList);
        assertThat(documentMetadataList.size()).isGreaterThanOrEqualTo(1);
        assertThat(documentMetadataList.get(0).getName()).isEqualTo("testDocument1.txt");
    }

    @Test
    public void findByExtensionTest() {
        ResponseEntity<List<DocumentMetadata>> response = restTemplate.exchange("/documents/findByExtension/txt",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<DocumentMetadata>>() {
                });
        List<DocumentMetadata> documentMetadataList = response.getBody();
        logger.info("Document Metadata:" + documentMetadataList);
        assertThat(documentMetadataList.size()).isGreaterThanOrEqualTo(3);
        assertThat(documentMetadataList.get(0).getName()).isEqualTo("testDocument1.txt");
    }

    @Test
    public void findByDescriptionContainingTest() {
        ResponseEntity<List<DocumentMetadata>> response = restTemplate.exchange("/documents/findByDescriptionContaining/Setup Test",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<DocumentMetadata>>() {
                });
        List<DocumentMetadata> documentMetadataList = response.getBody();
        logger.info("Document Metadata:" + documentMetadataList);
        assertThat(documentMetadataList.size()).isGreaterThanOrEqualTo(3);
        assertThat(documentMetadataList.get(0).getName()).isEqualTo("testDocument1.txt");
    }

    @Test
    public void deleteTest() {
        try {
            restTemplate.delete("/documents/delete/2");
        } catch (Throwable t) {
            logger.error("Error deleting document", t);
            fail("Error deleting document");
        }
    }

    @Test
    public void downloadTest() {
        ResponseEntity<byte[]> response = restTemplate.exchange("/documents/download/1",
                HttpMethod.GET, null, byte[].class);
        byte[] documentBytes = response.getBody();
        String docBody = new String(documentBytes);
        logger.info("Document Body:" + docBody);
        assertThat(docBody).isEqualTo("This is a test document 1.");
        assertThat(response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION))
                .isEqualTo("attachment; filename=\"testDocument1.txt\"");
    }

    @Test
    public void downloadNotFoundTest() {
        ResponseEntity<byte[]> response = restTemplate.exchange("/documents/download/1000",
                HttpMethod.GET, null, byte[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }




    @Test
    public void uploadTextFileTest() {
        ClassPathResource uploadFile = new ClassPathResource("/testDocument1.txt", getClass());
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("file", uploadFile);
        map.add("description", "This is a test document");
        ResponseEntity<DocumentMetadata> response = this.restTemplate.postForEntity("/documents/upload/", map, DocumentMetadata.class);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        DocumentMetadata documentMetadata = response.getBody();
        logger.info(documentMetadata.toString());
        assertThat(documentMetadata.getDescription()).isEqualTo("This is a test document");
        assertThat(documentMetadata.getName()).isEqualTo("testDocument1.txt");
        assertThat(documentMetadata.getSize()).isEqualTo(26L);
        assertThat(documentMetadata.getExtension()).isEqualTo("txt");
    }

}
