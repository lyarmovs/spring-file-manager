package com.lyarmovs.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Lev Yarmovsky
 * @version $Id: DocumentConfig.java,v 1.0 4/1/2017 3:24 PM lyarmovs Exp $
 *
 * Configuration for Document Management services
 */
@Configuration
public class DocumentConfig extends WebMvcConfigurerAdapter {
    /**
     * Prevents path extension in the URL path to be used to determine
     * the requested media type.
     * @param configurer content negotiation configurer
     */
    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
        // Turn off suffix-based content negotiation
        configurer.favorPathExtension(false);
    }

    /**
     * Bean that creates a FreeMarker Configuration and provides it as
     * bean reference.
     * Specifies a template path for ftl templates
     * @return bean reference
     */
    @Bean(name ="freemarkerConfiguration")
    public FreeMarkerConfigurationFactoryBean freemarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean freemarkerConfiguration = new FreeMarkerConfigurationFactoryBean();
        freemarkerConfiguration.setTemplateLoaderPath("classpath:/templates");
        return freemarkerConfiguration;
    }
}
