package Cosmeticshop.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class User {
    private int id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private int role;
//    private String updateDate;
//    private String createDate;

    public User(int id, String username, String name, String email, String phone) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public User(String username, String password, String name, String phone, String email, int role) {
        this.username =username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;

    }

    public User(int id, String username, String name, String email, String phone, int role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public User() {

    }

    public User(int id, String username, String password, String name, String phone, String email, int role) {
        this.id = id;
        this.username =username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email= email;
        this.role =role;
    }



//    public String getUpdateDate() {
//        return updateDate;
//    }
//
//    public void setUpdateDate(String updateDate) {
//        this.updateDate = updateDate;
//    }
//
//    public String getCreateDate() {
//        return createDate;
//    }
//
//    public void setCreateDate(String createDate) {
//        this.createDate = createDate;
//    }

    public User(int id, String username, String password, String name, String phone, String email) {
        this.id =  id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

//    public User(int id, String username, String password, String name, String phone, String email, int role, String updateDate, String createDate) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.name = name;
//        this.phone = phone;
//        this.email = email;
//        this.role =role;
//        this.updateDate = updateDate;
//        this.createDate = createDate;
//    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotEmpty(message = "Tên không được để trống")
    @Pattern(regexp = "^([A-Z]+[a-z]*[ ]?)+$", message = "Bắt đầu bằng 1 chữ cái In hoa")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
@NotEmpty
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

@NotEmpty
@Pattern(regexp = "^([A-Z]+[a-z]{1,})\\s([A-Z]+[a-z]*[ ]?)+$")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty
    @Email(message = "Định dạng không đúng")
    @Pattern(regexp = "^([a-zA-Z0-9!#$%&'*+\\/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+\\/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?)$", message = "Format mail not right")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotEmpty
    @Pattern(regexp = "((84|0[1|2|3|4|5|6|7|8|9])+([0-9]{8})\\b)", message = "Số điện thoại phải bắt đầu từ số 0 và có 10 chữ số !!!")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", Role='" + role + '\'' +
                '}';
    }
}
