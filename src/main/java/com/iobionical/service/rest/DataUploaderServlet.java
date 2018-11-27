/*
 * 2018 Sami.
 */
package com.iobionical.service.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.iobionical.service.common.DataBaseConnection;
import com.iobionical.service.dao.FeatureDAO;
import com.iobionical.service.model.Feature;
import com.iobionical.service.model.UploadStatus;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sami
 */
@WebServlet(name = "upload", value = "/upload")
public class DataUploaderServlet extends HttpServlet {

    private Logger logger = Logger.getLogger(DataUploaderServlet.class.getName());

    @Inject
    FeatureDAO dao;

    @Inject
    DataBaseConnection conn;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UploadStatus status = readStream(req.getInputStream());
        PrintWriter writer = resp.getWriter();
        writer.println("Uploaded: " + status.getUploaded() + " rows. " + status.getFailed() + " rows failed");
    }

    private UploadStatus readStream(InputStream in) {

        UploadStatus status = new UploadStatus();
        logger.log(Level.INFO, "Uploading data...");
        try {
            Connection con = conn.getDatabaseConnection();
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            Gson gson = new GsonBuilder().create();
            reader.beginArray();
            while (reader.hasNext()) {
                Feature feat = gson.fromJson(reader, Feature.class);
                if (!dao.insertRow(feat, con)) {
                    status.incrementFailed();
                } else {
                    status.incrementUploaded();
                }
            }
            reader.close();
            con.close();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Something went wrong during upload...");
        }
        logger.log(Level.INFO, "Upload complete...");
        return status;
    }

}
