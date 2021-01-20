package com.pa.refactoring.view;

import com.pa.refactoring.model.Product;
import com.pa.refactoring.model.ShoppingCart;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ShoppingCartUI extends BorderPane {
    private ShoppingCart shoppingCart;
    private ListView<Product> listViewCartContents;

    public ShoppingCartUI(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        createView();
    }

    private void createView() {
        // Add product
        GridPane gridPaneAddProduct = getGridPane();
        Label labelAddProduct = getLabelWithStyle("Add products to cart");
        gridPaneAddProduct.add(labelAddProduct, 0, 0);
        gridPaneAddProduct.add(new Label("Name"), 0, 1);
        TextField textFieldProductName = getTextField();
        gridPaneAddProduct.add(textFieldProductName, 1, 1);
        gridPaneAddProduct.add(new Label("Price"), 0, 2);
        TextField textFieldPrice = getTextField();
        gridPaneAddProduct.add(textFieldPrice, 1, 2);

        Label labelEnd = new Label();
        setBottom(labelEnd);

        HBox hBoxAddProductButtons = getHBox();
        Label labelCost = new Label("Total cost: 0.0 €");
        gridPaneAddProduct.add(labelCost, 0, 3);
        gridPaneAddProduct.add(hBoxAddProductButtons, 1, 3);

        getAllButtons(gridPaneAddProduct, textFieldProductName, textFieldPrice, labelEnd, hBoxAddProductButtons, labelCost);

        // Shopping cart
        getListViewGridPane();

        createStage();
    }

    private void getListViewGridPane() {
        GridPane gridPaneCartContents = getGridPane();
        createListView(gridPaneCartContents);
        setStyle("-fx-padding: 5px");
    }

    private void getAllButtons(GridPane gridPaneAddProduct, TextField textFieldProductName, TextField textFieldPrice, Label labelEnd, HBox hBoxAddProductButtons, Label labelCost) {
        Button buttonAddProduct = getAddButton(textFieldProductName, textFieldPrice, labelCost);
        Button buttonTerminate = getTerminateButton(labelEnd, labelCost, buttonAddProduct);

        hBoxAddProductButtons.getChildren().addAll(buttonAddProduct, buttonTerminate);
        setTop(gridPaneAddProduct);
    }

    private HBox getHBox() {
        HBox hBoxAddProductButtons = new HBox(6);
        hBoxAddProductButtons.setAlignment(Pos.CENTER_RIGHT);
        hBoxAddProductButtons.setStyle("-fx-padding: 2px 0 0 0");
        return hBoxAddProductButtons;
    }

    private Button getAddButton(TextField textFieldProductName, TextField textFieldPrice, Label labelCost) {
        Button buttonAddProduct = new Button("Add");
        buttonAddProduct.setOnAction((ActionEvent e) -> {
            addToCart(textFieldProductName, textFieldPrice, labelCost);
        });
        return buttonAddProduct;
    }

    private void addToCart(TextField textFieldProductName, TextField textFieldPrice, Label labelCost) {
        if (textFieldProductName.getText().isEmpty() || textFieldPrice.getText().isEmpty()) {
            showShoppingCartError("empty fields");

        } else {
            try {
                String name = textFieldProductName.getText();
                double price = Double.parseDouble(textFieldPrice.getText());
                shoppingCart.addProduct(new Product(name, price));
                labelCost.setText(String.format("Total Cost %.1f €", shoppingCart.getTotal()));
                listViewCartContents.getItems().clear();
                for (Product product : shoppingCart.getProducts()) {
                    listViewCartContents.getItems().add(product);
                }
                textFieldPrice.clear();
                textFieldProductName.clear();
            } catch (NumberFormatException nfe) {
                showShoppingCartError("invalid format");
            }
        }
    }

    private Button getTerminateButton(Label labelEnd, Label labelCost, Button buttonAddProduct) {
        Button buttonTerminate = new Button("End");
        buttonTerminate.setOnAction((ActionEvent e) -> {
            terminateCart(labelEnd, labelCost, buttonAddProduct);
        });
        return buttonTerminate;
    }

    private void terminateCart(Label labelEnd, Label labelCost, Button buttonAddProduct) {
        shoppingCart.terminate();

        String strEnd;
        if (shoppingCart.isTerminated()) {
            strEnd = String.format("%s Total Cost %.2f Number of Items %d", shoppingCart.getDateStr(),
                    shoppingCart.getTotal(), shoppingCart.getProducts().size());
        } else {
            strEnd = "";
        }
        labelCost.setText(String.format("Current Cost %.1f €", shoppingCart.getTotal()));
        buttonAddProduct.setDisable(true);

        labelEnd.setText(strEnd);
    }

    private Label getLabelWithStyle(String s) {
        Label labelAddProduct = new Label(s);
        labelAddProduct.setStyle("-fx-font-weight: bold");
        return labelAddProduct;
    }

    private TextField getTextField() {
        return new TextField();
    }

    private GridPane getGridPane() {
        return new GridPane();
    }

    private void createListView(GridPane gridPaneCartContents) {
        Label labelCartContents = getLabelWithStyle("Cart contents");
        listViewCartContents = new ListView<>();
        gridPaneCartContents.add(labelCartContents, 0, 0);
        gridPaneCartContents.add(listViewCartContents, 0, 1);
        GridPane.setHgrow(listViewCartContents, Priority.ALWAYS);
        setCenter(gridPaneCartContents);
        listViewCartContents.getItems().clear();
        for (Product product : shoppingCart.getProducts()) {
            listViewCartContents.getItems().add(product);
        }
    }

    private void showShoppingCartError(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Shopping Cart Error");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    private void createStage() {
        Stage stage = new Stage();
        Scene scene = new Scene(this, 400, 600);
        stage.setTitle("Shopping Cart");
        stage.setScene(scene);
        stage.show();
    }
}
