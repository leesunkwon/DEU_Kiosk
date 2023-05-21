/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kiosktest;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lsk85
 */
class FoodState implements MenuState {
    @Override
    public void loadMenu(DefaultTableModel model) {
        loadMenuFromTxtFile("menu2.txt", model);
    }

}
