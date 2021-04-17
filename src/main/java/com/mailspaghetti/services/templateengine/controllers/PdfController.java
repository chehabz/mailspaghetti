package com.mailspaghetti.services.templateengine.controllers;

import java.util.Calendar;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mailspaghetti.services.templateengine.models.Response;
import com.mailspaghetti.services.templateengine.services.TemplateService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController

public class PdfController {

    private  Logger _logger = LoggerFactory.getLogger(PdfController.class);

    @Autowired
    private TemplateService _templateService;

    @RequestMapping("/ping")
    public Response ping() {
        return new Response(true, String.format("%s", Calendar.getInstance().getTime()));
    }

    @RequestMapping("/pdf/create")
    public Response generate(@RequestParam Map<String, Object> params) {

        if (!params.containsKey("template"))
            return new Response(false, "required [template] parameter is missing");

        try {

            String url = this._templateService.createPdf( params.get("template").toString(),  params);

            return new Response(true, url);
        }
        catch (Exception e) {
            _logger.error("Exception: Failed to generate the pdf document", e);            
            return new Response(e);
        }
    }
}