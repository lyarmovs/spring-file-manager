package com.lyarmovs.logic;

import com.lyarmovs.conf.DocumentProperties;
import com.lyarmovs.exceptions.ServiceException;
import com.lyarmovs.model.DocumentMetadata;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Lev Yarmovsky
 * @version $Id: DocumentService.java,v 1.0 3/31/2017 2:48 PM lyarmovs Exp $
 *
 * Document management service, responsible for storing and retrieval of a
 * document and associated metadata
 */
@Service
public class DocumentService {
    private final Logger logger = LoggerFactory.getLogger(DocumentService.class);
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private DocumentProperties documentProperties;
    @Autowired
    private Configuration freemarkerConfiguration;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private EmailService emailService;

    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    /**
     * Retrieves metadata for all uploaded documents
     * @return list of metadata {@link DocumentMetadata @DocumentMetadata}
     */
    public List<DocumentMetadata> getDocumentMetadataList() {
        List<DocumentMetadata> documentMetadataList = new ArrayList<>();
        documentRepository.findAll().forEach(documentMetadataList::add);
        return documentMetadataList;
    }

    /**
     * Retrieves list of document metadata associated with given document name
     * @param name document name
     * @return list of metadata {@link DocumentMetadata @DocumentMetadata}
     */
    public List<DocumentMetadata> findByName(String name) {
        return documentRepository.findByName(name);
    }

    /**
     * Retrieves list of document metadata with given document file extension
     * @param extension file name extension
     * @return list of metadata {@link DocumentMetadata @DocumentMetadata}
     */
    public List<DocumentMetadata> findByExtension(String extension) {
        return documentRepository.findByExtension(extension);
    }

    /**
     * Retrieves document content as stream
     * @param metadata document metadata for a required file
     * @return stream of contents of the required file {@link InputStream @InputStream}
     */
    public InputStream getDocumentStream(DocumentMetadata metadata) {
        if(metadata != null && metadata.getFilePath() != null) {
            return fileStorageService.getFileInputStream(metadata.getFilePath());
        } else {
            return null;
        }
    }

    /**
     * Retrieves document metadata by document id
     * @param id document id
     * @return metadata {@link DocumentMetadata @DocumentMetadata}
     */
    public DocumentMetadata findById(Long id) {
        return documentRepository.findOne(id);
    }

    /**
     * Delete document by document id
     * @param id document id
     */
    public void deleteDocument(Long id) {
        DocumentMetadata metadata = findById(id);
        if(metadata != null) {
            fileStorageService.deleteFile(metadata.getFilePath());
            documentRepository.delete(id);
        }
    }

    /**
     * Handles file upload with correspondent document description
     * Extracts and stores document metadata {@link DocumentMetadata @DocumentMetadata}
     * @param file uploaded file {@link MultipartFile @MultipartFile}
     * @param description document description
     * @return created document metadata {@link DocumentMetadata @DocumentMetadata}
     */
    public DocumentMetadata addDocument(MultipartFile file, String description) {
        DocumentMetadata documentMetadata = new DocumentMetadata(file, description);
        documentMetadata.setFilePath(fileStorageService.storeDocument(file));
        return documentRepository.save(documentMetadata);
    }

    /**
     * Retrieves list of document metadata containing a phrase in description
     * @param description a phrase in description
     * @return list of metadata {@link DocumentMetadata @DocumentMetadata}
     */
    public List<DocumentMetadata> findByDescriptionContaining(String description) {
        return documentRepository.findByDescriptionContaining(description);
    }

    /**
     * Schedules Job over a fixed intervals, controlled by the property
     * The job retrieves a list of uploaded documents over a last hour
     * and formats an email to a user, defined by the properties with
     * notification, containing uploaded documents metadata.
     * Uses email service to send a notification {@link EmailService @EmailService}
     */
    @Scheduled(fixedDelayString = "${document.fixedDelay}")
    public void lastHourUploadsNotification() {
        Date endDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        Date startDate = calendar.getTime();
        logger.info("send lastHourUploadsNotification Job ran at " + dateFormat.format(endDate));
        List<DocumentMetadata> documentMetadataList = documentRepository.findByCreateDateBetween(startDate, endDate);
        if(documentMetadataList != null && documentMetadataList.size() > 0) {
            emailService.send(
                    documentProperties.getTo(),
                    documentProperties.getFrom(),
                    "Uploaded Files",
                    formatDocumentsListing(documentMetadataList, startDate, endDate),
                    true);
        }
    }

    /**
     * Formats an html email body, containing uploaded documents metadata over a date range
     * @param documentMetadataList list of uploaded documents metadata  {@link DocumentMetadata @DocumentMetadata}
     * @param startDate start of the date range
     * @param endDate end of the date range
     * @return html formatted email body
     */
    private String formatDocumentsListing(List<DocumentMetadata> documentMetadataList, Date startDate, Date endDate) {
        try {
            Map data = new HashMap();
            data.put("documentMetadataList", documentMetadataList);
            data.put("startDate", startDate);
            data.put("endDate", endDate);
            return FreeMarkerTemplateUtils
                    .processTemplateIntoString(freemarkerConfiguration.getTemplate("emailBody.ftl"), data);
        } catch (Throwable t) {
            String msg = "Error parsing the email template";
            logger.error(msg, t);
            throw new ServiceException(msg, t);
        }
    }


}
