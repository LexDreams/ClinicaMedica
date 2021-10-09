
import Dao.ClinicaDAO;
import Dto.PagosDTO;
import Dto.abonoDTO;
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

/**
 *
 * 
 */
public class dialog_Pago extends javax.swing.JDialog {
    private String idCita;
    private String pago;
    private String dia,mes,año;
    private Connection conn;
    private ClinicaDAO dao;
    
    public dialog_Pago(java.awt.Frame parent, boolean modal,Connection conn, String idCita, String pago) {
        super(parent, modal);
        initComponents();
        this.conn = conn;
        this.idCita = idCita;
        this.pago = pago;
        dao = new ClinicaDAO(conn);
        obtenerMonto();
        fecha();
    }
    
    public void obtenerMonto(){
        label_Monto.setText(pago);
    }
    
    public void actualizarCita(){
        String atendido = "Si";
        try {
            dao.actualizarCita(idCita,atendido);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el registro.","Error.",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public String fecha(){
        Date fecha = new Date();
        SimpleDateFormat formato1 = new SimpleDateFormat("dd");
        SimpleDateFormat formato2 = new SimpleDateFormat("MM");
        SimpleDateFormat formato3 = new SimpleDateFormat("yyyy");
        dia= formato1.format(fecha);
        mes = formato2.format(fecha);
        año = formato3.format(fecha);
        
        String fechaActual = dia+"/"+mes+"/"+año;
        
        return fechaActual;
    }
    
    private int guardarPago(){
        int idPago = 0;
        PagosDTO pagos = new PagosDTO();
        int codCita = Integer.parseInt(idCita);
        float pagoFloat = Float.parseFloat(pago);
        float monto = Float.parseFloat(tex_Pago.getText());
        
        if(monto < pagoFloat || monto > pagoFloat){
            JOptionPane.showMessageDialog(this, "No se puede guardar como un pago al contado.", "Error.",JOptionPane.ERROR_MESSAGE);
        }else{
            pagos.setIdcita(codCita);
            pagos.setDia(dia);
            pagos.setMes(mes);
            pagos.setAño(año);
            pagos.setCantidad(monto);
            pagos.setTipoPago(combo_TipoPago.getSelectedItem().toString());
        
            try {
                dao.insertarPagos(pagos);
                JOptionPane.showMessageDialog(this, "Los registros se guardaron correctamente.");
                actualizarCita();
                dispose();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error al guardar los registros verifique la información.","Error.",JOptionPane.ERROR_MESSAGE);
            }
        }
        return idPago;
    }
    
    private int guardarAbonos(){
        int idAbonos = 0;
        abonoDTO abonos = new abonoDTO();
        float monto = Float.parseFloat(tex_Pago.getText());
        float pagoFloat = Float.parseFloat(pago);
        int codCita = Integer.parseInt(idCita);
        
        if(monto >= pagoFloat){
            JOptionPane.showMessageDialog(this, "No se puede guardar como un pago en abonos.","Error.",JOptionPane.ERROR_MESSAGE);
        }else{
            abonos.setFecha(fecha());
            abonos.setCantidad(monto);
            abonos.setIdCita(codCita);
            try {
                dao.insertarAbono(abonos);
                JOptionPane.showMessageDialog(this, "Los registros se guardaron correctamente.");
                actualizarCita();
                dispose();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error al guardar los registros verifique la información.","Error.",JOptionPane.ERROR_MESSAGE);
            }
        }
        return idAbonos;
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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        label_Monto = new javax.swing.JLabel();
        tex_Pago = new javax.swing.JTextField();
        combo_TipoPago = new javax.swing.JComboBox();
        boton_Guardar = new javax.swing.JButton();
        boton_Cancerlar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Nuevo");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 51, 204));
        jLabel2.setText("Pago");

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        jLabel3.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel3.setText("Monto Total:");

        jLabel4.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel4.setText("Pago:");

        jLabel5.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel5.setText("Tipo de Pago:");

        label_Monto.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label_Monto.setText("0");

        tex_Pago.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N

        combo_TipoPago.setBackground(new java.awt.Color(204, 204, 255));
        combo_TipoPago.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        combo_TipoPago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Contado", "Abono" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(47, 47, 47)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(label_Monto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tex_Pago)
                    .addComponent(combo_TipoPago, 0, 148, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(label_Monto))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tex_Pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(combo_TipoPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        boton_Guardar.setBackground(new java.awt.Color(204, 204, 255));
        boton_Guardar.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        boton_Guardar.setText("Guardar");
        boton_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_GuardarActionPerformed(evt);
            }
        });

        boton_Cancerlar.setBackground(new java.awt.Color(204, 204, 255));
        boton_Cancerlar.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        boton_Cancerlar.setText("Cancelar");
        boton_Cancerlar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_CancerlarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(boton_Guardar)
                        .addGap(41, 41, 41)
                        .addComponent(boton_Cancerlar))
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator1))
                .addContainerGap(13, Short.MAX_VALUE))
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
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boton_Guardar)
                    .addComponent(boton_Cancerlar))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void boton_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_GuardarActionPerformed
        int seleccion = JOptionPane.showConfirmDialog(this, "¿Desea guardar los datos?","Guardar.",JOptionPane.OK_OPTION);
        
        if(tex_Pago.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Los campos estan vacíos.","Error.",JOptionPane.QUESTION_MESSAGE);
        }else{
            if(seleccion == 0){
                if(combo_TipoPago.getSelectedIndex() == 0){
                    guardarPago();
                }else{
                    guardarAbonos();
                }
            }
        }
    }//GEN-LAST:event_boton_GuardarActionPerformed

    private void boton_CancerlarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_CancerlarActionPerformed
        dispose();
    }//GEN-LAST:event_boton_CancerlarActionPerformed

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
            java.util.logging.Logger.getLogger(dialog_Pago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dialog_Pago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dialog_Pago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dialog_Pago.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boton_Cancerlar;
    private javax.swing.JButton boton_Guardar;
    private javax.swing.JComboBox combo_TipoPago;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel label_Monto;
    private javax.swing.JTextField tex_Pago;
    // End of variables declaration//GEN-END:variables
}
