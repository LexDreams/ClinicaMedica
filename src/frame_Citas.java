
import Dao.ClinicaDAO;
import Dto.CitasDTO;
import Dto.ConsultasDTO;
import Dto.PacienteDTO;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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


public class frame_Citas extends javax.swing.JFrame {
    private Connection conn;
    private ClinicaDAO dao;
    private final String ruta = System.getProperties().getProperty("user.dir");
    List<CitasDTO> listacitas;
    List<PacienteDTO> listaPaciente;
    List<ConsultasDTO> listaConsulta;
    
    public frame_Citas(Connection conn) {
        initComponents();
        this.conn = conn;
        dao = new ClinicaDAO(conn);
        obtenerCitasxFecha(fechaActual());
        cargarConsulta();
        cargarPacientes();
    }

    public void cargarPacientes(){
        try {
            listaPaciente = new ArrayList<PacienteDTO>();
            listaPaciente = dao.getPacientes();
            
            for (int i=0; i < listaPaciente.size(); i++){
                combo_Pasiente.addItem(listaPaciente.get(i).getNombre());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al recuperar los registros, por favor contacte al administrador.","Error.", JOptionPane.ERROR_MESSAGE);
        }
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
    
    public void obtenerCitasxFecha(String valor){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Código Cita");
        modelo.addColumn("Nombre");
        modelo.addColumn("Tipo de Consulta");
        modelo.addColumn("Precio");
        modelo.addColumn("Fecha");
        modelo.addColumn("Hora");
        modelo.addColumn("Atendido");
        String datos[] = new String[7];
        String Query;
        
        if(valor.equals("")){
            Query ="select c.id_cita, p.nombre, tc.consulta, tc.precio, c.fecha_cita, c.hora, c.atendido from pasientes p, tipo_de_consulta tc, citas c where p.id_pasiente = c.id_pasiente and tc.id_consulta = c.id_consulta group by id_cita";
        }else{
            Query ="select c.id_cita, p.nombre, tc.consulta, tc.precio, c.fecha_cita, c.hora, c.atendido from pasientes p, tipo_de_consulta tc, citas c where p.id_pasiente = c.id_pasiente and tc.id_consulta = c.id_consulta and c.fecha_cita ='"+valor+"' and c.atendido = 'No' group by id_cita";
        }
        
        tabla_Citas.setModel(modelo);
        try {
            Statement sentencia = conn.createStatement();
            ResultSet rs = sentencia.executeQuery(Query);
            
            while (rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                datos[5] = rs.getString(6);
                datos[6] = rs.getString(7);
                modelo.addRow(datos);
            }
            tabla_Citas.setModel(modelo);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error al recuperar el registro contacte al administrador.", "Error." , JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void guardarCita(){
        CitasDTO cita = new CitasDTO();
        listacitas = new ArrayList<CitasDTO>();
        String atentido = "No";
        int pasiente = listaPaciente.get(combo_Pasiente.getSelectedIndex()).getIdPasiente();
        int consulta = listaConsulta.get(combo_Consulta.getSelectedIndex()).getIdConsulta();
        
        cita.setFechaCita(fecha());
        cita.setHora(tex_Hora.getText());
        cita.setAtendido(atentido);
        cita.setIdPasiente(pasiente);
        cita.setIdConsulta(consulta);
        
        try {
            dao.insertarCita(cita);
            JOptionPane.showMessageDialog(this, "Los registros se guardaron correctamente.");
            obtenerCitasxFecha("");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error al guardar los registros verifique la información.","Error.",JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void eliminarCita(){
        int fila = tabla_Citas.getSelectedRow();
        if(fila >= 0){
           try {
                dao.eliminarCita(tabla_Citas.getValueAt(fila, 0).toString());
                JOptionPane.showMessageDialog(this,"Los registros fueron eliminados correctamente.");
                obtenerCitasxFecha("");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al eliminar los registros.","Error.",JOptionPane.ERROR_MESSAGE);
            } 
        }else{
            JOptionPane.showMessageDialog(this, "La fila que intenta eliminar esta vacia.","Error.",JOptionPane.QUESTION_MESSAGE);
        }
        
    }
    
    public String fecha(){
        Date fecha = date_Fecha.getDate();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String nuevaFecha = formato.format(fecha);
        return nuevaFecha;
    }
    
    public String fechaActual(){
        Date fecha = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String nuevaFecha = formato.format(fecha);
        tex_Fecha.setText(nuevaFecha);
        return nuevaFecha;
    }
    
    public void obtenerCitasParaReporte(){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Código Cita");
        modelo.addColumn("Nombre");
        modelo.addColumn("Tipo de Consulta");
        modelo.addColumn("Precio");
        modelo.addColumn("Fecha");
        modelo.addColumn("Hora");
        modelo.addColumn("Atendido");
        String datos[] = new String[7];
        String Query ="select c.id_cita, p.nombre, tc.consulta, tc.precio, c.fecha_cita, c.hora, c.atendido from pasientes p, tipo_de_consulta tc, citas c where p.id_pasiente = c.id_pasiente and tc.id_consulta = c.id_consulta and c.atendido = 'Si' group by id_cita;";
        
        tabla_Citas.setModel(modelo);
        try {
            Statement sentencia = conn.createStatement();
            ResultSet rs = sentencia.executeQuery(Query);
            
            while (rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                datos[5] = rs.getString(6);
                datos[6] = rs.getString(7);
                modelo.addRow(datos);
            }
            tabla_Citas.setModel(modelo);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error al recuperar el registro contacte al administrador.", "Error." , JOptionPane.ERROR_MESSAGE);
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

        menu_Emergente = new javax.swing.JPopupMenu();
        menu_Pago = new javax.swing.JMenuItem();
        menu_Alta = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        combo_Pasiente = new javax.swing.JComboBox();
        label_ID = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        date_Fecha = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        combo_Consulta = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        combo_Precio = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        tex_Hora = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_Citas = new javax.swing.JTable();
        boton_Guardar = new javax.swing.JButton();
        boton_Eliminar = new javax.swing.JButton();
        label_Regresar = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tex_Fecha = new javax.swing.JTextField();
        boton_Citas = new javax.swing.JButton();
        label_Ir = new javax.swing.JLabel();
        boton_Reporte = new javax.swing.JButton();
        progress_Proceso = new javax.swing.JProgressBar();

        menu_Pago.setText("Agregar Pago");
        menu_Pago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_PagoActionPerformed(evt);
            }
        });
        menu_Emergente.add(menu_Pago);

        menu_Alta.setText("Dar Alta");
        menu_Alta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_AltaActionPerformed(evt);
            }
        });
        menu_Emergente.add(menu_Alta);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Nueva Cita");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Nueva");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 204, 153));
        jLabel2.setText("Cita");

        jLabel4.setText("Ingrese la información de las citas.");

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        jLabel5.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel5.setText("Nombre-Paciente:");

        jLabel6.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel6.setText("Código:");

        combo_Pasiente.setBackground(new java.awt.Color(153, 153, 255));
        combo_Pasiente.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N

        label_ID.setBackground(new java.awt.Color(255, 255, 255));
        label_ID.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label_ID.setText("0");
        label_ID.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel8.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel8.setText("Fecha:");

        date_Fecha.setBackground(new java.awt.Color(204, 204, 255));
        date_Fecha.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel9.setText("Cita:");

        combo_Consulta.setBackground(new java.awt.Color(153, 153, 255));
        combo_Consulta.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        combo_Consulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_ConsultaActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel10.setText("Precio de la cita:");

        combo_Precio.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel11.setText("Hora:");

        tex_Hora.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        tex_Hora.setText("00:00");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(date_Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(tex_Hora, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(combo_Pasiente, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(48, 48, 48)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(label_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(combo_Consulta, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(combo_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(101, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combo_Pasiente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_ID))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(date_Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(combo_Consulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(combo_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tex_Hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Citas Agregadas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        tabla_Citas.setModel(new javax.swing.table.DefaultTableModel(
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
        tabla_Citas.setComponentPopupMenu(menu_Emergente);
        jScrollPane1.setViewportView(tabla_Citas);

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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        label_Regresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/regresar.png"))); // NOI18N
        label_Regresar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        label_Regresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        label_Regresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_RegresarMouseClicked(evt);
            }
        });

        jLabel7.setText("Buscar Citas del Día");

        tex_Fecha.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        tex_Fecha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tex_FechaKeyReleased(evt);
            }
        });

        boton_Citas.setBackground(new java.awt.Color(204, 204, 255));
        boton_Citas.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        boton_Citas.setText("Mostrar todo");
        boton_Citas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_CitasActionPerformed(evt);
            }
        });

        label_Ir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ir a.png"))); // NOI18N
        label_Ir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        label_Ir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_IrMouseClicked(evt);
            }
        });

        boton_Reporte.setBackground(new java.awt.Color(204, 204, 255));
        boton_Reporte.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        boton_Reporte.setText("Reporte Citas");
        boton_Reporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_ReporteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(717, 717, 717)
                .addComponent(progress_Proceso, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(label_Regresar)
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(boton_Guardar)
                                .addGap(53, 53, 53)
                                .addComponent(boton_Eliminar)
                                .addGap(37, 37, 37)
                                .addComponent(boton_Reporte, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tex_Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(boton_Citas))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_Ir)))))
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
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(label_Regresar))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(boton_Reporte)
                            .addComponent(boton_Eliminar)
                            .addComponent(boton_Guardar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(boton_Citas, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(tex_Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_Ir))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(progress_Proceso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void combo_ConsultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_ConsultaActionPerformed
        float cod = listaConsulta.get(combo_Consulta.getSelectedIndex()).getPrecio();
        combo_Precio.setEnabled(false);
        combo_Precio.setSelectedItem(cod);
    }//GEN-LAST:event_combo_ConsultaActionPerformed

    private void boton_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_GuardarActionPerformed
        int idPasiente = listaPaciente.get(combo_Pasiente.getSelectedIndex()).getIdPasiente();
        int idConsulta = listaConsulta.get(combo_Consulta.getSelectedIndex()).getIdConsulta();
        String codPasiente = String.valueOf(idPasiente);
        String codConsulta = String.valueOf(idConsulta);
        
        if(date_Fecha.getDate() == null){
            JOptionPane.showMessageDialog(this, "Los campos estan vacíos.","Error.",JOptionPane.QUESTION_MESSAGE);
        }else if(comfirmarCita(codPasiente, fecha(), codConsulta)){
            JOptionPane.showMessageDialog(this, "La nueva consulta ya existe en la Base de Datos.","Error.",JOptionPane.ERROR_MESSAGE);
        }else{
            guardarCita();
        }
    }//GEN-LAST:event_boton_GuardarActionPerformed

    private void boton_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_EliminarActionPerformed
        int seleccion = JOptionPane.showConfirmDialog(this,"¿Desea eliminar el registro?", "Eliminar.", JOptionPane.OK_OPTION);
        
        
        if (seleccion ==0){
            eliminarCita();
        }
    }//GEN-LAST:event_boton_EliminarActionPerformed

    private void menu_PagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_PagoActionPerformed
        int fila = tabla_Citas.getSelectedRow();
        String atendido = tabla_Citas.getValueAt(tabla_Citas.getSelectedRow(), 6).toString();
        
        if(atendido.equals("No")){
            if (fila >= 0){
                String idCita = tabla_Citas.getValueAt(fila, 0).toString();
                String pago = tabla_Citas.getValueAt(fila, 3).toString();
        
                dialog_Pago dialogpago = new dialog_Pago(this, true, conn, idCita, pago);
                dialogpago.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(this, "La fila esta vacía.","Error.",JOptionPane.QUESTION_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "Este paciente ya ha sido atendido, verifique en la tabla 'Pagos'.","Error.",JOptionPane.ERROR_MESSAGE);
        }
        obtenerCitasxFecha(fechaActual());
    }//GEN-LAST:event_menu_PagoActionPerformed

    private void tex_FechaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tex_FechaKeyReleased
        obtenerCitasxFecha(tex_Fecha.getText());
    }//GEN-LAST:event_tex_FechaKeyReleased

    private void boton_CitasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_CitasActionPerformed
        obtenerCitasxFecha("");
    }//GEN-LAST:event_boton_CitasActionPerformed

    private void label_IrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_IrMouseClicked
        frame_Pagos pagos = new frame_Pagos(conn);
        pagos.setVisible(true);
        dispose();
    }//GEN-LAST:event_label_IrMouseClicked

    private void boton_ReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_ReporteActionPerformed
        obtenerCitasParaReporte();
        try {
            Thread hilo = new Thread() {
                public void run() {   
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    XSSFSheet hoja = workbook.createSheet();

                    XSSFRow fila = hoja.createRow(0);

                    fila.createCell(0).setCellValue("Código Cita");
                    fila.createCell(1).setCellValue("Nombre");
                    fila.createCell(2).setCellValue("Tipo de Consulta");
                    fila.createCell(3).setCellValue("Precio");
                    fila.createCell(4).setCellValue("Fecha");
                    fila.createCell(5).setCellValue("Hora");
                    fila.createCell(6).setCellValue("Atendido");

                    progress_Proceso.setMaximum(tabla_Citas.getRowCount());
                    XSSFRow filas;
                    Rectangle rect;

                    for (int i = 0; i <  tabla_Citas.getRowCount(); i++) {
                        rect =  tabla_Citas.getCellRect(i, 0, true);
                        try {
                             tabla_Citas.scrollRectToVisible(rect);

                        } catch (java.lang.ClassCastException e) {
                            //e.printStackTrace();
                        }    
                         tabla_Citas.setRowSelectionInterval(i, i);
                        progress_Proceso.setValue((i + 1));

                        filas = hoja.createRow((i + 1));
                        for (int j = 0; j < 10; j++) {
                            try {
                                filas.createCell(j).setCellValue( tabla_Citas.getValueAt(i, j).toString());
                            } catch (Exception ex) {
                                //ex.printStackTrace();
                            }
                        }
                    }
                    progress_Proceso.setValue(0);
                    progress_Proceso.setString("Abriendo Excel...");

                    try {
                        workbook.write(new FileOutputStream(new File(ruta + "//reporteCita" +  ".xlsx")));
                        Desktop.getDesktop().open(new File(ruta + "//reporteCita" +".xlsx"));
                    } catch (IOException ex) {
                    }
                }
            };
            hilo.start();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }//GEN-LAST:event_boton_ReporteActionPerformed

    private void menu_AltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_AltaActionPerformed
        int fila = tabla_Citas.getSelectedRow();
        
        if (fila >= 0){
            String atendido = "Si";
            try {
                dao.actualizarCita(tabla_Citas.getValueAt(fila, 0).toString(), atendido);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al dar de alta.","Error.",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "No ha seleccionado la fila.","Error.",JOptionPane.QUESTION_MESSAGE);
        }
        obtenerCitasxFecha(fechaActual());
    }//GEN-LAST:event_menu_AltaActionPerformed

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
            java.util.logging.Logger.getLogger(frame_Citas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frame_Citas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frame_Citas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frame_Citas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boton_Citas;
    private javax.swing.JButton boton_Eliminar;
    private javax.swing.JButton boton_Guardar;
    private javax.swing.JButton boton_Reporte;
    private javax.swing.JComboBox combo_Consulta;
    private javax.swing.JComboBox combo_Pasiente;
    private javax.swing.JComboBox combo_Precio;
    private com.toedter.calendar.JDateChooser date_Fecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel label_ID;
    private javax.swing.JLabel label_Ir;
    private javax.swing.JLabel label_Regresar;
    private javax.swing.JMenuItem menu_Alta;
    private javax.swing.JPopupMenu menu_Emergente;
    private javax.swing.JMenuItem menu_Pago;
    private javax.swing.JProgressBar progress_Proceso;
    private javax.swing.JTable tabla_Citas;
    private javax.swing.JTextField tex_Fecha;
    private javax.swing.JTextField tex_Hora;
    // End of variables declaration//GEN-END:variables
}
