package api;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.*;
import java.util.ArrayList;

@Path("/vaccines")
public class VaccineHandler {

    private static final String URL = "jdbc:mariadb://localhost:3306/awror";
    private static final String USER = "root";
    private static final String PASS = "";

    @GET
    @Path("/pet/{petId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVaccines(@PathParam("petId") int petId) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String sql = "SELECT * FROM vaccines WHERE pet_id=?";
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, petId);

            ResultSet rs = ps.executeQuery();
            ArrayList<Vaccine> list = new ArrayList<>();

            while (rs.next()) {
                Vaccine v = new Vaccine();
                v.setId(rs.getInt("id"));
                v.setPetId(rs.getInt("pet_id"));
                v.setName(rs.getString("name"));
                v.setDateGiven(rs.getString("date_given"));
                v.setNextDate(rs.getString("next_date"));
                v.setNotes(rs.getString("notes"));
                list.add(v);
            }

            return Response.ok(list).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createVaccine(Vaccine v) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String sql = "INSERT INTO vaccines (pet_id, name, date_given, next_date, notes) VALUES (?, ?, ?, ?, ?)";
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, v.getPetId());
            ps.setString(2, v.getName());
            ps.setString(3, v.getDateGiven());
            ps.setString(4, v.getNextDate());
            ps.setString(5, v.getNotes());

            ps.executeUpdate();

            return Response.ok("{\"message\":\"Vaccine created successfully\"}").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateVaccine(@PathParam("id") int id, Vaccine v) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String sql = "UPDATE vaccines SET name=?, date_given=?, next_date=?, notes=? WHERE id=?";
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, v.getName());
            ps.setString(2, v.getDateGiven());
            ps.setString(3, v.getNextDate());
            ps.setString(4, v.getNotes());
            ps.setInt(5, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok("{\"message\":\"Vaccine updated successfully\"}").build();
            } else {
                return Response.status(404).entity("{\"error\":\"Vaccine not found\"}").build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

 
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteVaccine(@PathParam("id") int id) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String sql = "DELETE FROM vaccines WHERE id=?";
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok("{\"message\":\"Vaccine deleted successfully\"}").build();
            } else {
                return Response.status(404).entity("{\"error\":\"Vaccine not found\"}").build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
