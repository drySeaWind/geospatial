/*
 * 2018 Sami.
 */
package com.iobionical.service.rest;

import com.iobionical.service.common.DataBaseConnection;
import com.iobionical.service.dao.FeatureDAO;
import java.sql.Connection;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Sami
 */
@Path("/trunks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GeoSpatialService {

    @Inject
    FeatureDAO dao;

    @Inject
    DataBaseConnection conn;

    @POST
    @Path("inarea")
    public Response getTrunksInArea(@NotNull JsonObject data) throws Exception {
        Connection con = conn.getDatabaseConnection();
        return Response.status(Response.Status.OK).entity(dao.fetchFeatures(con, data)).build();
    }

}
