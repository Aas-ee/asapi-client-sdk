package com.aasee.asapiclientsdk;

import com.aasee.asapiclientsdk.client.AaseeApiClient;
import com.aasee.asapiclientsdk.model.User;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;

@Configuration
@ConfigurationProperties("asapi.client")
@Data
@ComponentScan
public class AaseeApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public AaseeApiClient aaseeApiClient() throws UnsupportedEncodingException {
//        String accessKey = "aasee";
//
//        String secretKey = "abcdefghijk";
        return new AaseeApiClient(accessKey,secretKey);
//        AaseeApiClient aaseeApiClient = new AaseeApiClient(accessKey,secretKey);
//        String aasee = aaseeApiClient.getNameByGet("Aasee");
//        String yupi = aaseeApiClient.getNameByPost("鱼皮");
//        User user = new User();
//        user.setUsername("鱼皮哈哈");
//        String Aasee1111 = aaseeApiClient.getUserNameByPost(user);
    }
}
