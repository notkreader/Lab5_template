package com.pa.refactoring.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author patricia.macedo
 */
public class ShoppingCart {
    private Time time;
    private boolean terminated;
    private List<Product> products;

    public ShoppingCart() {
        time = new Time();
        terminated = false;
        products = new ArrayList<>();
    }

    public void addProduct(Product prd) {
        products.add(prd);
    }

    public void removeProduct(Product prd) {
        if(products.contains(prd))
            products.remove(prd);
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public double getTotal() {
        double total = 0.0;
        for (Product p : products) {
            total += p.getCost();
        }
        return total;
    }

    public boolean isTerminated() {
        return terminated;
    }

    public void terminate() {
        time = new Time();
        terminated = true;
    }

    public String getDateStr() {
        String dateStr = String.format("%02d/%02d/%4d %02d:%02d", time.getDay(), time.getMonth(), time.getYear(), time.getHour(), time.getMinute());
        return dateStr;
    }
}
