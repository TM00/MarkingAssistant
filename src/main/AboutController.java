/**
 * Sample Skeleton for 'AboutGUI.fxml' Controller Class
 */

package main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class AboutController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="infoText"
    private TextArea infoText; // Value injected by FXMLLoader

    @FXML // fx:id="mainPane"
    private AnchorPane mainPane; // Value injected by FXMLLoader


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert infoText != null : "fx:id=\"infoText\" was not injected: check your FXML file 'AboutGUI.fxml'.";
        assert mainPane != null : "fx:id=\"mainPane\" was not injected: check your FXML file 'AboutGUI.fxml'.";
        
        infoText.setWrapText(true);
        
        String s = "";
        
        s+= "\t\t\tWelcome to the About page\n\n";
        s+= "This application was developed as an open-source,";
        s+= "tool to assist with the capturing of marks from ";
        s+= "students or pupils from written or electronic tests";
        s+= "into a pre-existing Microsoft Excel file.\n\n ";
        s+="The reason for this is due to the tedious ";
        s+="and time consuming task to searching for each student in the file and addind the mark before continuing to the next one";
        s+="It started with just written tests, but soon expanded to a number of various functionalities, as you saw in the start page.\n\n";
        s+="If you have any questions, bug reports, suggestions for future releases please send the developer an email at\n\n";
        s+="\t\t\ttheomuller00@gmail.com\n";
        s+= "\n\t\t\t-----------------------";
                
        
        infoText.setText(s);
        infoText.setEditable(false);

    }
    
    public void setInfoText(String s){
    	infoText.setText(s);
    }
    
    
}
