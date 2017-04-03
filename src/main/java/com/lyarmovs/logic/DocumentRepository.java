package com.lyarmovs.logic;

import com.lyarmovs.model.DocumentMetadata;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * @author Lev Yarmovsky
 * @version $Id: DocumentRepository.java,v 1.0 3/31/2017 2:46 PM lyarmovs Exp $
 *
 * JPA CRUD operations for {@link DocumentMetadata @DocumentMetadata} type
 */
public interface DocumentRepository extends CrudRepository<DocumentMetadata, Long> {
    /**
     * Find document metadata by document file name
     * @param name document file name
     * @return list of document metadata
     */
    public List<DocumentMetadata> findByName(String name);

    /**
     * Find document metadata by document file extension
     * @param extension document file extension
     * @return list of document metadata
     */
    public List<DocumentMetadata> findByExtension(String extension);

    /**
     * Find document metadata with description containing the required phrase
     * @param description a phrase, contained in document description
     * @return list of document metadata
     */
    public List<DocumentMetadata> findByDescriptionContaining(String description);

    /**
     * Find document metadata within a date range of Creation Date
     * @param startDate start of the date range
     * @param endDate end of the date range
     * @return list of document metadata
     */
    public List<DocumentMetadata> findByCreateDateBetween(Date startDate, Date endDate);
}
