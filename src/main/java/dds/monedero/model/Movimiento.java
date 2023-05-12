package dds.monedero.model;

import java.time.LocalDate;

abstract class Movimiento {
  private LocalDate fecha;
  private double monto;

  public Movimiento(LocalDate fecha, double monto) {
    this.fecha = fecha;
    this.monto = monto;
  }

  public double getMonto() {
    return monto;
  }
  public LocalDate getFecha() {
    return fecha;
  }

  public boolean esDeLaFecha(LocalDate fecha) {
    return getFecha().equals(fecha);
  }

  abstract public double nuevoSaldo(Cuenta cuenta);
}
