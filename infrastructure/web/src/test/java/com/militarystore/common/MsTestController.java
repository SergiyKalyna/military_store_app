package com.militarystore.common;

import com.militarystore.exception.MsNotFoundException;
import com.militarystore.exception.MsValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.ServiceUnavailableException;

@RestController
public class MsTestController {

    @GetMapping("/test")
    public void test() {
        throw new MsValidationException("error message");
    }

    @GetMapping("/test/404")
    public void testNotFound() {
        throw new MsNotFoundException("not found");
    }

    @GetMapping("/test/500")
    void testInternalErrorEndpoint() {
        try {
            throw new ServiceUnavailableException("Service Unavailable");
        } catch (ServiceUnavailableException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service Unavailable", e);
        }
    }
}
