package com.apa.common.error;

import org.springframework.beans.factory.annotation.Autowired;

public class ErrorMessageService {
    @Autowired
    private ErrorMessageRepository errorMessageRepository;

    public void save(ErrorMessage errorMessage) {
        errorMessageRepository.save(errorMessage);
    }
}
