/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.vjproject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
/**
 *
 * @author mhm55
 */
public class customerDashBoard extends javax.swing.JFrame {
    public DefaultTableModel model;
    //medic proprties
    private String medicinName ;
    private int medicinquantity;
    private int medicinprice ;
    private String medicinId ;
    private int medicinquantityInCart;
    private int medicinQuantityInCartTable;
    
    private String userID;
    
    //order proprties
    private String orderID;
    private String date;
    private String totalAmount;
    private String status;
            
    private String db_query = "";
    private ResultSet rs ;
    private Connection conn = null;
    boolean MedicinSelceted = false;
    boolean ismedicinAlreadyThere = false;
    private PreparedStatement pst = null;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(customerDashBoard.class.getName());

    private customerDashBoard() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    private void Update_MedicinesTable() throws SQLException{
        try{
            conn = Connection2DB.ConneectorDB();
            db_query = "SELECT MedicineName, MedicineID, quantity, price, Details FROM `pharmacyDB`.`Medicines`";
            pst = conn.prepareStatement(db_query);
            rs = pst.executeQuery();
            
            MedicinsTable.setRowSorter(null); 
            
            MedicinsTable.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e);
        }
        
        
    }
    
   
    
    private void Update_OrdersTable() throws SQLException{
        try{
            conn = Connection2DB.ConneectorDB();
            db_query = "select `orderID`, `orderDate`, `totalAmount`, `status` from `Orders` where userID = ? order by `Orders`.`status`;";
            pst = conn.prepareStatement(db_query);
            pst.setString(1,userID);
            rs = pst.executeQuery();
            
            OrdersTable.setRowSorter(null); 
            
            OrdersTable.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e);
        }
        
    }
    
    private void Update_OrdersDetailsTable(String orderID) throws SQLException{
        try{
            conn = Connection2DB.ConneectorDB();
            db_query = "SELECT m.MedicineName, m.MedicineID, od.quantity, od.unitPrice, od.subtotal " +
                       "FROM OrderDetails od " +
                       "INNER JOIN Medicines m ON od.medicineID = m.MedicineID " +
                       "WHERE od.orderID = ?";
            pst = conn.prepareStatement(db_query);
            pst.setString(1,orderID);
            rs = pst.executeQuery();
            
            OrderDetailsTable.setRowSorter(null); 
            
            OrderDetailsTable.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e);
        }
        finally{
            conn.close();
        }
        
    }
    
    
    /**
     * Creates new form customerDashBoard
     * @param userId
     * @throws java.sql.SQLException
     */
    public customerDashBoard(String userId) throws SQLException {
    initComponents();
    userID = userId;
    Update_MedicinesTable();
    Update_OrdersTable();
    model = (DefaultTableModel) CartTable.getModel();
    TableColumn quantityColumn = CartTable.getColumnModel().getColumn(3);

    DefaultCellEditor quantityEditor = new DefaultCellEditor(new javax.swing.JTextField());

    quantityEditor.addCellEditorListener(new javax.swing.event.CellEditorListener() {

        @Override
        public void editingStopped(javax.swing.event.ChangeEvent e) {

            int newQuantity = 0;
            int selectedRow = CartTable.getSelectedRow();
            
            // 1. Get the Medicine ID from the Cart Row being edited
            String currentMedID = CartTable.getValueAt(selectedRow, 1).toString();
            int actualStock = 0;

            try {
                // 2. Query the DB to get the REAL stock for this specific item
                Connection tempConn = Connection2DB.ConneectorDB();
                String sql = "SELECT quantity FROM Medicines WHERE MedicineID = ?";
                PreparedStatement tempPst = tempConn.prepareStatement(sql);
                tempPst.setString(1, currentMedID);
                ResultSet tempRs = tempPst.executeQuery();
                
                if (tempRs.next()) {
                    actualStock = tempRs.getInt("quantity");
                }
                tempConn.close();

                String valueStr = CartTable.getValueAt(selectedRow, 3).toString();
                newQuantity = Integer.parseInt(valueStr);

            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(null, "Invalid Number or DB Error");
                CartTable.setValueAt(1, selectedRow, 3); 
                return;
            }

            // 3. Compare New Quantity directly against Actual Stock
            if (newQuantity > actualStock) {
                javax.swing.JOptionPane.showMessageDialog(null, 
                    "This is more than what is available!\n(Stock Limit: " + actualStock + ")");
                
                // Set it to the Max Stock, NOT the difference
                CartTable.setValueAt(actualStock, selectedRow, 3);

            } else if (newQuantity <= 0) {
                javax.swing.JOptionPane.showMessageDialog(null, "Quantity must be at least 1.");
                CartTable.setValueAt(1, selectedRow, 3);
            }

            // 4. Update the total price
            updateTotalPrice();
        }

        @Override
        public void editingCanceled(javax.swing.event.ChangeEvent e) {
        }
    });

    quantityColumn.setCellEditor(quantityEditor);
}

    public double updateTotalPrice() {
    double total = 0;
    
    for (int i = 0; i < CartTable.getRowCount(); i++) {
        try {
            double price = Double.parseDouble(CartTable.getValueAt(i, 2).toString());
            int quantity = Integer.parseInt(CartTable.getValueAt(i, 3).toString());
            
            total += price * quantity;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,ex);
            return 0;
        }
       
    }
    
    Totaltxt.setText(String.valueOf(total)+"$");
    return total;
}
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jButton4 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        IdMedField = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        MedicinsTable = new javax.swing.JTable();
        QuantityMedField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        PriceMedField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        Searchtxt = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        NameMedField = new javax.swing.JTextField();
        AddtoCartBtn = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        MedDetailsArea = new javax.swing.JTextArea();
        QuantityCombo = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        CartTable = new javax.swing.JTable();
        BuyBtn = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        Totaltxt = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        OrdersTable = new javax.swing.JTable();
        OrderSearchtxt = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        OrderDetailsTable = new javax.swing.JTable();
        ODMedicSearchtxt = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        IDtxt = new javax.swing.JTextField();
        Datetxt = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        Pricetxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        Statustxt = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        jButton4.setText("jButton4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel10.setText("Quantity");

        jLabel11.setText("Search");

        IdMedField.setEditable(false);
        IdMedField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdMedFieldActionPerformed(evt);
            }
        });

        MedicinsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "ID", "Quantity", "Price", "Details"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        MedicinsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        MedicinsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MedicinsTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(MedicinsTable);

        QuantityMedField.setEditable(false);
        QuantityMedField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuantityMedFieldActionPerformed(evt);
            }
        });

        jLabel14.setText("Name");

        PriceMedField.setEditable(false);

        jLabel15.setText("ID");

        Searchtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchtxtActionPerformed(evt);
            }
        });
        Searchtxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SearchtxtKeyReleased(evt);
            }
        });

        jLabel16.setText("Price");

        NameMedField.setEditable(false);

        AddtoCartBtn.setText("Add to Cart");
        AddtoCartBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddtoCartBtnActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Liberation Serif", 3, 36)); // NOI18N
        jLabel17.setText("Medicins");

        jLabel1.setText("Details");

        MedDetailsArea.setEditable(false);
        MedDetailsArea.setColumns(20);
        MedDetailsArea.setRows(5);
        jScrollPane1.setViewportView(MedDetailsArea);

        QuantityCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuantityComboActionPerformed(evt);
            }
        });

        jLabel2.setText("Quantity");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel14)
                                        .addComponent(jLabel15)
                                        .addComponent(jLabel10)
                                        .addComponent(jLabel16))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(IdMedField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(NameMedField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(PriceMedField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(QuantityMedField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(111, 111, 111)
                                    .addComponent(jLabel1))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(AddtoCartBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(QuantityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 343, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(Searchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(269, 269, 269)
                        .addComponent(jLabel17)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(342, 342, 342)
                    .addComponent(jScrollPane4)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Searchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)))
                    .addComponent(jLabel17))
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(NameMedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IdMedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(QuantityMedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PriceMedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(QuantityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddtoCartBtn))
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap(71, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        jTabbedPane1.addTab("Market", jPanel2);

        jLabel9.setFont(new java.awt.Font("FreeSerif", 3, 24)); // NOI18N
        jLabel9.setText("Cart");

        CartTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Medicin Name", "Medicin ID", "Medicin price per unit", "Medicin Quantity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        CartTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CartTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(CartTable);

        BuyBtn.setText("Buy");
        BuyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuyBtnActionPerformed(evt);
            }
        });

        jButton5.setText("Delete Medicin");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Liberation Serif", 3, 18)); // NOI18N
        jLabel3.setText("Total Price");

        Totaltxt.setEditable(false);
        Totaltxt.setText("0$");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1040, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(385, 385, 385)
                .addComponent(jButton5))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(429, 429, 429)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(Totaltxt, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(478, 478, 478)
                        .addComponent(BuyBtn)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jButton5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(Totaltxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(BuyBtn))
        );

        jTabbedPane1.addTab("Cart", jPanel1);

        jScrollPane5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane5MouseClicked(evt);
            }
        });

        OrdersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "OrderNO.", "Date", "Price", "status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        OrdersTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        OrdersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OrdersTableMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(OrdersTable);

        OrderSearchtxt.setColumns(17);
        OrderSearchtxt.setText("Order number");
        OrderSearchtxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                OrderSearchtxtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                OrderSearchtxtFocusLost(evt);
            }
        });
        OrderSearchtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrderSearchtxtActionPerformed(evt);
            }
        });
        OrderSearchtxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                OrderSearchtxtKeyReleased(evt);
            }
        });

        jLabel18.setText("search");

        jLabel19.setFont(new java.awt.Font("Liberation Serif", 3, 24)); // NOI18N
        jLabel19.setText("Orders");

        OrderDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MedicinName", "MedicinID.", "Quantity", "Price", "subTotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        OrderDetailsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        OrderDetailsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OrderDetailsTableMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(OrderDetailsTable);

        ODMedicSearchtxt.setColumns(17);
        ODMedicSearchtxt.setText("Medicin name");
        ODMedicSearchtxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ODMedicSearchtxtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                ODMedicSearchtxtFocusLost(evt);
            }
        });
        ODMedicSearchtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ODMedicSearchtxtActionPerformed(evt);
            }
        });
        ODMedicSearchtxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ODMedicSearchtxtKeyReleased(evt);
            }
        });

        jLabel20.setText("search");

        jLabel21.setFont(new java.awt.Font("Liberation Serif", 3, 24)); // NOI18N
        jLabel21.setText("Order details");

        jLabel8.setFont(new java.awt.Font("Liberation Serif", 3, 24)); // NOI18N
        jLabel8.setText("Info");

        jLabel22.setText("ID");

        IDtxt.setEditable(false);
        IDtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDtxtActionPerformed(evt);
            }
        });

        Datetxt.setEditable(false);
        Datetxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DatetxtActionPerformed(evt);
            }
        });

        jLabel23.setText("Date");

        jLabel24.setText("Price");

        Pricetxt.setEditable(false);

        jLabel4.setText("Status");

        Statustxt.setEditable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap(32, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(IDtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Pricetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Datetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Statustxt, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ODMedicSearchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(OrderSearchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGap(106, 106, 106)
                            .addComponent(jLabel8)
                            .addGap(18, 18, 18)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(IDtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel22))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Datetxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel23))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(Pricetxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel24))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Statustxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel20)
                                .addComponent(ODMedicSearchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel21))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(OrderSearchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Orders", jPanel3);

        jMenu1.setText("Settings");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem1.setText("Log out");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>                        

    private void IdMedFieldActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void MedicinsTableMouseClicked(java.awt.event.MouseEvent evt) {                                           
        try{
            MedicinSelceted = true;
            conn = Connection2DB.ConneectorDB();
            int row = MedicinsTable.getSelectedRow();
            QuantityCombo.removeAllItems();
            String item;
            String table_click=(MedicinsTable.getModel().getValueAt(row, 1).toString());
            db_query = "select * from `Medicines` where `MedicineID`='"+table_click+"'";
            pst = conn.prepareStatement(db_query);
            rs = pst.executeQuery();
            if(rs.next()){
                
                medicinId = rs.getString("MedicineID");
                medicinprice = rs.getInt("price");
                medicinquantity = rs.getInt("quantity");
                medicinName = rs.getString("MedicineName");
                
                IdMedField.setText(medicinId);
                PriceMedField.setText(String.format("%s", medicinprice));
                QuantityMedField.setText(String.format("%s", medicinquantity));
                NameMedField.setText(medicinName);
                MedDetailsArea.setText(rs.getString("Details"));
                medicinquantity=rs.getInt("quantity");
                
                for(int i = 1;i<=medicinquantity;i++){
                    item = String.format("%s", i);
                    QuantityCombo.addItem(item);
                }
                
            }
            else
                JOptionPane.showMessageDialog(null,"we didn't find anyething!");

        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e);
        }
        finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,ex);
            }
        }
    }                                          

    private void QuantityMedFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
    }                                                

    private void OrdersTableMouseClicked(java.awt.event.MouseEvent evt) {                                         
        try{
            conn = Connection2DB.ConneectorDB();
            
            int viewRow = OrdersTable.getSelectedRow();
            int modelRow = OrdersTable.convertRowIndexToModel(viewRow);
            
            String table_click = (OrdersTable.getModel().getValueAt(modelRow, 0).toString());
            
            db_query = "select * from `Orders` where `orderID`='"+table_click+"'";
            pst = conn.prepareStatement(db_query);
            rs = pst.executeQuery();
            if(rs.next()){
                String attrebut1 = rs.getString("orderID");
                String attrebut2 = rs.getString("orderDate");
                String attrebut3 = rs.getString("totalAmount");
                String attrebut4 = rs.getString("status");
                        
                orderID = attrebut1;
                date = attrebut2;
                totalAmount = attrebut3;
                status = attrebut4;
                
                IDtxt.setText(orderID);
                Datetxt.setText(date);
                Pricetxt.setText(totalAmount);
                Statustxt.setText(status);
                
                Update_OrdersDetailsTable(orderID); 
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e);
        }
        finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,ex);
            }
        }
    }                                        

    private void OrderSearchtxtFocusGained(java.awt.event.FocusEvent evt) {                                           
        if(OrderSearchtxt.getText().equals("Order number"))
        OrderSearchtxt.setText("");        // TODO add your handling code here:
    }                                          

    private void OrderSearchtxtFocusLost(java.awt.event.FocusEvent evt) {                                         
        if(OrderSearchtxt.getText().equals(""))
        OrderSearchtxt.setText("Order number");    // TODO add your handling code here:
    }                                        

    private void OrderSearchtxtActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // TODO add your handling code here:
    }                                              

    private void OrderDetailsTableMouseClicked(java.awt.event.MouseEvent evt) {                                               
        // TODO add your handling code here:
    }                                              

    private void ODMedicSearchtxtFocusGained(java.awt.event.FocusEvent evt) {                                             
        if(ODMedicSearchtxt.getText().equals("Medicin name"))
        ODMedicSearchtxt.setText("");        // TODO add your handling code here:
    }                                            

    private void ODMedicSearchtxtFocusLost(java.awt.event.FocusEvent evt) {                                           
        if(ODMedicSearchtxt.getText().equals(""))
        ODMedicSearchtxt.setText("Medicin name");        // TODO add your handling code here:
    }                                          

    private void ODMedicSearchtxtActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        if(!ODMedicSearchtxt.getText().equals("")){
            try{
                conn = Connection2DB.ConneectorDB();
                db_query = "select * from `pharmacyDB`.`Medicines` where `MedicineName` like ?";
                pst = conn.prepareStatement(db_query);
                pst.setString(1, Searchtxt.getText()+"%");
                rs = pst.executeQuery();
                MedicinsTable.setModel(DbUtils.resultSetToTableModel(rs));
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null,e);
            }
            
            
        }
        else{
            
        }
    }                                                

    private void IDtxtActionPerformed(java.awt.event.ActionEvent evt) {                                      
        // TODO add your handling code here:
    }                                     

    private void DatetxtActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void QuantityComboActionPerformed(java.awt.event.ActionEvent evt) {                                              
            medicinquantityInCart = QuantityCombo.getSelectedIndex()+1;
    }                                             

    private void AddtoCartBtnActionPerformed(java.awt.event.ActionEvent evt) {                                             
        DefaultTableModel cart = (DefaultTableModel) CartTable.getModel();
//        DefaultTableModel MedicineTable = (DefaultTableModel) MedicinsTable.getModel();
        if(MedicinSelceted){
            for(int i = 0;i<cart.getRowCount();i++){
                if(cart.getValueAt(i, 1).equals(medicinId)){
                    ismedicinAlreadyThere = true;
                    if(medicinquantityInCart<=medicinquantity-(Integer.parseInt(cart.getValueAt(i, 3).toString()))){
                        medicinQuantityInCartTable += medicinquantityInCart ;
                        cart.setValueAt(medicinQuantityInCartTable, i, 3);
                        updateTotalPrice();
                        
                    }else{
                        JOptionPane.showMessageDialog(null,"This is more than what is available!");
                    }
                    break;
                }
            }
            if(!ismedicinAlreadyThere){
                medicinQuantityInCartTable = medicinquantityInCart;
                cart.addRow(new Object[]{medicinName, medicinId, medicinprice,medicinQuantityInCartTable});
                updateTotalPrice();
            }
        }
        else{
                JOptionPane.showMessageDialog(null,"There is no item is selected!");
        }
    }                                            

    private void CartTableMouseClicked(java.awt.event.MouseEvent evt) {                                       
    }                                      

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        DefaultTableModel cart = (DefaultTableModel) CartTable.getModel();

        int selectedRow = CartTable.getSelectedRow();

        if (selectedRow != -1) {
            cart.removeRow(selectedRow);

            updateTotalPrice();

        } else {
            JOptionPane.showMessageDialog(null, "Please select an item to remove.");
        }
    }                                        

    @SuppressWarnings("empty-statement")
    private void BuyBtnActionPerformed(java.awt.event.ActionEvent evt) {                                       
        DefaultTableModel cart = (DefaultTableModel) CartTable.getModel();

        // 1. Check if cart is empty
        if(cart.getRowCount() == 0){
            JOptionPane.showMessageDialog(null, "There is no item to buy.");
            return; // Stop execution
        }

        try {
            conn = Connection2DB.ConneectorDB();

            // --- START TRANSACTION ---
            // By default, Java commits every query immediately. We turn this off
            // so we can roll back if something fails halfway through.
            conn.setAutoCommit(false);

            double total = updateTotalPrice();
            int newOrderID = -1;

            // 2. Insert the Main Order
            // Note: I added 'PreparedStatement.RETURN_GENERATED_KEYS' to get the ID immediately
            String insertOrderQuery = "INSERT INTO `pharmacyDB`.`Orders`(`userID`, `totalAmount`, `orderDate`) VALUES (?, ?, NOW())";
            pst = conn.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, userID);
            pst.setDouble(2, total);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                // 3. Get the generated Order ID automatically
                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    newOrderID = rs.getInt(1); // This is the new auto-incremented ID
                }
            }

            if (newOrderID == -1) {
                throw new SQLException("Failed to create order ID.");
            }

            // 4. Loop through the Cart Table (The Transaction Core)
            String insertDetailQuery = "INSERT INTO `pharmacyDB`.`OrderDetails`(`orderID`, `medicineID`, `unitPrice` , `quantity`, `subtotal`) VALUES (?, ?, ?, ?, ?)";
            String updateStockQuery = "UPDATE `pharmacyDB`.`Medicines` SET `quantity` = `quantity` - ? WHERE `MedicineID` = ?";

            PreparedStatement pstDetail = conn.prepareStatement(insertDetailQuery);
            PreparedStatement pstStock = conn.prepareStatement(updateStockQuery);

            for (int i = 0; i < cart.getRowCount(); i++) {
                // Get data from Cart Table columns (Adjust indices if your table changed)
                String medID = cart.getValueAt(i, 1).toString(); // Col 1 is ID
                int qty = Integer.parseInt(cart.getValueAt(i, 3).toString()); // Col 3 is Quantity
                int unitprice= Integer.parseInt(cart.getValueAt(i, 2).toString());
                int subtotal = unitprice*qty;
                // A. Add to OrderDetails
                pstDetail.setInt(1, newOrderID);
                pstDetail.setString(2, medID);
                pstDetail.setInt(3, unitprice);
                pstDetail.setInt(4, qty);
                pstDetail.setInt(5,subtotal);
                pstDetail.executeUpdate();

                // B. Update Stock (Subtract quantity)
                pstStock.setInt(1, qty);
                pstStock.setString(2, medID);
                pstStock.executeUpdate();
            }
            // --- COMMIT TRANSACTION ---
            // If we reached here, everything worked. Save changes.
            conn.commit();
            Update_OrdersTable();
            
            JOptionPane.showMessageDialog(this, "Order Placed Successfully! Order ID: " + newOrderID);

            // 5. Clear the cart UI
            cart.setRowCount(0);
            Totaltxt.setText("0$");
            updateTotalPrice();
            Update_MedicinesTable();

        } catch (SQLException e) {
            // --- ROLLBACK ---
            // If ANY error happened (SQL error, stock issue), undo everything.
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Rollback failed: " + ex);
            }
            JOptionPane.showMessageDialog(null, "Transaction Failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                // Reset auto-commit to true for future actions and close connection
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
                if (pst != null) pst.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null , ex);
            }
        }

    }                                      

    private void SearchtxtActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if(!Searchtxt.getText().equals("")){
            try{
                conn = Connection2DB.ConneectorDB();
                db_query = "select * from `pharmacyDB`.`Medicines` where `MedicineName` like ?";
                pst = conn.prepareStatement(db_query);
                pst.setString(1, Searchtxt.getText()+"%");
                rs = pst.executeQuery();
                MedicinsTable.setModel(DbUtils.resultSetToTableModel(rs));
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null,e);
            }
            
            
        }
        else{
            try {
                Update_MedicinesTable();
            } catch (SQLException ex) {
                System.getLogger(customerDashBoard.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        try {
            conn.close();
        } catch (SQLException ex) {
            System.getLogger(pharmcistDashBoard.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }                                         

    private void jScrollPane5MouseClicked(java.awt.event.MouseEvent evt) {                                          

    }                                         

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {                                       

    }                                      

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        pharmacyLogin frm = new pharmacyLogin();
        frm.setVisible(true);
        this.dispose();
    }                                          

    private void SearchtxtKeyReleased(java.awt.event.KeyEvent evt) {                                      
        // or use the generic TableModel interface.
        javax.swing.table.TableModel searchedMedicineTable = MedicinsTable.getModel();

        // 2. Create a specific Sorter for this model
        TableRowSorter<javax.swing.table.TableModel> sorter = new TableRowSorter<>(searchedMedicineTable);

        // 3. Attach the sorter to the table
        MedicinsTable.setRowSorter(sorter);

        // 4. Get the text from the search box
        String searchString = Searchtxt.getText();

        // 5. Apply the filter
        if (searchString.trim().length() == 0 || searchString.equals("Order number")) {
            sorter.setRowFilter(null); // Reset filter if empty
        } else {
            // (?i) makes it case-insensitive
            // The indices parameter (optional) specifies which column to look at. 
            // If left empty, it searches all columns.
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchString, 0));
        }

    }                                     

    private void OrderSearchtxtKeyReleased(java.awt.event.KeyEvent evt) {                                           
        // Note: DbUtils returns a TableModel, so we cast to DefaultTableModel if possible, 
        // or use the generic TableModel interface.
        javax.swing.table.TableModel model = OrdersTable.getModel();

        // 2. Create a specific Sorter for this model
        TableRowSorter<javax.swing.table.TableModel> sorter = new TableRowSorter<>(model);

        // 3. Attach the sorter to the table
        OrdersTable.setRowSorter(sorter);

        // 4. Get the text from the search box
        String searchString = OrderSearchtxt.getText();

        // 5. Apply the filter
        if (searchString.trim().length() == 0 || searchString.equals("Order number")) {
            sorter.setRowFilter(null); // Reset filter if empty
        } else {
            // (?i) makes it case-insensitive
            // The indices parameter (optional) specifies which column to look at. 
            // If left empty, it searches all columns.
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchString, 0));
        }    }                                          

    private void ODMedicSearchtxtKeyReleased(java.awt.event.KeyEvent evt) {                                             
        javax.swing.table.TableModel searchedOrderDetailsTable = OrderDetailsTable.getModel();

        // 2. Create the sorter
        TableRowSorter<javax.swing.table.TableModel> sorter = new TableRowSorter<>(searchedOrderDetailsTable);

        // 3. Set the sorter
        OrderDetailsTable.setRowSorter(sorter);

        // 4. Get text
        String searchString = ODMedicSearchtxt.getText();

        // 5. Apply Filter
        if (searchString.trim().length() == 0 || searchString.equals("Medicin name")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchString,0));
        }
    }                                            

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new customerDashBoard().setVisible(true);
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton AddtoCartBtn;
    private javax.swing.JButton BuyBtn;
    private javax.swing.JTable CartTable;
    private javax.swing.JTextField Datetxt;
    private javax.swing.JTextField IDtxt;
    private javax.swing.JTextField IdMedField;
    private javax.swing.JTextArea MedDetailsArea;
    private javax.swing.JTable MedicinsTable;
    private javax.swing.JTextField NameMedField;
    private javax.swing.JTextField ODMedicSearchtxt;
    private javax.swing.JTable OrderDetailsTable;
    private javax.swing.JTextField OrderSearchtxt;
    private javax.swing.JTable OrdersTable;
    private javax.swing.JTextField PriceMedField;
    private javax.swing.JTextField Pricetxt;
    private javax.swing.JComboBox<String> QuantityCombo;
    private javax.swing.JTextField QuantityMedField;
    private javax.swing.JTextField Searchtxt;
    private javax.swing.JTextField Statustxt;
    private javax.swing.JTextField Totaltxt;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration                   
}
