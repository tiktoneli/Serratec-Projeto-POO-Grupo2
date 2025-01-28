package com.serratec.menu;

import com.serratec.classes.Cliente;
import com.serratec.conexao.Connect;
import com.serratec.constantes.Util;
import com.serratec.dao.ClienteDAO;
import com.serratec.dml.ClienteDML;
import java.util.Scanner;
import com.serratec.ListaClasse.ListaCliente;

public class MenuCliente {

	public static int menu() {

		Util.escrever(Util.LINHAD);
		Util.escrever("Menu Cliente");
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
				alterar("Alteração de cliente - insira o CPF do cliente a ser alterado: ");
				break;
			case 3:
				excluir("Exclusão de cliente - insira o CPF do cliente a ser excluido: ");
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

		// método cadastrarcliente retorna um novo cliente

		Cliente c = Cliente.cadastrarCliente();

		// método gravarcliente chama o clienteDAO, que escreve esse novo cliente em um
		// query pro banco de dados

		ClienteDML.gravarCliente(Connect.getCon(), Connect.dadosCon.getSchema(), c);
		c.setIdCliente(ClienteDAO.retornarIdCliente(Connect.dadosCon.getSchema()));

		// aqui adicionamos o novo cliente ao array do java, para excluir, imprimir,
		// alterar, entre outras coisas
		// puxamos os array criados na classe connect, pois é onde criamos esses arrays
		// ao abrir o nosso aplicativo

		Connect.clientes.adicionarClienteLista(c);

		// depois de criar o cliente, incluir no banco de dados e adicionar no array,
		// voltamos ao menu

		Util.aperteEnter();
		return opcoes(menu());
	}

	public static int alterar(String msg) {
		System.out.println(msg);

		// primeiro localizamos o cliente no array do java, o mesmo criado no connect,
		// quando abrimos o aplicativo

		Cliente c = ListaCliente.localizarCliente();
		if (!(c == null)) {

			// só alterar caso o cliente localizado não seja nulo, via if else
			// ao alterar um cliente existente, não precisamos atualizar a lista java,
			// mas precisamos atualizar o banco de dados com o clienteDML

			Cliente.alterarCliente(c);
			ClienteDML.alterarCliente(Connect.getCon(), Connect.dadosCon.getSchema(), c);

		} else {
			System.out.println("Cliente não encontrado, retornando ao menu.");
		}
		Util.aperteEnter();
		return opcoes(menu());
	}

	public static int excluir(String msg) {
		System.out.println(msg);
		if (ListaCliente.excluirCliente(ListaCliente.localizarCliente())) {
			System.out.println("Cliente excluído com sucesso!");

			// mesmo procedimento do alterar, com a diferença que o método
			// excluircliente (que recebe o cliente localizado como parâmetro)
			// retorna um valor boolean que pode ser usado como condição do if else

		} else {
			System.out.println("Cliente não encontrado, retornando ao menu.");
		}
		Util.aperteEnter();
		return opcoes(menu());
	}

	@SuppressWarnings("resource")
	public static int listar() {

		// a listagem de clientes exibe todos os clientes que estão
		// atualmente salvos no array criado no início do aplicativo

		@SuppressWarnings("unused")
		Scanner in = new Scanner(System.in);
		Connect.clientes.imprimirClientes();

		ClienteDAO clienteDAO = new ClienteDAO(Connect.getCon(), Connect.dadosCon.getSchema());
		clienteDAO.exibirClientesPedidos();

		Util.aperteEnter();
		return opcoes(menu());

	}
}