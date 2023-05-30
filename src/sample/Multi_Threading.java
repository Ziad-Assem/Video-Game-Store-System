package sample;

import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Multi_Threading extends Thread {

     TableView productTV;
     TableView customerTV;
     private int choice;

     public void JDBC_Customer_Delete()
     {
         ObservableList<Customer> customer;
         customer=customerTV.getSelectionModel().getSelectedItems();

         String email=customer.get(0).getEmail();

         String url= "jdbc:mysql://localhost:3306/video_game_store";
         String username="admin",password="admin";

         try {
             Connection con= DriverManager.getConnection(url,username,password);

             String sql="DELETE FROM customer WHERE email=?";
             PreparedStatement prepStatement=con.prepareStatement(sql);

             prepStatement.setString(1,email);

             prepStatement.execute();

             con.close();}
         catch(Exception e){e.getMessage();}
     }

     public void deleteRowCustomer()
     {
         JDBC_Customer_Delete();
         //TableView productTV_temp=productTV;
         customerTV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

         ObservableList<Customer> selectedRows, allCustomers;
         allCustomers=customerTV.getItems();

         //this gives us the row that were highlighted
         selectedRows =customerTV.getSelectionModel().getSelectedItems();


         allCustomers.removeAll(selectedRows);
     }

    public void deleteRow()
    {

        JDBC_Delete();
        //TableView productTV_temp=productTV;
        productTV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ObservableList<Product> selectedRows, allProducts;
        allProducts=productTV.getItems();

        //this gives us the row that were highlighted
        selectedRows =productTV.getSelectionModel().getSelectedItems();


        allProducts.removeAll(selectedRows);
    }

    public void JDBC_Delete()
    {

        ObservableList<Product> product;
        product=productTV.getSelectionModel().getSelectedItems();

        String name=product.get(0).getName();


       String url= "jdbc:mysql://localhost:3306/video_game_store";
        String username="admin",password="admin";

        try {
            Connection con= DriverManager.getConnection(url,username,password);

            String sql="DELETE FROM product WHERE name=?";
            PreparedStatement prepStatement=con.prepareStatement(sql);

            prepStatement.setString(1,name);

            prepStatement.execute();

            con.close();}
        catch(Exception e){e.getMessage();}
    }
    public void run()
    {
        if(choice==1){deleteRow();}
        else if(choice==2){deleteRowCustomer();}
    }

    public TableView getProductTV()
    {
        return productTV;
    }

    public void setProductTV(TableView productTV) {
        this.productTV = productTV;
    }

    public void setCustomerTV(TableView customerTV) {
        this.customerTV = customerTV;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }
}
