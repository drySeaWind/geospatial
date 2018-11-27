/*
 * 2018 Sami.
 */
package com.iobionical.service.common;

import com.iobionical.service.dao.FeatureDAO;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author sami
 */
@Singleton
@Startup
public class ServiceInitializer {

    @Inject
    FeatureDAO dao;

    /**
     * Inits database. Runs during startup and creates a table in case of empty
     * database
     */
    @PostConstruct
    protected void initDatabase() {
        dao.createTable();
    }

}
