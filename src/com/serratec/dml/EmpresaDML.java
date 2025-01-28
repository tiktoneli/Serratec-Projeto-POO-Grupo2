package com.serratec.dml;

import com.serratec.classes.Empresa;
import com.serratec.conexao.Conexao;
import com.serratec.dao.EmpresaDAO;

public class EmpresaDML {

	public static void gravarEmpresa(Conexao con, String schema, Empresa e) {

		EmpresaDAO edao = new EmpresaDAO(con, schema);

		edao.incluirEmpresa(e);
	}

	public static void alterarEmpresa(Conexao con, String schema, Empresa e) {

		EmpresaDAO edao = new EmpresaDAO(con, schema);

		edao.alterarEmpresa(e);
	}

	public static void excluirEmpresa(Conexao con, String schema, Empresa e) {

		EmpresaDAO edao = new EmpresaDAO(con, schema);

		edao.excluirEmpresa(e);
	}
}
