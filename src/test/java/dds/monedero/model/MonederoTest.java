package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private ValidadorMovimiento validador;
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    validador = new ValidadorMovimiento();
    cuenta = new Cuenta(validador);
  }

  @Test
  void AlPonerUnMontoSeAgregaElDepositoALaCuentaSiEsValido() {
    cuenta.poner(1500);
    cuenta.poner(2000);
    assertEquals(cuenta.getDepositos().get(0).getMonto(), 1500);
    assertEquals(cuenta.getDepositos().get(1).getMonto(), 2000);
  }

  @Test
  void AlPonerUnMontoSeAumentaElSaldoDeLaCuentaSiElDepositoEsValido() {
    cuenta.poner(1500);
    cuenta.poner(2000);
    assertEquals(cuenta.getSaldo(), 3500);
  }

  @Test
  void AlSacarUnMontoSeAgregaLaExtraccionALaCuentaSiEsValida() {
    cuenta.setSaldo(1000);
    cuenta.sacar(200);
    cuenta.sacar(700);
    assertEquals(cuenta.getExtracciones().get(0).getMonto(), 200);
    assertEquals(cuenta.getExtracciones().get(1).getMonto(), 700);
  }

  @Test
  void AlSacarUnMontoSeDisminuyeElSaldoDeLaCuentaSiLaExtraccionEsValida() {
    cuenta.setSaldo(1000);
    cuenta.sacar(200);
    cuenta.sacar(700);
    assertEquals(cuenta.getSaldo(), 100);
  }

  @Test
  void NoSePuedeDepositarNiExtraerUnMontoNegativoONulo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(0));
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void NoSePuedenRealizarMasDeTresDepositosPorDia() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }

  @Test
  void NoSePuedeExtraerUnMontoMayorAlSaldoActual() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  public void NoSePuedeExtraerPorDiaMasDelLimite() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

}