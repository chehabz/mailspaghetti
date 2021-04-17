package com.mailspaghetti.services.templateengine.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.antlr.stringtemplate.StringTemplate;

import com.mailspaghetti.services.templateengine.common.DirectoryHelper;
import com.mailspaghetti.services.templateengine.common.Settings;
import com.mailspaghetti.services.templateengine.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javassist.NotFoundException;

import com.mashape.unirest.http.exceptions.UnirestException;

import com.mailspaghetti.services.templateengine.chrome.ChromeContext;

@Component
public class TemplateService {

    private static final String PROCESSOR_DIRECTORY = "html-templates";
    private static final String DOCUMENTS_DIRECTORY = "webapp/documents";

    @Autowired
    private Settings settings;

    /**
     * Constructor
     * @param _settings
     */
    public TemplateService(Settings _settings) {
        this.settings = _settings;
    }

    /**
     * 
     * @return String the template Content
     * @throws UnirestException
     * @throws IOException
     * @throws NotFoundException
     */
    public String createPdf (String template, Map<String, Object> params) throws IOException, NotFoundException {

        //setup the initial directories
        DirectoryHelper.createIfNotExists(PROCESSOR_DIRECTORY);
        DirectoryHelper.createIfNotExists(DOCUMENTS_DIRECTORY);

        /*
            when working with local directories
            css does not loads properly
            so if the template is a directory
            we copy the entire content once
            we also compare the modified date of the folder
            with our folder
            if any changes we overwrite.
        */

        String path = this.settings.getRepository() + "/" + template;

        /*
            lets first check if this directory is available in our
            staging folder
        */

        String stagingDirectory = Paths.get(PROCESSOR_DIRECTORY, template).toString();

        if(DirectoryHelper.isDirectory(stagingDirectory)) {
            //use that directory
            //why copy when we can simply use the same one?
            //todo: probably sometime later we can check the last modified date
           return this.generatePdfFromTemplatedDirectory ( stagingDirectory , params);
        }

        //check if the template folder exists
        String templateDirectory = Paths.get( this.settings.getRepository(), template).toString();

        if(!DirectoryHelper.isDirectory( templateDirectory ))
            throw new FileNotFoundException("Template Directory Not Found");

        //copy the templates folder to the templates directory
        DirectoryHelper.copyTo( templateDirectory  , stagingDirectory );

        //generate a pdf document and return the link to the requester;
        return this.generatePdfFromTemplatedDirectory ( stagingDirectory , params);
    }

    /**
     *
     * @param path
     * @return string the pdf url document
     */
    private String generatePdfFromTemplatedDirectory(String path, Map<String, Object> params) throws IOException {

        String guid = java.util.UUID.randomUUID().toString();

        ChromeContext context = new ChromeContext();

        //open the index.html file
        String htmlDocument = Paths.get(path, "index.html").toString();

        //read the raw content;
        String rawContent = DirectoryHelper.readStringFromFile( htmlDocument );

        //read & tokenize the content
        String stringTemplate = this.processTemplate( rawContent, params );

        //prepare the html file
        String tokenizedContentPath = DirectoryHelper.createFromString(path, guid + ".html", stringTemplate);

        String pdf = DirectoryHelper.joinFromRoot(DOCUMENTS_DIRECTORY) + String.format("/%s.pdf", guid);

        String pageURL = "file:///" + tokenizedContentPath;

        //generate pdf from the headless chrome browser
        context.generatePDF( Paths.get(pdf),  pageURL, this.settings.getChromePath());

        //delete  the html generated file
        DirectoryHelper.deleteFile(Paths.get(path, guid + ".html").toString());

        return String.format("%s/documents/%s.pdf", this.settings.getServerHostName(), guid);
    }

    /***
     * 
     * @param content
     * @param params
     * @return
     */
    private String processTemplate(String content, Map<String, Object> params){
        
        StringTemplate stringTemplate = new StringTemplate(content);
   
        params.keySet().forEach((param)->{
            if(!param.equals("template")){
                Object value = params.get(param);    
                
                if(param.equals("data")){
                    JSONObject data = new JSONObject(value.toString());
                    stringTemplate.setAttribute(param, data);
                }
                else
                    stringTemplate.setAttribute(param, value);
            }
        });

        return stringTemplate.toString();
    }

    /**
     * Remove old documents sitting in the documents directory
     * it will keep documents which 10 minutes of Age
     */
    public void removeOldDocuments() {

        String documentsDirectory = DirectoryHelper.joinFromRoot(DOCUMENTS_DIRECTORY);

        if(!DirectoryHelper.isDirectory(documentsDirectory))
            return;

        Date now = new Date();
        Date threshold = new Date(now.getTime() - 100000);

        Iterator<File> filesToDelete = FileUtils.iterateFiles(new File(documentsDirectory),
                new AgeFileFilter(threshold),
                new AgeFileFilter(threshold));

        for (Iterator<File> it = filesToDelete; it.hasNext(); ) {
            File aFile = it.next();
            aFile.delete();
        }
    }
}