package ejem1;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response UserUp(User user) throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        try {
            Connection conexion = DriverManager.getConnection(URL, USER, PASS);
            Statement st = conexion.createStatement();
            st.executeUpdate("insert into users (user_name,real_name,real_surname,email,password,created) values ('"
                    + user.getUser_name() + "','" + user.getReal_name() + "','" + user.getReal_surname() + "','"
                    + user.getEmail() + "','" + user.getPassword() + "','" + LocalDate.now() + "')");
            return Response.ok("Subido correctamente").build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error").build();
        }
    }

    @GET
    @Path("login/{usuario}/{password}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response loginuser(@PathParam("usuario") String usuario, @PathParam("password") String password)
            throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        try {
            Connection conexion = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement ps = conexion.prepareStatement("SELECT * FROM users WHERE user_name = ? AND password = ?");
            ps.setString(1, usuario);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Response.ok("Sesión iniciada").build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Usuario o contraseña incorrectos").build();
            }

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error").build();
        }
    }

}
