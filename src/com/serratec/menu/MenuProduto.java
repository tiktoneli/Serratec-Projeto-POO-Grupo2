package com.serratec.menu;

import com.serratec.ListaClasse.ListaProduto;
import com.serratec.classes.Produto;
import com.serratec.conexao.Connect;
import com.serratec.constantes.Util;
import com.serratec.dao.ProdutoDAO;
import com.serratec.dml.ProdutoDML;

public class MenuProduto {

	public static int menu() {

		Util.escrever(Util.LINHAD);
		Util.escrever("Menu Produto");
		Util.escrever(Util.LINHAD);
		Util.escrever("1- Cadastrar");
		Util.escrever("2- Alterar");
		Util.escrever("3- Excluir");
		Util.escrever("4- Listar");
		Util.escrever("5- Voltar");
		Util.escrever("6- Sair");
		Util.escrever(Util.LINHA);

		return Util.validarInteiro("Informe uma opcao: ");
	}

	public static int opcoes(int opcao) {

		switch (opcao) {
			case 1:
				cadastrar();
				break;
			case 2:
				alterar("Alteração do produto! Insira o código do produto a ser alterado.");
				break;
			case 3:
				excluir("Exclusão do produto! Insira o código do produto a ser excluído.");
				break;
			case 4:
				listar();
				break;
			case 5:
				int opcaoMenuPrincipal = MenuPrincipal.menuPrincipal();
				return MenuPrincipal.opcoes(opcaoMenuPrincipal);
			case 6:
				Util.escrever("Sistema Finalizado!");
				break;
			default:
				Util.escrever("Opcao inválida");
				Util.aperteEnter();
				return opcoes(menu());
		}
		return opcao;
	}

	public static void cadastrar() {

		Produto produto = Produto.cadastrarProduto();
		ProdutoDML.gravarProduto(Connect.getCon(), Connect.dadosCon.getSchema(), produto);
		produto.setIdProduto(ProdutoDAO.retornarIdProduto(Connect.dadosCon.getSchema()));

		Connect.produtos.adicionarProdutoLista(produto);
		opcoes(menu());
	}

	public static void alterar(String msg) {
		System.out.println(msg);

		Produto p = ListaProduto.localizarProduto();

		if (!(p == null)) {
			Produto.alterarProduto(p);
			ProdutoDML.alterarProduto(Connect.getCon(), Connect.dadosCon.getSchema(), p);
		} else
			System.out.println("Produto não encontrado!");
		Util.aperteEnter();
		opcoes(menu());
	}

	public static void excluir(String msg) {

		System.out.println(msg);
		if (Connect.produtos.excluirProduto(ListaProduto.localizarProduto())) {
			System.out.println("Produto excluído com sucesso!");
			Util.aperteEnter();
		} else {
			System.out.println("Produto não encontrado, retornando ao menu.");
			Util.aperteEnter();
		}

		opcoes(menu());
	}

	public static int listar() {
		ListaProduto.imprimirProdutos();

		ProdutoDAO produtoDAO = new ProdutoDAO(Connect.getCon(), Connect.dadosCon.getSchema());
		produtoDAO.exibirProdutosPedidos();

		Util.aperteEnter();
		return opcoes(menu());
	}

}