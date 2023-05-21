/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kiosktest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/** 
 *
 * @author lsk85
 * 2023년 5월 9일 수정
 */
public class KioskTest extends JFrame implements ActionListener {

    private JPanel categoryPanel, menuPanel, cartPanel;
    private JButton coffeeBtn, foodBtn, smoothieBtn, addToCartBtn, removeBtn;
    private JLabel selectedMenuLabel;
    private DefaultTableModel cartModel, menuModel;
    private MenuState currentState;
    
    ImageIcon img1 = new ImageIcon("menu1img.jpg");
    ImageIcon img2 = new ImageIcon("menu2img.jpg");
    ImageIcon img3= new ImageIcon("menu3img.jpg");

    public KioskTest() {
        // JFrame 설정
        setTitle("Kiosk");
        setSize(1600, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
    

        // 카테고리 패널 설정
        categoryPanel = new JPanel(new GridLayout(1, 3));
        coffeeBtn = new JButton(img1);
        coffeeBtn.setPreferredSize(new Dimension(300, 200));
        foodBtn = new JButton(img2);
        foodBtn.setPreferredSize(new Dimension(300, 200));
        smoothieBtn = new JButton(img3);
        smoothieBtn.setPreferredSize(new Dimension(300, 200));
        coffeeBtn.addActionListener(this);
        foodBtn.addActionListener(this);
        smoothieBtn.addActionListener(this);
        categoryPanel.add(coffeeBtn);
        categoryPanel.add(foodBtn);
        categoryPanel.add(smoothieBtn);
        
// 메뉴 패널 설정
        menuPanel = new JPanel(new BorderLayout());
        menuModel = new DefaultTableModel(new Object[][]{}, new String[]{"메뉴", "가격"});
        loadMenuFromTxtFile("menu.txt", menuModel); // txt 파일에서 메뉴 데이터를 읽어옴
        JTable table = new JTable(menuModel);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 46)); // 폰트 크기 변경
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(50);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String menu = (String) menuModel.getValueAt(row, 0);
                    String price = (String) menuModel.getValueAt(row, 1);
                    selectedMenuLabel.setText(menu + " " + price);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        menuPanel.add(scrollPane, BorderLayout.CENTER);
        selectedMenuLabel = new JLabel("메뉴를 선택하세요.");
        
 
          // 장바구니 패널 설정
    cartPanel = new JPanel(new BorderLayout());
    cartPanel.setPreferredSize(new Dimension(600, 1200)); // 가로 크기 600으로 설정
    cartModel = new DefaultTableModel(new Object[][]{}, new String[]{"메뉴", "가격", "수량"});
    JTable cartTable = new JTable(cartModel);
    cartTable.setFont(new Font("맑은 고딕", Font.PLAIN, 20)); // 폰트 크기 변경
        cartTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        cartTable.setRowHeight(50);
    JScrollPane cartScrollPane = new JScrollPane(cartTable);
    addToCartBtn = new JButton("장바구니에 추가");
    addToCartBtn.setPreferredSize(new Dimension(200, 100));
    addToCartBtn.addActionListener((ActionEvent e) -> {
    String selectedMenu = selectedMenuLabel.getText();
    if (selectedMenu.equals("메뉴를 선택하세요.")) {
        JOptionPane.showMessageDialog(KioskTest.this, "장바구니에 추가할 메뉴를 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
        return;
    }
    String[] cartData = {getMenu(selectedMenu), getPrice(selectedMenu + "원")};
    int rowCount = cartModel.getRowCount();
    boolean alreadyExist = false;
    for (int i = 0; i < rowCount; i++) {
        String menu = (String) cartModel.getValueAt(i, 0);
        if (menu.equals(cartData[0])) {
            int quantity = Integer.parseInt((String) cartModel.getValueAt(i, 2));
            cartModel.setValueAt(String.valueOf(quantity + 1), i, 2);
            alreadyExist = true;
            break;
        }
    }
    if (!alreadyExist) {
        cartModel.addRow(new String[]{cartData[0], cartData[1], "1"});
    }
    
});
    
    
    JButton checkoutBtn = new JButton("결제하기");
checkoutBtn.setPreferredSize(new Dimension(200, 100));
checkoutBtn.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        // 결제 로직을 구현해주세요.
        JOptionPane.showMessageDialog(KioskTest.this, "결제가 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
        cartModel.setRowCount(0); // 장바구니 비우기
    }
});


cartPanel.add(checkoutBtn, BorderLayout.EAST);
     cartPanel.add(cartScrollPane, BorderLayout.CENTER);
    cartPanel.add(addToCartBtn, BorderLayout.SOUTH);
    
 removeBtn = new JButton("장바구니 비우기");
removeBtn.setPreferredSize(new Dimension(200, 100));
removeBtn.addActionListener((ActionEvent e) -> {
    cartModel.setRowCount(0);
});
cartPanel.add(removeBtn, BorderLayout.NORTH);
    
    

    // JFrame에 패널 추가
    add(categoryPanel, BorderLayout.NORTH);
    add(menuPanel, BorderLayout.CENTER);
    add(selectedMenuLabel, BorderLayout.SOUTH);

    add(cartPanel, BorderLayout.EAST);

    setVisible(true);
}
    
     public void setState(MenuState state) { //상태 변경을 위한 메소드
        currentState = state;
        currentState.loadMenu(menuModel);
    }
    @Override
public void actionPerformed(ActionEvent e) { //메소드에서 버튼을 누를 때마다 상태를 변경
     Object source = e.getSource();
    if (source == coffeeBtn) {
        setState(new CoffeeState());
    } else if (source == foodBtn) {
        setState(new FoodState());
    } else if (source == smoothieBtn) {
        setState(new SmoothieState());
    }
}

private String getPrice(String menu) { //선택한 메뉴에서 가격 정보를 추출하여 반환
    String[] tokens = menu.split(" ");
    return tokens[tokens.length - 1];
}

private String getMenu(String menu) { //선택한 메뉴에서 메뉴명 정보를 추출하여 반환
    String[] tokens = menu.split(" ");
    String menuName = "";
    for(int i = 0; i < tokens.length - 1; i++) {
        menuName += tokens[i] + " ";
    }
    return menuName.trim();
}

private void loadMenuFromTxtFile(String filename, DefaultTableModel model) {
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



public static void main(String[] args) {
    new KioskTest();
}
}





