package api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserHandler {

    private static final String URL = "jdbc:mariadb://localhost:3306/awror";
    private static final String USER = "root";
    private static final String PASS = "";

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user) throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");

        String emailNormalizado = user.getEmail().trim().toLowerCase();

        String checkSql = "SELECT COUNT(*) FROM users WHERE LOWER(email)=?";
        String insertSql = "INSERT INTO users (user_name, real_name, real_surname, email, password, created) VALUES (?,?,?,?,?,CURRENT_DATE())";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setString(1, emailNormalizado);
            ResultSet rsCheck = checkPs.executeQuery();
            rsCheck.next();

            if (rsCheck.getInt(1) > 0) {
                return Response.status(409).entity("{\"error\":\"Email already exists\"}").build();
            }

            PreparedStatement ps = con.prepareStatement(insertSql);
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            ps.setString(1, user.getUser_name());
            ps.setString(2, user.getReal_name());
            ps.setString(3, user.getReal_surname());
            ps.setString(4, emailNormalizado);
            ps.setString(5, hashedPassword);

            ps.executeUpdate();

            return Response.ok("{\"message\":\"User created successfully\"}").build();

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                return Response.status(409).entity("{\"error\":\"Email already exists\"}").build();
            }

            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"Internal server error\"}").build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User user) throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashBD = rs.getString("password");

                if (BCrypt.checkpw(user.getPassword(), hashBD)) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setUser_name(rs.getString("user_name"));
                    u.setReal_name(rs.getString("real_name"));
                    u.setReal_surname(rs.getString("real_surname"));
                    u.setEmail(rs.getString("email"));
                    return Response.ok(u).build();
                } else {
                    return Response.status(401).entity("{\"error\":\"Wrong password\"}").build();
                }
            } else {
                return Response.status(401).entity("{\"error\":\"User not found\"}").build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"Internal server error\"}").build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") int id) throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUser_name(rs.getString("user_name"));
                u.setReal_name(rs.getString("real_name"));
                u.setReal_surname(rs.getString("real_surname"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                return Response.ok(u).build();
            } else {
                return Response.status(404).entity("{\"error\":\"User not found\"}").build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"Internal server error\"}").build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") int id, User user) throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            String passwordFinal;

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                passwordFinal = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            } else {
                String sqlSelect = "SELECT password FROM users WHERE id=?";
                PreparedStatement psSelect = con.prepareStatement(sqlSelect);
                psSelect.setInt(1, id);
                ResultSet rs = psSelect.executeQuery();

                if (rs.next()) {
                    passwordFinal = rs.getString("password");
                } else {
                    return Response.status(404).entity("{\"error\":\"User not found\"}").build();
                }
            }

            String sqlUpdate = "UPDATE users SET user_name=?, real_name=?, real_surname=?, email=?, password=? WHERE id=?";
            PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);

            psUpdate.setString(1, user.getUser_name());
            psUpdate.setString(2, user.getReal_name());
            psUpdate.setString(3, user.getReal_surname());
            psUpdate.setString(4, user.getEmail());
            psUpdate.setString(5, passwordFinal);
            psUpdate.setInt(6, id);

            int rs = psUpdate.executeUpdate();

            if (rs > 0) {
                return Response.ok("{\"message\":\"User updated successfully\"}").build();
            } else {
                return Response.status(404).entity("{\"error\":\"User not found\"}").build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"Internal server error\"}").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") int id) throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        String sql = "DELETE FROM users WHERE id=?";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rs = ps.executeUpdate();

            if (rs > 0) {
                return Response.ok("{\"message\":\"User deleted successfully\"}").build();
            } else {
                return Response.status(404).entity("{\"error\":\"User not found\"}").build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"Internal server error\"}").build();
        }
    }
}
