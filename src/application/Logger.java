package application;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.util.ResourceBundle;

public class Logger implements Initializable {
	@FXML
	private TextField usrField;
	@FXML
	private TextField pswdField;
	@FXML
	private Label usr;
	@FXML
	private Label pswd;
	@FXML
	private Button btn;
	@FXML
	private Label message;
	@FXML
	private Label greet;
	@FXML
	private ImageView hun=new ImageView();
	@FXML
	private ImageView eng =new ImageView();
	private String reg="application.EN";

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		hun.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				reg = "application.HU";
				usr.setText("Felhasználó név:");
				pswd.setText("Jelszó:");
				btn.setText("Belépés");
				greet.setText("Üdv!");
			}
		});
		eng.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				reg = "application.EN";
				usr.setText("User name:");
				pswd.setText("Password:");
				btn.setText("Login");
				greet.setText("Welcome!");
			}
		});
	}

	private void validate() {
		if ((usrField.getText().toUpperCase().equals("ADMIN")) && (pswdField.getText().toUpperCase().equals("ADMIN"))) {
			message.setText("Logged in as Admin");
			try {
				Parent Admin = FXMLLoader.load(getClass().getResource("AdminXML.fxml"));
				Scene AdminScene = new Scene(Admin, 1200, 600);
				Stage AdminStage = (Stage) btn.getScene().getWindow();
				AdminStage.setScene(AdminScene);
				AdminStage.setFullScreenExitHint("");
				AdminStage.setFullScreen(true);
				AdminStage.show();
			} catch (IOException e) {
				System.out.println("Nincs meg az AdminFXML file.");
			}
		} else if ((usrField.getText().toUpperCase().equals("USER"))
				&& (pswdField.getText().toUpperCase().equals("USER"))) {
			message.setText("Logged in as User");
			try {
				Parent User = FXMLLoader.load(getClass().getResource("UserXML.fxml"), ResourceBundle.getBundle(reg));
				Scene UserScene = new Scene(User, 300, 300);
				Stage UserStage = (Stage) btn.getScene().getWindow();
				UserScene.getStylesheets().add(this.getClass().getResource("User.css").toExternalForm());
				UserStage.setScene(UserScene);
				UserStage.setFullScreenExitHint("");
				UserStage.setResizable(true);
				UserStage.setFullScreen(true);
				UserStage.show();
			} catch (IOException e) {
				System.out.println("Nincs meg a UserFXML file.");
			}
		} else
			message.setText("Acces denied!");
	}

	@FXML
	private void btnPressed(ActionEvent event) {
		validate();
	}

	@FXML
	private void entered(KeyEvent key) {
		if (key.getCode() == KeyCode.ENTER)
			validate();
	}
}