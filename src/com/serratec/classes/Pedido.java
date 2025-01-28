package com.serratec.classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.ArrayList;
import com.serratec.ListaClasse.ListaCliente;
import com.serratec.ListaClasse.ListaEmpresa;
import com.serratec.ListaClasse.ListaProduto;
import com.serratec.conexao.Connect;
import com.serratec.constantes.Util;
import com.serratec.dao.Prod_PedidoDAO;
import com.serratec.dml.Prod_PedidoDML;

public class Pedido {

	private long idPedido;
	private long cdPedido;
	private LocalDate dtPedido;
	private Cliente cliente;
	private Empresa empresa;

	public class Itens extends Produto {
		private int quantidade;

		public int getQuantidade() {
			return quantidade;
		}

		public void setQuantidade(int quantidade) {
			this.quantidade = quantidade;
		}
	}

	private ArrayList<Itens> produtos = new ArrayList<>();

	public ArrayList<Itens> getProdutos() {
		return produtos;
	}

	public Itens dadosItens(ResultSet tabela) {
		Itens i = new Itens();

		try {
			i.setCdProduto(tabela.getLong("cdproduto"));
			i.setQuantidade(tabela.getInt("quantidade"));
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(long idPedido) {
		this.idPedido = idPedido;
	}

	public long getCdPedido() {
		return cdPedido;
	}

	public void setCdPedido(long cdPedido) {
		this.cdPedido = cdPedido;
	}

	public LocalDate getDtPedido() {
		return dtPedido;
	}

	public void setDtPedido(LocalDate dtPedido) {
		this.dtPedido = dtPedido;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public static Pedido cadastrarPedido() {
		Pedido p = new Pedido();

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);

		System.out.println(Util.LINHA);
		Util.escrever("Cadastro de novo pedido: ");
		System.out.println(Util.LINHA);

		Util.br();

		Util.escrever("Informe o código do pedido:");
		int i = in.nextInt();
		in.nextLine();
		p.setCdPedido(i);

		Util.escrever("Informe o cpf do Cliente:");
		Cliente cliente = ListaCliente.localizarCliente();
		if (!(cliente == null)) {
			p.setCliente(cliente);
		} else {
			System.err.println("Cliente não encontrado! ");
			return null;
		}

		Util.escrever("Informe o CNPJ da empresa responsável: ");
		Empresa empresa = ListaEmpresa.localizarEmpresa();
		if (!(empresa == null)) {
			p.setEmpresa(empresa);
		} else {
			System.err.println("Empresa não encontrada! ");
			return null;
		}

		p.AdicionarProdutos();

		// Prod_Pedido pd = new Prod_Pedido();
		// pd.AdicionarProdutos();
		// p.setProdutos(pd);
		// System.out.println(p.getProdutos().toString());

		System.out.println(p.getIdPedido());
		p.dtPedido = LocalDate.now();

		return p;
	}

	public static Pedido alterarPedido(Pedido p) {

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);

		System.out.println(Util.LINHA);
		Util.escrever("Alteração de pedido de código " + p.getCdPedido() + ":");
		System.out.println(Util.LINHA);

		Util.br();

		Util.escrever("Modificar as informações do pedido? (S/N)");
		String opcao = in.nextLine();

		if (opcao.toUpperCase().equals("S")) {

			p.alterarProdutosPedido(p);

			p.dtPedido = LocalDate.now();

			return p;
		}
		return p;
	}

	public int localizarItem(int idProduto) {

		for (Itens i : produtos) {
			if (i.getIdProduto() == idProduto) {
				return produtos.lastIndexOf(i);
			}
		}
		return -1;
	}

	public ArrayList<Itens> localizarIdItem(long codigo) {

		Itens item = null;
		for (int i = 0; i < this.produtos.size(); i++) {
			if (produtos.get(i).getIdProduto() == codigo) {
				item = produtos.get(i);
				System.out.println(item.getIdProduto());
				return produtos;
			}
		}
		return null;
	}

	public void alterarItem(int index) {
		int quant = 0;

		System.out.println("Alteração do item: " + produtos.get(index).getDescricao());

		do {
			quant = Util.validarInteiro("Quantidade: ");

			if (quant > 0) {
				produtos.get(index).setQuantidade(quant);
			} else {
				System.out.println("Quantidade precisa ser maior que zero.");
			}
		} while (quant <= 0);
	}

	public void AdicionarProdutos() {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		boolean continua = true;
		String resposta;

		do {
			Util.escrever("Informe o código do produto:");

			Produto produto = ListaProduto.localizarProduto();

			if (produto != null) {
				Util.escrever("Informe a quantidade:");
				int quantidade = in.nextInt();
				in.nextLine();

				Itens item = new Itens();
				item.setQuantidade(quantidade);
				item.setCdProduto(produto.getCdProduto());
				item.setDescricao(produto.getDescricao());
				item.setIdProduto(produto.getIdProduto());
				item.setNome(produto.getNome());
				item.setPorcento(produto.getPorcento());
				item.setValorUnit(produto.getValorUnit());
				item.somarValor(produto.getValorVenda());

				this.produtos.add(item);

				Util.escrever("Produto adicionado com sucesso!");
			} else {
				Util.escrever("Produto não encontrado. Verifique o código informado.");
			}

			Util.escrever("Digite 'ENTER' para adicionar mais produtos, digite 's' para sair");
			resposta = in.nextLine();
			continua = !resposta.equals("s");

		} while (continua);
	}

	public void alterarProdutosPedido(Pedido p) {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		boolean continua = true;
		String resposta;

		Util.escrever("--------------------PRODUTOS---------------------");
		Util.escrever("codigo do produto \tnome \tvalor \tquant. \ttotal");
		Prod_PedidoDAO.carregarProd_Pedido(p);

		System.out.println("\nDeseja realizar alguma alteração nos produtos desse pedido? S/N");
		resposta = in.nextLine();
		continua = resposta.toUpperCase().equals("s");

		if (resposta.toUpperCase().equals("S")) {

			do {
				System.out.println(" 1)Adicionar item\n 2)Alterar item\n 3)Excluir item\n");

				int opcao;
				opcao = in.nextInt();

				switch (opcao) {

					case 1:

						AdicionarProdutos();
						Prod_PedidoDML.gravarProdutoPedido(Connect.con, Connect.dadosCon.getSchema(), p);

						break;

					case 2:

						Util.escrever("Informe o código do produto a ser alterado: ");
						int input = in.nextInt();

						System.out.println(p.localizarItem(input));
						in.next();

					case 3:

				}

			} while (continua);
		}
	}

	public void excluirProdutosPedido() {

	}

	@Override
	public String toString() {
		return "Pedido [idPedido=" + idPedido + ", cdPedido=" + cdPedido + ", dtPedido=" + dtPedido + ", cliente="
				+ cliente + ", empresa=" + empresa + ", produtos=" + produtos + ", getProdutos()=" + getProdutos()
				+ ", getIdPedido()=" + getIdPedido() + ", getCdPedido()=" + getCdPedido() + ", getDtPedido()="
				+ getDtPedido() + ", getCliente()=" + getCliente() + ", getEmpresa()=" + getEmpresa() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}