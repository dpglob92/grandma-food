package com.javastudio.grandmafood;

import com.javastudio.grandmafood.common.exceptions.ApplicationException;
import com.javastudio.grandmafood.common.exceptions.ExceptionCode;
import com.javastudio.grandmafood.common.exceptions.InvalidInputException;
import com.javastudio.grandmafood.errors.CustomerNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/errors")
@ConditionalOnProperty("config.utils.enable-dummy-errors-controller")
public class DummyErrorController {

    @GetMapping("/application")
    public String myApplicationExc() {
        throw new ApplicationException("this is app exception", ExceptionCode.NOT_FOUND, "DUMMY-ERROR");
    }

    @GetMapping("/invalid")
    public String myInvalidInputExc() {
        InvalidInputException ex = new InvalidInputException();
        ex.addError("email", "the field is an invalid email");
        throw ex;
    }

    @GetMapping("/custom")
    public String myCustomExc() {
        throw new CustomerNotFoundException();
    }
}
