package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import java.time.LocalDate;

public class ValidadorMovimiento {
  private double limite;
  private double cantDeposDiarios;

  public ValidadorMovimiento(){ this(1000, 3); }
  public ValidadorMovimiento(double limite, double cantDeposDiarios){
    this.limite = limite;
    this.cantDeposDiarios = cantDeposDiarios;
  }

  public void chequearMontoPositivo(double monto){
    if (monto <= 0) {
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void validarDeposito(Cuenta cuenta, double monto){
    if (cantDepositosFecha(cuenta, LocalDate.now()) == cantDeposDiarios) {
      throw new MaximaCantidadDepositosException("Excedio los " + cantDeposDiarios + " depositos diarios");
    }
    chequearMontoPositivo(monto);
  }

  public void validarExtraccion(Cuenta cuenta, double monto){
    chequearMontoPositivo(monto);
    if (cuenta.getSaldo()-monto < 0) {
      throw new SaldoMenorException("No puede sacar mas de $" + cuenta.getSaldo());
    }
    else if (monto > limite - montoExtraidoA(cuenta, LocalDate.now())) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $" + limite + " diarios");
    }
  }

  public long cantDepositosFecha(Cuenta cuenta, LocalDate fecha){
    return cuenta.getDepositos().stream().filter(deposito -> deposito.fueDepositado(fecha)).count();
  }

  public double montoExtraidoA(Cuenta cuenta, LocalDate fecha) {
    return cuenta.getExtracciones().stream()
        .filter(extraccion -> extraccion.fueExtraido(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }
}
