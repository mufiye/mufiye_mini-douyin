package org.example.mufiye.utils;

import org.springframework.stereotype.Component;
import lombok.Data;
import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Component
@Data
@PropertySource("classpath:tencentcloud.properties")
@ConfigurationProperties(prefix = "tencent.cloud")
public class TencentCloudProperties {

    private String secretId;
    private String secretKey;

}
