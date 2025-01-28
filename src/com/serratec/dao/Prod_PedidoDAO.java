package com.serratec.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import com.serratec.classes.Pedido;
import com.serratec.classes.Produto;
import com.serratec.conexao.Conexao;
import com.serratec.conexao.Connect;

public class Prod_PedidoDAO {

	private Conexao conexao;
	private String schema;

	PreparedStatement pInclusao;
	PreparedStatement pAlteracao;
	PreparedStatement pExclusao;

	public Prod_PedidoDAO(Conexao conexao, String schema) {
		this.conexao = conexao;
		this.schema = schema;
		prepararSqlInclusao();
		prepararSqlAlteracao();
		prepararSqlExclusao();
	}

	private void prepararSqlExclusao() {
		String sql = "delete from " + this.schema + ".produto_pedido";
		sql += " where idpedido = ?";

		// update + nome da tabela + set + coluna = valor where codigo = 2 and

		try {
			this.pExclusao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	private void prepararSqlInclusao() {
		String sql = "insert into " + this.schema + ".produto_pedido";
		sql += " (idproduto, quantidade, idpedido)";
		sql += " values ";
		sql += " (?, ?, ?)";

		try {
			this.pInclusao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	private void prepararSqlAlteracao() {
		String sql = "update " + this.schema + ".produto_pedido";
		sql += " set idproduto = ?,";
		sql += " quantidade = ?";
		sql += " where idpedido = ? and idproduto = ?";

		try {
			this.pAlteracao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	public int alterarProd_Pedido() {
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		System.out.println("Codigo do produto que deseja alterar");
		long cod = input.nextLong();

		System.out.println("Escreva a quantidade: ");
		int quant = input.nextInt();
		System.out.println("Confirme o codigo do pedido para continuar! : ");
		int cd = input.nextInt();

		try {
			pAlteracao.setLong(1, cod);
			pAlteracao.setInt(2, quant);
			pAlteracao.setInt(3, cd);
			pAlteracao.setLong(4, cod);
			return pAlteracao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nLista nÃ£o alterada.\nVerifique se foi chamada a conexÃ£o:\n" + e);
			} else {
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}

	public int incluirProd_Pedido(Pedido ped) {
		try {
			int salvo = 0;

			for (Produto produto : ped.getProdutos()) {

				pInclusao.setLong(1, ped.getProdutos().get(ped.getProdutos().lastIndexOf(produto)).getIdProduto());
				pInclusao.setInt(2, ped.getProdutos().get(ped.getProdutos().lastIndexOf(produto)).getQuantidade());
				pInclusao.setLong(3, ped.getIdPedido());

				salvo = pInclusao.executeUpdate();
			}
			ped.getProdutos().clear();
			return salvo;

		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nLista não incluída.\nVerifique se foi chamada a conexão:\n" + e);
			} else {
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}

	public int excluirProd_Pedido(Pedido p) {
		try {
			pExclusao.setLong(1, p.getIdPedido());

			return pExclusao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nLista não excluída.\nVerifique se foi chamada a conexão:\n" + e);
			} else {
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}

	public static void carregarProd_Pedido(Pedido p) {
		double total = 0.00;
		ResultSet tabela;
		String sql = "select prod.cdproduto, prod.nome, prod.valorvenda, pp.quantidade " +
				"from " + Connect.dadosCon.getSchema() + ".produto as prod " +
				"left join " + Connect.dadosCon.getSchema() + ".produto_pedido as pp on pp.idproduto = prod.idproduto "
				+
				"where pp.idpedido = " + p.getIdPedido();

		tabela = Connect.getCon().query(sql);

		try {
			while (tabela.next()) {

				String cdProduto = tabela.getString("cdproduto");
				String nome = tabela.getString("nome");
				double valorVenda = tabela.getDouble("valorvenda");
				int quantidade = tabela.getInt("quantidade");
				String formatado = String.format("%-15s | %-13s | %-13s | %-13s | %-10s", cdProduto, nome, valorVenda,
						quantidade, valorVenda * quantidade);

				System.out.println(formatado);

				total += valorVenda * quantidade;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\nTotal do pedido: R$ " + total);
	}

	public ResultSet carregarItemPedido(Pedido p) {
		ResultSet tabela;
		String sql = "select prod.cdproduto, pp.quantidade " +
				"from " + Connect.dadosCon.getSchema() + ".produto as prod " +
				"left join " + Connect.dadosCon.getSchema() + ".produto_pedido as pp on pp.idproduto = prod.idproduto "
				+
				"where pp.idpedido = " + p.getIdPedido();

		tabela = conexao.query(sql);

		return tabela;
	}

}
