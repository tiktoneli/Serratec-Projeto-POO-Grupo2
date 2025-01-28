package com.serratec.constantes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Util {
	public static Scanner in = new Scanner(System.in);
	public static final String CABECALHO = "SISTEMA DE VENDAS GRUPO 2";
	public static final String LINHA = "----------------------------------";
	public static final String LINHAD = "==================================";

	public enum CRUD {
		CADASTRAR, ALTERAR, EXCLUIR, IMPRIMIR
	}

	public static void br() {
		System.out.println("");
	}

	public static void escrever(String mensagem) {
		System.out.println(mensagem);
	}

	public static LocalDate validarData(String mensagem) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate dataConvertida = null;
		String sData;
		boolean dataValidada = false;

		do {
			System.out.println(mensagem);
			sData = in.nextLine();

			try {
				dataConvertida = LocalDate.parse(sData, dtf);
				dataValidada = true;
				return dataConvertida;
			} catch (Exception e) {
				System.out.println("Data invalida");
				return null;
			}
		} while (!dataValidada);
	}

	public static int validarInteiro(String mensagem) {
		int numero = 0;
		boolean validado = false;

		do {
			try {
				System.out.println(mensagem);
				String s = in.nextLine();
				numero = Integer.parseInt(s);
				validado = true;
			} catch (Exception e) {
				System.out.println("Informe um numero valido - " + e.getMessage());
			}
		} while (!validado);

		return numero;
	}

	public static double validarDouble(String mensagem) {
		double numero = 0.0;
		boolean validado = false;

		do {
			try {
				System.out.println(mensagem);
				String s = in.nextLine();
				numero = Integer.parseInt(s);
				validado = true;
			} catch (Exception e) {
				System.out.println("Informe um numero (0,00) - " + e.getMessage());
			}
		} while (!validado);

		return numero;
	}

	public static void aperteEnter() {
		System.out.println("Aperte ENTER para continuar: ");
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		s.nextLine();
	}

}
