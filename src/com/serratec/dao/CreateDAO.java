package com.serratec.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.serratec.conexao.Conexao;
import com.serratec.conexao.DadosConexao;

public class CreateDAO {
	private static Conexao conexao;

	public static boolean createBD(String bd, String schema, DadosConexao dadosCon) {
		boolean bdCriado = false;
		conexao = conectar("postgres", dadosCon);

		if (criarDatabase(conexao, bd)) {
			desconectar(conexao);

			conexao = conectar(bd, dadosCon);

			if (criarSchema(conexao, schema)) {
				criarEntidadeCliente(conexao, schema);
				criarEntidadeEmpresa(conexao, schema);
				criarEntidadeProduto(conexao, schema);
				criarEntidadePedido(conexao, schema);
				criarEntidadeProdPedido(conexao, schema);

				bdCriado = true;
			}
		}
		desconectar(conexao);

		return bdCriado;
	}

	private static Conexao conectar(String bd, DadosConexao dadosCon) {
		dadosCon.setBanco(bd);
		Conexao conexao = new Conexao(dadosCon);
		conexao.conect();
		return conexao;
	}

	private static void desconectar(Conexao con) {
		con.disconect();
	}

	private static boolean criarDatabase(Conexao con, String bd) {
		boolean bdExiste;
		int tentativas = 1;
		String sql;

		class Database {
			public static ResultSet Exists(Conexao con, String bd) {
				ResultSet entidade;
				String sql = "select datname from pg_database where datname = '" + bd + "'";
				entidade = con.query(sql);
				return entidade;
			}
		}

		do {
			try {
				bdExiste = Database.Exists(con, bd).next();

				if (!bdExiste) {
					sql = "create database " + bd;
					con.query(sql);
					tentativas++;
				}
			} catch (Exception e) {
				System.err.printf("Nao foi possivel criar o database %s: %s", bd, e);
				e.printStackTrace();
				return false;
			}
		} while (!bdExiste && (tentativas <= 3));

		return bdExiste;
	}

	private static boolean criarSchema(Conexao con, String schema) {
		boolean schemaExiste;
		int tentativas = 1;
		String sql;

		class Schema {
			public static ResultSet Exists(Conexao con, String schema) {
				ResultSet entidade;
				String sql = "select * from pg_namespace where nspname = '" + schema + "'";
				entidade = con.query(sql);
				return entidade;
			}
		}

		do {
			try {
				schemaExiste = Schema.Exists(con, schema).next();

				if (!schemaExiste) {
					sql = "create schema " + schema;
					con.query(sql);
					tentativas++;
				}
			} catch (Exception e) {
				System.err.printf("Nao foi possivel criar o schema %s: %s", schema, e);
				e.printStackTrace();
				return false;
			}
		} while (!schemaExiste && (tentativas <= 3));

		return schemaExiste;
	}

	private static void criarTabela(Conexao con, String entidade, String schema) {
		String sql = "create table " + schema + "." + entidade + " ()";
		con.query(sql);
	}

	private static void criarCampo(Conexao con, String schema, String entidade, String atributo, String tipoAtributo,
			boolean primario, boolean estrangeiro, String entidadeEstrangeira, String atributoEstrangeiro) {

		if (!atributoExists(con, schema, entidade, atributo)) {
			String sql = "alter table " + schema + "." + entidade + " add column " + atributo + " " + tipoAtributo
					+ " ";

			if (primario) {
				sql += "primary key ";
			}

			if (estrangeiro) {
				sql += "references " + schema + "." + entidadeEstrangeira + "(" + atributoEstrangeiro + ")";
			}

			con.query(sql);
		}
	}

	// Criação das Entidades do Trabalho

	private static void criarEntidadeCliente(Conexao con, String schema) {
		String entidade = "cliente";

		if (!entidadeExists(con, schema, entidade))
			criarTabela(con, entidade, schema);

		if (entidadeExists(con, schema, entidade)) {
			criarCampo(con, schema, entidade, "idcliente", "serial", true, false, null, null);
			criarCampo(con, schema, entidade, "nome", "varchar(100)", false, false, null, null);
			criarCampo(con, schema, entidade, "cpf", "varchar(11) UNIQUE NOT NULL", false, false, null, null);
			criarCampo(con, schema, entidade, "telefone", "varchar(15)", false, false, null, null);
			criarCampo(con, schema, entidade, "email", "varchar(100)", false, false, null, null);
			criarCampo(con, schema, entidade, "endereco", "varchar(150)", false, false, null, null);
			criarCampo(con, schema, entidade, "dtnasc", "date", false, false, null, null);
		}
	}

	private static void criarEntidadeEmpresa(Conexao con, String schema) {
		String entidade = "empresa";

		if (!entidadeExists(con, schema, entidade))
			criarTabela(con, entidade, schema);

		if (entidadeExists(con, schema, entidade)) {
			criarCampo(con, schema, entidade, "idempresa", "serial", true, false, null, null);
			criarCampo(con, schema, entidade, "nome", "varchar(100)", false, false, null, null);
			criarCampo(con, schema, entidade, "cnpj", "varchar(14) UNIQUE NOT NULL", false, false, null, null);
			criarCampo(con, schema, entidade, "telefone", "varchar(15)", false, false, null, null);
			criarCampo(con, schema, entidade, "email", "varchar(100)", false, false, null, null);
			criarCampo(con, schema, entidade, "endereco", "varchar(150)", false, false, null, null);
		}
	}

	private static void criarEntidadeProduto(Conexao con, String schema) {
		String entidade = "produto";

		if (!entidadeExists(con, schema, entidade))
			criarTabela(con, entidade, schema);

		if (entidadeExists(con, schema, entidade)) {
			criarCampo(con, schema, entidade, "idproduto", "serial", true, false, null, null);
			criarCampo(con, schema, entidade, "nome", "varchar(100)", false, false, null, null);
			criarCampo(con, schema, entidade, "cdproduto", "bigint UNIQUE NOT NULL", false, false, null, null);
			criarCampo(con, schema, entidade, "descricao", "varchar(150)", false, false, null, null);
			criarCampo(con, schema, entidade, "valorunit", "double precision", false, false, null, null);
			criarCampo(con, schema, entidade, "porcento", "double precision", false, false, null, null);
			criarCampo(con, schema, entidade, "valorvenda", "double precision", false, false, null, null);
		}
	}

	private static void criarEntidadePedido(Conexao con, String schema) {
		String entidade = "pedido";

		if (!entidadeExists(con, schema, entidade))
			criarTabela(con, entidade, schema);

		if (entidadeExists(con, schema, entidade)) {
			criarCampo(con, schema, entidade, "idpedido", "serial", true, false, null, null);
			criarCampo(con, schema, entidade, "codigo", "bigint UNIQUE NOT NULL", false, false, null, null);
			criarCampo(con, schema, entidade, "datapedido", "date", false, false, null, null);
			criarCampo(con, schema, entidade, "idcliente", "integer", false, true, "cliente", "idcliente");
			criarCampo(con, schema, entidade, "idempresa", "integer", false, true, "empresa", "idempresa");
		}
	}

	private static void criarEntidadeProdPedido(Conexao con, String schema) {
		String entidade = "produto_pedido";

		if (!entidadeExists(con, schema, entidade))
			criarTabela(con, entidade, schema);

		if (entidadeExists(con, schema, entidade)) {
			criarCampo(con, schema, entidade, "idprod_pedido", "serial", true, false, null, null);
			criarCampo(con, schema, entidade, "idproduto", "integer", false, true, "produto", "idproduto");
			criarCampo(con, schema, entidade, "quantidade", "integer", false, false, null, null);
			criarCampo(con, schema, entidade, "idpedido", "integer", false, true, "pedido", "idpedido");
		}
	}

	// Verificando se já existe no BD

	public static boolean databaseExists(Conexao con, String bd) {
		ResultSet entidade;
		boolean dbExists = false;

		String sql = "select datname from pg_database where datname = '" + bd + "'";
		entidade = con.query(sql);

		try {
			dbExists = entidade.next();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dbExists;
	}

	public static boolean entidadeExists(Conexao con, String schema, String entidade) {
		boolean entidadeExist = false;
		String sql = "SELECT n.nspname AS schemaname, c.relname AS tablename " + "FROM pg_class c "
				+ "LEFT JOIN pg_namespace n ON n.oid = c.relnamespace "
				+ "LEFT JOIN pg_tablespace t ON t.oid = c.reltablespace " + "WHERE c.relkind = 'r' "
				+ "AND n.nspname = '" + schema + "' " + "AND c.relname = '" + entidade + "'";

		ResultSet tabela = con.query(sql);

		try {
			entidadeExist = (tabela.next() ? true : false);

		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		}

		return entidadeExist;
	}

	public static boolean atributoExists(Conexao con, String schema, String entidade, String atributo) {

		boolean atributoExist = false;

		String sql = "select table_schema, table_name, column_name from information_schema.columns "
				+ "where table_schema = '" + schema + "' " + "and table_name = '" + entidade + "' "
				+ "and column_name = '" + atributo + "'";

		ResultSet result = con.query(sql);

		try {
			atributoExist = (result.next() ? true : false);

		} catch (SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}

		return atributoExist;
	}

	public static void alimentaTabelaProduto(Conexao con, String schema) {

		try {
			con.conect();

			String sql = "insert into " + schema + ".produto\n"
					+ "(nome,cdproduto,descricao,valorunit,porcento,valorvenda)\n"
					+ "values\n"
					+ "('caderno',1,'cheio de papel',25.5,2,26.0),\n"
					+ "('caneta',2,'cheio de tinta',3,10,3.3),\n"
					+ "('estojo',3,'cheio de espaço',10,2,12),\n"
					+ "('garrafa',4,'cheio de agua',5,0,5),\n"
					+ "('livro',5,'cheio de historia',50,5,52.5),\n"
					+ "('ssd',6,'cheio de memoria',100,2,102)";

			int idGerado = con.executeInsert(sql);

			if (idGerado != -1) {
				System.out.println("Inserção bem-sucedida. ID gerado: " + idGerado);
			} else {
				System.out.println("Erro ao inserir dados.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.disconect();
		}

	}

	public static void alimentaTabelaEmpresa(Conexao con, String schema) {

		try {
			con.conect(); //

			String sql = "insert into " + schema + ".empresa\n" +
					"(nome, cnpj, telefone, email, endereco)\n" +
					"VALUES\n" +
					"('empresaX', '1', '564654', 'empresax@agencia', 'rua y loja 30'),\n" +
					"('empresay', '2', '564444654', 'empresay@agencia', 'rua x loja 38');";

			int idGerado = con.executeInsert(sql);

			if (idGerado != -1) {
				System.out.println("Inserção bem-sucedida. ID gerado: " + idGerado);
			} else {
				System.out.println("Erro ao inserir dados.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.disconect();
		}

	}

	public static void alimentaTabelaCliente(Conexao con, String schema) {

		try {
			con.conect();

			String sql = "\n"
					+ "insert into " + schema + ".cliente\n"
					+ "(nome , cpf, telefone, email, endereco, dtnasc)\n"
					+ "values\n"
					+ "('Joaquim', '1','24787845','joaquim@joaquim','rua b casa10','1985-02-02'),\n"
					+ "('moises', '2','24787845','moises@joaquim','rua c casa15','1985-02-02'),\n"
					+ "('joao', '3','24787845','joao@joaquim','rua d casa10','1985-02-02');\n";

			int idGerado = con.executeInsert(sql);

			if (idGerado != -1) {
				System.out.println("Inserção bem-sucedida. ID gerado: " + idGerado);
			} else {
				System.out.println("Erro ao inserir dados.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.disconect();
		}

	}
}