package com.bsp.demo.configuration;



import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;

import org.apache.hc.core5.ssl.PrivateKeyDetails;
import org.apache.hc.core5.ssl.PrivateKeyStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.net.ssl.*;
import java.io.*;

import java.security.*;

import java.util.Map;


@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate buildRestTemplate() throws Exception {
		char[] password = "1234".toCharArray();
		KeyStore identityKeyStore = KeyStore.getInstance("pkcs12");
		identityKeyStore.load(getClass().getClassLoader().getResourceAsStream("keystore.p12"), password);

		SSLContext sslContext = SSLContextBuilder.create()
				.loadKeyMaterial(identityKeyStore,password,  new PrivateKeyStrategy() {
					@Override
					public String chooseAlias(Map<String, PrivateKeyDetails> map, SSLParameters sslParameters) {
						return "myalias";
					}
				})
				.loadTrustMaterial((cert,we)-> true)
				.build();

		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
		HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
				.setSSLSocketFactory(socketFactory)
				.build();

		CloseableHttpClient httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.build();



		ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

		return new RestTemplateBuilder().requestFactory(() -> requestFactory).build();
	}

}
