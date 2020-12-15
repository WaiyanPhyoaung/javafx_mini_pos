package com.jdc.pos.views;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jdc.pos.service.ItemService;
import com.jdc.pos.util.MessageHandler;
import com.jdc.pos.util.PosException;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MainFrame implements Initializable{

    @FXML
    private SVGPath icon;
    @FXML
    private StackPane content;
    @FXML
    private Label message;
    @FXML
    private HBox button;
    @FXML
    private MenuItem uploadMenu;
    
    private static final String POS = "M0 2v20h32v-20h-32zM30 20h-28v-16h28v16zM21 24h-10l-1 4-2 2h16l-2-2z";
    private static final String REPORT = "M27 0h-24c-1.65 0-3 1.35-3 3v26c0 1.65 1.35 3 3 3h24c1.65 0 3-1.35 3-3v-26c0-1.65-1.35-3-3-3zM26 28h-22v-24h22v24zM8 14h14v2h-14zM8 18h14v2h-14zM8 22h14v2h-14zM8 10h14v2h-14z";
    
    private static EventHandler<MouseEvent> showReport;
    private static EventHandler<MouseEvent> showPos;
    
    private ItemService itemService;
    private FileChooser chooser;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	itemService = ItemService.getInstance();
    //	itemService.add("items.txt");
    	
		showPos = event -> {
			loadView("Pos.fxml");
			icon.setContent(POS);
			button.setOnMouseClicked(showReport);
			uploadMenu.setVisible(true);
		};
		
		showReport = event -> {
			loadView("Report.fxml");
			icon.setContent(REPORT);
			button.setOnMouseClicked(showPos);
			uploadMenu.setVisible(false);
		};
		
		button.setOnMouseClicked(showReport);
		loadView("Pos.fxml");
		
		initFileChooser();
	}
    
    @FXML
    void upload() {
    	try {
    		File file = chooser.showOpenDialog(null);
			
    		if(null == file) {
    			throw new PosException("Please select a file to upload !!!");
    		}
    		
    		itemService.add(file.getAbsolutePath());
    		loadView("Pos.fxml");
    		
		} catch (Exception e) {
			MessageHandler.showAlert(e.getMessage());
		}
    }
    
    @FXML
    void about() {
    	String message = String.format("%n%-11s: 1.0%n%-11s: Waiyan Phyoaung","Version","Developer");
    	MessageHandler.showAlert(message);
    }
    
    @FXML
    void close() {
    	Platform.exit();
    }

    private void initFileChooser() {
    	chooser = new FileChooser();
    	
    	File path = new File(System.getProperty("user.home"), "Desktop");
    	chooser.setInitialDirectory(path);
    	chooser.setTitle("Select text file to upload !!!");
    	
    	ExtensionFilter extension = 
    			new FileChooser.ExtensionFilter("Text File Only (*.txt)", "*.txt");
    	chooser.setSelectedExtensionFilter(extension);
    	chooser.getExtensionFilters().add(extension);
    }
    
	public static void show() {
		try {
			Parent root = FXMLLoader.load(MainFrame.class.getResource("MainFrame.fxml"));
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadView(String fxml) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource(fxml));
			content.getChildren().clear();
			content.getChildren().add(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
