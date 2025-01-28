package com.serratec.menu;

import com.serratec.ListaClasse.ListaPedido;
import com.serratec.classes.Pedido;
import com.serratec.conexao.Connect;
import com.serratec.constantes.Util;
import com.serratec.dao.PedidoDAO;
import com.serratec.dao.Prod_PedidoDAO;
import com.serratec.dml.PedidoDML;
import com.serratec.dml.Prod_PedidoDML;

public class MenuPedido {
	public static int menu() {

		Util.escrever(Util.LINHAD);
		Util.escrever("Menu Pedido");
		Util.escrever(Util.LINHAD);
		Util.escrever("1- Novo Pedido");
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
				alterar();
				break;
			case 3:
				excluir();
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
				Util.escrever("Opcao invalida");
				Util.aperteEnter();
				return opcoes(menu());
		}
		return opcao;
	}

	public static int cadastrar() {

		Pedido p = Pedido.cadastrarPedido();

		try {
			PedidoDML.gravarPedido(Connect.getCon(), Connect.dadosCon.getSchema(), p);
		} catch (Exception e) {
			e.printStackTrace();
		}

		p.setIdPedido(PedidoDAO.retornarIdPedido(Connect.dadosCon.getSchema()));
		Connect.pedidos.adicionarPedidoLista(p);

		if (!(p == null)) {
			Prod_PedidoDML.gravarProdutoPedido(Connect.getCon(), Connect.dadosCon.getSchema(), p);
		}

		Util.aperteEnter();

		return opcoes(menu());
	}

	public static int alterar() {

		System.out.println("Insira o CODIGO do pedido a ser alterado: ");

		Pedido p = ListaPedido.localizarPedido();
		if (!(p == null)) {

			String formatarTamanho = String.format("%-7s | %-13s | %-13s | %-5s", p.getCdPedido(), p.getDtPedido(),
					p.getCliente().getNome(), p.getEmpresa().getNome());
			Util.escrever("\nPEDIDO SELECIONADO: \n");
			Util.escrever("\nID \t| DATA \t\t |CLIENTE \t| EMPRESA: \n");
			System.out.println(formatarTamanho);

			// Pedido.alterarPedido(p);

			Prod_PedidoDAO.carregarProd_Pedido(p);
			Prod_PedidoDAO prod_PedidoDAO = new Prod_PedidoDAO(Connect.getCon(), Connect.dadosCon.getSchema());
			prod_PedidoDAO.alterarProd_Pedido();

			Util.aperteEnter();
			return opcoes(menu());

		}
		return opcoes(menu());
	}

	public static int excluir() {
		System.out.println("digite o codigo do pedido: ");

		Pedido p = ListaPedido.localizarPedido();

		Connect.pedidos.excluirPedido(p);

		Prod_PedidoDML.excluirProd_Pedido(Connect.con, Connect.dadosCon.getSchema(), p);
		PedidoDML.excluirPedido(Connect.con, Connect.dadosCon.getSchema(), p);

		Util.aperteEnter();
		return opcoes(menu());
	}

	public static int listar() {
		ListaPedido.imprimirPedido();
		PedidoDAO pedidoDAO = new PedidoDAO(Connect.getCon(), Connect.dadosCon.getSchema());
		pedidoDAO.exibirPedidosCodigo();

		Util.aperteEnter();
		return opcoes(menu());
	}
}