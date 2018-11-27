/*
 * 2018 Sami.
 */
package com.iobionical.service.dao;

import com.iobionical.service.common.DataBaseConnection;
import com.iobionical.service.model.Feature;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.naming.NamingException;

/**
 *
 * @author sami
 */
public class FeatureDAO {

    @Inject
    DataBaseConnection conn;

    private static final Logger logger = Logger.getLogger(FeatureDAO.class.getName());
    private static final int TM35FIN_SRID = 3067;
    private static final int WGS84_SRID = 4326;
    private static final String AREA = "area";
    private static final String HEIGHT = "height";
    private static final String SPECIES = "species";
    private static final String LOC = "loc";
    private static final String X = "X1";
    private static final String Y = "Y1";

    /**
     * stores a new row into feature table. Converts TM35FIN (SRID3067)
     * coordinates into WGS84 (SRID 4326) in PostGIS
     *
     * @param feat object to be inserted. Holds coordinates in TM35FIM
     * coordinate system
     * @param con Connection from connection pool
     * @return boolean true for successful insert. false for failed.
     */
    public boolean insertRow(Feature feat, Connection con) throws SQLException {

        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(getInsertSqlAsString(feat));
            return true;
        } catch (SQLException pse) {
            logger.log(Level.SEVERE, "insert row failed", pse);
            return false;
        } finally {
            stmt.close();
        }

    }

    /**
     * fetches geometry points. ..whose are inside area of shape defined by
     * geojson input object.
     *
     * @param con Connection from connection pool
     * @return
     */
    public List<Feature> fetchFeatures(Connection con, JsonObject json) throws SQLException {

        Statement stmt = null;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(getQuerySqlAsString(json));
            return getResultsAsFeatureArray(rs);
        } finally {
            stmt.close();
        }
    }

    /**
     * creates a table. should be only used during initialization of app. If
     * table already exists exception is handled and logged.
     */
    public void createTable() {
        try {
            Connection cnt = conn.getDatabaseConnection();
            Statement stmt = cnt.createStatement();
            stmt.executeUpdate(getInitSqlAsString());
            conn.closeStatement(stmt);
            cnt.close();
            logger.log(Level.INFO, "Success! Created table into PostGIS");

        } catch (NamingException ex) {
            logger.log(Level.SEVERE, "Failed getting connection from connection pool", ex);
        } catch (SQLException pse) {
            logger.log(Level.SEVERE, "Table already exists or connection failed");
        }
    }

    public List<Feature> getResultsAsFeatureArray(ResultSet resultSet) {

        List<Feature> results = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Feature feat = new Feature();
                feat.setArea(resultSet.getDouble(AREA));
                feat.setHeight(resultSet.getDouble(HEIGHT));
                feat.setSpecies(resultSet.getString(SPECIES));
                feat.setX(resultSet.getDouble(X));
                feat.setY(resultSet.getDouble(Y));
                results.add(feat);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Problem reading query resultset", ex);
        }
        return results;
    }

    public String getInsertSqlAsString(Feature feat) {
        return "INSERT INTO feature "
                + "(" + AREA + "," + HEIGHT + "," + SPECIES + "," + LOC + ") VALUES('"
                + feat.getArea() + "','"
                + feat.getHeight() + "','"
                + feat.getSpecies() + "',"
                + "ST_Transform(ST_GeomFromText('POINT(" + feat.getX() + " " + feat.getY() + ")',"
                + TM35FIN_SRID + ")," + WGS84_SRID + "))";
    }

    public String getQuerySqlAsString(JsonObject json) {
        return "SELECT " + AREA + "," + HEIGHT + "," + SPECIES + ","
                + "ST_X(" + LOC + ") AS X1, ST_Y(" + LOC + ") AS Y1 "
                + "from feature where "
                + "ST_Within(loc, "
                + "ST_SetSRID("
                + "ST_GeomFromGeoJSON('" + json + "')," + WGS84_SRID + "))";
    }

    public String getInitSqlAsString() {
        return "CREATE TABLE feature (id serial primary key, "
                + AREA + " decimal(30,28), "
                + HEIGHT + " decimal(30,28), "
                + SPECIES + " varchar(100), "
                + LOC + " geometry(Point," + WGS84_SRID + "))";
    }

}
