package dds.monedero.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {
  private double saldo;
  private ValidadorMovimiento validador;
  private List<Deposito> depositos = new ArrayList<>();
  private List<Extraccion> extracciones = new ArrayList<>();

  public Cuenta(ValidadorMovimiento validador) { this(0, validador); }
  public Cuenta(double montoInicial, ValidadorMovimiento validador) {
    this.saldo = montoInicial;
    this.validador = validador;
  }

  public void poner(double monto) {
    validador.validarDeposito(this, monto);
    registrarDeposito(LocalDate.now(), monto);
  }

  public void sacar(double monto) {
    validador.validarExtraccion(this, monto);
    registrarExtraccion(LocalDate.now(), monto);
  }

  public void registrarDeposito(LocalDate fecha, double monto) {
    Deposito deposito = new Deposito(fecha, monto);
    depositos.add(deposito);
    setSaldo(deposito);
  }

  public void registrarExtraccion(LocalDate fecha, double monto) {
    Extraccion extraccion = new Extraccion(fecha, monto);
    extracciones.add(extraccion);
    setSaldo(extraccion);
  }

  public void setSaldo(Movimiento movimiento) {
    this.saldo = movimiento.nuevoSaldo(this);
  }
  public double getSaldo() { return saldo; }

  public List<Deposito> getDepositos() {
    return depositos;
  }
  public List<Extraccion> getExtracciones() {
    return extracciones;
  }
}
