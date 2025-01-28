package com.serratec.dml;

import com.serratec.classes.Pedido;
import com.serratec.conexao.Conexao;
import com.serratec.dao.PedidoDAO;

public class PedidoDML {

	public static void gravarPedido(Conexao con, String schema, Pedido p) {

		PedidoDAO pdao = new PedidoDAO(con, schema);
		pdao.incluirPedido(p);

	}

	public static void alterarPedido(Conexao con, String schema, Pedido p) {

		PedidoDAO pdao = new PedidoDAO(con, schema);
		pdao.alterarPedido(p);
	}

	public static void excluirPedido(Conexao con, String schema, Pedido p) {

		PedidoDAO pdao = new PedidoDAO(con, schema);
		pdao.excluirPedido(p);
	}

	public static void selecionarPedido(Conexao con, String schema, Pedido p) {

		PedidoDAO pdao = new PedidoDAO(con, schema);

		pdao.selecionarPedido(p);
	}
}
