package com.serratec.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.serratec.classes.Pedido;
import com.serratec.conexao.Conexao;
import com.serratec.conexao.Connect;

public class PedidoDAO {
	private Conexao conexao;
	private String schema;

	PreparedStatement pInclusao;
	PreparedStatement pAlteracao;
	PreparedStatement pExclusao;
	PreparedStatement pLocalizacao;

	public PedidoDAO(Conexao conexao, String schema) {
		this.conexao = conexao;
		this.schema = schema;
		prepararSqlInclusao();
		prepararSqlAlteracao();
		prepararSqlExclusao();

	}

	private void prepararSqlExclusao() {
		String sql = "delete from " + this.schema + ".pedido";
		sql += " where codigo = ?";

		try {
			this.pExclusao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	private void prepararSqlInclusao() {
		String sql = "insert into " + this.schema + ".pedido";
		sql += " (codigo, datapedido, idcliente, idempresa)";
		sql += " values ";
		sql += " (?, ?, ?, ?)";

		try {
			this.pInclusao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	private void prepararSqlAlteracao() {
		String sql = "update " + this.schema + ".pedido";
		sql += " set codigo = ?,";
		sql += " datapedido = ?,";
		sql += " idcliente = ?,";
		sql += " idempresa = ?";
		sql += " where idpedido = ?";

		try {
			this.pAlteracao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	public int alterarPedido(Pedido pedido) {
		try {
			pAlteracao.setLong(1, pedido.getCdPedido());
			pAlteracao.setDate(2, Date.valueOf(pedido.getDtPedido()));
			pAlteracao.setLong(3, pedido.getCliente().getIdCliente());
			pAlteracao.setLong(4, pedido.getEmpresa().getIdEmpresa());
			pAlteracao.setLong(5, pedido.getIdPedido());

			return pAlteracao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nPedido nao alterado.\nVerifique se foi chamado o conect:\n" + e);
			} else {
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}

	public int incluirPedido(Pedido pedido) {
		try {

			pInclusao.setLong(1, pedido.getCdPedido());
			pInclusao.setDate(2, Date.valueOf(pedido.getDtPedido()));
			pInclusao.setLong(3, pedido.getCliente().getIdCliente());
			pInclusao.setLong(4, pedido.getEmpresa().getIdEmpresa());

			return pInclusao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nPedido nao incluida.\nVerifique se foi chamado o conect:\n" + e);
			} else {
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}

	public int localizarPedido(Pedido pedido) {
		try {
			pLocalizacao.setLong(1, pedido.getIdPedido());

			return pLocalizacao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nPedido nao localizado.\nVerifique se foi chamado o conect:\n" + e);
			} else {
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}

	public void selecionarPedido(Pedido p) {

		ResultSet tabela;
		String sql = "select ped.idpedido, cli.nome, prod.nome, prod.descricao from " + this.schema + ".pedido as ped "
				+ "left join " + this.schema + ".cliente as cli on cli.idcliente = ped.idcliente" + " left join "
				+ this.schema + ".produto_pedido as pp on pp.idpedido = ped.idpedido " + "left join " + this.schema
				+ ".produto as prod on prod.idproduto = pp.idproduto " + "where ped.idpedido = " + p.getIdPedido()
				+ "order by idpedido";

		tabela = conexao.query(sql);

		try {
			while (tabela.next()) {
				String idpedido = tabela.getString("idpedido");
				String cliente = tabela.getString("cliente");
				String produto = tabela.getString("produto");
				String descricao = tabela.getString("descricao");

				String formatado = String.format("%-15s | %-13s | %-13s | %-13s | %-10s", idpedido, cliente, produto,
						descricao);

				System.out.println(formatado);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public int excluirPedido(Pedido pedido) {
		try {
			pExclusao.setLong(1, pedido.getCdPedido());

			return pExclusao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nPedido nao excluido.\nVerifique se foi chamado o conect:\n" + e);
			} else {
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}

	}

	public ResultSet carregarPedido() {
		ResultSet tabela;
		String sql = "select * from " + this.schema + ".pedido order by idpedido";

		tabela = conexao.query(sql);

		return tabela;
	}

	public void relatarPedido(Pedido p) {
		ResultSet tabela;
		String sql = "select ped.codigo from " + this.schema + ".pedido as ped" + " left join " + this.schema
				+ ".produto_pedido as pp on ped.codigo = pp.codigo" + " left join " + this.schema
				+ ".produto as prod on pp.idproduto = prod.idproduto" + " left join " + this.schema
				+ ".cliente as cli on cli.idcliente = ped.idcliente" + " left join " + this.schema
				+ ".empresa as emp on emp.idempresa = ped.idempresa" + " where ped.codigo = " + p.getCdPedido()
				+ " and ped.idempresa = " + p.getEmpresa().getIdEmpresa();

		tabela = conexao.query(sql);

		try {
			while (tabela.next()) {
				System.out.print(tabela.getString("codigo") + "\t");
				System.out.print(tabela.getString("codigo") + "\t");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int retornarIdPedido(String schema) {
		String sql = "select idpedido from " + schema + ".pedido" + " order by idpedido desc limit 1";

		try {
			ResultSet tabela = Connect.con.query(sql);
			if (tabela.next()) {
				return tabela.getInt("idpedido");
			} else {
				return 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public void exibirPedidosCodigo() {
		Pedido p = new Pedido();
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		System.out.println("Informe o codigo do pedido :");
		p.setCdPedido(input.nextLong());
		ResultSet tabela;
		String sql = "Select Distinct *, pp.nome as nomeprod, cl.nome as nomecliente,pd.codigo as codigopedido, emp.nome as empnome from "
				+ this.schema + ".pedido pd\n" + "\n" + "join " + this.schema
				+ ".produto_pedido pt on pt.idpedido = pd.idpedido\n" + "join " + this.schema
				+ ".produto pp on pt.idproduto = pp.idproduto\n" + "join " + this.schema
				+ ".empresa emp on emp.idempresa = pd.idempresa\n" + "join " + this.schema
				+ ".cliente cl on cl.idcliente = pd.idcliente\n" + "where pd.codigo = '" + p.getCdPedido() + "'";
		tabela = conexao.query(sql);
		try {
			boolean informacoesExibidas = false;
			while (tabela.next()) {
				if (!informacoesExibidas) {
					int idCliente = tabela.getInt("codigopedido");
					String nomeCliente = tabela.getString("nomecliente");
					String nomeEmpresa = tabela.getString("empnome");

					System.out.println("Codigo do Pedido: " + idCliente);
					System.out.println("Nome do Cliente: " + nomeCliente);
					System.out.println("Empresa que vendeu:" + nomeEmpresa);
					System.out.println("\nProdutos que possui seu pedidos:");
					informacoesExibidas = true;
				}

				String produtos = tabela.getString("nomeprod");
				int quant = tabela.getInt("quantidade");
				System.out.println(produtos + "\tQuantidade: " + quant);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}