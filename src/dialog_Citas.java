
import Dao.ClinicaDAO;
import Dto.CitasDTO;
import Dto.ConsultasDTO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class dialog_Citas extends javax.swing.JDialog {
    private final Connection conn;
    private final String cod, nombre;
    private ClinicaDAO dao;
    List<CitasDTO> listaCitas;
    List<ConsultasDTO> listaConsulta;
    
    public dialog_Citas(java.awt.Frame parent, boolean modal,Connection conn, String cod, String nombre) {
        super(parent, modal);
        initComponents();
        this.conn = conn;
        this.cod = cod;
        this.nombre = nombre;
        dao = new ClinicaDAO(conn);
        obtenerID();
        cargarConsulta();
    }
    
    public void obtenerID(){
        label_ID.setText(cod);
        tex_nombre.setText(nombre);
    }
    
    public String fecha(){
        Date fecha = date_fecha.getDate();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String nuevaFecha = formato.format(fecha);
        return nuevaFecha;
    }
    
    public void cargarConsulta(){
        try {
            listaConsulta = new ArrayList<ConsultasDTO>();
            listaConsulta = dao.getConsultas();
            
            for (int i=0; i < listaConsulta.size(); i++){
                combo_Consulta.addItem(listaConsulta.get(i).getConsultas());
                combo_Precio.addItem(listaConsulta.get(i).getPrecio());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al recuperar los registros, por favor contacte al administrador.","Error.", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void guardarCita(){
        int idCita = 0;
        CitasDTO cita = new CitasDTO();
        String atendido = "No";
        int id = Integer.parseInt(label_ID.getText());
        int consulta = listaConsulta.get(combo_Consulta.getSelectedIndex()).getIdConsulta();
        
        cita.setFechaCita(fecha());
        cita.setHora(tex_Hora.getText());
        cita.setAtendido(atendido);
        cita.setIdPasiente(id);
        cita.setIdConsulta(consulta);
        
        try {
            dao.insertarCita(cita);
            JOptionPane.showMessageDialog(this, "Los registros se guardaron correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error al guardar los registros verifique la información.","Error.",JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public boolean comfirmarCita(String codPasiente, String fecha,String codConsulta){
        String id = null;
        String fechaDia = null;
        String idConsulta = null;
        boolean respuesta= false;
        String Query ="select id_pasiente,fecha_cita,id_consulta from citas where atendido = 'No'";
        
        try {
            Statement sentencia = conn.createStatement();
            ResultSet rs = sentencia.executeQuery(Query);
            
            while(rs.next()){
                id = rs.getString(1);
                fechaDia = rs.getString(2);
                idConsulta = rs.getString(3);
                
                if(codPasiente.equals(id) && fechaDia.equals(fecha) && idConsulta.equals(codConsulta)){
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        label_ID = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        date_fecha = new com.toedter.calendar.JDateChooser();
        tex_nombre = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tex_Hora = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        combo_Consulta = new javax.swing.JComboBox();
        combo_Precio = new javax.swing.JComboBox();
        boton_Guardar = new javax.swing.JButton();
        boton_Cancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nueva Cita");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Nueva");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 51, 204));
        jLabel2.setText("Cita");

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        jLabel4.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel4.setText("Código:");

        jLabel5.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel5.setText("Nombre:");

        label_ID.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label_ID.setText("0");

        jLabel7.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel7.setText("Fecha:");

        date_fecha.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        tex_nombre.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel8.setText("Hora:");

        tex_Hora.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tex_Hora.setText("00:00");

        jLabel9.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel9.setText("Consulta:");

        jLabel10.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel10.setText("Precio:");

        combo_Consulta.setBackground(new java.awt.Color(153, 153, 255));
        combo_Consulta.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        combo_Consulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_ConsultaActionPerformed(evt);
            }
        });

        combo_Precio.setBackground(new java.awt.Color(153, 153, 255));
        combo_Precio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tex_nombre)
                    .addComponent(date_fecha, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(tex_Hora)
                    .addComponent(combo_Consulta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_ID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(combo_Precio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(label_ID))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(tex_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7))
                    .addComponent(date_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(tex_Hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(combo_Consulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(combo_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        boton_Guardar.setBackground(new java.awt.Color(204, 204, 255));
        boton_Guardar.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        boton_Guardar.setText("Guardar");
        boton_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_GuardarActionPerformed(evt);
            }
        });

        boton_Cancelar.setBackground(new java.awt.Color(204, 204, 255));
        boton_Cancelar.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        boton_Cancelar.setText("Cancelar");
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addComponent(jSeparator2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 6, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(boton_Guardar)
                .addGap(56, 56, 56)
                .addComponent(boton_Cancelar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(boton_Cancelar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
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

    private void boton_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_CancelarActionPerformed
        dispose();
    }//GEN-LAST:event_boton_CancelarActionPerformed

    private void boton_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_GuardarActionPerformed
        int codConsulta = listaConsulta.get(combo_Consulta.getSelectedIndex()).getIdConsulta();
        String consultaString = String.valueOf(codConsulta);
        
        if(date_fecha.getDate()== null){
            JOptionPane.showMessageDialog(this, "Los campos estan vacíos.","Error.",JOptionPane.QUESTION_MESSAGE);
        }else if(comfirmarCita(label_ID.getText(), fecha(), consultaString) == true){
            JOptionPane.showMessageDialog(this, "La nueva consulta ya existe en la Base de Datos.","Error.",JOptionPane.ERROR_MESSAGE);
        }else{
            guardarCita();
            dispose();
        }
    }//GEN-LAST:event_boton_GuardarActionPerformed

    private void combo_ConsultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_ConsultaActionPerformed
        float codigo = listaConsulta.get(combo_Consulta.getSelectedIndex()).getPrecio();
        
        combo_Precio.setEnabled(false);
        combo_Precio.setSelectedItem(codigo);
    }//GEN-LAST:event_combo_ConsultaActionPerformed

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
            java.util.logging.Logger.getLogger(dialog_Citas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dialog_Citas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dialog_Citas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dialog_Citas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boton_Cancelar;
    private javax.swing.JButton boton_Guardar;
    private javax.swing.JComboBox combo_Consulta;
    private javax.swing.JComboBox combo_Precio;
    private com.toedter.calendar.JDateChooser date_fecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel label_ID;
    private javax.swing.JTextField tex_Hora;
    private javax.swing.JTextField tex_nombre;
    // End of variables declaration//GEN-END:variables
}
