package com.lyarmovs.service;

import com.lyarmovs.SpringFileManagerBaseTest;
import com.lyarmovs.conf.DocumentProperties;
import com.lyarmovs.logic.DocumentService;
import com.lyarmovs.logic.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author Lev Yarmovsky (lev.yarmovsky@cgifederal.com)
 * @version $Id: EmailServiceTest.java,v 1.0 4/2/2017 11:26 AM lyarmovs Exp $
 *
 *
 * Set of SpringBootTest {@link SpringBootTest @SpringBootTest} test cases
 * for Email service {@link EmailService @EmailService}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmailServiceTest extends SpringFileManagerBaseTest {
    protected final Logger logger = LoggerFactory.getLogger(EmailServiceTest.class);
    @Autowired
    private EmailService emailService;
    @Autowired
    private DocumentProperties documentProperties;

    @Test
    public void lastHourUploadsNotificationTest() {
        try {
            emailService.send(documentProperties.getTo(), documentProperties.getFrom(), "Test Subject", "Test Body", false);
        }catch (Throwable t) {
            logger.error("Error sending notifications", t);
            fail("Error sending notifications", t);
        }
    }

}
