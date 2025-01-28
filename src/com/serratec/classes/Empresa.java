package com.serratec.classes;

import java.util.Scanner;

import com.serratec.ListaClasse.ListaEmpresa;
import com.serratec.constantes.Util;
import com.serratec.menu.MenuEmpresa;

public class Empresa extends Parceiro {

	private int idEmpresa;

	public int getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@SuppressWarnings("resource")
	public static Empresa cadastrarEmpresa() {

		Empresa e = new Empresa();

		Scanner in = new Scanner(System.in);

		System.out.println(Util.LINHA);
		System.out.println("Cadastro de empresa: ");
		System.out.println(Util.LINHA);

		Util.br();

		System.out.println("Informe o CNPJ da empresa:");
		String s = in.nextLine();
		if (ListaEmpresa.localizarEmpresaCNPJ(s) == null) {
			e.setCpf_cnpj(s);
		} else {
			System.out.println("Empresa já cadastrada");
			Util.aperteEnter();
			int opcaoMenuEmpresa = MenuEmpresa.menu();
			MenuEmpresa.opcoes(opcaoMenuEmpresa);
		}

		System.out.println("Informe o nome da empresa:");
		s = in.nextLine();
		e.setNome(s);

		System.out.println("Informe o endereço da empresa: ");
		s = in.nextLine();
		e.setEndereco(s);

		System.out.println("Informe o telefone da empresa:");
		s = in.nextLine();
		e.setTelefone(s);

		System.out.println("Informe o email da empresa:");
		s = in.nextLine();
		e.setEmail(s);

		return e;
	}

	public static Empresa alterarEmpresa(Empresa e) {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);

		System.out.println(Util.LINHA);
		Util.escrever("Alteração de empresa: " + e.getCpf_cnpj());
		System.out.println(Util.LINHA);

		Util.br();

		Util.escrever("Alterar o CNPJ ou pressione ENTER para manter original:");
		String cnpj = in.nextLine();
		if (ListaEmpresa.localizarEmpresaCNPJ(cnpj) == null) {
			if (cnpj != null && !cnpj.trim().isEmpty()) {
				e.setCpf_cnpj(cnpj);
			}
		} else {
			System.out.println(
					"Empresa com o CNPJ " + cnpj + " já cadastrada!\nVerifique se o CNPJ digitado está correto");
			Util.aperteEnter();
			int opcao = MenuEmpresa.menu();
			MenuEmpresa.opcoes(opcao);
		}

		Util.escrever("Alterar o nome ou pressione ENTER para manter original: ");
		String nome = in.nextLine();
		if (nome != null && !nome.trim().isEmpty()) {
			e.setNome(nome);
		}

		Util.escrever("Alterar o telefone ou pressione ENTER para manter original::");
		String tel = in.nextLine();
		if (tel != null && !tel.trim().isEmpty()) {
			e.setTelefone(tel);
		}

		Util.escrever("Alterar o email ou pressione ENTER para manter original::");
		String email = in.nextLine();
		if (email != null && !email.trim().isEmpty()) {
			e.setEmail(email);
		}

		Util.escrever("Alterar o endereço ou pressione ENTER para manter original::");
		String end = in.nextLine();
		if (end != null && !end.trim().isEmpty()) {
			e.setEndereco(end);
		}

		return e;
	}

}
