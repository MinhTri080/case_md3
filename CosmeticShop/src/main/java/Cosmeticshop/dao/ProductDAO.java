package Cosmeticshop.dao;

import Cosmeticshop.connect_MySQL.ConnectMySQL;
import Cosmeticshop.model.Product;
import Cosmeticshop.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO {
    ConnectMySQL connectionMySQL = new ConnectMySQL();
    Connection connection = connectionMySQL.getConnection();
    private int noOfRecords;
    Statement stmt;

    private static final String INSERT_PRODUCT_SQL = "INSERT INTO product (name,price,quantity,image) VALUES ( ? , ? , ? , ?);";
    private static final String SELECT_PRODUCT_BY_ID = "select * from product where productId = ?;";
    private static final String SELECT_ALL_PRODUCT = "select * from product;";
    private static final String DELETE_PRODUCT_SQL = "delete from product where productId = ?;";
    private static final String UPDATE_PRODUCT_SQL = "update product set name = ? ,price = ? ,quantity = ?  ,image = ? ,updatedate = ?   where productId = ? ;";
    public static String SEARCH_BY_KEY = "" +
            "SELECT * FROM product WHERE productId LIKE ? OR name LIKE ? OR price LIKE ? OR quantity LIKE  ?;";

    public ProductDAO() {
    }

    @Override
    public void insertProduct(Product product) throws SQLException {
        connection = connectionMySQL.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_SQL);

//        preparedStatement.setLong(1, user.getId());
        preparedStatement.setString(1, product.getproductName());
        preparedStatement.setDouble(2, product.getPrice());
        preparedStatement.setInt(3, product.getQuantity());
        preparedStatement.setString(4, product.getImage());


        System.out.println(this.getClass() + " insertProduct() query: " + preparedStatement);
        preparedStatement.executeUpdate();
        connection.close();
    }

    @Override
    public Product selectProduct(int id) {
        System.out.println("selecproduct");
        Product product = new Product();
        try (Connection connection = connectionMySQL.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID);
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int productid = rs.getInt("productId");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String image = rs.getString("image");
//                String email = rs.getString("email");
//                int role = rs.getInt("role");

                product = new Product(productid, name, price, quantity, image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public List<Product> selectAllProduct() {

        // using try-with-resources to avoid closing resources (boiler plate code)
        List<Product> products = new ArrayList<>();
        // Step 1: Establishing a Connection
        try (Connection connection = connectionMySQL.getConnection();

             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCT);) {
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int productId = rs.getInt("productId");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String image = rs.getString("image");
                Timestamp createDate = rs.getTimestamp("createdate");
                Timestamp updateDate = rs.getTimestamp("updatedate");

                products.add(new Product(productId, name, price, quantity, image, createDate, updateDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> selectProductPagging(int offset, int noOfRecords) {
        return null;
    }

    @Override
    public boolean existByProductname(String productname) {
        return false;
    }

    @Override
    public boolean existByProductId(long userId) {
        return false;
    }

    @Override
    public boolean deleteProduct(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = connectionMySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT_SQL);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }


    @Override
    public boolean updateProduct(Product product) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = connectionMySQL.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT_SQL);) {
            statement.setString(1, product.getproductName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setString(4, product.getImage());
            statement.setTimestamp(5, product.getUpdateDate());
//            statement.setString(5, user.getEmail());
//            statement.setInt(3, user.getIdCountry());
            statement.setInt(6, product.getProductId());


            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }


    @Override
    public List<Product> findNameProduct(String query) throws SQLException {
//        List<Product> productList = new ArrayList<>();
//        try {
//            Connection connection = connectionMySQL.getConnection();
//            CallableStatement statement = connection.prepareCall(SEARCH_BY_KEY);
//            statement.setString(1, '%' + query + '%');
//            statement.setString(2, '%' + query + '%');
//            statement.setString(3, '%' + query + '%');
//            statement.setString(4, '%' + query + '%');
////            statement.setString(5, '%' + query + '%');
//            ResultSet rs = statement.executeQuery();
//            while (rs.next()) {
//                int id = rs.getInt("productId");
//                String name = rs.getString("name");
//                Double price = rs.getDouble("price");
//                int quantity = rs.getInt("quantity");
//                String image = rs.getString("image");
////                int role = rs.getInt("role");
//                productList.add(new Product(id, name, price, quantity, image));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    public List<Product> getNumberPage(int offset, int noOfRecords, String name) throws ClassNotFoundException, SQLException {
        Connection connection = connectionMySQL.getConnection();
        System.out.println("numberpage");

        String query = "SELECT SQL_CALC_FOUND_ROWS * FROM product where name LIKE ? OR price LIKE ? OR quantity LIKE ? limit " + offset + "," + noOfRecords;
        List<Product> list = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, '%' + name + '%');
        ps.setString(2, '%' + name + '%');
        ps.setString(3, '%' + name + '%');

        System.out.println(this.getClass() + " getNumberPage() query: " + ps);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            Product product = new Product();
            product.setProductId(rs.getInt("productId"));
            product.setproductName(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            product.setQuantity(rs.getInt("quantity"));
            product.setImage(rs.getString("image"));
            product.setCreateDate(rs.getTimestamp("createdate"));
            product.setUpdateDate(rs.getTimestamp("updatedate"));
//            user.setAddress(rs.getString("address"));
//            user.setPhone(rs.getString("phone"));
//            user.setEmail(rs.getString("email"));
            list.add(product);
        }
        rs = ps.executeQuery("SELECT FOUND_ROWS()");
        if (rs.next()){
            this.noOfRecords = rs.getInt(1);
        }
        connection.close();
        return list;
    }
    public int getNoOfRecords() {
        return noOfRecords;
    }

}

