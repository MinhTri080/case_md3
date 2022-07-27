package Cosmeticshop.controller;

import Cosmeticshop.connect_MySQL.ConnectMySQL;
import Cosmeticshop.dao.UserDAO;
import Cosmeticshop.model.Role;
import Cosmeticshop.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/user")
public class UserSeverlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String PHONE_REGEX = "^[0][1-9][0-9]{8}$";
    private UserDAO userDAO;
    private Role roleDAO;
    private String errors = "";
    ConnectMySQL connectMySQL = new ConnectMySQL();

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        roleDAO = new Role();
        if (this.getServletContext().getAttribute("listRole") == null) {
            getServletContext().setAttribute("listRole", selectAllRole());
        }
    }

    public boolean isPhoneValid(String number) {
        return Pattern.compile(PHONE_REGEX).matcher(number).matches();
    }

    public List<Role> selectAllRole () {
        List<Role> listRole = new ArrayList<>();
        // Step 1: Establishing a Connection
        try (Connection connection = connectMySQL.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from role;")) {
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String role = rs.getString("role");
                listRole.add(new Role(id, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listRole;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        System.out.println(action);
        try {
            switch (action) {

                case "edit":
//                    System.out.println("oiiii");
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                case "register":
//                    System.out.println("bcd");
                    showNewUser(request, response);
                    break;
//                case "p" :
//                    System.out.println("help");
//                    selectUsersPage(request, response);
//                    break;
                default:
//                    System.out.println("abc");
//                    listUser(request, response);
                    searchByName(request, response);
                    break;

            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "edit":
                    updateUser(request, response);
                    break;
                case "search":
                    System.out.println("blooo");
                    searchByName(request, response);
                    break;
                case "register":
//                    System.out.println("alo");
                    insertUser(request, response);
                    break;
//                case "p" :
//                    System.out.println("iiiiiiiiiiiiiiii");
//                    selectUsersPage(request, response);
//                    break;

                default:
//                    selectUsersPage(request, response);
                    searchByName(request, response);
                    break;

            }

        } catch (SQLException e) {
            throw new ServletException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

//    private void listUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println("listUser");
//        List<User> listUser = userDAO.selectAllUsers();
//        request.setAttribute("listUser", listUser);
//        System.out.println(listUser);
//        RequestDispatcher rq = request.getRequestDispatcher("/WEB-INF/user/list.jsp");
//        rq.forward(request, response);
//    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//        String name = request.getParameter("name");
//        String phone = request.getParameter("phone");
//        String email = request.getParameter("email");
//        int role = Integer.parseInt(request.getParameter("role"));
//        User newUser = new User(username, password, name, phone, email, role);
//        userDAO.insertUser(newUser);
//        RequestDispatcher rq = request.getRequestDispatcher("/WEB-INF/user/register.jsp");
//        rq.forward(request, response);

        User user = new User();
        boolean flag = true;
        Map<String, String> hashMap = new HashMap<String, String>();  // Luu lai truong nao bi loi va loi gi


        System.out.println(this.getClass() + " insertUser with validate");
        try {
            user.setId(Integer.parseInt(request.getParameter("iduser")));
            user.setName(request.getParameter("name"));
            user.setPassword(request.getParameter("password"));

            String userName = request.getParameter("username");
            user.setUsername(userName);

            String phone = request.getParameter("phone");
            user.setPhone(phone);

            String email = request.getParameter("email");
            user.setEmail(email);

            int inRole = Integer.parseInt(request.getParameter("role"));
            user.setRole(inRole);

            System.out.println(this.getClass() + "User info from request: " + user);

            ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
            Validator validator = validatorFactory.getValidator();

            Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

            System.out.println("User: " + user);

            if (!constraintViolations.isEmpty()) {

                errors = "<ul>";
                // constraintViolations is has error
                for (ConstraintViolation<User> constraintViolation : constraintViolations) {
                    errors += "<li>" + constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage()
                            + "</li>";
                }
                errors += "</ul>";


                request.setAttribute("user", user);
                request.setAttribute("errors", errors);

//                List<Role> roleList = roleDAO.selectAllRole();
//                request.setAttribute("listRole", roleList);

                System.out.println(this.getClass() + " !constraintViolations.isEmpty()");
                request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
            } else {
                if (userDAO.selectUserByUserName(userName) != null) {
                    flag = false;
                    hashMap.put("userName", "userName exits in database");
                }
                if (userDAO.selectUserByEmail(email) != null) {
                    flag = false;
                    hashMap.put("email", "Email exits in database");
                }
                if (userDAO.selectUserByPhone(phone) != null) {
                    flag = false;
                    hashMap.put("phone", "phone exits in database");
                }

                if (flag) {
                    userDAO.insertUser(user);
                    User c = new User();
                    request.setAttribute("user", c);
                    request.setAttribute("message", "Insert success...........");
                    request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
                } else {
                    errors = "<ul>";
                    hashMap.forEach(new BiConsumer<String, String>() {
                        @Override
                        public void accept(String keyError, String valueError) {
                            errors += "<li>" + valueError
                                    + "</li>";
                        }
                    });
                    errors += "</ul>";

                    request.setAttribute("user", user);
                    request.setAttribute("errors", errors);


                    System.out.println(this.getClass() + " !constraintViolations.isEmpty()");

                    request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
                }
            }
        } catch (NumberFormatException ex) {
            System.out.println(this.getClass() + " NumberFormatException: User info from request: " + user);
            errors = "<ul>";
            errors += "<li>" + "Input format not right"
                    + "</li>";

            errors += "</ul>";


            request.setAttribute("user", user);
            request.setAttribute("errors", errors);

            request.getRequestDispatcher("/WEB-INF/user/register.jsp").forward(request, response);
        } catch (Exception ex) {
            System.out.println("ko tim thay");
        }
    }

    private void showNewUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        User user = new User();
//        List<User> user = userDAO.selectAllUsers();
        request.setAttribute("user", user);
        RequestDispatcher rq = request.getRequestDispatcher("/WEB-INF/user/register.jsp");
        rq.forward(request, response);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.deleteUser(id);

//        List<User> listUser = userDAO.selectAllUsers();
//        request.setAttribute("listUser", listUser);
        listNumberPage(request,response);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/list.jsp");
        dispatcher.forward(request, response);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
//        int id = Integer.parseInt(request.getParameter("id"));
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//        String name = request.getParameter("name");
//        String phone = request.getParameter("phone");
//        String email = request.getParameter("email");
//        User book = new User(id, username, password, name, phone, email);
//        userDAO.updateUser(book);
//        response.sendRedirect("/user");


        User user = new User();
        boolean flag = true;
        Map<String, String> hashMap = new HashMap<String, String>();  // Luu lai truong nao bi loi va loi gi

        System.out.println(this.getClass() + " insertUser with validate");

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            user.setId(id);
            user.setName(request.getParameter("name"));
            user.setPassword(request.getParameter("password"));
            String userName = request.getParameter("username");
            user.setUsername(userName);
            String phone = request.getParameter("phone");
            user.setPhone(phone);
            String email = request.getParameter("email");
            user.setEmail(email);
            int inRole = Integer.parseInt(request.getParameter("role"));
            user.setRole(inRole);
            System.out.println(this.getClass() + "User info from request: " + user);

            ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
            Validator validator = validatorFactory.getValidator();

            Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

            System.out.println("User: " + user);
            if (!constraintViolations.isEmpty()) {
                errors = "<ul>";
                // constraintViolations is has error
                for (ConstraintViolation<User> constraintViolation : constraintViolations) {
                    errors += "<li>" + constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage()
                            + "</li>";
                }
                errors += "</ul>";
                request.setAttribute("user", user);
                request.setAttribute("errors", errors);
//                List<Role> roleList = roleDAO.selectAllRole();
//                request.setAttribute("listRole", roleList);
                System.out.println(this.getClass() + " !constraintViolations.isEmpty()");
                request.getRequestDispatcher("/WEB-INF/user/edit.jsp").forward(request, response);
            } else {
                if (userDAO.selectUserByUserName(userName) != null) {
                    flag = false;
                    hashMap.put("userName", "userName exits in database");
                }
                if (userDAO.selectUserByEmail(email) != null) {
                    flag = false;
                    hashMap.put("email", "Email exits in database");
                }
                if (userDAO.selectUserByPhone(phone) != null) {
                    flag = false;
                    hashMap.put("phone", "phone exits in database");
                }
                if (flag) {
                    userDAO.updateUser(user);
//                    List<User> listUser = userDAO.selectAllUsers();
/////////////////////
//                    request.setAttribute("listUser", listUser);
                    request.setAttribute("message", "Insert success...........");
//                    request.getRequestDispatcher("/WEB-INF/user/list.jsp").forward(request, response);
                    searchByName(request, response);
                } else {
                    errors = "<ul>";
                    hashMap.forEach(new BiConsumer<String, String>() {
                        @Override
                        public void accept(String keyError, String valueError) {
                            errors += "<li>" + valueError
                                    + "</li>";
                        }
                    });
                    errors += "</ul>";

                    request.setAttribute("user", user);
                    request.setAttribute("errors", errors);
                    System.out.println(this.getClass() + " !constraintViolations.isEmpty()");
                    request.getRequestDispatcher("/WEB-INF/user/edit.jsp").forward(request, response);
                }
            }
        } catch (NumberFormatException ex) {
            System.out.println(this.getClass() + " NumberFormatException: User info from request: " + user);
            errors = "<ul>";
            errors += "<li>" + "Input format not right"
                    + "</li>";
            errors += "</ul>";
            request.setAttribute("user", user);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/WEB-INF/user/edit.jsp").forward(request, response);
        } catch (Exception ex) {
            System.out.println("ko tim thay");
        }
    }


    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDAO.selectUser(id);
        request.setAttribute("username", existingUser.getUsername());
        request.setAttribute("password", existingUser.getPassword());
        request.setAttribute("name", existingUser.getName());
        request.setAttribute("phone", existingUser.getPhone());
        request.setAttribute("email", existingUser.getEmail());
        request.setAttribute("role", existingUser.getRole());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/edit.jsp");
        request.setAttribute("user", existingUser);
        dispatcher.forward(request, response);
        System.out.println(existingUser);
    }

    private void searchByName(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException, ClassNotFoundException {
        List<User> listUser = null;
        String query = "";
        if (request.getParameter("search") != null) {
            query = request.getParameter("search");
            listUser = userDAO.findNameUser(query);
        } else {
            listUser = userDAO.selectAllUsers();
        }
//       request.setAttribute("listUser",listUser);
        listNumberPage(request, response);
        RequestDispatcher rq = request.getRequestDispatcher("/WEB-INF/user/list.jsp");
        rq.forward(request, response);
//        response.sendRedirect("/user");
    }

    private void listNumberPage(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ClassNotFoundException, ServletException, IOException {
        System.out.println("numberPage");
        int page = 1;
        int recordsPerPage = 5;
        String search = "";
        if (req.getParameter("page") != null) {
            page = Integer.parseInt(req.getParameter("page"));
        }
        if (req.getParameter("search") != null) {
            search = req.getParameter("search");
        }
        List<User> listUser = userDAO.selectUsersPagging((page - 1) * recordsPerPage, recordsPerPage, search);
        int noOfRecords = userDAO.getNoOfRecords();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
//        System.out.println("noOfPages" + noOfPages);
//        System.out.println(noOfRecords);
        req.setAttribute("listUser", listUser);
        req.setAttribute("noOfPages", noOfPages);
        req.setAttribute("currentPage", page);
        req.setAttribute("search", search);

//        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/user/list.jsp");
//        requestDispatcher.forward(req, resp);

    }

}

