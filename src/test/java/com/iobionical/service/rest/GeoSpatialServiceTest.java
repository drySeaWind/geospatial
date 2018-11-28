/*
 * 2018 Sami.
 */
package com.iobionical.service.rest;

import com.iobionical.service.common.DataBaseConnection;
import com.iobionical.service.dao.FeatureDAO;
import java.sql.Connection;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import org.junit.Test;
import mockit.Tested;

/**
 * @author Sami
 */
public class GeoSpatialServiceTest {

    @Tested
    private GeoSpatialService cut;

    @Injectable
    FeatureDAO dao;

    @Injectable
    DataBaseConnection conn;

    @Test
    public void testGetTrunksInArea(@Mocked Response resp, @Mocked JsonObject json, @Mocked Connection con) throws Exception {

        new Expectations() {
            {
                conn.getDatabaseConnection();
                times = 1;
                dao.fetchFeatures(con, json);
                times = 1;
            }
        };
        resp = cut.getTrunksInArea(json);
    }

}
