package learning.center.uz.controller;

import learning.center.uz.exp.AppBadException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(AppBadException.class)
    public String handler(AppBadException e) {
        e.printStackTrace();
        return "index";
    }

}
