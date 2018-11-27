/*
 * 2018 Sami.
 */
package com.iobionical.service.rest;

import com.iobionical.service.common.DataBaseConnection;
import com.iobionical.service.dao.FeatureDAO;
import com.iobionical.service.model.UploadStatus;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Test;

/**
 *
 * @author Sami
 */
public class DataUploaderServletTest {

    @Tested
    DataUploaderServlet cut;

    @Injectable
    FeatureDAO dao;

    @Injectable
    DataBaseConnection conn;

    @Injectable
    PrintWriter writer;

    private final static String TEST = "some_test_stuff_for_stream";

    /**
     * test that:. InputStream is invoked, PrintWriter is invoked, Correct
     * response is written. Private methods are not tested
     *
     * @param req mocked request
     * @param resp mocked response
     */
    @Test
    public void isAllFunctionsInvoked(@Mocked HttpServletRequest req, @Mocked HttpServletResponse resp) throws Exception {

        InputStream is = new ByteArrayInputStream(TEST.getBytes());
        UploadStatus us = new UploadStatus();

        new Expectations() {
            {
                req.getInputStream();
                times = 1;
                resp.getWriter();
                times = 1;
                result = writer;
                writer.println("Uploaded: " + us.getUploaded() + " rows. " + us.getFailed() + " rows failed");
                times = 1;
            }
        };
        cut.doPost(req, resp);
    }

}
