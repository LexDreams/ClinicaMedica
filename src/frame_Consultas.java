
import Dao.ClinicaDAO;
import Dto.ConsultasDTO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author angel
 */
public class frame_Consultas extends javax.swing.JFrame {
    private Connection conn;
    private ClinicaDAO dao;
    private TableModel consulta;
    List<ConsultasDTO> listaConsulta;
    private String columnas[] = {"Código Consulta","Consulta","Precio"};
    
    public frame_Consultas(Connection conn) {
        initComponents();
        this.conn = conn;
        dao = new ClinicaDAO(conn);
        consulta = new DefaultTableModel(columnas,10);
        obtenerConsultas();
    }
    
    public void obtenerConsultas(){
        try {
            listaConsulta = dao.getConsultas();
            consulta = new DefaultTableModel(columnas,listaConsulta.size());
            tabla_Consulta.setModel(consulta);
            
            for(int i=0; i<listaConsulta.size(); i++){
                tabla_Consulta.setValueAt(listaConsulta.get(i).getIdConsulta(), i, 0);
                tabla_Consulta.setValueAt(listaConsulta.get(i).getConsultas(), i, 1);
                tabla_Consulta.setValueAt(listaConsulta.get(i).getPrecio(), i, 2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error al obtener los registros, contacte al administrador.","Error.",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public int guardarConsulta(){
        int idconsulta = 0;
        ConsultasDTO consulta = new ConsultasDTO();
        listaConsulta = new ArrayList<ConsultasDTO>();
        float precio = Float.parseFloat(tex_Precio.getText());
        
        consulta.setConsultas(tex_Consulta.getText());
        consulta.setPrecio(precio);
        
        try {
            dao.insertarConsulta(consulta);
            JOptionPane.showMessageDialog(this, "Los registros se guardaron correctamente.");
            limpiar();
            obtenerConsultas();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error al guardar los registros verifique la información.","Error.",JOptionPane.ERROR_MESSAGE);
        }
        
        return idconsulta;
    }
    
    public void eliminarConsulta(){
        int fila = tabla_Consulta.getSelectedRow();
        
        if(fila >= 0){
            try {
                dao.eliminarConsulta(tabla_Consulta.getValueAt(fila, 1).toString());
                JOptionPane.showMessageDialog(this,"Los registros fueron eliminados correctamente.");
                obtenerConsultas();
            }catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error al eliminar el registro.", "Error." , JOptionPane.ERROR_MESSAGE);    
            }
        }else{
            JOptionPane.showMessageDialog(this, "La fila que intenta eliminar esta vacía.","Error.",JOptionPane.QUESTION_MESSAGE);
        } 
    }
    
    public void modificar(){
        int fila = tabla_Consulta.getSelectedRow();
        
        if(fila >= 0){
            desactivarBotones();
            label_ID.setText(tabla_Consulta.getValueAt(fila, 0).toString());
            tex_Consulta.setText(tabla_Consulta.getValueAt(fila, 1).toString());
            tex_Precio.setText(tabla_Consulta.getValueAt(fila, 2).toString());
        }else{
            JOptionPane.showMessageDialog(this, "Fila no seleccionada.","Error.",JOptionPane.QUESTION_MESSAGE);
        }
    }
    
    public void actualizarConsulta(){
        try {
            dao.actualizarConsulta(label_ID.getText(),
                    tex_Consulta.getText(),
                    tex_Precio.getText());
            JOptionPane.showMessageDialog(this, "Los registros se actualizaron correctamente.");
            obtenerConsultas();
            limpiar();
            activarBotones();
        } catch (SQLException e) {
             e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar registros.","Error.",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void limpiar(){
        tex_Consulta.setText(null);
        label_ID.setText("0");
        tex_Precio.setText(null);
    }
    
    public void desactivarBotones(){
        boton_Guardar.setEnabled(false);
        boton_Eliminar.setEnabled(false);
    }
    
    public void activarBotones(){
        boton_Guardar.setEnabled(true);
        boton_Eliminar.setEnabled(true);
    }
    
    public boolean comfirmarConsulta(String consulta, String precio){
        boolean respuesta = false;
        String nombres,precios;
        String Query="select consulta,precio from tipo_de_consulta";
        
        try {
            Statement sentencia = conn.createStatement();
            ResultSet rs = sentencia.executeQuery(Query);
            
            while(rs.next()){
                nombres = rs.getString(1);
                precios = rs.getString(2);
                
                if(consulta.equals(nombres) && precio.equals(precios)){
                    respuesta = true;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al recuperar los registros.","Error.",JOptionPane.ERROR_MESSAGE);
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
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tex_Consulta = new javax.swing.JTextField();
        label_ID = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        tex_Precio = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_Consulta = new javax.swing.JTable();
        boton_Guardar = new javax.swing.JButton();
        boton_Eliminar = new javax.swing.JButton();
        boton_Actualizar = new javax.swing.JButton();

        menu_Modificar.setText("Modificar");
        menu_Modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_ModificarActionPerformed(evt);
            }
        });
        menu_Emergente.add(menu_Modificar);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Nueva Consulta");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 600));
        jPanel1.setVerifyInputWhenFocusTarget(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Nueva");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 204, 153));
        jLabel2.setText("Consulta");

        label_Regresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/regresar.png"))); // NOI18N
        label_Regresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        label_Regresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_RegresarMouseClicked(evt);
            }
        });

        jLabel5.setText("Ingresar las consultas (Especialidades).");

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        jLabel6.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel6.setText("Consulta - Especialidad:");

        jLabel7.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel7.setText("Código:");

        tex_Consulta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tex_Consulta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tex_ConsultaKeyTyped(evt);
            }
        });

        label_ID.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label_ID.setText("0");

        jLabel9.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel9.setText("Precio de la consulta:");

        tex_Precio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tex_Precio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tex_PrecioKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(tex_Consulta, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(166, 166, 166)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addComponent(jLabel9)
                    .addComponent(tex_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(343, Short.MAX_VALUE))
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
                    .addComponent(tex_Consulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_ID))
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tex_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Consultas Agregadas"));

        tabla_Consulta.setModel(new javax.swing.table.DefaultTableModel(
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
        tabla_Consulta.setComponentPopupMenu(menu_Emergente);
        jScrollPane1.setViewportView(tabla_Consulta);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(label_Regresar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(boton_Guardar)
                                        .addGap(59, 59, 59)
                                        .addComponent(boton_Eliminar)
                                        .addGap(63, 63, 63)
                                        .addComponent(boton_Actualizar))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel5)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addGap(0, 38, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_Regresar)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(boton_Guardar)
                            .addComponent(boton_Actualizar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(boton_Eliminar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void boton_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_GuardarActionPerformed
        if(tex_Consulta.getText().isEmpty() && tex_Precio.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Los campos estan vacíos.","Error.",JOptionPane.QUESTION_MESSAGE);
        }else if(comfirmarConsulta(tex_Consulta.getText(), tex_Precio.getText())){
            JOptionPane.showMessageDialog(this, "La nueva consulta ya existe en la Base de Datos.","Error.",JOptionPane.ERROR_MESSAGE);
        }else{
            guardarConsulta();
        }
    }//GEN-LAST:event_boton_GuardarActionPerformed

    private void boton_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_EliminarActionPerformed
        int seleccion = JOptionPane.showConfirmDialog(this,"¿Desea elimininar el registro?", "Eliminar.", JOptionPane.OK_OPTION);
        
        if (seleccion ==0){
           eliminarConsulta();
        }
    }//GEN-LAST:event_boton_EliminarActionPerformed

    private void boton_ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_ActualizarActionPerformed
        if(tex_Consulta.getText().isEmpty() && tex_Precio.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Los campos estan vacíos.", "Error.", JOptionPane.QUESTION_MESSAGE);
        }else{
            actualizarConsulta();
        }
    }//GEN-LAST:event_boton_ActualizarActionPerformed

    private void label_RegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_RegresarMouseClicked
        frame_Opciones opciones = new frame_Opciones(conn);
        opciones.setVisible(true);
        dispose();
    }//GEN-LAST:event_label_RegresarMouseClicked

    private void menu_ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_ModificarActionPerformed
        modificar();
    }//GEN-LAST:event_menu_ModificarActionPerformed

    private void tex_ConsultaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tex_ConsultaKeyTyped
        String caracter = String.valueOf(evt.getKeyChar());
        
        if(!(caracter.matches("[a-zA-Z0-9 ]"))){
            evt.consume();
        }
    }//GEN-LAST:event_tex_ConsultaKeyTyped

    private void tex_PrecioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tex_PrecioKeyTyped
        String caracter = String.valueOf(evt.getKeyChar());
        
        if(!(caracter.matches("[0-9]"))){
            evt.consume();
        }
    }//GEN-LAST:event_tex_PrecioKeyTyped

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
            java.util.logging.Logger.getLogger(frame_Consultas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frame_Consultas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frame_Consultas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frame_Consultas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boton_Actualizar;
    private javax.swing.JButton boton_Eliminar;
    private javax.swing.JButton boton_Guardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JTable tabla_Consulta;
    private javax.swing.JTextField tex_Consulta;
    private javax.swing.JTextField tex_Precio;
    // End of variables declaration//GEN-END:variables
}
