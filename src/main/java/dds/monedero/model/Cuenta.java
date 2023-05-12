package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cuenta {
  private double saldo;
  private double limite = 1000;
  private double cantDeposDiarios = 3;
  private List<Deposito> depositos = new ArrayList<>();
  private List<Extraccion> extracciones = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }
  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void poner(double monto) {
    validarDeposito(monto);
    registrarDeposito(LocalDate.now(), monto);
  }

  public void sacar(double monto) {
    validarExtraccion(monto);
    registrarExtraccion(LocalDate.now(), monto);
  }

  public void chequearMontoPositivo(double monto){
    if (monto <= 0) {
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void validarDeposito(double monto){
    if (cantDepositosFecha(LocalDate.now()) == cantDeposDiarios) {
      throw new MaximaCantidadDepositosException("Excedio los " + cantDeposDiarios + " depositos diarios");
    }
    chequearMontoPositivo(monto);
  }

  public void validarExtraccion(double monto){
    chequearMontoPositivo(monto);
    if (getSaldo()-monto < 0) {
      throw new SaldoMenorException("No puede sacar mas de $" + getSaldo());
    }
    else if (monto > limite - montoExtraidoA(LocalDate.now())) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $" + limite + " diarios");
    }
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

  public void setSaldo(Movimiento movimiento){
    this.saldo = movimiento.nuevoSaldo(this);
  }

  public long cantDepositosFecha(LocalDate fecha){
    return getDepositos().stream().filter(deposito -> deposito.fueDepositado(fecha)).count();
  }

  public double montoExtraidoA(LocalDate fecha) {
    return getExtracciones().stream()
        .filter(extraccion -> extraccion.fueExtraido(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public double getSaldo() { return saldo; }
  public List<Deposito> getDepositos() {
    return depositos;
  }
  public List<Extraccion> getExtracciones() {
    return extracciones;
  }
}
