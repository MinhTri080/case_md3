package Cosmeticshop.dao;

import Cosmeticshop.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    public void insertUser(User user) throws SQLException;

    public User selectUser(int id);

    public List<User> selectAllUsers();
    public List<User> selectUsersPagging(int offset,int noOfRecords);
    boolean existByUsername(String username);
    boolean existByUserId(long userId);
    boolean existByRoles(String Roles);

    public boolean deleteUser(int id) throws SQLException;

    public boolean updateUser(User user) throws SQLException;
   List<User> findNameUser(String query) throws SQLException;

}
