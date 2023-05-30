package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CustomerLogIn implements ChangeScene, LogOut {

    @FXML
    private TableView<Product> productTV;
    @FXML private TableColumn<Product, String> nameCOL;
    @FXML private TableColumn<Product, String>priceCOL;
    @FXML private TableColumn<Product, String>stockCOL;
    @FXML private TableColumn<Product, String>conditionCOL;
    @FXML private TextField nameTF;
    @FXML private TextField priceTF;
    @FXML private TextField stockTF;
    @FXML private TextField conditionTF;
    @FXML private Text errorPR_TXT;


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
            errorPR_TXT.setText("Table is up-to-date !");
        }
    }

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



//changes scene
    @Override
    public void changeScene(String fxmlName) {
        Main m = new Main();

        try {
            m.changeScene(fxmlName);
        } catch (Exception e) {
            e.printStackTrace();
        }    }

    public void logOut(){changeScene("LogInForm.fxml");}

}
