package com.serratec.ListaClasse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.serratec.classes.Produto;
import com.serratec.conexao.Conexao;
import com.serratec.dao.ProdutoDAO;
import com.serratec.dml.ProdutoDML;

public class ListaProduto {

	private Conexao con;
	private String schema;

	public static ArrayList<Produto> produtos = new ArrayList<>();

	public ListaProduto(Conexao con, String schema) {
		this.con = con;
		this.schema = schema;

		carregarListaProdutos();
	}

	private Produto dadosProduto(ResultSet tabela) {
		Produto p = new Produto();

		try {
			p.setCdProduto(tabela.getLong("cdproduto"));
			p.setNome(tabela.getString("nome"));
			p.setDescricao(tabela.getString("descricao"));
			p.setValorUnit(tabela.getDouble("valorunit"));
			p.setPorcento(tabela.getDouble("porcento"));
			p.somarValor(tabela.getDouble("valorvenda"));
			p.setIdProduto(tabela.getLong("idproduto"));

			return p;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void carregarListaProdutos() {
		ProdutoDAO produtoDAO = new ProdutoDAO(con, schema);
		ResultSet tabela = produtoDAO.carregarProdutos();
		ListaProduto.produtos.clear();

		try {
			tabela.beforeFirst();

			while (tabela.next()) {
				ListaProduto.produtos.add(dadosProduto(tabela));
			}

			tabela.close();

		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	public void adicionarProdutoLista(Produto prod) {
		ListaProduto.produtos.add(prod);
	}

	public static Produto localizarProduto() {
		Produto localizado = null;
		Long cdprod;
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		cdprod = input.nextLong();

		for (Produto prod : produtos) {
			if (prod.getCdProduto() == cdprod) {
				localizado = prod;
				break;
			}
		}
		return localizado;
	}

	public static Produto localizarProdutoId(Long id) {
		Produto localizado = null;

		for (Produto p : produtos) {
			if (p.getIdProduto() == id) {
				localizado = p;
				break;
			}
		}
		return localizado;
	}

	public static Produto localizarProdutoCod(int i) {
		Produto localizado = null;

		for (Produto p : produtos) {
			if (p.getCdProduto() == i) {
				localizado = p;
				break;
			}
		}
		return localizado;
	}

	public boolean excluirProduto(Produto prodExcluir) {

		boolean excluido = false;
		for (Produto pd : produtos) {
			if (pd.getIdProduto() == prodExcluir.getIdProduto()) {
				produtos.remove(produtos.lastIndexOf(pd));
				ProdutoDML.excluirProduto(con, schema, prodExcluir);
				excluido = true;
				break;
			}
		}
		return excluido;
	}

	public static void imprimirProdutos() {

		System.out.println("==================");
		System.out.println("LISTA DE PRODUTOS:");
		System.out.println("==================");
		System.out.print(
				"-------------------------------------------------------------------------------------------------------");
		System.out.println("\nCODIGO\t| NOME\t\t| DESCRICAO\t\t| VL. UNIT\t| PORC. DE LUCRO\t| VL. VENDA");
		System.out.println(
				"-------------------------------------------------------------------------------------------------------");
		for (Produto p : produtos) {
			String formatarTamanho = String.format("%-7s | %-13s | %-21s | %-13s | %-21s | %2s", p.getCdProduto(),
					p.getNome(), p.getDescricao(), p.getValorUnit(), p.getPorcento(), p.getValorVenda());
			System.out.println(formatarTamanho);
		}
		System.out.println(
				"-------------------------------------------------------------------------------------------------------");
	}

}
