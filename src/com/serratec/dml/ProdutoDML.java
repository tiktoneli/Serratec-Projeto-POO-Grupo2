package com.serratec.dml;

import com.serratec.classes.Produto;
import com.serratec.conexao.Conexao;
import com.serratec.dao.ProdutoDAO;

public class ProdutoDML {

	public static void gravarProduto(Conexao con, String schema, Produto produto) {
		ProdutoDAO produtoDAO = new ProdutoDAO(con, schema);
		produtoDAO.incluirProduto(produto);
	}

	public static void alterarProduto(Conexao con, String schema, Produto produto) {
		ProdutoDAO produtoDAO = new ProdutoDAO(con, schema);
		produtoDAO.alterarProduto(produto);
	}

	public static void excluirProduto(Conexao con, String schema, Produto produto) {
		ProdutoDAO produtoDAO = new ProdutoDAO(con, schema);
		produtoDAO.excluirProduto(produto);
	}
}
