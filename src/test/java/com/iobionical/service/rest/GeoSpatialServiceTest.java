/*
 * 2018 Sami.
 */
package com.iobionical.service.rest;

import com.iobionical.service.common.DataBaseConnection;
import com.iobionical.service.dao.FeatureDAO;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import org.junit.Test;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;

/**
 * @author Sami
 */
@RunWith(JMockit.class)
public class GeoSpatialServiceTest {

    private final static String STATUS_OK = "ok";
    private final static String PATTERN = "e";

    @Tested
    private GeoSpatialService cut;

    @Injectable
    FeatureDAO dao;

    @Injectable
    DataBaseConnection conn;
//
//    @Injectable
//    ManagedExecutorService mes;
    // Check that async action is invoked when endpoint is called with correct payload

    @Test
    public void testMethod(@Mocked JsonObject data, @Mocked Response resp) throws Exception {

        cut.getTrunksInArea(data);

    }

}
