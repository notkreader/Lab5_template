package com.pa.refactoring.view;

import com.pa.refactoring.model.Product;
import com.pa.refactoring.model.ShoppingCart;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

/**
 * Template para o laboratório de deteçãod e BAD Smells e aplicação de tecnicas de refatoring
 *
 * @author patricia.macedo
 */
public class MainGui extends Application {
    private ShoppingCart shoppingCart;
    private ListView<Product> listViewCartContents;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        shoppingCart = new ShoppingCart();
        ShoppingCartUI cartUI = new ShoppingCartUI(shoppingCart);
    }


}
