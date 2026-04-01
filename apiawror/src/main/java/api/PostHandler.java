package api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;

import jakarta.ws.rs.*;
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
    public Response crearPost(Post post) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String sql = "INSERT INTO posts (user_id, contenido, imagen) VALUES (?, ?, ?)";
            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, post.getUser_id());
                ps.setString(2, post.getContenido());

                if (post.getImagen() != null && !post.getImagen().isEmpty()) {
                    byte[] imagenBytes = Base64.getDecoder().decode(post.getImagen());
                    ps.setBytes(3, imagenBytes);
                } else {
                    ps.setNull(3, java.sql.Types.BLOB);
                }

                ps.executeUpdate();
                return Response.ok("Post creado").build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear post").build();
        }
    }

    @GET
    @Path("/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPosts(@PathParam("id") int id) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String sql = "SELECT p.id, p.user_id, p.contenido, p.imagen, p.fecha, u.user_name AS username " +
                    "FROM posts p " +
                    "JOIN users u ON p.user_id = u.id " +
                    "WHERE p.user_id=? " +
                    "ORDER BY p.fecha DESC";

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

                    byte[] imgBytes = rs.getBytes("imagen");
                    if (imgBytes != null && imgBytes.length > 0) {
                        p.setImagen(Base64.getEncoder().encodeToString(imgBytes));
                    }

                    p.setUsername(rs.getString("username"));

                    lista.add(p);
                }

                return Response.ok(lista).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener posts").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarPost(@PathParam("id") int id) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String sql = "DELETE FROM posts WHERE id=?";
            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                int filas = ps.executeUpdate();

                if (filas > 0) {
                    return Response.ok("Post eliminado").build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("Post no encontrado").build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al eliminar post").build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editarPost(@PathParam("id") int id, Post post) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String sql = "UPDATE posts SET contenido=? WHERE id=? AND user_id=?";
            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, post.getContenido());
                ps.setInt(2, id);
                ps.setInt(3, post.getUser_id());

                int filas = ps.executeUpdate();

                if (filas > 0) {
                    return Response.ok("Post actualizado").build();
                } else {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("No puedes editar este post").build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al editar post").build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTodosLosPosts() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String sql = "SELECT p.id, p.user_id, p.contenido, p.imagen, p.fecha, u.user_name AS username " +
                    "FROM posts p " +
                    "JOIN users u ON p.user_id = u.id " +
                    "ORDER BY p.fecha DESC";

            ArrayList<Post> lista = new ArrayList<>();

            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement ps = con.prepareStatement(sql)) {

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Post p = new Post();
                    p.setId(rs.getInt("id"));
                    p.setUser_id(rs.getInt("user_id"));
                    p.setContenido(rs.getString("contenido"));
                    p.setFecha(rs.getString("fecha"));

                    byte[] imgBytes = rs.getBytes("imagen");
                    if (imgBytes != null && imgBytes.length > 0) {
                        p.setImagen(Base64.getEncoder().encodeToString(imgBytes));
                    }

                    p.setUsername(rs.getString("username"));

                    lista.add(p);
                }

                return Response.ok(lista).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener posts").build();
        }
    }

}
