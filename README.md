## Monedero

Code smells identificados
- Type tests – missing polymorphism: la clase Movimiento la convertí en clase abstracta, de la cual heredan las clases Deposito y Extracción.
- Duplicated code – missing inheritance or delegation: la idea de agregar un Movimiento a una Cuenta se repite en ambas clases. Decidí dejar dicha lógica en la clase Cuenta.
- Large class: delegué la validación de un movimiento a la clase ValidadorMovimiento, inyectada a la clase Cuenta mediante su constructor. 
