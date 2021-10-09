
import Dao.ClinicaDAO;
import Dto.inventarioDTO;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author angel
 */
public class frame_Inventario extends javax.swing.JFrame {
    private Connection conn;
    private ClinicaDAO dao;
    private final String ruta = System.getProperties().getProperty("user.dir");
    private TableModel inventarios;
    List<inventarioDTO> listaInventario;
    private String columnas[] ={"Código","Producto","Descripción","Cantidad"};
    
    public frame_Inventario(Connection conn) {
        initComponents();
        this.conn = conn;
        dao = new ClinicaDAO(conn);
        inventarios = new DefaultTableModel(columnas,10);
        obtenerInventario();
    }
    
    public void obtenerInventario(){
        try {
            listaInventario = dao.getInventario();
            inventarios =  new DefaultTableModel(columnas,listaInventario.size());
            tabla_Inventario.setModel(inventarios);
            
            for(int i=0; i<listaInventario.size(); i++){
                tabla_Inventario.setValueAt(listaInventario.get(i).getIdProducto(), i, 0);
                tabla_Inventario.setValueAt(listaInventario.get(i).getNombre(), i, 1);
                tabla_Inventario.setValueAt(listaInventario.get(i).getDescripcion(), i, 2);
                tabla_Inventario.setValueAt(listaInventario.get(i).getCantidad(), i, 3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error al recuperar los registros, contacte al administrador.","Error.",JOptionPane.ERROR_MESSAGE);
        }
    }
    
        public int guardarInventario(){
            int idInventario = 0;
            inventarioDTO inventario = new inventarioDTO();
            listaInventario = new ArrayList<inventarioDTO>();
            int cantidad = Integer.parseInt(tex_Cantidad.getText());
            
            inventario.setNombre(tex_Nombre.getText());
            inventario.setDescripcion(tex_Descripcion.getText());
            inventario.setCantidad(cantidad);
            
            try {
                dao.insertarInventario(inventario);
                JOptionPane.showMessageDialog(this, "Los datos han sido guardados correctamente.");
                limpiar();
                obtenerInventario();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Ha ocurrido un error verifique los datos.","Error.",JOptionPane.ERROR_MESSAGE);
            }
            
            return idInventario;
        }
        
        public void eliminarInventario(){
            int fila = tabla_Inventario.getSelectedRow();
            
            if(fila >=0){
                try {
                    dao.eliminarInventario(tabla_Inventario.getValueAt(fila, 0).toString());
                    JOptionPane.showMessageDialog(this,"Los registros fueron eliminados correctamente.");
                    obtenerInventario();
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this,"Error al eliminar el registro.", "Error." , JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(this, "La fila que intenta eliminar esta vacía.","Error.",JOptionPane.QUESTION_MESSAGE);
            }
        }
        
        public void modificar(){
            int fila = tabla_Inventario.getSelectedRow();
            
            if(fila >= 0){
                desactivarBotones();
                label_ID.setText(tabla_Inventario.getValueAt(fila, 0).toString());
                tex_Nombre.setText(tabla_Inventario.getValueAt(fila, 1).toString());
                tex_Descripcion.setText(tabla_Inventario.getValueAt(fila, 2).toString());
                tex_Cantidad.setText(tabla_Inventario.getValueAt(fila, 3).toString());
            }else{
                JOptionPane.showMessageDialog(this, "Fila no seleccionada.","Error.",JOptionPane.QUESTION_MESSAGE);
            }
        }
        
        public void actualizarInventario(){
            try {
                dao.actualizarInventario(label_ID.getText(), 
                        tex_Nombre.getText(),
                        tex_Descripcion.getText(),
                        tex_Cantidad.getText());
            JOptionPane.showMessageDialog(this, "Los registros se actualizaron correctamente.");
            obtenerInventario();
            limpiar();
            activarBotones();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al actualizar registros.","Error.",JOptionPane.ERROR_MESSAGE);
            }
        }
        
        public void limpiar(){
            label_ID.setText("0");
            tex_Nombre.setText(null);
            tex_Descripcion.setText(null);
            tex_Cantidad.setText(null);
        }
        
    public void desactivarBotones(){
        boton_Guardar.setEnabled(false);
        boton_Eliminar.setEnabled(false);
        boton_ReporteInv.setEnabled(false);
    }
    
    public void activarBotones(){
        boton_Guardar.setEnabled(true);
        boton_Eliminar.setEnabled(true);
        boton_ReporteInv.setEnabled(true);
    }
    
    public boolean ComfirmarInventario(String nombre, String descripcion){
        boolean respuesta = false;
        String nomHerra = null;
        String desHerra = null;
        String Query = "select nombre,descripcion from inventario_clinica";
        
        try {
            Statement sentencia = conn.createStatement();
            ResultSet rs = sentencia.executeQuery(Query);
            
            while(rs.next()){
                nomHerra = rs.getString(1);
                desHerra = rs.getString(2);
                
                if(nombre.equals(nomHerra) && descripcion.equals(desHerra)){
                    respuesta = true;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al recuperar el registro.","Error.",JOptionPane.ERROR_MESSAGE);
        }
        return respuesta;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menu_Emergente = new javax.swing.JPopupMenu();
        menu_Modificar = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        label_Regresar = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        tex_Nombre = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        label_ID = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tex_Cantidad = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tex_Descripcion = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_Inventario = new javax.swing.JTable();
        boton_Guardar = new javax.swing.JButton();
        boton_Eliminar = new javax.swing.JButton();
        boton_Actualizar = new javax.swing.JButton();
        boton_ReporteInv = new javax.swing.JButton();
        progress_Proceso = new javax.swing.JProgressBar();

        menu_Modificar.setBackground(new java.awt.Color(204, 204, 255));
        menu_Modificar.setText("Modificar");
        menu_Modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_ModificarActionPerformed(evt);
            }
        });
        menu_Emergente.add(menu_Modificar);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Nuevo");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 204, 153));
        jLabel2.setText("Inventario");

        label_Regresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/regresar.png"))); // NOI18N
        label_Regresar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        label_Regresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        label_Regresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_RegresarMouseClicked(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Cantidad:");

        tex_Nombre.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        tex_Nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tex_NombreKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Código:");

        label_ID.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        label_ID.setText("0");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Producto:");

        tex_Cantidad.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        tex_Cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tex_CantidadKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Descripción:");

        tex_Descripcion.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        tex_Descripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tex_DescripcionKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(330, 330, 330)
                                .addComponent(jLabel6))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(tex_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                                .addComponent(label_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(358, 358, 358))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addGap(432, 432, 432))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(tex_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tex_Descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(199, 199, 199))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tex_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_ID))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tex_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tex_Descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jLabel4.setText(" Ingrese las herramientas de la clínica.");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Inventario"));

        tabla_Inventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabla_Inventario.setComponentPopupMenu(menu_Emergente);
        jScrollPane1.setViewportView(tabla_Inventario);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                .addContainerGap())
        );

        boton_Guardar.setBackground(new java.awt.Color(204, 204, 255));
        boton_Guardar.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        boton_Guardar.setText("Guardar");
        boton_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_GuardarActionPerformed(evt);
            }
        });

        boton_Eliminar.setBackground(new java.awt.Color(204, 204, 255));
        boton_Eliminar.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        boton_Eliminar.setText("Eliminar");
        boton_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_EliminarActionPerformed(evt);
            }
        });

        boton_Actualizar.setBackground(new java.awt.Color(204, 204, 255));
        boton_Actualizar.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        boton_Actualizar.setText("Actualizar");
        boton_Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_ActualizarActionPerformed(evt);
            }
        });

        boton_ReporteInv.setBackground(new java.awt.Color(204, 204, 255));
        boton_ReporteInv.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        boton_ReporteInv.setText("Reporte Inventario");
        boton_ReporteInv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_ReporteInvActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(progress_Proceso, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label_Regresar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(boton_Guardar)
                                .addGap(51, 51, 51)
                                .addComponent(boton_Eliminar)
                                .addGap(66, 66, 66)
                                .addComponent(boton_Actualizar)
                                .addGap(53, 53, 53)
                                .addComponent(boton_ReporteInv))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel4)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2))
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 980, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 980, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(1, 1, 1)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(label_Regresar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(boton_Eliminar)
                                .addComponent(boton_Guardar))
                            .addComponent(boton_ReporteInv))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(boton_Actualizar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(progress_Proceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void label_RegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_RegresarMouseClicked
        frame_Opciones opciones = new frame_Opciones(conn);
        opciones.setVisible(true);
        dispose();
    }//GEN-LAST:event_label_RegresarMouseClicked

    private void boton_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_GuardarActionPerformed
        if(tex_Nombre.getText().isEmpty() && tex_Cantidad.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Los campos estan vacíos.", "Error.", JOptionPane.QUESTION_MESSAGE);
        }else if(ComfirmarInventario(tex_Nombre.getText(), tex_Descripcion.getText())){
            JOptionPane.showMessageDialog(this, "El nuevo inventario ya existe en la Base de Datos.","Error.",JOptionPane.ERROR_MESSAGE);
        }else{
            guardarInventario();
        }
    }//GEN-LAST:event_boton_GuardarActionPerformed

    private void boton_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_EliminarActionPerformed
        int seleccion = JOptionPane.showConfirmDialog(this,"¿Desea elimininar el registro?", "Eliminar.", JOptionPane.OK_OPTION);
        
        if (seleccion ==0){
           eliminarInventario();
        }
    }//GEN-LAST:event_boton_EliminarActionPerformed

    private void menu_ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_ModificarActionPerformed
        modificar();
    }//GEN-LAST:event_menu_ModificarActionPerformed

    private void boton_ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_ActualizarActionPerformed
        if(tex_Nombre.getText().isEmpty() && tex_Cantidad.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Los campos estan vacíos.", "Error.", JOptionPane.QUESTION_MESSAGE);
        }else{
            actualizarInventario();
        }
    }//GEN-LAST:event_boton_ActualizarActionPerformed

    private void boton_ReporteInvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_ReporteInvActionPerformed
        try {
            Thread hilo = new Thread() {
                public void run() {   
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    XSSFSheet hoja = workbook.createSheet();

                    XSSFRow fila = hoja.createRow(0);

                    fila.createCell(0).setCellValue("Código Inventario");
                    fila.createCell(1).setCellValue("Nombre");
                    fila.createCell(2).setCellValue("Descripción");
                    fila.createCell(3).setCellValue("cantidad");

                    progress_Proceso.setMaximum(tabla_Inventario.getRowCount());
                    XSSFRow filas;
                    Rectangle rect;

                    for (int i = 0; i <  tabla_Inventario.getRowCount(); i++) {
                        rect =  tabla_Inventario.getCellRect(i, 0, true);
                        try {
                             tabla_Inventario.scrollRectToVisible(rect);

                        } catch (java.lang.ClassCastException e) {
                            //e.printStackTrace();
                        }    
                         tabla_Inventario.setRowSelectionInterval(i, i);
                        progress_Proceso.setValue((i + 1));

                        filas = hoja.createRow((i + 1));
                        for (int j = 0; j < 10; j++) {
                            try {
                                filas.createCell(j).setCellValue( tabla_Inventario.getValueAt(i, j).toString());
                            } catch (Exception ex) {
                                //ex.printStackTrace();
                            }
                        }
                    }
                    progress_Proceso.setValue(0);
                    progress_Proceso.setString("Abriendo Excel...");

                    try {
                        workbook.write(new FileOutputStream(new File(ruta + "//reporteInventario" +  ".xlsx")));
                        Desktop.getDesktop().open(new File(ruta + "//reporteInventario" +".xlsx"));
                    } catch (IOException ex) {
                    }
                }
            };
            hilo.start();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }//GEN-LAST:event_boton_ReporteInvActionPerformed

    private void tex_CantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tex_CantidadKeyTyped
        String caracter = String.valueOf(evt.getKeyChar());
        
        if(!(caracter.matches("[0-9]"))){
            evt.consume();
        }
    }//GEN-LAST:event_tex_CantidadKeyTyped

    private void tex_DescripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tex_DescripcionKeyTyped
        String caracter = String.valueOf(evt.getKeyChar());
        
        if(!(caracter.matches("[a-zA-Z0-9. ]"))){
            evt.consume();
        }
    }//GEN-LAST:event_tex_DescripcionKeyTyped

    private void tex_NombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tex_NombreKeyTyped
        String caracter = String.valueOf(evt.getKeyChar());
        
        if(!(caracter.matches("[a-zA-Z ]"))){
            evt.consume();
        }
    }//GEN-LAST:event_tex_NombreKeyTyped
    
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frame_Inventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frame_Inventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frame_Inventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frame_Inventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boton_Actualizar;
    private javax.swing.JButton boton_Eliminar;
    private javax.swing.JButton boton_Guardar;
    private javax.swing.JButton boton_ReporteInv;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel label_ID;
    private javax.swing.JLabel label_Regresar;
    private javax.swing.JPopupMenu menu_Emergente;
    private javax.swing.JMenuItem menu_Modificar;
    private javax.swing.JProgressBar progress_Proceso;
    private javax.swing.JTable tabla_Inventario;
    private javax.swing.JTextField tex_Cantidad;
    private javax.swing.JTextField tex_Descripcion;
    private javax.swing.JTextField tex_Nombre;
    // End of variables declaration//GEN-END:variables
}
