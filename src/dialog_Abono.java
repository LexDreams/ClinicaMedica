
import Dao.ClinicaDAO;
import Dto.PagosDTO;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class dialog_Abono extends javax.swing.JDialog {
    private Connection conn;
    private String idAbono;
    private String idCita;
    private String nombre;
    private String precio;
    private String Cantidad;
    private String dia,mes,año;
    private ClinicaDAO dao;
    
    public dialog_Abono(java.awt.Frame parent, boolean modal,Connection conn, String idAbono, String idCita,
            String nombre, String precio, String cantidad) {
        super(parent, modal);
        initComponents();
        this.conn = conn;
        this.idAbono = idAbono;
        this.idCita = idCita;
        this.nombre = nombre;
        this.precio = precio;
        this.Cantidad = cantidad;
        dao = new ClinicaDAO(conn);
        
        mostrarDatos();
        fecha();
    }
    
    public String fecha(){
        Date fecha = new Date();
        SimpleDateFormat formato1 = new SimpleDateFormat("dd");
        SimpleDateFormat formato2 = new SimpleDateFormat("MM");
        SimpleDateFormat formato3 = new SimpleDateFormat("yyyy");
        dia = formato1.format(fecha);
        mes = formato2.format(fecha);
        año = formato3.format(fecha);
        String fechaActual = dia+"/"+mes+"/"+año;
        
        return fechaActual;
    }
    
    public void mostrarDatos(){
        label_Nombre.setText(nombre);
        label_Monto.setText(precio);
        label_Abono.setText(Cantidad);
    }
    
    private int guardarPago(){
        int idPago = 0;
        PagosDTO pagos = new PagosDTO();
        int codCita = Integer.parseInt(idCita);
        float monto = Float.parseFloat(precio);
        String tipoPago ="Abono";
        
        pagos.setIdcita(codCita);
        pagos.setDia(dia);
        pagos.setMes(mes);
        pagos.setAño(año);
        pagos.setCantidad(monto);
        pagos.setTipoPago(tipoPago);
        
        try {
            dao.insertarPagos(pagos);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error al guardar los registros verifique la información.","Error.",JOptionPane.ERROR_MESSAGE);
        }
        return idPago;
    }
    
    public void eliminarAbono(){
        try {
            dao.eliminarAbono(idAbono);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar el registro.","Error.",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void GuardarAbono(){
        float cantidad = Float.parseFloat(Cantidad);
        float nuevoAbono = Float.parseFloat(tex_Abono.getText());
        float resultado = cantidad + nuevoAbono;
        String AbonoFinal = String.valueOf(resultado);
        float precioFloat = Float.parseFloat(precio);
        
        if(resultado == precioFloat){
            JOptionPane.showMessageDialog(this,"El pago de la consulta ha sido saldado.");
            guardarPago();
            eliminarAbono();
            dispose();
        }else if(resultado > precioFloat  || resultado < 0){
            JOptionPane.showMessageDialog(this, "La cantidad ingresada sobrepasa la deuda o es menor a cero.","Error.",JOptionPane.ERROR_MESSAGE);
        }else{
            try {
                dao.ActualizarAbono(idAbono, fecha(), AbonoFinal, idCita);
                JOptionPane.showMessageDialog(this, "Se ingreso el abono correctamente.");
                dispose();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al actualizar el registro, contacte al administrador.","Error,",JOptionPane.ERROR_MESSAGE);
            }
        }

    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        label_Nombre = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        label_Monto = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        label_Abono = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        tex_Abono = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        Boton_Guardar = new javax.swing.JButton();
        boton_Cancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Nuevo");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 51, 204));
        jLabel2.setText("Abono");

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        jLabel3.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel3.setText("Nombre:");

        label_Nombre.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label_Nombre.setText("Nombre");

        jLabel5.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel5.setText("Monto total:");

        label_Monto.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label_Monto.setText("0");

        jLabel7.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel7.setText("Abono:");

        label_Abono.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label_Abono.setText("0");

        jLabel9.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel9.setText("Nuevo Abono:");

        tex_Abono.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(56, 56, 56)
                        .addComponent(label_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_Abono, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_Monto, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(tex_Abono, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(label_Nombre))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(label_Monto))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(label_Abono))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(tex_Abono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        Boton_Guardar.setBackground(new java.awt.Color(204, 204, 255));
        Boton_Guardar.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        Boton_Guardar.setText("Guardar");
        Boton_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Boton_GuardarActionPerformed(evt);
            }
        });

        boton_Cancelar.setBackground(new java.awt.Color(204, 204, 255));
        boton_Cancelar.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        boton_Cancelar.setText("cancelar");
        boton_Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_CancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(Boton_Guardar)
                .addGap(54, 54, 54)
                .addComponent(boton_Cancelar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(0, 175, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING))
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
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Boton_Guardar)
                    .addComponent(boton_Cancelar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Boton_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Boton_GuardarActionPerformed
        int seleccion = JOptionPane.showConfirmDialog(this, "¿Desea guardar el Abono?","Guardar.",JOptionPane.OK_OPTION);
        
        if (tex_Abono.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "El Campo 'Abono' esta vacío.");
        }else{
            if(seleccion == 0){
                GuardarAbono();
            }
        }
    }//GEN-LAST:event_Boton_GuardarActionPerformed

    private void boton_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_CancelarActionPerformed
        dispose();
    }//GEN-LAST:event_boton_CancelarActionPerformed

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
            java.util.logging.Logger.getLogger(dialog_Abono.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dialog_Abono.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dialog_Abono.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dialog_Abono.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Boton_Guardar;
    private javax.swing.JButton boton_Cancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel label_Abono;
    private javax.swing.JLabel label_Monto;
    private javax.swing.JLabel label_Nombre;
    private javax.swing.JTextField tex_Abono;
    // End of variables declaration//GEN-END:variables
}
