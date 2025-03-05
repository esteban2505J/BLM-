package com.service.branch.client;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "msvc-user", url = "http://localhost:9090/auth")
public interface AuthClient {
}
