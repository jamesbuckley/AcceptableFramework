package com.buckley.AcceptableFramework.config;

import com.buckley.AcceptableFramework.interceptors.RecorderInterceptor;
import com.buckley.AcceptableFramework.models.RequestModel;
import com.buckley.AcceptableFramework.utils.RequestResponseWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import static java.util.Arrays.stream;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class AcceptableConfig extends SpringBootServletInitializer implements WebMvcConfigurer {

    @Value("${acceptable.output.filename}")
    private String PROPERTIES_FILENAME;

    @Value("${acceptable.query.param.ignore}")
    public String[] paramsToIgnoreProps;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new RecorderInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public RequestResponseWriter requestResponseWriter() {
        return new RequestResponseWriter();
    }

    @Bean
    public RequestModel requestModel(){
        return new RequestModel();
    }

    @Bean
    Properties userProperties() throws IOException {
        final Resource[] possiblePropertiesResources = {
                new ClassPathResource(PROPERTIES_FILENAME),
                new PathResource("config/" + PROPERTIES_FILENAME),
                new PathResource(PROPERTIES_FILENAME)
        };
        // Find the last existing properties location to emulate spring boot application.properties discovery
        final Resource propertiesResource = stream(possiblePropertiesResources)
                .filter(Resource::exists)
                .reduce((previous, current) -> current)
                .get();
        final Properties userProperties = new Properties();

        userProperties.load(propertiesResource.getInputStream());

        populateComponentWithUserProperties(userProperties);

        return userProperties;
    }

    private void populateComponentWithUserProperties(Properties userProperties){
        for(String propertyName : userProperties.stringPropertyNames()){
            if(propertyName.substring(0,10).equals("acceptable")){
                String queryNumber = propertyName.substring(propertyName.indexOf("["), propertyName.indexOf("]")+1);
                if(propertyName.substring(11,14).equals("get")){
                    String response = userProperties.getProperty("acceptable.get.requests"+queryNumber+".response");
                    String url = userProperties.getProperty("acceptable.get.requests"+queryNumber+".url");
                    String query = userProperties.getProperty("acceptable.get.requests"+queryNumber+".query");
                    requestModel().createGetRequest(query, url, response);
                }else if(propertyName.substring(11,15).equals("post")){
                    String response = userProperties.getProperty("acceptable.post.requests"+queryNumber+".response");
                    String url = userProperties.getProperty("acceptable.post.requests"+queryNumber+".url");
                    String query = userProperties.getProperty("acceptable.post.requests"+queryNumber+".query");
                    requestModel().createPostRequest(query, url, response);
                }
            }
        }
        System.out.println("Do Things");
    }

}
