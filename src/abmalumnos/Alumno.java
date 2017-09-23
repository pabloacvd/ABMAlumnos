/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abmalumnos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author educacionit
 */
public class Alumno {
    private IntegerProperty id;
    private StringProperty nombre;
    private IntegerProperty dni;
    private StringProperty mail;
    private static Connection laConexion;

    public Alumno(int id) {
        laConexion = obtenerConexion();
        String sql = "SELECT * FROM alumnos WHERE id="+id;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = laConexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                this.id = new SimpleIntegerProperty(rs.getInt("id"));
                this.nombre = new SimpleStringProperty(rs.getString("nombre"));
                this.dni = new SimpleIntegerProperty(rs.getInt("dni"));
                this.mail = new SimpleStringProperty(rs.getString("mail"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Alumno.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{    rs.close();     } catch (SQLException e){}
            try{    stmt.close();   } catch (SQLException e){}       
            try{    laConexion.close();   } catch (SQLException e){}       
        }
    }
    public Alumno(Integer id, String nombre, Integer dni, String mail) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.dni = new SimpleIntegerProperty(dni);
        this.mail = new SimpleStringProperty(mail);
    }
    
    
    public static ObservableList<Alumno> getAlumnos(){
        ObservableList<Alumno> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM alumnos;";
        laConexion = obtenerConexion();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = laConexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next() ){
                Alumno alu = new Alumno(
                    rs.getInt("id"), 
                    rs.getString("nombre"),
                    rs.getInt("dni"),
                    rs.getString("mail")
                );
                lista.add(alu);
            }
        } catch (SQLException ex) {
            //
        } finally {
            try{    rs.close();         } catch (SQLException e){}
            try{    stmt.close();       } catch (SQLException e){}       
            try{    laConexion.close(); } catch (SQLException e){}
        }
        return lista;
    }

    public static Connection obtenerConexion(){
        String dbDriver = "com.mysql.jdbc.Driver"; 
        String dbConnString = "jdbc:mysql://localhost/instituto";
        try {
            Class.forName(dbDriver).newInstance();
            return DriverManager.getConnection(dbConnString, "root","unafacil");
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException ex) {
            return null;
        }
    }

    public IntegerProperty getId() {
        return id;
    }

    public void setId(IntegerProperty id) {
        this.id = id;
    }

    public StringProperty getNombre() {
        return nombre;
    }

    public void setNombre(StringProperty nombre) {
        this.nombre = nombre;
    }

    public IntegerProperty getDni() {
        return dni;
    }

    public void setDni(IntegerProperty dni) {
        this.dni = dni;
    }

    public StringProperty getMail() {
        return mail;
    }

    public void setMail(StringProperty mail) {
        this.mail = mail;
    }

    public boolean guardar() {
        boolean resultado = true;
        laConexion = obtenerConexion();
        String sql;
        if(this.getId().get()>0)
            sql = "UPDATE alumnos SET nombre=?, dni=?, mail=? WHERE id=?;";
        else
            sql = "INSERT INTO alumnos VALUES (NULL, ?, ?, ?);";
        PreparedStatement stmt = null;
        try {
            stmt = laConexion.prepareStatement(sql);
            stmt.setString(1, this.nombre.get());
            stmt.setInt(2, this.dni.get());
            stmt.setString(3, this.mail.get());
            if(this.getId().get()>0)
                stmt.setInt(4, this.id.get());
            if(stmt.executeUpdate() < 0)
                throw new SQLException();
        } catch (SQLException ex) {
            resultado = false;
        } finally {
            try{    stmt.close();   } catch (SQLException e){}       
            try{    laConexion.close();   } catch (SQLException e){}       
        }
        return resultado;
    }

    public boolean eliminar() {
        boolean resultado = true;
        laConexion = obtenerConexion();
        String sql = "DELETE FROM alumnos WHERE id="+this.id.get();
        Statement stmt = null;
        try{
           stmt = laConexion.createStatement();
           stmt.execute(sql);
        } catch (SQLException ex) {
            resultado = false;
        } finally {
            try{    stmt.close();   } catch (SQLException e){}       
            try{    laConexion.close();   } catch (SQLException e){}       
        }
        return resultado;
    }
}
