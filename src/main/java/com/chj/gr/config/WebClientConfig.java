package com.chj.gr.config;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.chj.gr.config.properties.ServiceParamsProperties;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.web.client.InstanceWebClient;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final ServiceParamsProperties serviceParamsProperties;
	private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;

    public WebClientConfig(ServiceParamsProperties serviceParamsProperties,
                          ClientRegistrationRepository clientRegistrationRepository,
                          OAuth2AuthorizedClientRepository authorizedClientRepository) {
        this.serviceParamsProperties = serviceParamsProperties;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClientRepository = authorizedClientRepository;
    }
    
    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager() {
        OAuth2AuthorizedClientProvider clientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();
        DefaultOAuth2AuthorizedClientManager clientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);
        clientManager.setAuthorizedClientProvider(clientProvider);
        return clientManager;
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
        
        /**
         * 
         */
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Filter =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager());
        oauth2Filter.setDefaultOAuth2AuthorizedClient(true);
//        oauth2Filter.setDefaultClientRegistrationId("ms3");
        /**
         * 
         */
        
        
     // Listener Filter
        ExchangeFilterFunction loggingFilter = ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
        	if (clientRequest.url().getPort() == 1203) {
        		logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
                clientRequest.headers().forEach((name, values) -> 
                    values.forEach(value -> logger.info("Request Header: {}={}", name, value)));
			}
            return Mono.just(clientRequest);
        }).andThen(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
        	if (clientResponse.statusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
	        	logger.info("Response: Status {}", clientResponse.statusCode());
	            clientResponse.headers().asHttpHeaders().forEach((name, values) -> 
	                values.forEach(value -> logger.info("Response Header: {}={}", name, value)));
        	}
            // Optionally log response body (be cautious with large bodies)
            return clientResponse.bodyToMono(String.class)
                    .doOnNext(body -> logger.info("Response Body: {}", body))
                    .thenReturn(clientResponse);
        }));
        
        
        
        return WebClient.builder()
        		.clientConnector(new ReactorClientHttpConnector(httpClient))
        		/**
        		 * 
        		 */
                .filter(oauth2Filter)
                .filter(loggingFilter);
    }
    
    @Bean
    InstanceWebClient instanceWebClient(AdminServerProperties adminServerProperties) throws Exception {
    	
    	WebClient.Builder sslWebClientBuilder = this.sslWebClientBuilder();
        return InstanceWebClient.builder()
                .webClient(sslWebClientBuilder)
                .build();
    }
}