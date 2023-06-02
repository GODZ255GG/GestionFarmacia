/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jfxgestionfarmacia.modelo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jfxgestionfarmacia.modelo.ConexionBD;
import jfxgestionfarmacia.modelo.pojo.Proveedor;

/**
 *
 * @author DELL
 */
public class ProveedorDAO {
   public List<Proveedor> obtenerNombresProveedores() {
        List<Proveedor> nombresProveedores = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexionBD();

        if (conexionBD != null) {
            try {
                String consulta = "SELECT idProveedor,nombre FROM Proveedor";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();

                while (resultado.next()) {
                    Proveedor proveedor = new Proveedor();
                    proveedor.setIdProveedor(resultado.getInt("idProveedor"));
                    proveedor.setNombre(resultado.getString("nombre"));
                    nombresProveedores.add(proveedor);
                }

                conexionBD.close();
            } catch (SQLException e) {
                // Manejo de excepciones
            }
        }

        return nombresProveedores;
    }

}
