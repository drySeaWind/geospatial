/*
 * 2018 Sami.
 */
package com.iobionical.service.dao;

import com.iobionical.service.common.DataBaseConnection;
import com.iobionical.service.model.Feature;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author sami
 */
public class FeatureDAOTest {

    @Tested
    FeatureDAO cut;

    @Injectable
    DataBaseConnection conn;

    @Test
    public void testRowInsertCase(@Mocked Connection con, @Mocked Statement stmt) throws Exception {
        Feature feat = new Feature();

        new Expectations() {
            {
                con.createStatement();
                times = 1;
                cut.getInsertSqlAsString(feat);
                times = 1;
                stmt.executeUpdate(anyString);
                times = 1;
            }
        };
        boolean test = cut.insertRow(feat, con);
        assertTrue(test = true);
    }

    @Test
    public void testFetchData(@Mocked JsonObject json, @Mocked Connection con, @Mocked Statement stmt) throws Exception {

        new Expectations() {
            {
                con.createStatement();
                times = 1;
                stmt.executeQuery(anyString);
                times = 1;
            }
        };
        cut.fetchFeatures(con, json);
    }

    @Test
    public void testCreateTable(@Mocked Connection con, @Mocked Statement stmt) throws Exception {

        new Expectations() {
            {
                conn.getDatabaseConnection();
                times = 1;
                con.createStatement();
                times = 1;
                cut.getInitSqlAsString();
                times = 1;
                stmt.executeUpdate(anyString);
                times = 1;
                conn.closeStatement(stmt);
                times = 1;
                con.close();
                times = 1;
            }
        };
        cut.createTable();
    }

    @Test
    public void testSqlInsertString() throws Exception {
        Feature feat = new Feature();
        feat.setX(123);
        feat.setY(456);
        feat.setSpecies("testspecies");
        feat.setArea(678);
        String test = cut.getInsertSqlAsString(feat);
        assertTrue(test.contains("testspecies"));
        assertTrue(test.contains("123"));
        assertTrue(test.contains("456"));
        assertTrue(test.contains("678"));
    }

    @Test
    public void testSqlQueryString() throws Exception {

        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        jsonBuilder.add("type", "polygon");
        jsonBuilder.add("coordinates", "[[ [ 22.367672757355923, 60.847698232804973 ], "
                + "[ 22.366103072838264, 60.848481594419098 ], [ 22.36615704790329, 60.848501204978056 ]"
                + ", [ 22.366394580499936, 60.848582391507655 ]]]");
        String test = cut.getQuerySqlAsString(jsonBuilder.build());
        assertTrue(test.contains("polygon"));
        assertTrue(test.contains("3676727"));
        assertTrue(test.contains("3661030728"));
        assertTrue(test.contains("22.366103072838264"));
    }

    @Test
    public void testSqlTableCreateString() throws Exception {
        String test = cut.getInitSqlAsString();
        assertTrue(test.contains("area"));
        assertTrue(test.contains("height"));
        assertTrue(test.contains("species"));
        assertTrue(test.contains("4326"));
    }

}
