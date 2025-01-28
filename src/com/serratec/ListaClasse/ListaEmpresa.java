package com.serratec.ListaClasse;

import com.serratec.classes.Empresa;
import com.serratec.conexao.Conexao;
import com.serratec.dao.EmpresaDAO;
import com.serratec.dml.EmpresaDML;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ListaEmpresa {
	private static Conexao con;
	private static String schema;

	static ArrayList<Empresa> empresas = new ArrayList<>();

	public ListaEmpresa(Conexao con, String schema) {
		ListaEmpresa.con = con;
		ListaEmpresa.schema = schema;
		carregarListaEmpresas();
	}

	private Empresa dadosEmpresa(ResultSet tabela) {
		Empresa e = new Empresa();

		try {
			e.setNome(tabela.getString("nome"));
			e.setCpf_cnpj(tabela.getString("cnpj"));
			e.setEndereco(tabela.getString("endereco"));
			e.setTelefone(tabela.getString("telefone"));
			e.setEmail(tabela.getString("email"));
			e.setIdEmpresa(tabela.getInt("idEmpresa"));
			return e;
		} catch (SQLException var4) {
			var4.printStackTrace();
			return null;
		}
	}

	private void carregarListaEmpresas() {
		EmpresaDAO edao = new EmpresaDAO(con, schema);
		ResultSet tabela = edao.carregarEmpresa();
		ListaEmpresa.empresas.clear();

		try {
			tabela.beforeFirst();

			while (tabela.next()) {
				ListaEmpresa.empresas.add(this.dadosEmpresa(tabela));
			}

			tabela.close();
		} catch (Exception var4) {
			System.err.println(var4);
			var4.printStackTrace();
		}

	}

	public void imprimirEmpresas() {
		System.out.println("==================");
		System.out.println("LISTA DE EMPRESAS: ");
		System.out.println("==================");
		System.out.print("-----------------------------------------------------");
		System.out.println("\nNOME\t\t| CNPJ\t\t  | E-MAIL");
		System.out.println("-----------------------------------------------------");

		for (Empresa e : empresas) {
			String formatarTamanho = String.format("%-15s | %-15s | %-10s", e.getNome(), e.getCpf_cnpj(), e.getEmail());
			System.out.println(formatarTamanho);
		}

	}

	public static Empresa localizarEmpresaId(Long id) {
		Empresa localizado = null;

		for (Empresa e : empresas) {
			if (e.getIdEmpresa() == id) {
				localizado = e;
				break;
			}
		}
		return localizado;
	}

	// Verificação para ver se já existe o CNPJ cadastrado no bd

	public static Object localizarEmpresaCNPJ(String cnpj) {
		Empresa localizado = null;

		for (Empresa e : empresas) {
			if (e.getCpf_cnpj().equals(cnpj)) {
				localizado = e;
				break;
			}
		}
		return localizado;
	}

	public static Empresa localizarEmpresa() {
		Empresa localizado = null;
		String cnpjempresa;
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);

		cnpjempresa = input.nextLine();
		for (Empresa e : empresas) {
			if (e.getCpf_cnpj().equals(cnpjempresa)) {
				localizado = e;
				break;
			}

		}
		return localizado;
	}

	public static boolean excluirEmpresa(Empresa e) {

		boolean excluido = false;
		for (Empresa cl : empresas) {
			if (cl.getIdEmpresa() == e.getIdEmpresa()) {
				empresas.remove(empresas.lastIndexOf(cl));
				EmpresaDML.excluirEmpresa(con, schema, e);
				excluido = true;
				break;
			}

		}
		return excluido;
	}

	public void adicionarEmpresaLista(Empresa e) {
		ListaEmpresa.empresas.add(e);
	}

}