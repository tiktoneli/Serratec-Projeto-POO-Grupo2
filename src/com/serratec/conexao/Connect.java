package com.serratec.conexao;

import java.util.Scanner;

import javax.swing.filechooser.FileSystemView;

import com.serratec.dao.CreateDAO;
import com.serratec.menu.MenuPrincipal;
import com.serratec.arquivo.ArquivoTxt;
import com.serratec.ListaClasse.*;

public class Connect {

	public static Conexao con;
	public static DadosConexao dadosCon = new DadosConexao();

	public static final String PATH = FileSystemView.getFileSystemView().getDefaultDirectory().toString();
	public static final String SFILE = "DadosConexao.ini";

	public static ListaCliente clientes;
	public static ListaEmpresa empresas;
	public static ListaProduto produtos;
	public static ListaPedido pedidos;

	// o método dadosEntrada "abre" nosso sistema
	// o sistema só é "aberto" depois que a configinicial retorna TRUE

	public static void dadosEntrada() {

		if (configInicial()) {

			// se conseguir criar o banco de dados com as informações que foram passadas na
			// configinicial,
			// armazene essas informações em uma variável do tipo Conexao chamada "con"

			if (CreateDAO.createBD(dadosCon.getBanco(), dadosCon.getSchema(), dadosCon)) {
				con = new Conexao(dadosCon);
				con.conect();

				// aqui criamos os arrays que vamos usar ao longo do aplicativo
				// os próprios construtores de cada array chamam um método para
				// carregar as informações do banco de dados para si

				clientes = new ListaCliente(getCon(), dadosCon.getSchema());
				empresas = new ListaEmpresa(getCon(), dadosCon.getSchema());
				produtos = new ListaProduto(getCon(), dadosCon.getSchema());
				pedidos = new ListaPedido(getCon(), dadosCon.getSchema());

				MenuPrincipal.opcoes(MenuPrincipal.menuPrincipal());

			} else {
				System.out.println("Ocorreu um problema na criacao do banco de dados");
			}
		} else
			System.out.println("Não foi possível executar o sistema.");
	}

	public static boolean configInicial() {
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		ArquivoTxt conexaoIni = new ArquivoTxt(PATH + SFILE);
		boolean abrirSistema = false;

		if (conexaoIni.criarArquivo()) {
			if (conexaoIni.alimentaDadosConexao()) {
				dadosCon = conexaoIni.getData();
				abrirSistema = true;
			} else {
				conexaoIni.apagarArquivo();
				System.out.println("Arquivo de configuração de conexão:\n");
				System.out.println("Local: ");
				String local = input.nextLine();
				System.out.println("Porta: ");
				String porta = input.nextLine();
				System.out.println("Usuário: ");
				String usuario = input.nextLine();
				System.out.println("Senha: ");
				String senha = input.nextLine();
				System.out.println("Database: ");
				String banco = input.nextLine();
				System.out.println("Schema: ");
				String schema = input.nextLine();

				if (conexaoIni.criarArquivo()) {
					conexaoIni.escreverArquivo("bd=PostgreSql");
					conexaoIni.escreverArquivo("local=" + local);
					conexaoIni.escreverArquivo("porta=" + porta);
					conexaoIni.escreverArquivo("usuario=" + usuario);
					conexaoIni.escreverArquivo("senha=" + senha);
					conexaoIni.escreverArquivo("banco=" + banco);
					conexaoIni.escreverArquivo("schema=" + schema);

					if (conexaoIni.alimentaDadosConexao()) {
						dadosCon = conexaoIni.getData();
						abrirSistema = true;
					} else
						System.out.println("Não foi possível efetuar a configuração.\nVerifique");
				}
			}
		} else
			System.out.println("Houve um problema na criação do arquivo de configuração.");

		return abrirSistema;
	}

	public static Conexao getCon() {
		return con;
	}

	public static ListaCliente getClientes() {
		return clientes;
	}

	public static ListaProduto getProdutos() {
		return produtos;
	}
}