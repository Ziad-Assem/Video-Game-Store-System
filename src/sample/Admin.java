package sample;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Admin implements ChangeScene, LogOut, JDBC_Connector {

    @FXML private TableView<Product> productTV;
    @FXML private TableColumn<Product, String>nameCOL;
    @FXML private TableColumn<Product, String>priceCOL;
    @FXML private TableColumn<Product, String>stockCOL;
    @FXML private TableColumn<Product, String>conditionCOL;
    @FXML private TextField nameTF;
    @FXML private TextField priceTF;
    @FXML private TextField stockTF;
    @FXML private TextField conditionTF;
    @FXML private Text errorPR_TXT;
    @FXML private Button refreshDB_BT;
    @FXML private TableView<Customer> customerTV;
    @FXML private TableColumn<Customer, String>first_nameCOL;
    @FXML private TableColumn<Customer, String>last_nameCOL;
    @FXML private TableColumn<Customer, String>phoneCOL;
    @FXML private TableColumn<Customer, String>emailCOL;
    @FXML private TextField first_TF;
    @FXML private TextField last_TF;
    @FXML private TextField phone_TF;
    @FXML private TextField email_TF;
    @FXML private Text errorCS_TXT;

    private static int rows_count=0;

    //will add a product that was inserted in the text fields
    public void AddProduct() throws Exception {
        errorPR_TXT.setText("");

        if(ProductDB_Empty_Checker() && rows_count>0){retrieveMYSQL();}
        else{
            String name, price, stock, condition;

            name = nameTF.getText();
            price = priceTF.getText();
            stock = stockTF.getText();
            condition = conditionTF.getText();

            //System.out.println(checkDuplicateName(productTV,name, price, stock, condition));

            //Checks the inputted values to see if it's correctly inputted and then proceeds to add it to the table
            if (name.equals("") || price.equals("") || stock.equals("") || condition.equals("")) {
                errorPR_TXT.setText("Please fill all the fields!");
            } else if (Pattern.matches("[0-9]+", price) == false || Pattern.matches("[0-9]+",stock)==false) {
                errorPR_TXT.setText("Price and stock can't contain text!");
            }
            else if(Pattern.matches("[a-zA-Z]+", condition) == false){errorPR_TXT.setText("Condition can't be a number!");}
            else
            {
                    if(checkDuplicateName(productTV,name,price,stock,condition)){errorPR_TXT.setText("This product has already been added! Please insert another product");}
                    else{newProductButton(name, price, stock, condition);rows_count++;}
            }
        }}



    //will create a new product object and will add it to the product's table
    public void newProductButton(String name, String price,String stock,String condition)
    {
        getProductsJDBC(name,price,stock,condition);

        Product product=new Product(name,price,stock,condition);


        nameCOL.setCellValueFactory(new PropertyValueFactory<Product,String>("name"));
        priceCOL.setCellValueFactory(new PropertyValueFactory<Product,String>("price"));
        stockCOL.setCellValueFactory(new PropertyValueFactory<Product,String>("stock"));
        conditionCOL.setCellValueFactory(new PropertyValueFactory<Product,String>("condition"));

        productTV.getItems().add(product);
    }

    //gets the product id from the MYSQL database to input it to the table
    /*public int getProductID() throws SQLException {

        int product_ID=0;

        String url= "jdbc:mysql://localhost:3306/video_game_store";
        String username="admin",password="admin";

        try
        {
            Connection con=DriverManager.getConnection(url,username,password);

            String sql="SELECT * FROM product";
            Statement statement=con.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while(result.next())
            {
                product_ID = result.getInt("product_ID");
            }


            con.close();
        }
        catch(Exception e){e.printStackTrace();}

        return product_ID;
    }*/

    public void getProductsJDBC(String name, String price,String stock,String condition)
    {
        String url= "jdbc:mysql://localhost:3306/video_game_store";
        String username="admin",password="admin";

        //System.out.println("Success! Connection was established.");

        try {
            Connection con=DriverManager.getConnection(url,username,password);

            String sql="INSERT INTO product (name,price,stock,productCondition) VALUES (?,?,?,?)";

            PreparedStatement statement= con.prepareStatement(sql);
            statement.setString(1,name);
            statement.setString(2,price);
            statement.setString(3,stock);
            statement.setString(4,condition);

            statement.execute();

            statement.close();
            con.close();


        } catch (SQLException e) {
            System.out.println("Couldn't connect to database!");
            e.printStackTrace();
        }
    }

    //checks if gui table is empty or not
    public boolean ProductDB_Empty_Checker()
    {
        boolean empty;

        ObservableList<Product> products = productTV.getItems();

        //if table is empty then it will return true
        if (products.isEmpty()) {return true;}else{return false;}
    }

    //checks if the mysql database is empty
    public boolean ProductMYSQL_Empty_Checker()
    {
        String url= "jdbc:mysql://localhost:3306/video_game_store";
        String username="admin",password="admin";
        try {
            Connection con = DriverManager.getConnection(url, username, password);

            String sql = "SELECT * FROM product";
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(sql);


            if (result.next() == false) {
                //System.out.println("ResultSet in empty in Java");
                return true;

            }
            else
            {
                do
                    {
                    String data = result.getString("name");
                    //System.out.println(data);
                    return false;
                    }
                while (result.next());
            }
        }
        catch(Exception e){System.out.println("Error retrieving table");return false;}
    }

    public boolean  MYSQL_checker()
    {
        String url= "jdbc:mysql://localhost:3306/video_game_store";
        String username="admin",password="admin";


        //checks if MYSQL is empty
        if (ProductDB_Empty_Checker())
        {

            try
            {
                Connection con = DriverManager.getConnection(url, username, password);

                String sql = "SELECT * FROM product";
                Statement statement = con.createStatement();
                ResultSet result = statement.executeQuery(sql);

                if (ProductMYSQL_Empty_Checker()){errorPR_TXT.setText("Can't refresh database as the MYSQL database is empty!");return true;}
            }
            catch(Exception e){e.getMessage();}

        }
        return false;
    }

    //retrieves MYSQL data from database if conditions are met
    public void retrieveMYSQL()
    {
        String name="", price="", stock="", condition="";

        String url= "jdbc:mysql://localhost:3306/video_game_store";
        String username="admin",password="admin";


        //if table is empty then it can retrieve mysql database and add it to the local GUI table
        if (ProductDB_Empty_Checker()) {

            try {
                Connection con = DriverManager.getConnection(url, username, password);

                String sql = "SELECT * FROM product";
                Statement statement = con.createStatement();
                ResultSet result = statement.executeQuery(sql);

                //if(!result.next()){errorPR_TXT.setText("JDBC Database is empty! add something to be able to refresh!"); return;}
                if(ProductMYSQL_Empty_Checker()){errorPR_TXT.setText("Can't refresh database as the MYSQL database is empty!");return;}



                while (result.next()) {
                    name = result.getString("name");
                    price = result.getString("price");
                    stock = result.getString("stock");
                    condition = result.getString("productCondition");

                    Product product = new Product(name, price, stock, condition);

                    nameCOL.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
                    priceCOL.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
                    stockCOL.setCellValueFactory(new PropertyValueFactory<Product, String>("stock"));
                    conditionCOL.setCellValueFactory(new PropertyValueFactory<Product, String>("condition"));

                    productTV.getItems().add(product);

                    errorPR_TXT.setText("The database was refreshed!");
                }
                con.close();
            } catch (Exception e) {
                System.out.println("Error in retrieving Product database");
            }
        }
        else {
            errorPR_TXT.setText("You can't update an already filled table!");
        }
    }


    //changes scene
    @Override
    public void changeScene(String fxmlName) {
        Main m = new Main();

        try {
            m.changeScene(fxmlName);
        } catch (Exception e) {
            e.printStackTrace();
        }    }

        //logs user out
    public void logOut(){changeScene("LogInForm.fxml");}

    //this will allow the user to highlight multiple products in the database
    /*public void multiSelectDelete()
    {
        productTV.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }*/

    //this method will delete highlighted products
    public void deleteRow()
    {
        if(ProductMYSQL_Empty_Checker()){errorPR_TXT.setText("Can't delete since the JDBC database is empty!");}else{multiThreadDelete();rows_count--;}
    }

    //checks for duplicate products
    public static boolean checkDuplicateName(TableView<Product> productTV,String name, String price,String stock,String condition)
    {
        Product product=new Product(name,price,stock,condition);

        for(Product item: productTV.getItems())
            if (item.getName().equals(product.getName()))
                return true;

        return false;
    }

    //multi thread that deletes both the JDBC database and local Tableview database
    public void multiThreadDelete()
    {
        Multi_Threading deleteThread=new Multi_Threading();

        deleteThread.setProductTV(productTV);
        deleteThread.setChoice(1);
        deleteThread.start();

    }

        //////////////////////////////////////////////////////////  PRODUCTS ^^^^^  ///////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////  CUSTOMERS vvvvv ///////////////////////////////////////////////////////////////////////////////////////////



    private static int customer_rows_count=0;

    public void AddCustomer() throws Exception {
        errorCS_TXT.setText("");

        if(CustomerDB_Empty_Checker() && customer_rows_count>0){CustomerRetrieveMYSQL();}
        else{
            String first, last, phone, email;

            first = first_TF.getText();
            last = last_TF.getText();
            phone = phone_TF.getText();
            email = email_TF.getText();

            //System.out.println(checkDuplicateNameCustomer(customerTV,first, last, phone, email));

            //Checks the inputted values to see if it's correctly inputted and then proceeds to add it to the table
            if (first.equals("") || last.equals("") || phone.equals("") || email.equals("")) {
                errorCS_TXT.setText("Please fill all the fields!");
            } else if (Pattern.matches("[0-9]+", phone) == false) {
                errorCS_TXT.setText("Price and stock can't contain text!");
            }
            else if(Pattern.matches("[a-zA-Z]+", first) == false || Pattern.matches("[a-zA-Z]+", last) == false){errorCS_TXT.setText("Name can't have a number!");}
            else if(!email.contains("@") || !email.contains(".")){errorCS_TXT.setText("Please write a valid email!");}
            else
            {
                if(checkDuplicateNameCustomer(customerTV,first,last,phone,email)){errorCS_TXT.setText("This email already exists! Please add another email!");}
                else{newPersonButton(first, last, phone, email);customer_rows_count++;}
            }
        }}



    //will create a new product object and will add it to the product's table
    public void newPersonButton(String first, String last,String phone,String email)
    {
        getCustomersJDBC(first, last, phone, email);

        Customer customer=new Customer(first, last, phone, email);


        first_nameCOL.setCellValueFactory(new PropertyValueFactory<Customer,String>("first_name"));
        last_nameCOL.setCellValueFactory(new PropertyValueFactory<Customer,String>("last_name"));
        phoneCOL.setCellValueFactory(new PropertyValueFactory<Customer,String>("phone"));
        emailCOL.setCellValueFactory(new PropertyValueFactory<Customer,String>("email"));

        customerTV.getItems().add(customer);
    }

    //gets the product id from the MYSQL database to input it to the table
    /*public int getProductID() throws SQLException {

        int product_ID=0;

        String url= "jdbc:mysql://localhost:3306/video_game_store";
        String username="admin",password="admin";

        try
        {
            Connection con=DriverManager.getConnection(url,username,password);

            String sql="SELECT * FROM product";
            Statement statement=con.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while(result.next())
            {
                product_ID = result.getInt("product_ID");
            }


            con.close();
        }
        catch(Exception e){e.printStackTrace();}

        return product_ID;
    }*/

    public void getCustomersJDBC(String first,String last,String phone,String email)
    {
        String url= "jdbc:mysql://localhost:3306/video_game_store";
        String username="admin",password="admin";

        //System.out.println("Success! Connection was established.");

        try {
            Connection con=DriverManager.getConnection(url,username,password);

            String sql="INSERT INTO customer (first_name,last_name,phone,email) VALUES (?,?,?,?)";

            PreparedStatement statement= con.prepareStatement(sql);
            statement.setString(1,first);
            statement.setString(2,last);
            statement.setString(3,phone);
            statement.setString(4,email);

            statement.execute();

            statement.close();
            con.close();


        } catch (SQLException e) {
            System.out.println("Couldn't connect to database!");
            e.printStackTrace();
        }
    }

    //checks if gui table is empty or not
    public boolean CustomerDB_Empty_Checker()
    {
        boolean empty;

        ObservableList<Customer> customers = customerTV.getItems();


        //if table is empty then it will return true
        if (customers.isEmpty()) {return true;}else{return false;}
    }

    //checks if the mysql database is empty
    public boolean CustomerMYSQL_Empty_Checker()
    {
        String url= "jdbc:mysql://localhost:3306/video_game_store";
        String username="admin",password="admin";
        try {
            Connection con = DriverManager.getConnection(url, username, password);

            String sql = "SELECT * FROM customer";
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(sql);


            if (result.next() == false) {
               // System.out.println("ResultSet in empty in Java");
                return true;

            }
            else
            {
                do
                {
                    String data = result.getString("first_name");
                    //System.out.println(data);
                    return false;
                }
                while (result.next());
            }
        }
        catch(Exception e){System.out.println("Error retrieving table");return false;}
    }

    public boolean  CustomerMYSQL_checker()
    {
        String url= "jdbc:mysql://localhost:3306/video_game_store";
        String username="admin",password="admin";


        //checks if MYSQL is empty
        if (ProductDB_Empty_Checker())
        {

            try
            {
                Connection con = DriverManager.getConnection(url, username, password);

                String sql = "SELECT * FROM customer";
                Statement statement = con.createStatement();
                ResultSet result = statement.executeQuery(sql);

                if (CustomerMYSQL_Empty_Checker()){errorCS_TXT.setText("Can't refresh database as the MYSQL database is empty!");return true;}
            }
            catch(Exception e){e.getMessage();}

        }
        return false;
    }

    //retrieves MYSQL data from database if conditions are met
    public void CustomerRetrieveMYSQL()
    {
        String first="", last="", phone="", email="";

        String url= "jdbc:mysql://localhost:3306/video_game_store";
        String username="admin",password="admin";


        //if table is empty then it can retrieve mysql database and add it to the local GUI table
        if (CustomerDB_Empty_Checker()) {

            try {
                Connection con = DriverManager.getConnection(url, username, password);

                String sql = "SELECT * FROM customer";
                Statement statement = con.createStatement();
                ResultSet result = statement.executeQuery(sql);

                if(CustomerMYSQL_Empty_Checker()){errorCS_TXT.setText("Can't refresh database as the MYSQL database is empty!");return;}



                while (result.next()) {
                    first = result.getString("first_name");
                    last = result.getString("last_name");
                    phone = result.getString("phone");
                    email = result.getString("email");

                    Customer customer = new Customer(first, last, phone, email);

                    first_nameCOL.setCellValueFactory(new PropertyValueFactory<Customer, String>("first_name"));
                    last_nameCOL.setCellValueFactory(new PropertyValueFactory<Customer, String>("last_name"));
                    phoneCOL.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
                    emailCOL.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));

                    customerTV.getItems().add(customer);

                    errorCS_TXT.setText("The database was refreshed!");
                }
                con.close();
            } catch (Exception e) {
                System.out.println("Error in retrieving Product database");
            }
        }
        else {
            errorCS_TXT.setText("You can't update an already filled table!");
        }
    }



    //this will allow the user to highlight multiple products in the database
    /*public void multiSelectDelete()
    {
        productTV.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }*/

    //this method will delete highlighted products
    public void CustomerDeleteRow()
    {
        if(CustomerMYSQL_Empty_Checker()){errorCS_TXT.setText("Can't delete since the JDBC database is empty!");}else{multiThreadDeleteCustomer();customer_rows_count--;}
    }

    //checks for duplicate products
    public static boolean checkDuplicateNameCustomer(TableView<Customer> customerTV,String first, String last,String phone,String email)
    {
        Customer customer=new Customer(first, last, phone, email);

        for(Customer item: customerTV.getItems())
            if (item.getEmail().equals(customer.getEmail()))
                return true;

        return false;
    }

    //multi thread that deletes both the JDBC database and local Tableview database
    public void multiThreadDeleteCustomer()
    {
        Multi_Threading deleteThread2=new Multi_Threading();

        deleteThread2.setCustomerTV(customerTV);
        deleteThread2.setChoice(2);

        deleteThread2.start();
    }



}
