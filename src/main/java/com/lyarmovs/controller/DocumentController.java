package com.lyarmovs.controller;

import com.lyarmovs.exceptions.ServiceException;
import com.lyarmovs.logic.DocumentService;
import com.lyarmovs.model.DocumentMetadata;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Lev Yarmovsky
 * @version $Id: DocumentController.java,v 1.0 3/31/2017 2:50 PM lyarmovs Exp $
 *
 * Document REST controller
 * Provides URL bindings for document management REST services
 * For purposes of this demo, all services are not secured
 */
@RestController
@RequestMapping(value = "/documents")
public class DocumentController {
    private final Logger logger = LoggerFactory.getLogger(DocumentController.class);
    @Autowired
    private DocumentService documentService;

    /**
     * Retrieves metadata for all uploaded documents
     * @return list of metadata {@link DocumentMetadata @DocumentMetadata}
     */
    @RequestMapping("/findAll/")
    public List<DocumentMetadata> getDocumentMetadataList() {
        return documentService.getDocumentMetadataList();
    }

    /**
     * Retrieves document metadata by document id
     * @param id document id
     * @return metadata {@link DocumentMetadata @DocumentMetadata}
     */
    @RequestMapping("/findById/{id}")
    public DocumentMetadata findById(@PathVariable Long id) {
        return documentService.findById(id);
    }

    /**
     * Retrieves list of document metadata associated with given document name
     * @param name document name
     * @return list of metadata {@link DocumentMetadata @DocumentMetadata}
     */
    @RequestMapping("/findByName/{name:.+}")
    public List<DocumentMetadata> findByName(@PathVariable String name) {
        return documentService.findByName(name);
    }

    /**
     * Retrieves list of document metadata with given document file extension
     * @param extension file name extension
     * @return list of metadata {@link DocumentMetadata @DocumentMetadata}
     */
    @RequestMapping("/findByExtension/{extension}")
    public List<DocumentMetadata> findByExtension(@PathVariable String extension) {
        return documentService.findByExtension(extension);
    }

    /**
     * Retrieves list of document metadata containing a phrase in description
     * @param description a phrase in description
     * @return list of metadata {@link DocumentMetadata @DocumentMetadata}
     */
    @RequestMapping("/findByDescriptionContaining/{description}")
    public List<DocumentMetadata> findByDescriptionContaining(@PathVariable String description) {
        return documentService.findByDescriptionContaining(description);
    }

    /**
     * Handles file upload with correspondent document description
     * Extracts and stores document metadata {@link DocumentMetadata @DocumentMetadata}
     * @param file uploaded file {@link MultipartFile @MultipartFile}
     * @param description document description
     * @return created document metadata {@link DocumentMetadata @DocumentMetadata}
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public DocumentMetadata uploadDocument(@RequestParam(value="file", required=true) MultipartFile file ,
                               @RequestParam(value="description", required=true) String description) {
        return documentService.addDocument(file, description);
    }

    /**
     * Delete document by document id
     * @param id document id
     */
    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    public void deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
    }

    /**
     * Retrieves document content as stream
     * @param id document id
     * @param response servlet response object {@link HttpServletResponse @HttpServletResponse}
     */
    @RequestMapping(value = "/download/{id}")
    public void getFile(@PathVariable Long id, HttpServletResponse response) {
        InputStream is = null;
        try {
            DocumentMetadata metadata = documentService.findById(id);
            is = documentService.getDocumentStream(metadata);
            if(is != null) {
                IOUtils.copy(is, response.getOutputStream());
                response.setContentType(metadata.getContentType());
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadata.getName() + "\"");
                response.flushBuffer();
            } else {
                throw new ServiceException("Document with id:" + id + " not found.");
            }
        } catch (IOException ex) {
            String msg = "Error writing file to output stream. Document id: " + id;
            logger.info(msg, ex);
            throw new ServiceException(msg);
        } finally {
            IOUtils.closeQuietly(is);
        }

    }

    /**
     * Service exception handling. For a purpose of demo
     * logging the error and return document not found http status
     * @param exc service exception {@link ServiceException @ServiceException}
     * @return http not found status 404
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity handleServiceException(ServiceException exc) {
        logger.error("Service Exception", exc);
        return ResponseEntity.notFound().build();
    }

}
