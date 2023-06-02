package jfxgestionfarmacia.modelo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jfxgestionfarmacia.modelo.ConexionBD;
import jfxgestionfarmacia.modelo.pojo.ProductoPedido;
import jfxgestionfarmacia.utils.Constantes;

public class ProductoPedidoDAO {
    public static ProductoPedido obtenerProductosPedidos2(int idPedido){
        ProductoPedido respuesta = new ProductoPedido();
        respuesta.setCodigoRespuesta(Constantes.OPERACION_EXITOSA);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
                String consulta = "SELECT Producto_idProducto as idProducto, Pedido_idPedido as idPedido, cantidad, total, Producto.nombre as nombreProducto FROM Producto_Pedido INNER JOIN Producto ON Producto_Pedido.Producto_idProducto = Producto.idProducto WHERE Producto_Pedido.Pedido_idPedido = ?";
            PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
            prepararSentencia.setInt(1, idPedido); 
            ResultSet resultado = prepararSentencia.executeQuery();
            ArrayList<ProductoPedido> pedidoConsulta = new ArrayList<>();
            while (resultado.next()) {
                ProductoPedido productoPedido = new ProductoPedido();
                productoPedido.setIdProducto(resultado.getInt("idProducto"));
                productoPedido.setIdPedido(resultado.getInt("idPedido"));
                productoPedido.setCantidad(resultado.getInt("cantidad"));
                productoPedido.setTotal(resultado.getFloat("total"));
                productoPedido.setNombreProducto(resultado.getString("nombreProducto"));
                pedidoConsulta.add(productoPedido);
                }
                respuesta.setProductosPedidos(pedidoConsulta);
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
