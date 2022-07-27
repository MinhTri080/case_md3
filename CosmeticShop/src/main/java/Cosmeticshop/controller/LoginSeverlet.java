package Cosmeticshop.controller;

import Cosmeticshop.dao.IUserDAO;
import Cosmeticshop.dao.UserDAO;
import Cosmeticshop.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(value = "/login")
public class LoginSeverlet extends HttpServlet {
    IUserDAO userDAO;

public LoginSeverlet(){
    userDAO=new UserDAO();
}
//    public LoginServlet(){
//        userDAO = new UserDAO();
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/login.jsp");
        dispatcher.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password =  req.getParameter("password");
        try {
            User user = userDAO.getUserByUsername(username);
            if(user != null){
                if(user.getPassword().equals(password) && user.getUsername().equals(username)){
                    User userLogin  = user;
                    HttpSession httpSession = req.getSession(true);
                    httpSession.setAttribute("userLogin", userLogin);
                    req.setAttribute("userLogin", userLogin);
                    RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/list.jsp");
                    dispatcher.forward(req, resp);
                } else {
                    req.setAttribute("message", "Login unsuccessful. Please log in again");
                    RequestDispatcher dispatcher = req.getRequestDispatcher("/index.html");
                    dispatcher.forward(req, resp);
                }
            }else{
                req.setAttribute("message", "Account does not exist. Please log in again");
                RequestDispatcher dispatcher = req.getRequestDispatcher("/index.html");
                dispatcher.forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
