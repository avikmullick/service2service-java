package com.sap.cc.ascii;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BaseUrlProvider {
    @Value("${service.ascii.url.base}")
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }
}
