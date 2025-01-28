package com.serratec.menu;

import com.serratec.ListaClasse.ListaCliente;
import com.serratec.ListaClasse.ListaEmpresa;
import com.serratec.ListaClasse.ListaProduto;
import com.serratec.conexao.Connect;
import com.serratec.constantes.Util;
import com.serratec.dao.CreateDAO;

public class MenuPrincipal {

	// esse menu principal é chamado depois que são checados todos os dados de
	// conexão
	// e depois que todos os arrays são criados (vide classe connect)

	public static int menuPrincipal() {

		Util.escrever(Util.LINHAD);
		Util.escrever(Util.CABECALHO);
		Util.escrever(Util.LINHAD);
		Util.br();
		Util.escrever("Menu Principal");
		Util.escrever(Util.LINHA);
		Util.escrever("1- Cliente");
		Util.escrever("2- Empresa");
		Util.escrever("3- Produto");
		Util.escrever("4- Pedido");
		Util.escrever("5- Sair");
		Util.escrever("6- Alimentar dados de exemplo");
		Util.escrever(Util.LINHA);

		return Util.validarInteiro("Informe uma opcao: ");
	}

	public static int opcoes(int opcao) {

		switch (opcao) {
			case 1:
				int opcaoMenuCliente = MenuCliente.menu();
				return MenuCliente.opcoes(opcaoMenuCliente);
			case 2:
				int opcaoMenuEmpresa = MenuEmpresa.menu();
				return MenuEmpresa.opcoes(opcaoMenuEmpresa);
			case 3:
				int opcaoMenuProduto = MenuProduto.menu();
				return MenuProduto.opcoes(opcaoMenuProduto);
			case 4:
				int opcaoMenuPedido = MenuPedido.menu();
				return MenuPedido.opcoes(opcaoMenuPedido);
			case 5:
				Util.escrever("Sistema Finalizado!");
				break;

			case 6:
				if (ListaCliente.localizarClienteId(1l) == null || ListaEmpresa.localizarEmpresaId(1l) == null
						|| ListaProduto.localizarProdutoId(1l) == null) {
					CreateDAO.alimentaTabelaCliente(Connect.con, Connect.dadosCon.getSchema());
					CreateDAO.alimentaTabelaEmpresa(Connect.con, Connect.dadosCon.getSchema());
					CreateDAO.alimentaTabelaProduto(Connect.con, Connect.dadosCon.getSchema());

					Connect.dadosEntrada();
				} else {
					System.err.println("Banco de dados não está vazio! ");
					Util.aperteEnter();
				}

				return opcoes(menuPrincipal());

			default:
				Util.escrever("Opcao invalida");
				Util.aperteEnter();
				return opcoes(menuPrincipal());
		}
		return opcao;
	}
}
