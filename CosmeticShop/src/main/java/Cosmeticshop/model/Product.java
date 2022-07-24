package Cosmeticshop.model;


import java.sql.Timestamp;

public class Product {
    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private String image;
    private Timestamp createDate;
    private Timestamp updateDate;

    public Product() {
    }

    public Product(int productId, String productName, double price, int quantity, String image, Timestamp createDate, Timestamp updateDate) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Product(int productId, String productName, double price, int quantity, String image, Timestamp updateDate) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.updateDate = updateDate;
    }

    public Product(String productName, double price, int quantity, String image) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public Product(int productId, String productName, double price, int quantity, String image) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public Product(int productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(String productName, double price, int quantity, String image, Timestamp createDate, Timestamp updateDate) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Product(int productId, String productName, double price, int quantity, Timestamp createDate, Timestamp updateDate) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getproductName() {
        return productName;
    }

    public void setproductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", image='" + image + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
