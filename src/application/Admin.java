package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Admin implements Initializable {
	// Misc.
	private Stock warehouse = new Stock();
	@FXML
	private ListView<String> catListView;
	@FXML
	private ListView<String> namesListView;
	@FXML
	private Label dialog;
	@FXML
	private Label selectedCat;
	@FXML
	private Label selectedPrice;
	@FXML
	private Label selectedPiece;
	@FXML
	private VBox PopUp;	
	private FileChooser fileChooser;
	
	Tooltip warning;
	HBox popwar;
	//= new HBox()
	Image warPic;
	
	// Buttons
	@FXML
	private Button addCatBtn;
	@FXML
	private Button removeCatBtn;
	@FXML
	private Button addItem;
	@FXML
	private Button remItemBtn;
	@FXML
	private Button popBtn;
	@FXML
	private Button rePriceBtn;
	@FXML
	private Button reCatBtn;

	// Fields
	@FXML
	private TextField catInput;
	@FXML
	private TextField newCateg;
	@FXML
	private TextField nameField;
	@FXML
	private TextField priceField;
	@FXML
	private TextField piecesField;
	@FXML
	private TextField catField;
	@FXML
	private TextField pathField;


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			FileInputStream fileIn = new FileInputStream("warehouse.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			warehouse = (Stock) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			dialog.setText("Warehouse not found");
			return;
		} catch (ClassNotFoundException c) {
			dialog.setText("Stock class not found");
			return;
		}
		addItem.setDisable(true);
		catListView.setItems(Convert(warehouse.getCats()));
		namesListView.setItems(Convert(warehouse.getNames()));
		PopUp.setVisible(false);
		warPic= new Image("File:war.png");		
		warning = new Tooltip();
		warning.setText("The price, and the number of pieces must be numbers!");
		warning.setFont(Font.font(16.0));
		warning.setGraphic(new ImageView(warPic));
		warning.setMinSize(500.0, 100.0);
		priceField.setTooltip(warning);
		piecesField.setTooltip(warning);
		fileChooser = new FileChooser();
		
	}

	@FXML
	private void addCategory(ActionEvent e) {
		String add = catInput.getText();
		add = add.replaceAll("\\s", "");
		if (!add.equals("")) {
			dialog.setText(warehouse.addCat(catInput.getText()));
			catListView.setItems(Convert(warehouse.getCats()));
		}
		catInput.setText("");
	}

	@FXML
	private void removeCategory(ActionEvent e) {
		if (catListView.getSelectionModel().getSelectedItem() != null) {
			String delete = catListView.getSelectionModel().getSelectedItem();
			dialog.setText(warehouse.removeCat(delete));
			catListView.setItems(Convert(warehouse.getCats()));
		}

	}

	@FXML
	private void openPop(ActionEvent e) throws IOException {
		PopUp.setVisible(true);
	}

	@FXML
	private void addProduct(ActionEvent e){
		try {
			Item in = new Item(nameField.getText(), Integer.parseInt(priceField.getText()));
			dialog.setText(warehouse.addItem(in, Integer.parseInt(piecesField.getText()), catField.getText(),
					pathField.getText()));
			catListView.setItems(Convert(warehouse.getCats()));
			namesListView.setItems(Convert(warehouse.getNames()));
			nameField.setText("");
			priceField.setText("");
			piecesField.setText("");
			catField.setText("");
			pathField.setText("photos/no_photo.png");
			PopUp.setVisible(false);
			addItem.setDisable(true);
		} catch (Exception exc) {		
			warning.show((Stage) popBtn.getScene().getWindow());
		}
	}

	@FXML
	private void removeProduct(ActionEvent e) {
		dialog.setText(warehouse.removeItem(namesListView.getSelectionModel().getSelectedItem()));
		namesListView.setItems(Convert(warehouse.getNames()));
		selectedPrice.setText("");
		selectedPiece.setText("");
		selectedCat.setText("");

	}

	@FXML
	private void handleSelect(MouseEvent e) {
		List<Item> tmp = warehouse.getSupply();
		for (int i = 0; i < tmp.size(); i++) {
			if (namesListView.getSelectionModel().getSelectedItem().equals(tmp.get(i).getStr()[0])) { // lol
				selectedCat.setText(tmp.get(i).getStr()[1]);
				selectedPrice.setText(Integer.toString(tmp.get(i).getNumb()[0])+" Ft");
				selectedPiece.setText(Integer.toString(tmp.get(i).getNumb()[1]));
			}
		}
	}

	@FXML
	private void reCat(ActionEvent e) {
		List<Item> tmp = warehouse.getSupply();
		for (int i = 0; i < tmp.size(); i++) {
			if (namesListView.getSelectionModel().getSelectedItem().equals(tmp.get(i).getStr()[0])) {
				Item tmpI = new Item(tmp.get(i).getStr()[0], tmp.get(i).getNumb()[0]);
				int pcs = tmp.get(i).getNumb()[1];
				String tempr = tmp.get(i).getStr()[2];
				warehouse.removeItem(tmp.get(i).getStr()[0]);
				warehouse.addItem(tmpI, pcs, newCateg.getText(), tempr);
				catListView.setItems(Convert(warehouse.getCats()));
				namesListView.setItems(Convert(warehouse.getNames()));
				selectedCat.setText(newCateg.getText());
				newCateg.setText("");
			}
		}
	}

	@FXML
	private void checkFields(KeyEvent e) {		
		warning.hide();
		if (nameField.getText().equals("") || priceField.getText().equals("") || piecesField.getText().equals("")
				|| catField.getText().equals("") || pathField.getText().equals(""))
			addItem.setDisable(true);
		else
			addItem.setDisable(false);
	}
	@FXML
	private void openFile(MouseEvent e) {
		File file = fileChooser.showOpenDialog((Stage) pathField.getScene().getWindow());
		if(file!=null)
			pathField.setText(file.getAbsolutePath());
	}

	private ObservableList<String> Convert(List<String> sima) {
		return FXCollections.observableArrayList(sima);
	}

	@FXML
	private void quit(MouseEvent e) {
		try {
			FileOutputStream fileOut = new FileOutputStream("warehouse.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(warehouse);
			out.close();
			System.out.println();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
		Stage stage = (Stage) popBtn.getScene().getWindow();
		stage.close();
	}
}
