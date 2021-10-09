
import Dao.ClinicaDAO;
import Dto.PacienteDTO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
/**
 *
 * 
 */
public class frame_Pacientes extends javax.swing.JFrame {
    private Connection conn;
    private ClinicaDAO dao;
    private TableModel pacientes;
    List <PacienteDTO> listaPaciente;
    private String columnas[] = {"Código paciente","Nombre","Presupuesto","Telefono","Dirección","Sexo","Edad"};
    
    public frame_Pacientes(Connection conn) {
        initComponents();
        this.conn = conn;
        dao = new ClinicaDAO(conn);
        pacientes = new DefaultTableModel(columnas,10);
        obtenerPasiente();
    }
    
    public void obtenerPasiente(){
        try {
            listaPaciente = dao.getPacientes();
            pacientes = new DefaultTableModel(columnas,listaPaciente.size());
            tabla_Pacientes.setModel(pacientes);
            
            for(int i=0; i<listaPaciente.size(); i++){
                tabla_Pacientes.setValueAt(listaPaciente.get(i).getIdPasiente(), i, 0);
                tabla_Pacientes.setValueAt(listaPaciente.get(i).getNombre(), i, 1);
                tabla_Pacientes.setValueAt(listaPaciente.get(i).getPresupuesto(), i, 2);
                tabla_Pacientes.setValueAt(listaPaciente.get(i).getTelefono(), i, 3);
                tabla_Pacientes.setValueAt(listaPaciente.get(i).getDireccion(), i, 4);
                tabla_Pacientes.setValueAt(listaPaciente.get(i).getSexo(), i, 5);
                tabla_Pacientes.setValueAt(listaPaciente.get(i).getFechaN(), i, 6);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,"Error al recuperar el registro contacte al administrador.", "Error." , JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void obtenerPasientexNombre(String cod){
        String Query = "select * from pasientes where nombre='"+cod+"'";
        String datos[] = new String[60];
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Código paciente");
        modelo.addColumn("Nombre");
        modelo.addColumn("Presupuesto");
        modelo.addColumn("Telefono");
        modelo.addColumn("Dirección");
        modelo.addColumn("Sexo");
        modelo.addColumn("Edad");
        
        tabla_Pacientes.setModel(modelo);
        
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
            tabla_Pacientes.setModel(modelo);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ha ocurrido un error verifique los datos.","Error.",JOptionPane.ERROR_MESSAGE);
        }
    }
        
    public int GuardarPasiente(){
        int idPasiente = 0;
        PacienteDTO pasiente = new PacienteDTO();
        listaPaciente = new ArrayList<PacienteDTO>();
        float presupuesto = Float.parseFloat(tex_Presupuesto.getText());
        
        pasiente.setNombre(tex_Nombre.getText());
        pasiente.setPresupuesto(presupuesto);
        pasiente.setTelefono(tex_Telefono.getText());
        pasiente.setDireccion(tex_Direccion.getText());
        pasiente.setSexo(combo_Sexo.getSelectedItem().toString());
        pasiente.setFechaN(tex_Edad.getText());
        
        try {
            dao.insertarPasientes(pasiente);
            JOptionPane.showMessageDialog(this, "Los datos han sido guardados correctamente.");
            limpiar();
            obtenerPasiente();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ha ocurrido un error verifique los datos.","Error.",JOptionPane.ERROR_MESSAGE);
        }
            
        return idPasiente;
    }
    
    public void EliminarPasiente(){
        int fila = tabla_Pacientes.getSelectedRow();
        
        if (fila >= 0){
            try {
                dao.eliminarPasiente(tabla_Pacientes.getValueAt(fila, 1).toString());
                JOptionPane.showMessageDialog(this,"Los registros fueron eliminados correctamente.");
                obtenerPasiente();
            }catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error al eliminar el registro.", "Error." , JOptionPane.ERROR_MESSAGE);    
            }
        }else{
            JOptionPane.showMessageDialog(this, "La fila que intenta eliminar esta vacía.","Error.",JOptionPane.QUESTION_MESSAGE);
        }
    }
    
    public void Modificar(){
        int fila = tabla_Pacientes.getSelectedRow();
        
        if(fila >= 0){
            desactivarBotones();
            label_ID.setText(tabla_Pacientes.getValueAt(fila, 0).toString());
            tex_Nombre.setText(tabla_Pacientes.getValueAt(fila, 1).toString());
            tex_Presupuesto.setText(tabla_Pacientes.getValueAt(fila, 2).toString());
            tex_Telefono.setText(tabla_Pacientes.getValueAt(fila, 3).toString());
            tex_Direccion.setText(tabla_Pacientes.getValueAt(fila, 4).toString());
            combo_Sexo.setSelectedItem(tabla_Pacientes.getValueAt(fila, 5));
            tex_Edad.setText(tabla_Pacientes.getValueAt(fila, 6).toString());
        }else{
            JOptionPane.showMessageDialog(this, "Fila no seleccionada.","Error.",JOptionPane.QUESTION_MESSAGE);
        }
    }
   
    public void actualizarPaciente(){
        try {
            dao.actualizarPasiente(label_ID.getText(), 
                    tex_Nombre.getText(), 
                    tex_Presupuesto.getText(), 
                    tex_Telefono.getText(), 
                    tex_Direccion.getText(), 
                    combo_Sexo.getSelectedItem().toString(), 
                    tex_Edad.getText());
            JOptionPane.showMessageDialog(this, "Los registros se actualizaron correctamente.");
            obtenerPasiente();
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
        tex_Edad.setText(null);
        tex_Direccion.setText(null);
        tex_Telefono.setText(null);
        tex_Presupuesto.setText(null);         
    }
    
    public void desactivarBotones(){
        boton_Guardar.setEnabled(false);
        boton_Eliminar.setEnabled(false);
    }
    
    public void activarBotones(){
        boton_Guardar.setEnabled(true);
        boton_Eliminar.setEnabled(true);
    }
    
    public boolean comfirmarPasiente(String nombre, String direccion, String edad){
        String pasiente = null;
        String EdadP = null;
        String DireccionP= null;
        boolean respuesta = false;
        String Query = "select nombre,direccion,fecha_nacimiento from pasientes";
        
        try {
            Statement sentencia = conn.createStatement();
            ResultSet rs = sentencia.executeQuery(Query);
            
            while(rs.next()){
                pasiente = rs.getString(1);
                DireccionP = rs.getString(3);
                EdadP = rs.getString(2);
               
                if(pasiente.equals(nombre) && DireccionP.equals(direccion) && EdadP.equals(edad)){
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
        menu_Cita = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        tex_Nombre = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        label_ID = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tex_Direccion = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tex_Telefono = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tex_Presupuesto = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        combo_Sexo = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        tex_Edad = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        boton_Guardar = new javax.swing.JButton();
        boton_Eliminar = new javax.swing.JButton();
        boton_Actualizar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_Pacientes = new javax.swing.JTable();
        label_Regresar = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        tex_BuscarPaciente = new javax.swing.JTextField();
        label_Ir2 = new javax.swing.JLabel();

        menu_Modificar.setBackground(new java.awt.Color(204, 204, 255));
        menu_Modificar.setText("Modificar");
        menu_Modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_ModificarActionPerformed(evt);
            }
        });
        menu_Emergente.add(menu_Modificar);

        menu_Cita.setText("Agregar una Cita");
        menu_Cita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_CitaActionPerformed(evt);
            }
        });
        menu_Emergente.add(menu_Cita);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Nuevo Paciente");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setText("Nuevo");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 204, 153));
        jLabel3.setText("Paciente");

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        jLabel4.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel4.setText("Nombre Completo:");

        tex_Nombre.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        tex_Nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tex_NombreKeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel7.setText("Código:");

        label_ID.setBackground(new java.awt.Color(255, 255, 255));
        label_ID.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label_ID.setText("0");
        label_ID.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel6.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel6.setText("Dirección:");

        tex_Direccion.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel8.setText("Teléfono:");

        tex_Telefono.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        tex_Telefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tex_TelefonoKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel9.setText("Costo de la cita:");

        tex_Presupuesto.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        tex_Presupuesto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tex_PresupuestoKeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel10.setText("Sexo:");

        combo_Sexo.setBackground(new java.awt.Color(153, 153, 255));
        combo_Sexo.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        combo_Sexo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Masculino", "Femenino" }));
        combo_Sexo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel11.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel11.setText("Edad:");

        tex_Edad.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        tex_Edad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tex_EdadKeyTyped(evt);
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
                            .addComponent(jLabel4)
                            .addComponent(tex_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(87, 87, 87)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(label_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(52, 52, 52)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tex_Edad, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(tex_Direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(55, 55, 55)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(tex_Telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(tex_Presupuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(68, 68, 68)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(combo_Sexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7)
                    .addComponent(jLabel11))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tex_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_ID)
                    .addComponent(tex_Edad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tex_Direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tex_Telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tex_Presupuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(combo_Sexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        jLabel5.setText("Ingresar la información de los pacientes");

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

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pacientes Agregados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        tabla_Pacientes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabla_Pacientes.setModel(new javax.swing.table.DefaultTableModel(
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
        tabla_Pacientes.setComponentPopupMenu(menu_Emergente);
        jScrollPane1.setViewportView(tabla_Pacientes);

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addContainerGap())
        );

        label_Regresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/regresar.png"))); // NOI18N
        label_Regresar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        label_Regresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        label_Regresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_RegresarMouseClicked(evt);
            }
        });

        jLabel12.setText("Buscar Paciente");

        tex_BuscarPaciente.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        tex_BuscarPaciente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tex_BuscarPacienteKeyReleased(evt);
            }
        });

        label_Ir2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ir a.png"))); // NOI18N
        label_Ir2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        label_Ir2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_Ir2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tex_BuscarPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(label_Regresar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(boton_Guardar)
                                                .addGap(36, 36, 36)
                                                .addComponent(boton_Eliminar)
                                                .addGap(37, 37, 37)
                                                .addComponent(boton_Actualizar)))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_Ir2)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_Regresar)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boton_Guardar)
                    .addComponent(boton_Eliminar)
                    .addComponent(boton_Actualizar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tex_BuscarPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_Ir2))
                .addGap(18, 18, 18)
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

    private void boton_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_GuardarActionPerformed
        if (tex_Nombre.getText().isEmpty() && tex_Presupuesto.getText().isEmpty() && tex_Direccion.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Los campos estan vacíos.", "Error.", JOptionPane.QUESTION_MESSAGE);
        }else if(comfirmarPasiente(tex_Nombre.getText(),tex_Edad.getText(),tex_Direccion.getText())){
            JOptionPane.showMessageDialog(this, "El nuevo paciente que intenta ingresar ya existe en la Base de Datos.","Error.",JOptionPane.ERROR_MESSAGE);
        }else{
            GuardarPasiente();
        }
    }//GEN-LAST:event_boton_GuardarActionPerformed

    private void boton_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_EliminarActionPerformed
        int seleccion = JOptionPane.showConfirmDialog(this,"¿Desea eliminar el registro?", "Eliminar.", JOptionPane.OK_OPTION);
        
        if (seleccion ==0){
           EliminarPasiente();
        }
    }//GEN-LAST:event_boton_EliminarActionPerformed

    private void label_RegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_RegresarMouseClicked
        frame_Opciones opciones = new frame_Opciones(conn);
        opciones.setVisible(true);
        dispose();
    }//GEN-LAST:event_label_RegresarMouseClicked

    private void menu_ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_ModificarActionPerformed
        Modificar();
    }//GEN-LAST:event_menu_ModificarActionPerformed

    private void boton_ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_ActualizarActionPerformed
        if (tex_Nombre.getText().isEmpty() && tex_Presupuesto.getText().isEmpty() && tex_Direccion.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Los campos estan vacíos.", "Error.", JOptionPane.QUESTION_MESSAGE);
        }else{
            actualizarPaciente();
        }
    }//GEN-LAST:event_boton_ActualizarActionPerformed

    private void menu_CitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_CitaActionPerformed
        int fila = tabla_Pacientes.getSelectedRow();
        
        if (fila >= 0){
            String cod = tabla_Pacientes.getValueAt(fila, 0).toString();
            String nombre = tabla_Pacientes.getValueAt(fila, 1).toString();
            limpiar();
            activarBotones();
            dialog_Citas cita = new dialog_Citas(this, true , conn, cod, nombre);
            cita.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(this, "La fila esta vacía.","Error.",JOptionPane.QUESTION_MESSAGE);
        }     
    }//GEN-LAST:event_menu_CitaActionPerformed

    private void tex_BuscarPacienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tex_BuscarPacienteKeyReleased
        if(tex_BuscarPaciente.getText().isEmpty()){
            obtenerPasiente();
        }else{
            obtenerPasientexNombre(tex_BuscarPaciente.getText());
        } 
    }//GEN-LAST:event_tex_BuscarPacienteKeyReleased

    private void label_Ir2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_Ir2MouseClicked
        frame_Citas citas = new frame_Citas(conn);
        citas.setVisible(true);
        dispose();
    }//GEN-LAST:event_label_Ir2MouseClicked

    private void tex_EdadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tex_EdadKeyTyped
        String caracter = String.valueOf(evt.getKeyChar());
        
        if(!(caracter.matches("[0-9]"))){
            evt.consume();
        }
    }//GEN-LAST:event_tex_EdadKeyTyped

    private void tex_TelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tex_TelefonoKeyTyped
        String caracter = String.valueOf(evt.getKeyChar());
        
        if(!(caracter.matches("[0-9]"))){
            evt.consume();
        }
    }//GEN-LAST:event_tex_TelefonoKeyTyped

    private void tex_PresupuestoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tex_PresupuestoKeyTyped
        String caracter = String.valueOf(evt.getKeyChar());
        
        if(!(caracter.matches("[0-9]"))){
            evt.consume();
        }
    }//GEN-LAST:event_tex_PresupuestoKeyTyped

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
            java.util.logging.Logger.getLogger(frame_Pacientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frame_Pacientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frame_Pacientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frame_Pacientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boton_Actualizar;
    private javax.swing.JButton boton_Eliminar;
    private javax.swing.JButton boton_Guardar;
    private javax.swing.JComboBox combo_Sexo;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JLabel label_Ir2;
    private javax.swing.JLabel label_Regresar;
    private javax.swing.JMenuItem menu_Cita;
    private javax.swing.JPopupMenu menu_Emergente;
    private javax.swing.JMenuItem menu_Modificar;
    private javax.swing.JTable tabla_Pacientes;
    private javax.swing.JTextField tex_BuscarPaciente;
    private javax.swing.JTextField tex_Direccion;
    private javax.swing.JTextField tex_Edad;
    private javax.swing.JTextField tex_Nombre;
    private javax.swing.JTextField tex_Presupuesto;
    private javax.swing.JTextField tex_Telefono;
    // End of variables declaration//GEN-END:variables
}
