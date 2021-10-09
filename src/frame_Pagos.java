
import Dao.ClinicaDAO;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
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
 * @author administrador
 */
public class frame_Pagos extends javax.swing.JFrame {
    private Connection conn;
    private ClinicaDAO dao;
    private final String ruta = System.getProperties().getProperty("user.dir");
    
    public frame_Pagos(Connection conn) {
        initComponents();
        this.conn = conn;
        dao= new ClinicaDAO(conn);
        obtenerPago("");
        obtenerAbonoxNombre("");
    }
    
    public String mes(){
        int fecha = date_Mes.getMonth() + 1;
        String mes = null;
        
        if(fecha < 10){
            mes = String.valueOf("0" + fecha);
        }else{
            mes = String.valueOf(fecha);
        }
        return mes;
    }
    
    public String ano(){
        int fecha = date_Ano.getYear();
        String ano = String.valueOf(fecha);

        return ano;
    }
    
    public void obtenerPago(String valor){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Código Pago");
        modelo.addColumn("Nombre");
        modelo.addColumn("Consulta");
        modelo.addColumn("Tipo de Pago");
        modelo.addColumn("Fecha");
        modelo.addColumn("Monto");
        modelo.addColumn("Pago");
        String datos[] = new String[7];
        String Query;
        
        if(valor.equals("")){
            Query = "select pg.id_pago, p.nombre, tc.consulta, pg.tipo_pago, pg.dia, pg.mes, pg.año, tc.precio, pg.cantidad from pasientes p,tipo_de_consulta tc,citas c, pagos pg where pg.id_cita = c.id_cita and tc.id_consulta = c.id_consulta and p.id_pasiente = c.id_pasiente group by id_pago";
        }else{
            Query = "select pg.id_pago, p.nombre, tc.consulta, pg.tipo_pago, pg.dia, pg.mes, pg.año, tc.precio, pg.cantidad from pasientes p,tipo_de_consulta tc,citas c, pagos pg where pg.id_cita = c.id_cita and tc.id_consulta = c.id_consulta and p.id_pasiente = c.id_pasiente and p.nombre='"+valor+"' group by id_pago";
        }
        tabla_Pagos.setModel(modelo);
        try {
            Statement sentencia = conn.createStatement();
            ResultSet rs = sentencia.executeQuery(Query);
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5)+"/"+rs.getString(6)+"/"+rs.getString(7);
                datos[5] = rs.getString(8);
                datos[6] = rs.getString(9);
                modelo.addRow(datos);
            }
            
            tabla_Pagos.setModel(modelo);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error al recuperar el registro contacte al administrador.", "Error." , JOptionPane.ERROR_MESSAGE);
        }
    }
  
    public void obtenerAbonoxNombre(String cod){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Código Abono");
        modelo.addColumn("Nombre");
        modelo.addColumn("Consulta");
        modelo.addColumn("Fecha");
        modelo.addColumn("Monto");
        modelo.addColumn("Abono");
        modelo.addColumn("Cantidad Restante");
        String datos[] = new String[7];
        String Query;
        
        if(cod.equals("")){
            Query = "select a.id_abonos, p.nombre, tc.consulta,a.fecha_abono, tc.precio, a.cantidad, sum(tc.precio - a.cantidad) from pasientes p,tipo_de_consulta tc,citas c, abonos a where a.id_cita = c.id_cita and tc.id_consulta = c.id_consulta and p.id_pasiente = c.id_pasiente group by id_abonos";
        }else{
            Query = "select a.id_abonos, p.nombre, tc.consulta,a.fecha_abono, tc.precio, a.cantidad, sum(tc.precio - a.cantidad) from pasientes p,tipo_de_consulta tc,citas c, abonos a where a.id_cita = c.id_cita and tc.id_consulta = c.id_consulta and p.id_pasiente = c.id_pasiente and p.nombre='"+cod+"' group by id_abonos";
        }
        
        tabla_Abonos.setModel(modelo);
        try {
            Statement sentencia = conn.createStatement();
            ResultSet rs = sentencia.executeQuery(Query);
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                datos[5] = rs.getString(6);
                datos[6] = rs.getString(7);
                modelo.addRow(datos);
            }
            
            tabla_Abonos.setModel(modelo);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error al recuperar el registro contacte al administrador", "Error" , JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public String obtenerIDCita(String cod){
        String datos = null;
        String Query = "select id_cita from abonos where id_abonos = '"+cod+"'";
        
        try {
            Statement sentencia = conn.createStatement();
            ResultSet rs = sentencia.executeQuery(Query);
            
            while (rs.next()){
                datos = rs.getString(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error al recuperar el registro contacte al administrador.", "Error." , JOptionPane.ERROR_MESSAGE);
        }
        return datos;
    }
    
    public void eliminarPagos(){
        int fila = tabla_Pagos.getSelectedRow();
        
        if (fila >= 0){
            try {
                dao.eliminarpagos(tabla_Pagos.getValueAt(fila, 0).toString());
                JOptionPane.showMessageDialog(this, "El registro se elimino correctamente.");
                obtenerPago("");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error a eliminar el registro.","Error.",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "La fila esta vacía.","Error.",JOptionPane.QUESTION_MESSAGE);
        }
    }
    
    public void eliminarAbono(){
        int fila = tabla_Abonos.getSelectedRow();
        
        if(fila >= 0){
            try {
                dao.eliminarAbono(tabla_Abonos.getValueAt(fila, 0).toString());
                JOptionPane.showMessageDialog(this, "El registro se elimino correctamente.");
                obtenerAbonoxNombre("");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error a eliminar el registro.","Error.",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "La fila esta vacia","Error",JOptionPane.QUESTION_MESSAGE);
        }
    }
    
    public void obtenerReportePago(String mes, String ano){
        String columnas[] = {"Código Pago","Nombre","Consulta","Tipo de Pago","Fecha","Monto","Pago"};
        DefaultTableModel modelo = new DefaultTableModel(columnas,0);
        String datos[] = new String[7];
        String Query = "select pg.id_pago, p.nombre, tc.consulta, pg.tipo_pago, pg.dia, pg.mes, pg.año, tc.precio, pg.cantidad from pasientes p,tipo_de_consulta tc,citas c, pagos pg where pg.id_cita = c.id_cita and tc.id_consulta = c.id_consulta and p.id_pasiente = c.id_pasiente and pg.mes='"+mes+"' and pg.año='"+ano+"' group by id_pago";
        
        tabla_Pagos.setModel(modelo);
        try {
            Statement sentencia = conn.createStatement();
            ResultSet rs = sentencia.executeQuery(Query);
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5)+"/"+rs.getString(6)+"/"+rs.getString(7);
                datos[5] = rs.getString(8);
                datos[6] = rs.getString(9);
                modelo.addRow(datos);
            }
            
            tabla_Pagos.setModel(modelo);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error al recuperar el registro contacte al administrador.", "Error." , JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Menu_Emergente = new javax.swing.JPopupMenu();
        menu_Abono = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        label_Regresar = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_Pagos = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla_Abonos = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        boton_Eliminar = new javax.swing.JButton();
        boton_Reporte = new javax.swing.JButton();
        tex_Buscar = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        progress_Proceso = new javax.swing.JProgressBar();
        boton_ReporteAbono = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        tex_BuscarPP = new javax.swing.JTextField();
        boton_EliminarA = new javax.swing.JButton();
        date_Mes = new com.toedter.calendar.JMonthChooser();
        date_Ano = new com.toedter.calendar.JYearChooser();

        menu_Abono.setText("Ingresar Abono");
        menu_Abono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_AbonoActionPerformed(evt);
            }
        });
        Menu_Emergente.add(menu_Abono);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Nuevo Pago & Abono");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Nuevo");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 204, 153));
        jLabel2.setText("Pago & Abono");

        label_Regresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/regresar.png"))); // NOI18N
        label_Regresar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        label_Regresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        label_Regresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_RegresarMouseClicked(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Pagos"));

        tabla_Pagos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tabla_Pagos);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel3.setText("Aquí se muestran los pagos de las consultas");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Abonos"));

        tabla_Abonos.setModel(new javax.swing.table.DefaultTableModel(
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
        tabla_Abonos.setComponentPopupMenu(Menu_Emergente);
        jScrollPane2.setViewportView(tabla_Abonos);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 729, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel5.setText("Aquí se muestran los abonos de las consultas, estos se actualizarán cada vez que se realice un abono.");

        boton_Eliminar.setBackground(new java.awt.Color(204, 204, 255));
        boton_Eliminar.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        boton_Eliminar.setText("Eliminar");
        boton_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_EliminarActionPerformed(evt);
            }
        });

        boton_Reporte.setBackground(new java.awt.Color(204, 204, 255));
        boton_Reporte.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        boton_Reporte.setText("Reporte");
        boton_Reporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_ReporteActionPerformed(evt);
            }
        });

        tex_Buscar.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        tex_Buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tex_BuscarKeyReleased(evt);
            }
        });

        jLabel6.setText("Buscar Paciente:");

        boton_ReporteAbono.setBackground(new java.awt.Color(204, 204, 255));
        boton_ReporteAbono.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        boton_ReporteAbono.setText("Reporte");
        boton_ReporteAbono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_ReporteAbonoActionPerformed(evt);
            }
        });

        jLabel7.setText("Buscar Paciente:");

        tex_BuscarPP.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        tex_BuscarPP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tex_BuscarPPActionPerformed(evt);
            }
        });
        tex_BuscarPP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tex_BuscarPPKeyReleased(evt);
            }
        });

        boton_EliminarA.setBackground(new java.awt.Color(204, 204, 255));
        boton_EliminarA.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        boton_EliminarA.setText("Eliminar");
        boton_EliminarA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_EliminarAActionPerformed(evt);
            }
        });

        date_Mes.setBackground(new java.awt.Color(204, 204, 255));

        date_Ano.setBackground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(365, 754, Short.MAX_VALUE)
                        .addComponent(progress_Proceso, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel2))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(label_Regresar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel3)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(tex_Buscar)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(boton_ReporteAbono)
                                                            .addComponent(jLabel6)
                                                            .addComponent(boton_EliminarA))
                                                        .addGap(0, 0, Short.MAX_VALUE))))
                                            .addComponent(jLabel5)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(tex_BuscarPP)
                                                    .addComponent(boton_Eliminar)
                                                    .addComponent(boton_Reporte)
                                                    .addComponent(jLabel7)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(date_Mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(date_Ano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                                .addGap(0, 15, Short.MAX_VALUE))
                            .addComponent(jSeparator2))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_Regresar)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tex_BuscarPP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(boton_Eliminar)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(date_Mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(date_Ano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(boton_Reporte)))))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tex_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(boton_EliminarA)
                                .addGap(18, 18, 18)
                                .addComponent(boton_ReporteAbono)
                                .addGap(82, 82, 82)))
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void label_RegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_RegresarMouseClicked
        frame_Opciones opciones = new frame_Opciones(conn);
        opciones.setVisible(true);
        dispose();
    }//GEN-LAST:event_label_RegresarMouseClicked

    private void menu_AbonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_AbonoActionPerformed
        int fila = tabla_Abonos.getSelectedRow();
        
        if(fila >= 0){
            String idAbono = tabla_Abonos.getValueAt(fila, 0).toString();
            String idCita = obtenerIDCita(tabla_Abonos.getValueAt(fila, 0).toString());
            String nombre = tabla_Abonos.getValueAt(fila, 1).toString();
            String monto = tabla_Abonos.getValueAt(fila, 4).toString();
            String cantidad = tabla_Abonos.getValueAt(fila, 5).toString();
            
            dialog_Abono abonos = new dialog_Abono(this, true, conn, idAbono, idCita, nombre, monto, cantidad);
            abonos.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(this, "La fila esta vacía.","Error.",JOptionPane.QUESTION_MESSAGE);
        }
        obtenerAbonoxNombre("");
        obtenerPago("");
    }//GEN-LAST:event_menu_AbonoActionPerformed

    private void tex_BuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tex_BuscarKeyReleased
        obtenerAbonoxNombre(tex_Buscar.getText());
    }//GEN-LAST:event_tex_BuscarKeyReleased

    private void boton_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_EliminarActionPerformed
        int seleccion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el registro?","Eliminar.",JOptionPane.OK_OPTION);
        
        if(seleccion == 0){
            eliminarPagos();
        }
    }//GEN-LAST:event_boton_EliminarActionPerformed

    private void boton_ReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_ReporteActionPerformed
        obtenerReportePago(mes(), ano());
        try {
            Thread hilo = new Thread() {
                public void run() {   
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    XSSFSheet hoja = workbook.createSheet();

                    XSSFRow fila = hoja.createRow(0);

                    fila.createCell(0).setCellValue("Código Pago");
                    fila.createCell(1).setCellValue("Nombre");
                    fila.createCell(2).setCellValue("Consulta");
                    fila.createCell(3).setCellValue("Tipo de Pago");
                    fila.createCell(4).setCellValue("Fecha");
                    fila.createCell(5).setCellValue("Monto");
                    fila.createCell(6).setCellValue("Pago");

                    progress_Proceso.setMaximum(tabla_Pagos.getRowCount());
                    XSSFRow filas;
                    Rectangle rect;

                    for (int i = 0; i <  tabla_Pagos.getRowCount(); i++) {
                        rect =  tabla_Pagos.getCellRect(i, 0, true);
                        try {
                             tabla_Pagos.scrollRectToVisible(rect);

                        } catch (java.lang.ClassCastException e) {
                            //e.printStackTrace();
                        }    
                         tabla_Pagos.setRowSelectionInterval(i, i);
                        progress_Proceso.setValue((i + 1));

                        filas = hoja.createRow((i + 1));
                        for (int j = 0; j < 10; j++) {
                            try {
                                filas.createCell(j).setCellValue( tabla_Pagos.getValueAt(i, j).toString());
                            } catch (Exception ex) {
                                //ex.printStackTrace();
                            }
                        }
                    }
                    progress_Proceso.setValue(0);
                    progress_Proceso.setString("Abriendo Excel...");

                    try {
                        workbook.write(new FileOutputStream(new File(ruta + "//reportePagos" +  ".xlsx")));
                        Desktop.getDesktop().open(new File(ruta + "//reportePagos" +".xlsx"));
                    } catch (IOException ex) {
                    }
                }
            };
            hilo.start();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }//GEN-LAST:event_boton_ReporteActionPerformed

    private void boton_ReporteAbonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_ReporteAbonoActionPerformed
        try {
            Thread hilo = new Thread() {
                public void run() {   
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    XSSFSheet hoja = workbook.createSheet();

                    XSSFRow fila = hoja.createRow(0);

                    fila.createCell(0).setCellValue("Código Abono");
                    fila.createCell(1).setCellValue("Nombre");
                    fila.createCell(2).setCellValue("Consulta");
                    fila.createCell(3).setCellValue("Fecha del Abono");
                    fila.createCell(4).setCellValue("Monto");
                    fila.createCell(5).setCellValue("Abono");
                    fila.createCell(6).setCellValue("Cantidad Restante");

                    progress_Proceso.setMaximum(tabla_Abonos.getRowCount());
                    XSSFRow filas;
                    Rectangle rect;

                    for (int i = 0; i <  tabla_Abonos.getRowCount(); i++) {
                        rect =  tabla_Abonos.getCellRect(i, 0, true);
                        try {
                             tabla_Abonos.scrollRectToVisible(rect);

                        } catch (java.lang.ClassCastException e) {
                            //e.printStackTrace();
                        }    
                         tabla_Abonos.setRowSelectionInterval(i, i);
                        progress_Proceso.setValue((i + 1));

                        filas = hoja.createRow((i + 1));
                        for (int j = 0; j < 10; j++) {
                            try {
                                filas.createCell(j).setCellValue( tabla_Abonos.getValueAt(i, j).toString());
                            } catch (Exception ex) {
                                //ex.printStackTrace();
                            }
                        }
                    }
                    progress_Proceso.setValue(0);
                    progress_Proceso.setString("Abriendo Excel...");

                    try {
                        workbook.write(new FileOutputStream(new File(ruta + "//reporteAbonos" +  ".xlsx")));
                        Desktop.getDesktop().open(new File(ruta + "//reporteAbonos" +".xlsx"));
                    } catch (IOException ex) {
                    }
                }
            };
            hilo.start();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }//GEN-LAST:event_boton_ReporteAbonoActionPerformed

    private void tex_BuscarPPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tex_BuscarPPKeyReleased
        obtenerPago(tex_BuscarPP.getText());
    }//GEN-LAST:event_tex_BuscarPPKeyReleased

    private void boton_EliminarAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_EliminarAActionPerformed
        int seleccion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar el registro?","Eliminar.",JOptionPane.OK_OPTION);
        
        if(seleccion == 0){
            eliminarAbono();
        }
    }//GEN-LAST:event_boton_EliminarAActionPerformed

    private void tex_BuscarPPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tex_BuscarPPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tex_BuscarPPActionPerformed

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
            java.util.logging.Logger.getLogger(frame_Pagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frame_Pagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frame_Pagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frame_Pagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu Menu_Emergente;
    private javax.swing.JButton boton_Eliminar;
    private javax.swing.JButton boton_EliminarA;
    private javax.swing.JButton boton_Reporte;
    private javax.swing.JButton boton_ReporteAbono;
    private com.toedter.calendar.JYearChooser date_Ano;
    private com.toedter.calendar.JMonthChooser date_Mes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel label_Regresar;
    private javax.swing.JMenuItem menu_Abono;
    private javax.swing.JProgressBar progress_Proceso;
    private javax.swing.JTable tabla_Abonos;
    private javax.swing.JTable tabla_Pagos;
    private javax.swing.JTextField tex_Buscar;
    private javax.swing.JTextField tex_BuscarPP;
    // End of variables declaration//GEN-END:variables
}
