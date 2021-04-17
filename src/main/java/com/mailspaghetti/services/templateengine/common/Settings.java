package com.mailspaghetti.services.templateengine.common;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties
@ConfigurationProperties

public class Settings {

    private String _authorizationToken;

    private String _repository;

    private String _chromePath;

    private String _serverHostName;

    @Value("${server.port}")
    private int _port;

    public int getPort(){
        return this._port;
    }

    public void setPort(int port){
        this._port = port;
    }

    /**
     * @return the _repository
     */
    public String getRepository() {
        return _repository;
    }

    /**
     * @return the _serverHostName
     */
    public String getServerHostName() {
        return _serverHostName;
    }

    /**
     * @param _serverHostName the _serverHostName to set
     */
    public void setServerHostName(String _serverHostName) {
        this._serverHostName = _serverHostName;
    }

    /**
     * @return the _chromePath
     */
    public String getChromePath() {
        return _chromePath;
    }

    /**
     * @param _chromePath the _chromePath to set
     */
    public void setChromePath(String _chromePath) {
        this._chromePath = _chromePath;
    }

    /**
     * @return the _authorizationToken
     */
    public String getAuthorizationToken() {
        return _authorizationToken;
    }

    /**
     * @param _authorizationToken the _authorizationToken to set
     */
    public void setAuthorizationToken(String _authorizationToken) {
        this._authorizationToken = _authorizationToken;
    }

    /**
     * @param _repository the _repository to set
     */
    public void setRepository(String _repository) {
        this._repository = _repository;
    }

    /**
     * checks if the url is valid then its a web repository otherwise it's a directory
     */
    public boolean isWebRepository(){
        String[] schemes = {"http","https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(this._repository);
    }

}

