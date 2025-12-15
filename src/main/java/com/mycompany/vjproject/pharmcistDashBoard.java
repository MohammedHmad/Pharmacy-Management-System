/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package com.mycompany.vjproject;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import net.proteanit.sql.DbUtils;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;/**
 *
 * @author mhm55
 */
public class pharmcistDashBoard extends javax.swing.JFrame {
    private String medicinName ;
    private String medicinquantity;
    private String medicinprice ;
    String db_query = "";
    private ResultSet rs ;
    Connection conn = null;
    boolean MedicinSelceted = false;
    PreparedStatement pst = null;
    
    //order proprties
    private String orderID;
    private String date;
    private String totalAmount;
    private String status;
    private String orderuserID;
    
    private void check_theStock(){
        String name;
        int quantity;
        try{
            conn = Connection2DB.ConneectorDB();
            db_query = "select * from `pharmacyDB`.`Medicines`;";
            pst = conn.prepareStatement(db_query);
            rs = pst.executeQuery();
            while (rs.next()){
                quantity = rs.getInt("quantity");
                if(quantity<15){
                    name =  rs.getString("MedicineName");
                    JOptionPane.showMessageDialog(
                        null,
                            "there is a shorteg in stock of "+name,
                        "Low-Stock Alert",
                            2
                    );
                }
                
                
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
    
    private void Update_table(){
        try{
            conn = Connection2DB.ConneectorDB();
            db_query = "SELECT MedicineName, MedicineID, quantity, price, Details FROM `pharmacyDB`.`Medicines`";
            pst = conn.prepareStatement(db_query);
            rs = pst.executeQuery();
            
            
            
            MedicinsTable.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e);
        }
        finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                System.getLogger(customerDashBoard.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        
    }
    
     private void Update_OrdersTable() throws SQLException{
        try{
            conn = Connection2DB.ConneectorDB();
            db_query = "select * from `Orders` order by `Orders`.`status` DESC;";
            pst = conn.prepareStatement(db_query);
            rs = pst.executeQuery();
            
            OrdersTable.setRowSorter(null); 
            
            OrdersTable.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e);
        }
        finally{
            conn.close();
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

    
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(pharmcistDashBoard.class.getName());

    /** Creates new form pharmcistDashBoard
     * @throws java.sql.SQLException */
    public pharmcistDashBoard() throws SQLException {
        initComponents();
        Update_table();
        Update_OrdersTable();
        check_theStock();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane1 = new java.awt.ScrollPane();
        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        NewMedbtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        MedicinsTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Searchtxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        NameMedField = new javax.swing.JTextField();
        IdMedField = new javax.swing.JTextField();
        QuantityMedField = new javax.swing.JTextField();
        PriceMedField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        Editbtn = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        MedDetailsArea = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        Pricetxt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        Datetxt = new javax.swing.JTextField();
        MedicID = new javax.swing.JLabel();
        OrderIDtxt = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        UserIDtxt = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        OrdersTable = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        OrderSearchtxt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        OrderDetailsTable = new javax.swing.JTable();
        ODMedicSearchtxt = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        Statustxt = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        NewMedbtn.setText("new medicine");
        NewMedbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewMedbtnActionPerformed(evt);
            }
        });

        MedicinsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "ID", "Quantity", "Price"
            }
        ));
        MedicinsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        MedicinsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MedicinsTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(MedicinsTable);

        jLabel1.setFont(new java.awt.Font("Liberation Serif", 3, 24)); // NOI18N
        jLabel1.setText("Medicins");

        jLabel2.setText("search");

        Searchtxt.setColumns(17);
        Searchtxt.setText("Medicin name");
        Searchtxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SearchtxtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                SearchtxtFocusLost(evt);
            }
        });
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

        jLabel3.setText("Name");

        jLabel4.setFont(new java.awt.Font("Liberation Serif", 3, 24)); // NOI18N
        jLabel4.setText("Info");

        IdMedField.setEditable(false);
        IdMedField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdMedFieldActionPerformed(evt);
            }
        });

        QuantityMedField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuantityMedFieldActionPerformed(evt);
            }
        });

        jLabel5.setText("ID");

        jLabel6.setText("Quantity");

        jLabel7.setText("Price");

        Editbtn.setText("Edit");
        Editbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditbtnActionPerformed(evt);
            }
        });

        jLabel15.setText("Details");

        MedDetailsArea.setColumns(20);
        MedDetailsArea.setRows(5);
        jScrollPane5.setViewportView(MedDetailsArea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(153, 153, 153))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addComponent(jLabel15))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(NameMedField, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(IdMedField, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(QuantityMedField, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(PriceMedField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addGap(68, 68, 68)
                                    .addComponent(Editbtn))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(46, Short.MAX_VALUE)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Searchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 636, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NewMedbtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(Searchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(NewMedbtn)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(NameMedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(IdMedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(QuantityMedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(PriceMedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(Editbtn))))
        );

        jTabbedPane1.addTab("Inventory", jPanel1);

        jLabel8.setFont(new java.awt.Font("Liberation Serif", 3, 24)); // NOI18N
        jLabel8.setText("Info");

        Pricetxt.setEditable(false);

        jLabel9.setText("Price");

        jLabel10.setText("Date");

        Datetxt.setEditable(false);
        Datetxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DatetxtActionPerformed(evt);
            }
        });

        MedicID.setText("Order ID");

        OrderIDtxt.setEditable(false);
        OrderIDtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrderIDtxtActionPerformed(evt);
            }
        });

        jLabel12.setText("User ID");

        UserIDtxt.setEditable(false);

        OrdersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "OrderNO.", "UserID", "Date", "Price", "status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
        jScrollPane3.setViewportView(OrdersTable);

        jLabel13.setFont(new java.awt.Font("Liberation Serif", 3, 24)); // NOI18N
        jLabel13.setText("Orders");

        jLabel14.setText("search");

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

        jButton1.setText("Accept");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Liberation Serif", 3, 24)); // NOI18N
        jLabel16.setText("Order details");

        OrderDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MedicinName", "MedicinID.", "Quantity", "Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

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
        jScrollPane4.setViewportView(OrderDetailsTable);

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

        jLabel17.setText("search");

        jLabel18.setText("Status");

        Statustxt.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addGap(119, 119, 119))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(MedicID)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Statustxt, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(OrderIDtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(UserIDtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Pricetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Datetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(106, 106, 106)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ODMedicSearchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(OrderSearchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(UserIDtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(OrderIDtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(MedicID))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Datetxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Pricetxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Statustxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(98, 98, 98))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel16)
                                    .addGap(417, 417, 417))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel17)
                                        .addComponent(ODMedicSearchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel14)
                                            .addComponent(OrderSearchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );

        jTabbedPane1.addTab("Orders", jPanel2);

        jMenu1.setText("settings");
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
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1001, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void NewMedbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewMedbtnActionPerformed
        AddMedicinefrm frm = new AddMedicinefrm();
        frm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_NewMedbtnActionPerformed

    private void SearchtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchtxtActionPerformed
      
    }//GEN-LAST:event_SearchtxtActionPerformed

    private void IdMedFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdMedFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdMedFieldActionPerformed

    private void QuantityMedFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuantityMedFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_QuantityMedFieldActionPerformed

    private void MedicinsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MedicinsTableMouseClicked
        
        try{
            conn = Connection2DB.ConneectorDB();
            int row = MedicinsTable.getSelectedRow();
            String table_click=(MedicinsTable.getModel().getValueAt(row, 1).toString());
            db_query = "select * from `Medicines` where `MedicineID`='"+table_click+"'";
            pst = conn.prepareStatement(db_query);
            rs = pst.executeQuery();
            if(rs.next()){
                String attrebut1 = rs.getString("MedicineID");
                String attrebut2 = rs.getString("price");
                String attrebut3 = rs.getString("quantity");
                String attrebut4 = rs.getString("MedicineName");
                String attrebut5 = rs.getString("Details");
                IdMedField.setText(attrebut1);
                PriceMedField.setText(attrebut2);
                QuantityMedField.setText(attrebut3);
                NameMedField.setText(attrebut4);
                MedDetailsArea.setText(attrebut5);
                
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
        
        
    }//GEN-LAST:event_MedicinsTableMouseClicked

    private void DatetxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DatetxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DatetxtActionPerformed

    private void OrderIDtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OrderIDtxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OrderIDtxtActionPerformed

    private void OrdersTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OrdersTableMouseClicked
                try{
            conn = Connection2DB.ConneectorDB();
            int row = OrdersTable.getSelectedRow();
            String table_click=(OrdersTable.getModel().getValueAt(row, 0).toString());
            db_query = "select * from `Orders` where `orderID`='"+table_click+"'";
            pst = conn.prepareStatement(db_query);
            rs = pst.executeQuery();
            if(rs.next()){
                String attrebut1 = rs.getString("orderID");
                String attrebut2 = rs.getString("orderDate");
                String attrebut3 = rs.getString("totalAmount");
                String attrebut4 = rs.getString("status");
                String attrebut5 = rs.getString("userID");
                       
                orderID = attrebut1;
                date = attrebut2;
                totalAmount = attrebut3;
                status = attrebut4;
                orderuserID = attrebut5;
                
                UserIDtxt.setText(orderuserID);
                OrderIDtxt.setText(orderID);
                Datetxt.setText(date);
                Pricetxt.setText(totalAmount);
                Statustxt.setText(status);
                
                Update_OrdersDetailsTable(orderID);
                
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
    }//GEN-LAST:event_OrdersTableMouseClicked

    private void OrderSearchtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OrderSearchtxtActionPerformed
        
    }//GEN-LAST:event_OrderSearchtxtActionPerformed

    private void OrderDetailsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OrderDetailsTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_OrderDetailsTableMouseClicked

    private void ODMedicSearchtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ODMedicSearchtxtActionPerformed
        // 1. Get the model
        javax.swing.table.TableModel model = OrderDetailsTable.getModel();

        // 2. Create the sorter
        TableRowSorter<javax.swing.table.TableModel> sorter = new TableRowSorter<>(model);

        // 3. Set the sorter
        OrderDetailsTable.setRowSorter(sorter);

        // 4. Get text
        String searchString = ODMedicSearchtxt.getText();

        // 5. Apply Filter
        if (searchString.trim().length() == 0 || searchString.equals("Medicin name")) {
            sorter.setRowFilter(null);
        } else {
            // (?i) for case insensitivity
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchString));
        }
    }//GEN-LAST:event_ODMedicSearchtxtActionPerformed

    private void SearchtxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchtxtFocusGained
        if(Searchtxt.getText().equals("Medicin name"))
            Searchtxt.setText("");// TODO add your handling code here:
    }//GEN-LAST:event_SearchtxtFocusGained

    private void SearchtxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchtxtFocusLost
        if(Searchtxt.getText().equals(""))
            Searchtxt.setText("Medicin name");        // TODO add your handling code here:
    }//GEN-LAST:event_SearchtxtFocusLost

    private void ODMedicSearchtxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ODMedicSearchtxtFocusGained
        if(ODMedicSearchtxt.getText().equals("Medicin name"))
            ODMedicSearchtxt.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_ODMedicSearchtxtFocusGained

    private void ODMedicSearchtxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ODMedicSearchtxtFocusLost
        if(ODMedicSearchtxt.getText().equals(""))
            ODMedicSearchtxt.setText("Medicin name");        // TODO add your handling code here:
    }//GEN-LAST:event_ODMedicSearchtxtFocusLost

    private void OrderSearchtxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OrderSearchtxtFocusGained
        if(OrderSearchtxt.getText().equals("Order number"))
            OrderSearchtxt.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_OrderSearchtxtFocusGained

    private void OrderSearchtxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_OrderSearchtxtFocusLost
        if(OrderSearchtxt.getText().equals(""))
            OrderSearchtxt.setText("Order number");    // TODO add your handling code here:
    }//GEN-LAST:event_OrderSearchtxtFocusLost

    private void EditbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditbtnActionPerformed
        String id = IdMedField.getText();
        String price = PriceMedField.getText();
        String quant = QuantityMedField.getText();
        String name = NameMedField.getText();
        String details = MedDetailsArea.getText();
        if(!IdMedField.getText().equals("")){
            try{

                conn = Connection2DB.ConneectorDB();
                db_query = "UPDATE `Medicines` SET `MedicineName` = ?, `quantity` = ?, `price` = ?, `Details` = ? WHERE `MedicineID` = ?;";
                pst = conn.prepareStatement(db_query);
                pst.setString(1, name);
                pst.setString(2, quant);
                pst.setString(3, price);
                pst.setString(4, details);
                pst.setString(5, id);
                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Record successfully updated!");
                    Update_table();
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed. User ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null,"sorry item didn't Update!");
                JOptionPane.showMessageDialog(null,e);
            }
            finally{
                try {
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,ex);
                }
            }
        }else{
                JOptionPane.showMessageDialog(null,"you didn't selsct any column from Medic table");

        }
    }//GEN-LAST:event_EditbtnActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed

    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try{
            conn = Connection2DB.ConneectorDB();
            db_query = "UPDATE `pharmacyDB`.`Orders`\n" +
            "SET\n" +
            "\n" +
            "`status` = \"Accepted\"\n" +
            "WHERE `orderID` = ?;";
            pst = conn.prepareStatement(db_query);
            pst.setString(1, orderID);
            int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Record successfully updated!");
                    Update_OrdersTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed. User ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
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
    }//GEN-LAST:event_jButton1ActionPerformed

    private void SearchtxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SearchtxtKeyReleased
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
    }//GEN-LAST:event_SearchtxtKeyReleased

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        pharmacyLogin frm = new pharmacyLogin();
        frm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

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
            try {
                new pharmcistDashBoard().setVisible(true);
            } catch (SQLException ex) {
                System.getLogger(pharmcistDashBoard.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Datetxt;
    private javax.swing.JButton Editbtn;
    private javax.swing.JTextField IdMedField;
    private javax.swing.JTextArea MedDetailsArea;
    private javax.swing.JLabel MedicID;
    private javax.swing.JTable MedicinsTable;
    private javax.swing.JTextField NameMedField;
    private javax.swing.JButton NewMedbtn;
    private javax.swing.JTextField ODMedicSearchtxt;
    private javax.swing.JTable OrderDetailsTable;
    private javax.swing.JTextField OrderIDtxt;
    private javax.swing.JTextField OrderSearchtxt;
    private javax.swing.JTable OrdersTable;
    private javax.swing.JTextField PriceMedField;
    private javax.swing.JTextField Pricetxt;
    private javax.swing.JTextField QuantityMedField;
    private javax.swing.JTextField Searchtxt;
    private javax.swing.JTextField Statustxt;
    private javax.swing.JTextField UserIDtxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private java.awt.ScrollPane scrollPane1;
    // End of variables declaration//GEN-END:variables

}
