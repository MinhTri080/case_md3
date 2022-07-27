package Cosmeticshop.controller;

import Cosmeticshop.dao.ProductDAO;
import Cosmeticshop.dao.UserDAO;
import Cosmeticshop.model.Product;
import Cosmeticshop.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

@WebServlet(urlPatterns = "/product")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 50, // 50MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class ProductSeverlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;
    private String error = "";


    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=UTF-8");
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        System.out.println(action);
        try {
            switch (action) {

                case "update":
                    System.out.println("oiiii");
                    showUpdateForm(req, resp);
                    break;
                case "create":
                    System.out.println("bcd");
                    showNewProduct(req, resp);
                    break;
                case "delete":
                    System.out.println("help");
                    deleteProduct(req, resp);
                    break;
                default:
                    System.out.println("abc");
                    listNumberPage(req, resp);
                    break;

            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=UTF-8");
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        System.out.println(action);
        try {
            switch (action) {

                case "create":
//                    System.out.println("oiiii");
                    insertProductr(req, resp);

                    break;
                case "delete":
                    deleteProduct(req, resp);
                    break;
                case "update":
//                    System.out.println("bcd");
                    updateProduct(req, resp);
                    break;
//                case "p" :
//                    System.out.println("help");
//                    selectUsersPage(request, response);
//                    break;
                default:
//                    System.out.println("ccccc");
//                    listProduct(req, resp);
                    listNumberPage(req, resp);
                    break;

            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println("listProduct");
        List<Product> listProduct = productDAO.selectAllProduct();
        request.setAttribute("listProduct", listProduct);
        System.out.println(listProduct);
        RequestDispatcher rq = request.getRequestDispatcher("/WEB-INF/product/list.jsp");
        rq.forward(request, response);
    }

    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        System.out.println("updateform");
        int id = Integer.parseInt(request.getParameter("id"));
        System.out.println("test");
        Product existingProduct = productDAO.selectProduct(id);
        request.setAttribute("name", existingProduct.getproductName());
        request.setAttribute("price", existingProduct.getPrice());
        request.setAttribute("quantity", existingProduct.getQuantity());
        request.setAttribute("image", existingProduct.getImage());
//        request.setAttribute("email",existingProduct.getEmail());
//        request.setAttribute("role",existingProduct.getRole());
        System.out.println(existingProduct);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/product/update.jsp");
        request.setAttribute("product", existingProduct);
        dispatcher.forward(request, response);


    }

    private void insertProductr(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        String image = null;
        for (Part part : request.getParts()) {
            if (part.getName().equals("file")) {
                String fileName = extractFileName(part);
                // refines the fileName in case it is an absolute path
                fileName = new File(fileName).getName();
                if (fileName.equals("")) {
                    image = "\\assets\\images\\maintenance.png";
                } else {
                    String servletRealPath = this.getServletContext().getRealPath("/") + "\\images\\" + fileName;
                    part.write("D:\\case_md3\\CosmeticShop\\src\\main\\webapp\\images\\" + fileName);
                    part.write(servletRealPath);
                    System.out.println(servletRealPath);
                    image = "\\images\\" + fileName;
                }
            }
        }
//        String email = request.getParameter("email");
//        int role = Integer.parseInt(request.getParameter("role"));
        Product newProduct = new Product(name, price, quantity, image);
        productDAO.insertProduct(newProduct);
        RequestDispatcher rq = request.getRequestDispatcher("/WEB-INF/product/create.jsp");
        rq.forward(request, response);

    }

    private void showNewProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
//        User user = new User();
        List<Product> product = productDAO.selectAllProduct();
        request.setAttribute("product", product);
        RequestDispatcher rq = request.getRequestDispatcher("/WEB-INF/product/create.jsp");
        rq.forward(request, response);
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

    //
//    public File getFolderUpload() {
//        File folderUpload = new File(System.getProperty("user.home") + "/Uploads");
//        if (!folderUpload.exists()) {
//            folderUpload.mkdirs();
//        }
//        return folderUpload;
//    }
    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        System.out.println("id");
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        Timestamp updateDate = Timestamp.valueOf(LocalDateTime.now());
        String image = null;
        for (Part part : request.getParts()) {
            if (part.getName().equals("file")) {
                String fileName = extractFileName(part);
                fileName = new File(fileName).getName();
                if (fileName.equals("")) {
                    image = "\\assets\\images\\maintenance.png";
                } else {
                    String servletRealPath = this.getServletContext().getRealPath("/") + "\\images\\" + fileName;
                    part.write("D:\\case_md3\\CosmeticShop\\src\\main\\webapp\\images\\" + fileName);
                    part.write(servletRealPath);
                    System.out.println(servletRealPath);
                    image = "\\images\\" + fileName;
                }
            }
            System.out.println("testfileimage");
        }
        Product book = new Product(id, name, price, quantity, image, updateDate);
        productDAO.updateProduct(book);
//        response.sendRedirect("/WEB-INF/product/update.jsp/");
        response.sendRedirect("/product");



    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        productDAO.deleteProduct(id);

        List<Product> productList = productDAO.selectAllProduct();
        request.setAttribute("productList", productList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/product?action=list");
        dispatcher.forward(request, response);
    }
    private void listNumberPage(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ClassNotFoundException, ServletException, IOException {
        System.out.println("numberPage");
        int page = 1;
        int recordsPerPage = 5;
        if (req.getParameter("page") != null) {
            page = Integer.parseInt(req.getParameter("page"));
        };
        String name = "";
        if (req.getParameter("search") != null) {
            name = req.getParameter("search");
        }
        List<Product> productList = productDAO.getNumberPage((page - 1) * recordsPerPage, recordsPerPage, name);
        int noOfRecords = productDAO.getNoOfRecords();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        req.setAttribute("productList", productList);
        req.setAttribute("noOfPages", noOfPages);
        req.setAttribute("currentPage", page);
        req.setAttribute("search" , name);



        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/product/list.jsp");
        requestDispatcher.forward(req, resp);

    }

}
