/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.kiosktest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lsk85
 */
public interface MenuState {
     void loadMenu(DefaultTableModel model);

    default void loadMenuFromTxtFile(String filename, DefaultTableModel model) {
        model.setRowCount(0); // 테이블 모델 초기화
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 2) {
                    String name = tokens[0].trim();
                    String price = tokens[1].trim();
                    model.addRow(new String[]{name, price});
                }
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
