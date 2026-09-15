package org.backend.prova.util;

public class CpfValidator {

  public static boolean isValid(String cpf) {
    if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d+")) {
      return false;
    }

    // Verifica se todos os dígitos são iguais (CPF inválido)
    if (cpf.matches("(\\d)\\1{10}")) {
      return false;
    }

    // Valida primeiro dígito verificador
    int soma = 0;
    for (int i = 0; i < 9; i++) {
      soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
    }
    int digito1 = 11 - (soma % 11);
    if (digito1 >= 10) digito1 = 0;

    if (Character.getNumericValue(cpf.charAt(9)) != digito1) {
      return false;
    }

    // Valida segundo dígito verificador
    soma = 0;
    for (int i = 0; i < 10; i++) {
      soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
    }
    int digito2 = 11 - (soma % 11);
    if (digito2 >= 10) digito2 = 0;

    return Character.getNumericValue(cpf.charAt(10)) == digito2;
  }
}
