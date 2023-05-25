package mn.delivery.system.resttemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author digz6666
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RestTemplateUtil {

    private final ObjectMapper objectMapper;

    private static final int TIMEOUT = (int) TimeUnit.SECONDS.toMillis(10);

    private List<MediaType> getMediaTypes() {
        return Collections.singletonList(MediaType.APPLICATION_JSON);
    }

    public String buildUrl(String baseUrl, Map<String, Object> uriVars) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
        for (Map.Entry<String, Object> entry : uriVars.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        return builder.build().toUriString();
    }

    public HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public List<HttpMessageConverter<?>> getMessageConvertersWithMedia() {
        return List.of(
                stringHttp(), formHttp(), mappingJackson2Http()
        );
    }

    public List<HttpMessageConverter<?>> getMessageConverters() {
        return List.of(
                new StringHttpMessageConverter(StandardCharsets.UTF_8),
                new FormHttpMessageConverter(),
                new MappingJackson2HttpMessageConverter()
        );
    }

    public MappingJackson2HttpMessageConverter mappingJackson2Http() {
        MappingJackson2HttpMessageConverter mappingJackson2Http = new MappingJackson2HttpMessageConverter();
        mappingJackson2Http.setSupportedMediaTypes(getMediaTypes());
        mappingJackson2Http.setObjectMapper(objectMapper);
        mappingJackson2Http.setDefaultCharset(StandardCharsets.UTF_8);
        return mappingJackson2Http;
    }

    public StringHttpMessageConverter stringHttp() {
        StringHttpMessageConverter stringHttp = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringHttp.setSupportedMediaTypes(getMediaTypes());
        stringHttp.setDefaultCharset(StandardCharsets.UTF_8);
        return stringHttp;
    }

    public FormHttpMessageConverter formHttp() {
        FormHttpMessageConverter formHttp = new FormHttpMessageConverter();
        formHttp.setSupportedMediaTypes(getMediaTypes());
        formHttp.setCharset(StandardCharsets.UTF_8);
        return formHttp;
    }

    public SimpleClientHttpRequestFactory httpRequestFactoryWithTimeout() {
        SimpleClientHttpRequestFactory requestFactory = httpRequestFactory();
        requestFactory.setConnectTimeout(TIMEOUT);
        requestFactory.setReadTimeout(TIMEOUT);
        return requestFactory;
    }

    public SimpleClientHttpRequestFactory httpRequestFactory() {
        return new SimpleClientHttpRequestFactory();
    }

    public List<ClientHttpRequestInterceptor> httpRequestInterceptors() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new LoggingRequestInterceptor(log));
        return interceptors;
    }
}
