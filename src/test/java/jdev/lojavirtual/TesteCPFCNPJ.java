package jdev.lojavirtual;

import jdev.lojavirtual.util.ValidaCNPJ;
import jdev.lojavirtual.util.ValidaCPF;

public class TesteCPFCNPJ {

	public static void main(String[] args) {
		boolean isCnpj = ValidaCNPJ.isCNPJ("42.099.930/0001-94");
		System.out.println("CNPJ Válido: " + isCnpj);
		
		boolean isCpf = ValidaCPF.isCPF("911.752.650-71");
		System.out.println("Valida CPF: " + isCpf);
		
	}
}
