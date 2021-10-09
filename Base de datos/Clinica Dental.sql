drop database if exists Clinica_Dental;
create database Clinica_Dental;
use Clinica_Dental;

create table pasientes(
id_pasiente int primary key auto_increment,
nombre varchar(50) not null,
presupuesto float not null,
telefono varchar (14),
direccion varchar(30),
sexo varchar(10) not null,
fecha_nacimiento varchar(20) not null 
);

create table tipo_de_consulta(
id_consulta int primary key auto_increment,
consulta varchar (100) not null,
precio float not null
);

create table citas(
id_cita int primary key auto_increment,
fecha_cita varchar(20) not null,
hora varchar(10),
atendido varchar (2),
id_pasiente int not null,
id_consulta int not null,
foreign key (id_pasiente) references pasientes(id_pasiente) on delete cascade on update cascade,
foreign key (id_consulta) references tipo_de_consulta(id_consulta)on delete cascade on update cascade
);

create table pagos(
id_pago int primary key auto_increment,
id_cita int not null,
dia varchar(2) not null,
mes varchar(2) not null,
año varchar(4) not null,
cantidad float not null,
tipo_pago varchar(20) not null,
foreign key(id_cita) references citas(id_cita) on delete cascade on update cascade
);

create table abonos(
id_abonos int primary key auto_increment,
fecha_abono varchar(20) not null,
cantidad float not null,
id_cita int not null,
foreign key (id_cita) references citas(id_cita) on delete cascade on update cascade
);

create table inventario_clinica(
id_inventario int primary key auto_increment,
nombre varchar(40) not null,
descripcion varchar(100),
cantidad int not null
);