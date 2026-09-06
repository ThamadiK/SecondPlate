package com.secondplate.app.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("loggedInUserId")
    public Long loggedInUserId(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }
}
