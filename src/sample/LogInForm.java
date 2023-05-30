package sample;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class LogInForm
{
    @FXML
    TextField usernameTF;

    @FXML
    PasswordField passwordPF;

    @FXML
    Text incorrectTF;

    @FXML
    CheckBox adminCB;







    public void userLogIn(ActionEvent event) throws Exception {submitted();}

    public void changeScene(String fxmlName) {
        Main m = new Main();

        try {
            m.changeScene(fxmlName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void submitted() throws Exception {
        String username,password;





        username=usernameTF.getText();
        password=passwordPF.getText();

        //The login info for the manager is admin and 123123
        //login info for the customer ziad 12341234
        if(adminCB.isSelected())
        {
            if (username.equals("admin") && password.equals("123123")) {
                changeScene("Admin.fxml");
            } else if (username.equals("") && password.equals("")) {
                incorrectTF.setText("Don't leave the fields empty!");
            } else {
                incorrectTF.setText("Wrong info! Please try again.");
            }
        }
        else
        {
            if (username.equals("ziad") && password.equals("12341234")) {
                changeScene("CustomerLogIn.fxml");
            } else if (username.equals("") && password.equals("")) {
                incorrectTF.setText("Don't leave the fields empty!");
            } else {
                incorrectTF.setText("Wrong info! Please try again lol.");
            }

        }

    }
}
