package com.mailspaghetti.services.templateengine.schedulers;

import com.mailspaghetti.services.templateengine.services.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PdfCleanUpScheduler {

    @Autowired
    private TemplateService templateService;

    @Scheduled(fixedRate = 5000)
    public void cleanUp() {
        this.templateService.removeOldDocuments();
    }
}
