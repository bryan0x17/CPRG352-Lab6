
package ca.sait.lab6.servlets;

import ca.sait.lab6.models.Role;
import ca.sait.lab6.models.User;
import ca.sait.lab6.services.RoleService;
import ca.sait.lab6.services.UserService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserServlet extends HttpServlet {

    

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            UserService userService = new UserService();
            List<User> users = userService.getAll();
            RoleService roleService = new RoleService();
            List<Role> roles = roleService.getAll();
            
            request.setAttribute("users", users);
            request.setAttribute("roles", roles);
            
            this.getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            // Add a user to the database
            if (action.equals("add")) {
                
                try {
                    
                    String email = request.getParameter("email");
                    boolean active = request.getParameter("active") != null;
                    String firstName = request.getParameter("firstName");
                    String lastName = request.getParameter("lastName");
                    String password = request.getParameter("password");
                    String roleName = request.getParameter("role");
                    int roleId = 0;

                    RoleService roleService = new RoleService();
                    List<Role> roleList;
                    roleList = roleService.getAll();
                    
                    for (Role role : roleList) {
                        if (role.getName().equals(roleName)) {
                            roleId = role.getId();
                        }
                    }
                    
                    if (roleId == 0) {
                        throw new Exception("Invalid role");
                    }
                    
                    Role role = new Role(roleId, roleName);
                    UserService userService = new UserService();
                    userService.insert(email, active, firstName, lastName, password, role);
                    
                } catch (Exception ex) {
                    Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        }
        try {
            UserService userService = new UserService();
            List<User> users = userService.getAll();
            RoleService roleService = new RoleService();
            List<Role> roles = roleService.getAll();
            
            request.setAttribute("users", users);
            request.setAttribute("roles", roles);
        } catch (Exception ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
    }
}
