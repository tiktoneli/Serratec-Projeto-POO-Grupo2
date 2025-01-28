package com.serratec.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.serratec.classes.Produto;
import com.serratec.conexao.Conexao;
import com.serratec.conexao.Connect;

public class ProdutoDAO {
	private Conexao conexao;
	private String schema;

	PreparedStatement pInclusao;
	PreparedStatement pAlteracao;
	PreparedStatement pExclusao;

	public ProdutoDAO(Conexao conexao, String schema) {
		this.conexao = conexao;
		this.schema = schema;
		prepararSqlInclusao();
		prepararSqlAlteracao();
		prepararSqlExclusao();
	}

	private void prepararSqlExclusao() {
		String sql = "delete from " + this.schema + ".produto";
		sql += " where idproduto = ?";

		try {
			this.pExclusao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	private void prepararSqlInclusao() {
		String sql = "insert into " + this.schema + ".produto";
		sql += " (cdproduto, nome, descricao, valorunit, porcento, valorvenda)";
		sql += " values ";
		sql += " (?, ?, ?, ?, ?, ?)";

		try {
			this.pInclusao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	private void prepararSqlAlteracao() {
		String sql = "update " + this.schema + ".produto";
		sql += " set cdproduto = ?,";
		sql += " nome = ?,";
		sql += " descricao = ?,";
		sql += " valorunit = ?,";
		sql += " porcento = ?,";
		sql += " valorvenda = ?";
		sql += " where idproduto = ?";

		try {
			this.pAlteracao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	public int alterarProduto(Produto produto) {
		try {
			pAlteracao.setLong(1, produto.getCdProduto());
			pAlteracao.setString(2, produto.getNome());
			pAlteracao.setString(3, produto.getDescricao());
			pAlteracao.setDouble(4, produto.getValorUnit());
			pAlteracao.setDouble(5, produto.getPorcento());
			pAlteracao.setDouble(6, produto.getValorVenda());
			pAlteracao.setLong(7, produto.getIdProduto());

			return pAlteracao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nProduto não alterado.\nVerifique se foi chamada a conexão:\n" + e);
			} else {
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}

	public int incluirProduto(Produto produto) {
		try {
			pInclusao.setLong(1, produto.getCdProduto());
			pInclusao.setString(2, produto.getNome());
			pInclusao.setString(3, produto.getDescricao());
			pInclusao.setDouble(4, produto.getValorUnit());
			pInclusao.setDouble(5, produto.getPorcento());
			pInclusao.setDouble(6, produto.getValorVenda());

			return pInclusao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nProduto não incluído.\nVerifique se foi chamada a conexão:\n" + e);
			} else {
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}

	public int excluirProduto(Produto produto) {
		try {
			pExclusao.setLong(1, produto.getIdProduto());

			return pExclusao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nProduto não excluído.\nVerifique se foi chamada a conexão:\n" + e);
			} else {
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}

	public ResultSet carregarProdutos() {
		ResultSet tabela;
		String sql = "select * from " + this.schema + ".produto order by idproduto";

		tabela = conexao.query(sql);

		return tabela;
	}

	public static int retornarIdProduto(String schema) {
		String sql = "select idproduto from " + schema + ".produto" + " order by idproduto desc limit 1";

		try {
			ResultSet tabela = Connect.con.query(sql);
			if (tabela.next()) {
				return tabela.getInt("idproduto");
			} else {
				return 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public void exibirProdutosPedidos() {
		Produto p = new Produto();
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		System.out.println("Informe o codigo do produto :");
		p.setCdProduto(input.nextLong());
		ResultSet tabela;
		String sql = "Select  * from " + this.schema + ".produto_pedido pt\n"
				+ "\n"
				+ "\n"
				+ "join " + this.schema + ".pedido pd on pt.idpedido = pd.idpedido\n"
				+ "join " + this.schema + ".produto pp on pt.idproduto = pp.idproduto\n"
				+ "join " + this.schema + ".empresa emp on emp.idempresa = pd.idempresa\n"
				+ "where pt.idproduto = " + p.getCdProduto();
		tabela = conexao.query(sql);

		try {

			while (tabela.next()) {

				int pedidos = tabela.getInt("codigo");

				System.out.println("Codigo dos Pedidos que possui o produto: " + pedidos);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}