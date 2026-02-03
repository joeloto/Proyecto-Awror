package ejem1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserHandler {

    private static final String URL = "jdbc:mariadb://localhost:3306/awror";
    private static final String USER = "root";
    private static final String PASS = "";

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response subirDeportistasAndroid(User user) throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        try {
            Connection conexion = DriverManager.getConnection(URL, USER, PASS);
            Statement st = conexion.createStatement();
          
            st.executeUpdate("insert into users (user_name,real_name,real_surname,email,password) values ('" + user.getUser_name() + "','" + user.getReal_name() + "','" + user.getReal_surname() + "','" + user.getEmail() + "','" + user.getPassword() + "')");
            return Response.ok("Subido correctamente").build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error").build();
        }
    }
}
