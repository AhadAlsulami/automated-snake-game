/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesnake;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ahadd
 */
public class MainFrameController implements Initializable {

    @FXML
    private AnchorPane MainFrame;
    @FXML
    private ImageView MainImage;
    @FXML
    private Button RegButton;
    @FXML
    private Button AStarButton;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void RegButtonListener(){
        new RegFrame();
    }
    
    public void AStarButtonListener(){
        //calling the class for this mode 
        new aStarFrame();
    }
}
