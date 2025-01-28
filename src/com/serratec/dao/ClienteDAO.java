package com.serratec.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.serratec.classes.Cliente;
import com.serratec.conexao.Conexao;
import com.serratec.conexao.Connect;

public class ClienteDAO {
	private Conexao conexao;
	private String schema;

	PreparedStatement pInclusao;
	PreparedStatement pAlteracao;
	PreparedStatement pExclusao;
	PreparedStatement pSelecao;

	public ClienteDAO(Conexao conexao, String schema) {
		this.conexao = conexao;
		this.schema = schema;
		prepararSqlInclusao();
		prepararSqlAlteracao();
		prepararSqlExclusao();

	}

	// preparar query na memória pra otimizar o sistema

	private void prepararSqlExclusao() {
		String sql = "delete from " + this.schema + ".cliente";
		sql += " where idcliente = ?";

		try {
			this.pExclusao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	private void prepararSqlInclusao() {
		String sql = "insert into " + this.schema + ".cliente";
		sql += " (nome, cpf, endereco, telefone, email, dtnasc)";
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
		String sql = "update " + this.schema + ".cliente";
		sql += " set nome = ?,";
		sql += " cpf = ?,";
		sql += " endereco = ?,";
		sql += " telefone = ?,";
		sql += " email = ?,";
		sql += " dtnasc = ?";
		sql += " where idcliente = ?";

		try {
			this.pAlteracao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	// execução de updates

	public int alterarCliente(Cliente cliente) {
		try {
			pAlteracao.setString(1, cliente.getNome());
			pAlteracao.setString(2, cliente.getCpf_cnpj());
			pAlteracao.setString(3, cliente.getEndereco());
			pAlteracao.setString(4, cliente.getTelefone());
			pAlteracao.setString(5, cliente.getEmail());
			pAlteracao.setDate(6, Date.valueOf(cliente.getDtnasc()));
			pAlteracao.setInt(7, cliente.getIdCliente());

			return pAlteracao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nCliente nao alterado.\nVerifique se foi chamado o conect:\n" + e);
			} else {
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}

	public int incluirCliente(Cliente cliente) {
		try {

			pInclusao.setString(1, cliente.getNome());
			pInclusao.setString(2, cliente.getCpf_cnpj());
			pInclusao.setString(3, cliente.getEndereco());
			pInclusao.setString(4, cliente.getTelefone());
			pInclusao.setString(5, cliente.getEmail());
			pInclusao.setDate(6, Date.valueOf(cliente.getDtnasc()));

			return pInclusao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nCliente nao incluido.\nVerifique se foi chamado o conect:\n" + e);
			} else {
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}

	public int excluirCliente(Cliente cliente) {
		try {
			pExclusao.setInt(1, cliente.getIdCliente());

			return pExclusao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nCliente nao incluido.\nVerifique se foi chamado o conect:\n" + e);
			} else {
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}

	// select com o retorno dos resultados sendo guardados em uma tabela do tipo
	// resultset

	public ResultSet carregarClientes() {
		ResultSet tabela;
		String sql = "select * from " + this.schema + ".cliente order by idcliente";

		tabela = conexao.query(sql);

		return tabela;
	}

	public static int retornarIdCliente(String schema) {
		String sql = "select idcliente from " + schema + ".cliente" + " order by idcliente desc limit 1";

		try {
			ResultSet tabela = Connect.con.query(sql);
			if (tabela.next()) {
				return tabela.getInt("idcliente");
			} else {
				return 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("resource")
	public void exibirClientesPedidos() {
		Cliente c = new Cliente();
		Scanner input = new Scanner(System.in);
		System.out.println("\nInforme o cpf do cliente para ver seus pedidos: ");
		c.setCpf_cnpj(input.nextLine());
		ResultSet tabela;
		String sql = "SELECT * FROM " + this.schema + ".cliente cl "
				+ "JOIN " + this.schema + ".pedido pd ON cl.idcliente = pd.idcliente "
				+ "WHERE cl.cpf = '" + c.getCpf_cnpj() + "' "
				+ "ORDER BY cl.idcliente";
		tabela = conexao.query(sql);

		try {

			while (tabela.next()) {

				int idCliente = tabela.getInt("idcliente");
				String nomeCliente = tabela.getString("nome");
				int pedidos = tabela.getInt("codigo");

				System.out.println("ID do Cliente: " + idCliente);
				System.out.println("Nome do Cliente: " + nomeCliente);
				System.out.println("Codigo dos Pedidos: " + pedidos);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}