package com.serratec.ListaClasse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import com.serratec.classes.Cliente;
import com.serratec.conexao.Conexao;
import com.serratec.dao.ClienteDAO;
import com.serratec.dml.ClienteDML;

public class ListaCliente {
	private static Conexao con;
	private static String schema;

	static ArrayList<Cliente> clientes = new ArrayList<>();

	public ListaCliente(Conexao con, String schema) {
		ListaCliente.con = con;
		ListaCliente.schema = schema;

		// ao criar um objeto ListaCliente novo, os dados do
		// banco de dados são carregados nele via carregarListaClientes()

		carregarListaClientes();
	}

	private Cliente dadosCliente(ResultSet tabela) {
		Cliente c = new Cliente();

		try {
			c.setNome(tabela.getString("nome"));
			c.setCpf_cnpj(tabela.getString("cpf"));
			c.setEndereco(tabela.getString("endereco"));
			c.setTelefone(tabela.getString("telefone"));
			c.setEmail(tabela.getString("email"));
			String dtnasc = tabela.getString("dtnasc");
			c.setDtnasc(LocalDate.parse(dtnasc));
			c.setIdCliente(tabela.getInt("idcliente"));

			return c;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void carregarListaClientes() {
		ClienteDAO cdao = new ClienteDAO(con, schema);

		// resultset é um tipo do java que guarda informações puxadas de uma query sql
		// nesse caso o cdao.carregarclientes faz um select de todos os clientes, o
		// resultado desse
		// select é armazenado no novo resultset chamado tabela

		ResultSet tabela = cdao.carregarClientes();

		// depois de ter os dados salvos no result set, limpamos o arraylist atual por
		// precaução
		// para então, alimentarmos esse array novamente, do zero

		ListaCliente.clientes.clear();

		try {

			// enquanto tiver uma linha com dados presente no result set, guarde essa
			// linha no arraylist que foi limpo logo acima

			tabela.beforeFirst();

			while (tabela.next()) {
				ListaCliente.clientes.add(dadosCliente(tabela));
			}

			tabela.close();

		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	public static Cliente localizarClienteId(Long id) {
		Cliente localizado = null;

		for (Cliente c : clientes) {
			if (c.getIdCliente() == id) {
				localizado = c;
				break;
			}
		}
		return localizado;
	}

	// Verificação para ver se já existe o CPF cadastrado no bd

	public static Cliente localizarClienteCPF(String cpf) {
		Cliente localizado = null;

		for (Cliente c : clientes) {
			if (c.getCpf_cnpj().equals(cpf)) {
				localizado = c;
				break;
			}
		}
		return localizado;
	}

	public static Cliente localizarCliente() {
		Cliente localizado = null;
		String cpfcliente;
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		cpfcliente = input.nextLine();

		// para cada Cliente, varrer o arraylist cliente, o mesmo array criado no
		// connect
		// caso seja igual

		for (Cliente c : clientes) {
			if (c.getCpf_cnpj().equals(cpfcliente)) {
				localizado = c;
				break;
			}
		}
		return localizado;
	}

	public void adicionarClienteLista(Cliente c) {
		ListaCliente.clientes.add(c);
	}

	public static boolean excluirCliente(Cliente c) {

		boolean excluido = false;
		for (Cliente cl : clientes) {
			if (cl.getIdCliente() == c.getIdCliente()) {

				// remover o cliente tanto do arraylist, como também no banco de dados

				clientes.remove(clientes.lastIndexOf(cl));
				ClienteDML.excluirCliente(con, schema, c);

				excluido = true;
				break;
			}
		}
		return excluido;
	}

	public void imprimirClientes() {
		System.out.println("==================");
		System.out.println("LISTA DE CLIENTES:");
		System.out.println("==================");
		System.out.print("--------------------------------------------------------");
		System.out.println("\nNOME\t\t| CPF\t\t  | DATA DE NASCIMENTO");
		System.out.println("--------------------------------------------------------");

		for (Cliente c : clientes) {
			String formatarTamanho = String.format("%-15s | %-15s | %-17s", c.getNome(), c.getCpf_cnpj(),
					c.getDtnasc());
			System.out.println(formatarTamanho);
		}
	}

}