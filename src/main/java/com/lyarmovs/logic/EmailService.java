package com.lyarmovs.logic;

import com.lyarmovs.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * @author Lev Yarmovsky
 * @version $Id: EmailService.java,v 1.0 4/2/2017 2:47 PM lyarmovs Exp $
 *
 * Email sending service
 * Configured in application.properties to use gmail smtp service
 * Test Google account have to use relaxed security settings
 */
@Service
public class EmailService {
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends email
     * @param to - recipient email address
     * @param from - sender email address
     * @param subject - email subject
     * @param body - email body
     * @param isHtml - is email body html formatted
     */
    public void send(String to, String from, String subject, String body, boolean isHtml) {
        try {
             mailSender.send(msg -> {
                MimeMessageHelper message = new MimeMessageHelper(msg,
                        true);
                message.setTo(to);
                message.setFrom(from);
                message.setSubject(subject);
                message.setText(body, isHtml);
            });
        } catch (Throwable t) {
            String msg = "Error sending email for " + to;
            logger.error(msg, t);
            throw new ServiceException(msg, t);
        }
    }

}
