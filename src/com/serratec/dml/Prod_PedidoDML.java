package com.serratec.dml;

import com.serratec.classes.Pedido;
import com.serratec.conexao.Conexao;
import com.serratec.dao.Prod_PedidoDAO;

public class Prod_PedidoDML {

	public static void gravarProdutoPedido(Conexao con, String schema, Pedido ped) {
		Prod_PedidoDAO pdao = new Prod_PedidoDAO(con, schema);
		pdao.incluirProd_Pedido(ped);

	}

	public static void excluirProd_Pedido(Conexao con, String schema, Pedido p) {

		Prod_PedidoDAO pdao = new Prod_PedidoDAO(con, schema);

		pdao.excluirProd_Pedido(p);

	}
}
