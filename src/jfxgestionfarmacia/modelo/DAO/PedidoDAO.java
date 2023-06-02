package jfxgestionfarmacia.modelo.DAO;

import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jfxgestionfarmacia.modelo.ConexionBD;
import jfxgestionfarmacia.modelo.pojo.Pedido;
import jfxgestionfarmacia.modelo.pojo.Producto;
import jfxgestionfarmacia.utils.Constantes;


public class PedidoDAO {
    public static Pedido obtenerPedidos(){
        
        Pedido respuesta = new Pedido();
        respuesta.setCodigoRespuesta(Constantes.OPERACION_EXITOSA);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
                String consulta = "SELECT idPedido, fechaPedido, total, rastreo, fechaEnvio, Sede.direccion AS direccion, Pedido.Proveedor_idProveedor AS idProveedor, Proveedor.nombre AS 'nombreProveedor', Pedido.Sede_idSede AS 'idSede', Sede.nombre AS 'nombreSede' "
                        + "FROM Pedido INNER JOIN Proveedor ON Pedido.Proveedor_idProveedor = Proveedor.idProveedor "
                        + "INNER JOIN Sede ON Pedido.Sede_idSede = Sede.idSede WHERE rastreo != 'Cancelado';";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Pedido> pedidoConsulta = new ArrayList();
                while(resultado.next()){
                    Pedido pedido = new Pedido();
                    pedido.setIdPedido(resultado.getInt("idPedido"));
                    pedido.setFechaPedido(resultado.getString("fechaPedido"));
                    pedido.setTotal(resultado.getFloat("total"));
                    pedido.setRastreo(resultado.getString("rastreo"));
                    pedido.setFechaEnvio(resultado.getString("fechaEnvio"));
                    pedido.setDireccion(resultado.getString("direccion"));
                    pedido.setIdProveedor(resultado.getInt("idProveedor"));
                    pedido.setNombreProveedor(resultado.getString("nombreProveedor"));
                    pedido.setIdSede(resultado.getInt("idSede"));
                    pedido.setNombreSede(resultado.getString("nombreSede"));
                    pedidoConsulta.add(pedido);
                }
                respuesta.setPedidos(pedidoConsulta);
                conexionBD.close();
            }catch (SQLException e){
                respuesta.setCodigoRespuesta(Constantes.ERROR_CONSULTA);
            }
        }else{
            respuesta.setCodigoRespuesta(Constantes.ERROR_CONEXION);
        }
        return respuesta;
    }

    public static Pedido getPedidoConProductos(int idPedido) {
        Pedido pedido = new Pedido();
        Connection conexionDB = ConexionBD.abrirConexionBD();
        if(conexionDB != null) {
            try {
                String query = "SELECT idPedido, fechaPedido, total, rastreo, fechaEnvio, Sede.direccion AS direccion, Pedido.Proveedor_idProveedor AS idProveedor, Proveedor.nombre AS 'nombreProveedor', Pedido.Sede_idSede AS 'idSede', Sede.nombre AS 'nombreSede'  FROM Pedido INNER JOIN Proveedor ON Pedido.Proveedor_idProveedor = Proveedor.idProveedor INNER JOIN Sede ON Pedido.Sede_idSede = Sede.idSede WHERE idPedido = ?";
                PreparedStatement prepararSentencia = conexionDB.prepareStatement(query);
                prepararSentencia.setInt(1, idPedido);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Pedido> pedidoConsulta = new ArrayList();
                while(resultado.next()){
                    pedido.setIdPedido(resultado.getInt("idPedido"));
                    pedido.setFechaPedido(resultado.getString("fechaPedido"));
                    pedido.setTotal(resultado.getFloat("total"));
                    pedido.setRastreo(resultado.getString("rastreo"));
                    pedido.setFechaEnvio(resultado.getString("fechaEnvio"));
                    pedido.setDireccion(resultado.getString("direccion"));
                    pedido.setIdProveedor(resultado.getInt("idProveedor"));
                    pedido.setNombreProveedor(resultado.getString("nombreProveedor"));
                    pedido.setIdSede(resultado.getInt("idSede"));
                    pedido.setNombreSede(resultado.getString("nombreSede"));
                    pedidoConsulta.add(pedido);
                }
                
                String productosQuery = "SELECT * FROM producto_pedido AS pp INNER JOIN Producto ON "
                        + "pp.Producto_idProducto = Producto.idProducto WHERE Pedido_idPedido = ?";
                PreparedStatement preparedStatement = conexionDB.prepareStatement(productosQuery);
                preparedStatement.setInt(1, idPedido);
                ResultSet result = preparedStatement.executeQuery();
                ArrayList<Producto> productos = new ArrayList();
                while(result.next()) {
                    Producto producto = new Producto();
                    producto.setCantidad(result.getInt("cantidad"));
                    producto.setPrecio(result.getFloat("precio"));
                    producto.setPrecioTotal(producto.getCantidad() * producto.getPrecio());
                    producto.setNombre(result.getString("nombre"));
                    productos.add(producto);
                }
                pedido.setProductos(productos);
                        
                
                conexionDB.close();
            } catch (SQLException e){
                e.printStackTrace();
                pedido.setCodigoRespuesta(Constantes.ERROR_CONSULTA);
            }
        } else {
            pedido.setCodigoRespuesta(Constantes.ERROR_CONEXION);
        }
        return pedido;
    }
    
    public List<Producto> obtenerProductosPedido(int pedidoId) {
    List<Producto> productosRelacionados = new ArrayList<>();
    Connection conexionBD = ConexionBD.abrirConexionBD();
    
    if (conexionBD != null) {
        try {
            String consulta = "SELECT * FROM Producto WHERE idPedido = ?";
            PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
            prepararSentencia.setInt(1, pedidoId);
            ResultSet resultado = prepararSentencia.executeQuery();
            
            while (resultado.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(resultado.getInt("id"));
                producto.setNombre(resultado.getString("nombre"));
                // Otros atributos del producto
                
                productosRelacionados.add(producto);
            }
            conexionBD.close();
            } catch (SQLException e) {
                // Manejo de excepciones
            }
        }
    return productosRelacionados;
    }
    
    public static void cancelarEstadoPedido(int pedidoId) {
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if (conexionBD != null) {
            try {
                String consulta = "UPDATE Pedido SET rastreo = 'Cancelado' WHERE idPedido = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, pedidoId);
                prepararSentencia.executeUpdate();
                conexionBD.close();
            } catch (SQLException e) {
                // Manejo de excepciones
            }
        }
    }    
    
    public static int registrarPedido(Pedido pedidoNuevo, List<Producto> productosRegistrados) {
    int respuesta = Constantes.ERROR_CONSULTA;
    Connection conexionBD = ConexionBD.abrirConexionBD();
    if (conexionBD != null) {
        try {
            String sentencia = "INSERT INTO Pedido (fechaPedido, total, rastreo, fechaEnvio, direccion, Proveedor_idProveedor, Sede_idSede) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);
            prepararSentencia.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            prepararSentencia.setFloat(2, pedidoNuevo.getTotal());
            prepararSentencia.setString(3, "Preparando");
            prepararSentencia.setString(4, pedidoNuevo.getFechaEnvio());
            prepararSentencia.setString(5, pedidoNuevo.getDireccion());
            prepararSentencia.setInt(6, pedidoNuevo.getIdProveedor());
            prepararSentencia.setInt(7, pedidoNuevo.getIdSede());
            
            int filasAfectadas = prepararSentencia.executeUpdate(); 
            
            int idPedidoInsertado = -1;
            ResultSet generatedKeys = prepararSentencia.getGeneratedKeys();
            if (generatedKeys.next()) {
                idPedidoInsertado = generatedKeys.getInt(1);
            } 
   
            if (filasAfectadas >= 1 && idPedidoInsertado != -1) {
                
                for (Producto item : productosRegistrados) {
                    String sentenciaInsert = "INSERT INTO Producto_Pedido (Producto_idProducto, Pedido_idPedido, cantidad, total) VALUES (?, ?, ?, ?)";
                    PreparedStatement prepararSentenciaInsert = conexionBD.prepareStatement(sentenciaInsert);
                    
                    int cantidad = item.getCantidad();
                    float total = item.getPrecioTotal();
                    
                    prepararSentenciaInsert.setInt(1, item.getIdProducto());
                    prepararSentenciaInsert.setInt(2, idPedidoInsertado);
                    prepararSentenciaInsert.setInt(3, cantidad);
                    prepararSentenciaInsert.setFloat(4, total);
                    prepararSentenciaInsert.executeUpdate();
                    respuesta = (filasAfectadas == 1) ? Constantes.OPERACION_EXITOSA : Constantes.ERROR_CONSULTA;
                }
            }
            conexionBD.close();
        } catch (SQLException e) {
            e.printStackTrace();
            respuesta = Constantes.ERROR_CONSULTA;
        }
    } else {
        respuesta = Constantes.ERROR_CONEXION;
    }
    return respuesta;
}
    
    public static int modificarPedido(Pedido pedidoNuevo, List<Producto> productosRegistrados){
        int respuesta = Constantes.ERROR_CONSULTA;
    Connection conexionBD = ConexionBD.abrirConexionBD();
    if (conexionBD != null) {
        try {
            String sentencia = "INSERT INTO Pedido (fechaPedido, total, rastreo, fechaEnvio, direccion, Proveedor_idProveedor, Sede_idSede) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);
            prepararSentencia.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            prepararSentencia.setFloat(2, pedidoNuevo.getTotal());
            prepararSentencia.setString(3, "Preparando");
            prepararSentencia.setString(4, pedidoNuevo.getFechaEnvio());
            prepararSentencia.setString(5, pedidoNuevo.getDireccion());
            prepararSentencia.setInt(6, pedidoNuevo.getIdProveedor());
            prepararSentencia.setInt(7, pedidoNuevo.getIdSede());
            
            int filasAfectadas = prepararSentencia.executeUpdate(); 
            
            int idPedidoInsertado = -1;
            ResultSet generatedKeys = prepararSentencia.getGeneratedKeys();
            if (generatedKeys.next()) {
                idPedidoInsertado = generatedKeys.getInt(1);
            } 
   
            if (filasAfectadas >= 1 && idPedidoInsertado != -1) {
                
                for (Producto item : productosRegistrados) {
                    String sentenciaInsert = "INSERT INTO Producto_Pedido (Producto_idProducto, Pedido_idPedido, cantidad, total) VALUES (?, ?, ?, ?)";
                    PreparedStatement prepararSentenciaInsert = conexionBD.prepareStatement(sentenciaInsert);
                    
                    int cantidad = item.getCantidad();
                    float total = item.getPrecioTotal();
                    
                    prepararSentenciaInsert.setInt(1, item.getIdProducto());
                    prepararSentenciaInsert.setInt(2, idPedidoInsertado);
                    prepararSentenciaInsert.setInt(3, cantidad);
                    prepararSentenciaInsert.setFloat(4, total);
                    prepararSentenciaInsert.executeUpdate();
                    respuesta = (filasAfectadas == 1) ? Constantes.OPERACION_EXITOSA : Constantes.ERROR_CONSULTA;
                }
            }
            conexionBD.close();
        } catch (SQLException e) {
            e.printStackTrace();
            respuesta = Constantes.ERROR_CONSULTA;
        }
    } else {
        respuesta = Constantes.ERROR_CONEXION;
    }
    return respuesta;
}
    
    public static Pedido informacionCompletaPedidos(){
        Pedido respuesta = new Pedido();
        respuesta.setCodigoRespuesta(Constantes.OPERACION_EXITOSA);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
                String consulta = "SELECT idPedido, fechaPedido, total, rastreo, fechaEnvio, Sede.direccion AS direccion, Pedido.Proveedor_idProveedor AS idProveedor, Proveedor.nombre AS 'nombreProveedor', Pedido.Sede_idSede AS 'idSede', Sede.nombre AS 'nombreSede' "
                        + "FROM Pedido INNER JOIN Proveedor ON Pedido.Proveedor_idProveedor = Proveedor.idProveedor "
                        + "INNER JOIN Sede ON Pedido.Sede_idSede = Sede.idSede WHERE rastreo <> 'Cancelado';";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Pedido> pedidoConsulta = new ArrayList();
                while(resultado.next()){
                    Pedido pedido = new Pedido();
                    pedido.setIdPedido(resultado.getInt("idPedido"));
                    pedido.setFechaPedido(resultado.getString("fechaPedido"));
                    pedido.setTotal(resultado.getFloat("total"));
                    pedido.setRastreo(resultado.getString("rastreo"));
                    pedido.setFechaEnvio(resultado.getString("fechaEnvio"));
                    pedido.setDireccion(resultado.getString("direccion"));
                    pedido.setIdProveedor(resultado.getInt("idProveedor"));
                    pedido.setNombreProveedor(resultado.getString("nombreProveedor"));
                    pedido.setIdSede(resultado.getInt("idSede"));
                    pedido.setNombreSede(resultado.getString("nombreSede"));
                    pedidoConsulta.add(pedido);
                }
                respuesta.setPedidos(pedidoConsulta);
                conexionBD.close();
            }catch (SQLException e){
                respuesta.setCodigoRespuesta(Constantes.ERROR_CONSULTA);
            }
        }else{
            respuesta.setCodigoRespuesta(Constantes.ERROR_CONEXION);
        }
        return respuesta;
    }
}
