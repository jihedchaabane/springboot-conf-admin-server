package com.chj.gr.config;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.chj.gr.config.properties.ServiceParamsProperties;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.web.client.InstanceWebClient;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final ServiceParamsProperties serviceParamsProperties;

	public WebClientConfig(ServiceParamsProperties serviceParamsProperties) {
		this.serviceParamsProperties = serviceParamsProperties;
	}

	/**
	 * Configuration adéquate pour que les APIs en ****HTTPS/SSL**** puissent s'enregistrer avec le status UP avec springboot admin.
	 * 
	 * Erreur d'origine avant la résolution:
	  		exception org.springframework.web.reactive.function.client.WebClientRequestException
			message	PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: 
			unable to find valid certification path to requested target; nested exception is javax.net.ssl.SSLHandshakeException: 
			PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: 
			unable to find valid certification path to requested target 
	 */
    @Bean
    WebClient.Builder sslWebClientBuilder() throws Exception {
        KeyStore trustStore = KeyStore.getInstance("JKS");
        try (FileInputStream trustStoreStream = new FileInputStream(this.serviceParamsProperties.getTruststore().getPath())) {
            trustStore.load(trustStoreStream, this.serviceParamsProperties.getTruststore().getPassword().toCharArray());
            logger.info("Truststore loaded successfully from {}", serviceParamsProperties.getTruststore().getPath());
        }

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(trustManagerFactory)
                .build();
        HttpClient httpClient = HttpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }
    
    @Bean
    InstanceWebClient instanceWebClient(AdminServerProperties adminServerProperties) throws Exception {
    	
    	WebClient.Builder sslWebClientBuilder = this.sslWebClientBuilder();
        return InstanceWebClient.builder()
                .webClient(sslWebClientBuilder)
                .build();
    }
}