package sample;

public class Product
{
    private String name;
    private String price;
    private String stock;
    private String condition;

    //Overloading
    public Product() {}

    public Product(String name, String price, String stock, String condition)
    {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.condition = condition;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getStock() {
        return stock;
    }

    public String getCondition() {
        return condition;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }


}
