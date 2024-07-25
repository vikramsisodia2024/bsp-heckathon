package com.bsp.demo.configuration;


import com.credit.invoker.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class ApiConfiguration {

    @Autowired
    private RestTemplate buildRestTemplate;

    @Bean
    @Primary
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ApiClient apiClient(@Value("${api.schema}") String schema,
                               @Value("${api.host}") String host,
                               @Value("${api.path}") String path) {


        final ApiClient apiClient = new ApiClient(buildRestTemplate);

        apiClient.setBasePath(UriComponentsBuilder.newInstance()
                .scheme(schema)
                .host(host)
                .path(path)
                .build()
                .toUriString());

        return apiClient;
     }


    @Bean
    @Primary
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public com.aws.invoker.ApiClient apiClientAWS(@Value("${aws.schema}") String schema,
                                                         @Value("${aws.host}") String host,
                                                         @Value("${aws.path}") String path) {

        final com.aws.invoker.ApiClient apiClient = new com.aws.invoker.ApiClient();

        apiClient.setBasePath(UriComponentsBuilder.newInstance()
                .scheme(schema)
                .host(host)
                .path(path)
                .build()
                .toUriString());

        return apiClient;
    }

}




