package api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Base64;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/pets")
public class PetHandler {

    private static final String URL = "jdbc:mariadb://localhost:3306/awror";
    private static final String USER = "root";
    private static final String PASS = "";

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPet(Pet pet) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String sql = "INSERT INTO pets (user_id, name, type, image) VALUES (?, ?, ?, ?)";
            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, pet.getUser_id());
                ps.setString(2, pet.getName());
                ps.setString(3, pet.getType());

                if (pet.getImage() != null && !pet.getImage().isEmpty()) {
                    String img = pet.getImage();
                    if (img.contains(","))
                        img = img.split(",")[1];
                    byte[] imageBytes = Base64.getDecoder().decode(img);
                    ps.setBytes(4, imageBytes);
                } else {
                    ps.setNull(4, Types.BLOB);
                }

                ps.executeUpdate();
                return Response.ok("{\"message\":\"Pet created successfully\"}").build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPetsByUser(@PathParam("id") int id) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String sql = "SELECT * FROM pets WHERE user_id=? ORDER BY date DESC";
            ArrayList<Pet> list = new ArrayList<>();

            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Pet p = new Pet();
                    p.setId(rs.getInt("id"));
                    p.setUser_id(rs.getInt("user_id"));
                    p.setName(rs.getString("name"));
                    p.setType(rs.getString("type"));

                    byte[] imgBytes = rs.getBytes("image");
                    if (imgBytes != null && imgBytes.length > 0) {
                        p.setImage(Base64.getEncoder().encodeToString(imgBytes));
                    }

                    p.setDate(rs.getString("date"));
                    list.add(p);
                }

                return Response.ok(list).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPetById(@PathParam("id") int id) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String sql = "SELECT * FROM pets WHERE id=?";
            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Pet p = new Pet();
                    p.setId(rs.getInt("id"));
                    p.setUser_id(rs.getInt("user_id"));
                    p.setName(rs.getString("name"));
                    p.setType(rs.getString("type"));

                    byte[] imgBytes = rs.getBytes("image");
                    if (imgBytes != null && imgBytes.length > 0) {
                        p.setImage(Base64.getEncoder().encodeToString(imgBytes));
                    }

                    p.setDate(rs.getString("date"));
                    return Response.ok(p).build();
                } else {
                    return Response.status(404).entity("{\"error\":\"Pet not found\"}").build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
@Path("/{id}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response updatePet(@PathParam("id") int id, Pet pet) {
    try {
        Class.forName("org.mariadb.jdbc.Driver");

        String sql = "UPDATE pets SET name=?, type=?, image=? WHERE id=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pet.getName());
            ps.setString(2, pet.getType());

            if (pet.getImage() != null && !pet.getImage().isEmpty()) {
                byte[] imageBytes = Base64.getDecoder().decode(pet.getImage());
                ps.setBytes(3, imageBytes);
            } else {
                ps.setNull(3, Types.BLOB);
            }

            ps.setInt(4, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                return Response.ok("{\"message\":\"Pet updated successfully\"}").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Pet not found\"}")
                        .build();
            }

        }

    } catch (Exception e) {
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\":\"" + e.getMessage() + "\"}")
                .build();
    }
}


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePet(@PathParam("id") int id) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String sql = "DELETE FROM pets WHERE id=?";
            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                int rows = ps.executeUpdate();

                if (rows > 0) {
                    return Response.ok("{\"message\":\"Pet deleted successfully\"}").build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\":\"Pet not found\"}")
                            .build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
