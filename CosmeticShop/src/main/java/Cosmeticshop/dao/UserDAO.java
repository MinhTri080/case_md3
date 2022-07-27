package Cosmeticshop.dao;

import Cosmeticshop.model.User;
import Cosmeticshop.connect_MySQL.ConnectMySQL;
import com.mysql.cj.protocol.Resultset;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {
    //    private String jdbcURL = "jdbc:mysql://localhost:3306/casemd3?useSSL=false";
//    private String jdbcUsername = "root";
//    private String jdbcPassword = "123123";
    ConnectMySQL connectionMySQL = new ConnectMySQL();
    Connection connection = connectionMySQL.getConnection();
    private int noOfRecords;
    Statement stmt;


    private static final String INSERT_USERS_SQL = "INSERT INTO user (username,password,name,phone,email,role) VALUES ( ? , ? , ? , ? , ? , ? );";
    private static final String SELECT_USER_BY_ID = "select * from user where iduser = ?;";
    private static final String SELECT_ALL_USERS = "select * from user;";
    private static final String DELETE_USERS_SQL = "delete from user where iduser = ?;";
    private static final String UPDATE_USERS_SQL = "update user set username = ? ,password = ? ,name = ?  ,phone = ? ,email = ?   where iduser = ? ;";
    private static final String SELECT_USER_BY_EMAIL = "select u.id,u.name,u.email, u.idcountry\r\n"
            + "    		 from users as u inner join country as c\r\n"
            + "    		where u.email = ? and u.idcountry = c.id;";
    public static String SEARCH_BY_KEY = "SELECT * FROM user WHERE username LIKE ? OR name LIKE ? OR email LIKE ?";
    private static final String SELECT_USER_BY_NAME = " select * from user where name or username like '% ? % ' ; ";
    private static final String SELECT_USER_BY_USERNAME =  "select u.iduser,u.username,u.password,u.name,u.phone,u.email,u.role    from user as u inner join role as role where username = ? and u.role = role.id;";

    public UserDAO() {
    }

    ;
//    Connection connection;

//    protected Connection connection() {
//        Connection connection = null;
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return connection;
//    }

    @Override
    public void insertUser(User user) throws SQLException {

        Connection connection = connectionMySQL.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);

//        preparedStatement.setLong(1, user.getId());
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getName());
        preparedStatement.setString(4, user.getPhone());
        preparedStatement.setString(5, user.getEmail());
        preparedStatement.setInt(6, user.getRole());

        System.out.println(this.getClass() + " insertUser() query: " + preparedStatement);
        preparedStatement.executeUpdate();
        connection.close();
    }

    @Override
    public User selectUser(int id) {
        User user = new User();
        try (Connection connection = connectionMySQL.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int iduser = rs.getInt("iduser");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                int role = rs.getInt("role");

                user = new User(iduser, username, password, name, phone, email, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    @Override
    public List<User> selectAllUsers() {

        // using try-with-resources to avoid closing resources (boiler plate code)
        List<User> users = new ArrayList<>();
        // Step 1: Establishing a Connection
        try (Connection connection = connectionMySQL.getConnection();

             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int id = rs.getInt("iduser");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                int role = rs.getInt("role");

                users.add(new User(id, username, password, name, phone, email, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
//
//        public static void main(String[] args) {
//        UserDAO t = new UserDAO();
//        System.out.println(t.existByUserId(1));

    //    }
    @Override
    public boolean existByUsername(String username) {
        return false;
    }

    @Override
    public boolean existByUserId(long userId) {
        return false;
    }

    @Override
    public boolean existByRoles(String Roles) {
        return false;
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = connectionMySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = connectionMySQL.getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getEmail());
//            statement.setInt(3, user.getIdCountry());
            statement.setInt(6, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    @Override
    public List<User> findNameUser(String query) throws SQLException {
        List<User> listUser = new ArrayList<>();
        try {
            Connection connection = connectionMySQL.getConnection();
            CallableStatement statement = connection.prepareCall(SEARCH_BY_KEY);
            statement.setString(1, '%' + query + '%');
            statement.setString(2, '%' + query + '%');
            statement.setString(3, '%' + query + '%');
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("iduser");
                String username = rs.getString("username");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                int role = rs.getInt("role");
                listUser.add(new User(id, username, name, phone, email, role));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return listUser;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = null;
        // Step 1: Establishing a Connection
        try (Connection connection = connectionMySQL.getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_USERNAME);) {
            preparedStatement.setString(1, username);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int id = rs.getInt("iduser");
                String username1 = rs.getString("username");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
//                String address = rs.getString("address");
                String email = rs.getString("email");
                int role = rs.getInt("role");
                user = new User(id, username1, password, name, phone, email, role);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    @Override
    public List<User> selectUsersPagging(int offset, int noOfRecords, String name) {
        String query = "select sql_calc_found_rows * from casemd3.user where name like ? limit " + offset + ", " + noOfRecords;
        List<User> list = new ArrayList<>();
        User user = null;
        try {
            connection = connectionMySQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, '%' + name + '%');
            System.out.println(this.getClass() + " selectUsersPagging() query: " + query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("iduser"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setName(rs.getString("name"));
                user.setPhone(rs.getString("phone"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getInt("role"));
                list.add(user);
            }
//            rs.close();

            rs = preparedStatement.executeQuery("SELECT FOUND_ROWS()");
            if (rs.next()) {
                this.noOfRecords = rs.getInt(1);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public int getNoOfRecords() {
        return noOfRecords;
    }

    public User selectUserByEmail(String email) throws ClassNotFoundException, SQLException {
        String query = "select *\n" +
                "from user inner join role where user.email = ? and user.role = role.id;";
        User user = null;
        connection = connectionMySQL.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("iduser");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String name = rs.getString("name");
            String phone = rs.getString("phone");
            int role = rs.getInt("role");

            return new User(id, username, password, name, phone, email, role);
        }
        connection.close();
        return null;
    }

    public User selectUserByPhone(String phone) throws ClassNotFoundException, SQLException {
        String query = "select *\n" +
                "from user inner join role where user.phone = ? and user.role = role.id;";
        User user = null;
        connection = connectionMySQL.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, phone);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("iduser");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String name = rs.getString("name");
            String email = rs.getString("email");
            int role = rs.getInt("role");

            return new User(id, username, password, name, phone, email, role);
        }
        connection.close();
        return null;
    }

    public User selectUserByUserName(String userName) throws SQLException {
        String query = "select *\n" +
                "from user inner join role where user.username = ? and user.role = role.id;";
        connection = connectionMySQL.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("iduser");
            String password = rs.getString("password");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            int role = rs.getInt("role");

            return new User(id, userName, password, name, phone, email, role);
        }
        connection.close();
        return null;
    }
}
