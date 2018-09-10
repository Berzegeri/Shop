package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class User implements Initializable {
	private Stock warehouse = new Stock();
	// Buttons

	// mics.
	@FXML
	private ListView<String> catListView;
	@FXML
	Label summLab;
	@FXML
	private TextField searchField;
	@FXML
	private TilePane everything = new TilePane(Orientation.HORIZONTAL);
	@FXML
	private TilePane categorized = new TilePane(Orientation.HORIZONTAL);

	@FXML
	private VBox hover;
	@FXML
	private StackPane stack;

	private List<Item> Supply = new ArrayList<Item>();
	@FXML
	private GridPane cartGrid = new GridPane();
	@FXML
	private TabPane Tabs;
	@FXML
	private Tab searchTab;
	@FXML
	private Tab allTab;
	private List<Cart> cart = new ArrayList<Cart>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			FileInputStream fileIn = new FileInputStream("warehouse.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			warehouse = (Stock) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("Stock class not found");
			c.printStackTrace();
			return;
		}
		catListView.setItems(Convert(warehouse.getCats()));
		Supply = warehouse.getSupply();
		for (int i = 0; i < Supply.size(); i++) {
			fillTiles(i, everything);
		}
	}

	private void fillTiles(int i, TilePane tiles) {
		Item item = Supply.get(i);
		String path = item.getStr()[2];
		Image im = new Image("File:" + path);

		ImageView image = new ImageView(im);

		Label name = new Label(item.getStr()[0]);
		name.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		Label price = new Label();
		int pr = item.getNumb()[0];
		price.setText(Integer.toString(pr) + " HUF");
		Label pieces = new Label();
		pieces.setText(Integer.toString((item.getNumb()[1])) + " pieces left.");
		VBox tile = new VBox();
		//
		tile.getChildren().addAll(image, name, price, pieces);
		//
		tile.setAlignment(Pos.CENTER);

		tile.setMinHeight(140);
		tile.setMinWidth(125);
		tile.setMaxHeight(140);
		tile.setMaxWidth(125);
		FadeTransition ft = new FadeTransition(Duration.valueOf("0.8s"), tile);
		ft.setFromValue(0.5);
		ft.setToValue(1.0);
		ft.play();
		tile.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				for (int i = 0; i < cart.size(); i++) {
					if (cart.get(i).getName().equals(name.getText())) {
						if( cart.get(i).getDB()<item.getNumb()[1]) {
							cart.get(i).inc();
							refreshCart();
						}
						return;
					}
				}
				if(item.getNumb()[1]>0)
					cart.add(new Cart(name.getText(), pr,item.getNumb()[1]));
				refreshCart();
			}

		});
		tile.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				DropShadow OuterGlow = new DropShadow();
				OuterGlow.setColor(Color.GREEN);
				tile.setEffect(OuterGlow);
			}
		});

		tile.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				tile.setEffect(null);
			}
		});

		tiles.getChildren().addAll(tile);

	}

	@FXML
	private void confirm(MouseEvent e) {
		int p=cart.size();
		for (int j = 0; j < p ; j++) {
			warehouse.sell(cart.get(0).getName(), cart.get(0).getDB());
			cart.remove(0);
		}
		refreshCart();
		SingleSelectionModel<Tab> selectionModel = Tabs.getSelectionModel();
		selectionModel.select(allTab);
		everything.getChildren().removeAll(everything.getChildren());
		for (int i = 0; i < Supply.size(); i++) {
			fillTiles(i, everything);
		}
	}

	@FXML
	private void refreshCart() {
		cartGrid.getChildren().removeAll(cartGrid.getChildren());
		int summ = 0;
		for (int i = 0; i < cart.size(); i++) {

			Label darab = new Label(Integer.toString(cart.get(i).getDB()));
			Label name = new Label(cart.get(i).getName());
			Label full = new Label(darab.getText() + " x " + name.getText() + " " + cart.get(i).getPr() + " HUF");

			Button minus = new Button();
			Button plus = new Button();
			minus.setGraphic(new ImageView(new Image("File:sminus.png")));
			minus.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					for (int j = 0; j < cart.size(); j++) {
						if (cart.get(j).getName().equals(name.getText()) && cart.get(j).getDB() > 0) {
							cart.get(j).dec();
							refreshCart();
							return;
						}
					}
				}
			});
			plus.setGraphic(new ImageView(new Image("File:splus.png")));
			plus.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					for (int j = 0; j < cart.size(); j++) {
						if (cart.get(j).getName().equals(name.getText()) && cart.get(j).getDB() < cart.get(j).getMax()  ) {
							cart.get(j).inc();
							refreshCart();
							return;
						}
					}
				}
			});
			full.setFont(Font.font("Verdana", FontWeight.BOLD, 11));
			summ += cart.get(i).getDB() * cart.get(i).getPr();

			if (!darab.getText().equals("0")) {
				GridPane.setConstraints(minus, 0, i);
				GridPane.setConstraints(full, 1, i);
				GridPane.setConstraints(plus, 2, i);
				cartGrid.getChildren().addAll(minus, full, plus);
				GridPane.setHalignment(full, HPos.CENTER);
			}
		}
		summLab.setText(Integer.toString(summ));
		summLab.setFont(Font.font("Verdana", FontWeight.BOLD, 11));
	}

	@FXML
	private void deleteText(MouseEvent event) {
		searchField.setText("");
	}

	@FXML
	private void handleSelectCat(MouseEvent e) {
		fillCats(catListView.getSelectionModel().getSelectedItem(), 1);
	}

	@FXML
	private void searchByName(KeyEvent e) {
		fillCats(searchField.getText(), 0);
	}

	private void fillCats(String fillBy, int searchBy) {
		categorized.getChildren().removeAll(categorized.getChildren());
		for (int i = 0; i < Supply.size(); i++) {
			if (Supply.get(i).getStr()[searchBy].toLowerCase().equals(fillBy.toLowerCase()))
				fillTiles(i, categorized);
		}
		SingleSelectionModel<Tab> selectionModel = Tabs.getSelectionModel();
		selectionModel.select(searchTab);
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
		Stage stage = (Stage) searchField.getScene().getWindow();
		stage.close();
	}
}
