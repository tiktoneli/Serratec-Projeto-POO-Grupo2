package com.serratec.ListaClasse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import com.serratec.classes.Pedido;
import com.serratec.classes.Pedido.Itens;
import com.serratec.conexao.Conexao;
import com.serratec.conexao.Connect;
import com.serratec.constantes.Util;
import com.serratec.dao.PedidoDAO;
import com.serratec.dao.Prod_PedidoDAO;

public class ListaPedido {
	private Conexao con;
	private String schema;

	public static ArrayList<Pedido> pedidos = new ArrayList<>();

	public ListaPedido(Conexao con, String schema) {
		this.con = con;
		this.schema = schema;
		this.carregarListaPedidos();
		// this.carregarListaItens();
	}

	private Pedido dadosPedido(ResultSet tabela) {
		Pedido p = new Pedido();

		try {
			p.setCdPedido(tabela.getLong("codigo"));
			String datapedido = tabela.getString("datapedido");
			p.setDtPedido(LocalDate.parse(datapedido));
			p.setCliente(ListaCliente.localizarClienteId(tabela.getLong("idcliente")));
			p.setEmpresa(ListaEmpresa.localizarEmpresaId(tabela.getLong("idempresa")));
			p.setIdPedido(tabela.getLong("idpedido"));
			return p;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void carregarListaPedidos() {
		PedidoDAO pdao = new PedidoDAO(con, schema);
		ResultSet tabela = pdao.carregarPedido();
		ListaPedido.pedidos.clear();

		try {
			while (tabela.next()) {
				ListaPedido.pedidos.add(dadosPedido(tabela));

			}

			tabela.close();
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	public void adicionarPedidoLista(Pedido ped) {
		ListaPedido.pedidos.add(ped);
	}

	public static Pedido localizarPedido() {
		Pedido localizado = null;
		Long ped;
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		ped = input.nextLong();

		for (Pedido p : pedidos) {
			if (p.getIdPedido() == ped) {
				localizado = p;
				break;
			}
		}
		return localizado;
	}

	public static Pedido localizarIdPedido(long codigo) {

		Pedido p = null;
		for (int i = 0; i < pedidos.size(); i++) {
			if (pedidos.get(i).getIdPedido() == codigo) {
				p = pedidos.get(i);
				System.out.println(p.getIdPedido());
				return p;
			}
		}
		return null;
	}

	public boolean excluirPedido(Pedido pExcluir) {
		boolean excluido = false;
		for (Pedido p : pedidos) {
			if (p.getIdPedido() == pExcluir.getIdPedido()) {
				pedidos.remove(pedidos.lastIndexOf(p));
				// PedidoDML.excluirPedido(con, schema, pExcluir);
				excluido = true;
				break;
			}
		}
		return excluido;
	}

	public static void imprimirPedido() {
		System.out.println("====================");
		System.out.println("RELATORIO DE PEDIDOS:");
		System.out.println("====================");
		System.out.print("----------------------------------------------------------");
		System.out.println("\nNUMERO\t\t| DATA\t\t| CLIENTE\t| EMPRESA");
		System.out.println("----------------------------------------------------------");

		for (Pedido p : ListaPedido.pedidos) {
			String formatarTamanho = String.format("%-15s | %-13s | %-13s | %-5s",
					p.getCdPedido(), p.getDtPedido(), p.getCliente().getNome(),
					p.getEmpresa().getNome());
			System.out.println(formatarTamanho);
			System.out.print("-----------------------------------------------------------------------------");
			Util.escrever("\nCOD.PRODUTO\t| NOME\t\t| VALOR\t\t| QUANT.\t| TOTAL");
			System.out.println("-----------------------------------------------------------------------------");
			Prod_PedidoDAO.carregarProd_Pedido(p);
			System.out.println("-----------------------------------------------------------------------------");

		}
	}

	public static ArrayList<Itens> produtos = new ArrayList<>();

	public static void carregarProdutos(Pedido pd) {
		Prod_PedidoDAO p = new Prod_PedidoDAO(Connect.getCon(), Connect.dadosCon.getSchema());
		ResultSet tabela = p.carregarItemPedido(pd);
		ListaPedido.produtos.clear();

		try {
			while (tabela.next()) {
				ListaPedido.produtos.add(pd.dadosItens(tabela));
			}
			tabela.close();
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

}