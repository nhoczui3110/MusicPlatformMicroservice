package com.MusicPlatForm.user_library_service.configuration;

import feign.codec.Encoder;
import feign.codec.Decoder;
import feign.form.spring.SpringFormEncoder;
import feign.optionals.OptionalDecoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import java.util.Collections;
@Configuration
public class FeignConfig {

}
