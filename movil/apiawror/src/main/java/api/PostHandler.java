package api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/posts")
public class PostHandler {

    private static final String URL = "jdbc:mariadb://localhost:3306/awror";
    private static final String USER = "root";
    private static final String PASS = "";

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearPost(Post post) throws ClassNotFoundException {

        Class.forName("org.mariadb.jdbc.Driver");

        String sql = "INSERT INTO posts (user_id, contenido) VALUES (?, ?)";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, post.getUser_id());
            ps.setString(2, post.getContenido());
            ps.executeUpdate();

            return Response.ok("Post creado").build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error interno").build();
        }
    }

    @GET
    @Path("/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPosts(@PathParam("id") int id) throws ClassNotFoundException {

        Class.forName("org.mariadb.jdbc.Driver");

        String sql = "SELECT * FROM posts WHERE user_id=? ORDER BY fecha DESC";

        ArrayList<Post> lista = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Post p = new Post();
                p.setId(rs.getInt("id"));
                p.setUser_id(rs.getInt("user_id"));
                p.setContenido(rs.getString("contenido"));
                p.setFecha(rs.getString("fecha"));
                lista.add(p);
            }

            return Response.ok(lista).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error interno").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarPost(@PathParam("id") int id) throws ClassNotFoundException {

        Class.forName("org.mariadb.jdbc.Driver");

        String sql = "DELETE FROM posts WHERE id=?";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();

            if (filas > 0) {
                return Response.ok("Post eliminado").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
