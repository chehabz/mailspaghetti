package com.mailspaghetti.services.templateengine.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import com.mailspaghetti.services.templateengine.TemplateEngineApplication;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.system.ApplicationHome;


public class DirectoryHelper {

    private static  Logger _logger = LoggerFactory.getLogger(DirectoryHelper.class);
  

    public static String getRootDirectory() {
        ApplicationHome home = new ApplicationHome(TemplateEngineApplication.class);
        String path = home.getSource() == null ? home.getDir().getPath() : home.getSource().getParent();
        if(path.contains("target"))
            return  Paths.get(path).getParent().toString();
        return path;
    }

    public static String joinFromRoot(String path) {
        String out =  getRootDirectory() + "/" + path;
        return out;
    }

    /**
     * Creates a directory if not exists
     * @param directory
     * @return
     */
    public static Boolean createIfNotExists(String directory) {

        File dir = new File(joinFromRoot(directory));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return true;
    }

    /**
     * Creates a file in a specific folder and returns the path
     * @param directory
     * @param path
     * @param content
     * @return
     * @throws IOException
     */
    public static String createFromString(String directory, String path, String content) throws IOException {
        FileWriter fileWriter = new FileWriter( joinFromRoot(directory) + "/" + path );
        fileWriter.write( content ) ;
        fileWriter.flush();
        fileWriter.close();
        return joinFromRoot(directory) + "/" + path;
    }

    /**
     * force deletes a file
     * @param path
     */
    public static void deleteFile(String path){
        File file = new File(path);
        file.delete();
    }

    /**
     * Read a content from a file;
     * @param path
     * @return
     */
    public static String readStringFromFile(String path) {
        File file = new File(path);
        String content = null;

        try {
            content = FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String toUrl( String parent, String file , Settings settings ){
        return parent + "/" +file;      
    }

    public static String getUnixFile(){
        return "file:///"; 
    }

    public static String getLocalUrl(String path) {
        return getUnixFile() + "/" + getRootDirectory()  + "/"  + path ;
    }

    public static boolean isDirectory(String path){

        File file = new File(path);
        return file.isDirectory();
    }

    /**
     *
     * @param source
     * @param destination
     */
    public static void copyTo(String source, String destination) throws IOException {
        File sourceDirectory = new File(source);
        File destinationDirectory = new File (destination);
        FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
    }
}