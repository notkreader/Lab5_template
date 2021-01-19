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
        GridPane gridPaneAddProduct = new GridPane();
        Label labelAddProduct = new Label("Add products to cart");
        labelAddProduct.setStyle("-fx-font-weight: bold");
        gridPaneAddProduct.add(labelAddProduct, 0, 0);
        gridPaneAddProduct.add(new Label("Name"), 0, 1);
        TextField textFieldProductName = new TextField();
        gridPaneAddProduct.add(textFieldProductName, 1, 1);
        gridPaneAddProduct.add(new Label("Price"), 0, 2);
        TextField textFieldPrice = new TextField();
        gridPaneAddProduct.add(textFieldPrice, 1, 2);
        HBox hBoxAddProductButtons = new HBox(6);
        Button buttonAddProduct = new Button("Add");
        Button buttonTerminate = new Button("End");
        Label labelEnd = new Label();
        setBottom(labelEnd);

        hBoxAddProductButtons.setAlignment(Pos.CENTER_RIGHT);
        hBoxAddProductButtons.setStyle("-fx-padding: 2px 0 0 0");
        Label labelCost = new Label("Total cost: 0.0 €");
        gridPaneAddProduct.add(labelCost, 0, 3);
        gridPaneAddProduct.add(hBoxAddProductButtons, 1, 3);
        hBoxAddProductButtons.getChildren().addAll(buttonAddProduct, buttonTerminate);
        buttonAddProduct.setOnAction((ActionEvent e) -> {
            addToCart(textFieldProductName, textFieldPrice, labelCost);
        });
        setTop(gridPaneAddProduct);
        buttonTerminate.setOnAction((ActionEvent e) -> {
            endCart(buttonAddProduct, labelEnd, labelCost);
        });

        // Shopping cart
        GridPane gridPaneCartContents = new GridPane();
        createListView(gridPaneCartContents);
        setStyle("-fx-padding: 5px");
    }

    private void createListView(GridPane gridPaneCartContents) {
        Label labelCartContents = new Label("Cart contents");
        labelCartContents.setStyle("-fx-font-weight: bold");
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

    private void showShoppingCartError(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Shopping Cart Error");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    private void endCart(Button buttonAddProduct, Label labelEnd, Label labelCost) {
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

    private void createStage() {
        Stage stage = new Stage();
        Scene scene = new Scene(this, 400, 600);
        stage.setTitle("Shopping Cart");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
