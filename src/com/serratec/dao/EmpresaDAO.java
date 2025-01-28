package com.serratec.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.serratec.classes.Empresa;
import com.serratec.conexao.Conexao;
import com.serratec.conexao.Connect;

public class EmpresaDAO {
	private Conexao conexao;
	private String schema;

	PreparedStatement pInclusao;
	PreparedStatement pAlteracao;
	PreparedStatement pExclusao;

	public EmpresaDAO(Conexao conexao, String schema) {
		this.conexao = conexao;
		this.schema = schema;
		prepararSqlInclusao();
		prepararSqlAlteracao();
		prepararSqlExclusao();
	}

	private void prepararSqlExclusao() {
		String sql = "delete from " + this.schema + ".empresa";
		sql += " where idempresa = ?";

		try {
			this.pExclusao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	private void prepararSqlInclusao() {
		String sql = "insert into " + this.schema + ".empresa";
		sql += " (nome, cnpj, endereco, telefone, email)";
		sql += " values ";
		sql += " (?, ?, ?, ?, ?)";

		try {
			this.pInclusao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	private void prepararSqlAlteracao() {
		String sql = "update " + this.schema + ".empresa";
		sql += " set nome = ?,";
		sql += " cnpj = ?,";
		sql += " endereco = ?,";
		sql += " telefone = ?,";
		sql += " email = ?";
		sql += " where idempresa = ?";

		try {
			this.pAlteracao = conexao.getC().prepareStatement(sql);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	public int alterarEmpresa(Empresa empresa) {
		try {
			pAlteracao.setString(1, empresa.getNome());
			pAlteracao.setString(2, empresa.getCpf_cnpj());
			pAlteracao.setString(3, empresa.getEndereco());
			pAlteracao.setString(4, empresa.getTelefone());
			pAlteracao.setString(5, empresa.getEmail());
			pAlteracao.setLong(6, empresa.getIdEmpresa());

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

	public int incluirEmpresa(Empresa empresa) {
		try {

			pInclusao.setString(1, empresa.getNome());
			pInclusao.setString(2, empresa.getCpf_cnpj());
			pInclusao.setString(3, empresa.getEndereco());
			pInclusao.setString(4, empresa.getTelefone());
			pInclusao.setString(5, empresa.getEmail());

			return pInclusao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nEmpresa nao incluida.\nVerifique se foi chamado o conect:\n" + e);
			} else {
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}

	public int excluirEmpresa(Empresa empresa) {
		try {
			pExclusao.setLong(1, empresa.getIdEmpresa());

			return pExclusao.executeUpdate();
		} catch (Exception e) {
			if (e.getLocalizedMessage().contains("is null")) {
				System.err.println("\nEmpresa nao incluida.\nVerifique se foi chamado o conect:\n" + e);
			} else {
				System.err.println(e);
				e.printStackTrace();
			}
			return 0;
		}
	}

	public ResultSet carregarEmpresa() {
		ResultSet tabela;
		String sql = "select * from " + this.schema + ".empresa order by idempresa";

		tabela = conexao.query(sql);

		return tabela;
	}

	public static int retornarIdEmpresa(String schema) {
		String sql = "select idempresa from " + schema + ".empresa" + " order by idempresa desc limit 1";

		try {
			ResultSet tabela = Connect.con.query(sql);
			if (tabela.next()) {
				return tabela.getInt("idempresa");
			} else {
				return 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
