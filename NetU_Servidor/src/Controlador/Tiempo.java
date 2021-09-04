/**
 * El propósito de esta clase es manejar el tiempo para los módulos de publicación
 * y de chat en NetU Cliente.
 */
package Controlador;

import java.time.LocalDate;
import java.time.LocalTime;

public class Tiempo {

    private static String[] meses = {"enero", "febrero", "marzo", "abril",
        "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre",
        "diciembre"};

    /**
     *
     * @return La fecha actual del sistema en formato año-mes-dia
     */
    synchronized public static String obtenerFechaActual() {

        return LocalDate.now() + "";
    }

    /**
     *
     * @return La hora actual del sistema en formato hora:minuto:segundo. La
     * hora está en formato 24h.
     */
    synchronized public static String obtenerHoraActual() {

        LocalTime horaActual = LocalTime.now();

        String hora = horaActual.getHour() + ":" + horaActual.getMinute() + ":";
        hora += horaActual.getSecond();

        return hora;
    }

    /**
     * El propósito del método es convertir la fecha en formato año-mes-dia a un
     * formato "[día] de [nombre_mes] de [año]" para NetU Cliente
     *
     * @param fecha Fecha de formato año-mes-dia
     * @return La fecha en formato "[día] de [nombre_mes] de [año]"
     */
    synchronized public static String convertirFecha(String fecha) {

        String[] fechaParticionada = fecha.split("-");
        String nombreMes = "";

        try {
            nombreMes = meses[Integer.parseInt(fechaParticionada[1]) - 1];

            return fechaParticionada[2] + " de " + nombreMes + " de " + fechaParticionada[0];
        } catch (NumberFormatException ne) {
            System.out.println("Error al formatear fecha: " + ne.getMessage());
            return null;
        }
    }

    synchronized public static String convertirHora(String hora) {
        String horaFormateada = "";
        String[] fechaParticionada = hora.split(":");
        String meridiano = "";
        int numeroHora = Integer.parseInt(fechaParticionada[0]);

        if (numeroHora == 0) {
            numeroHora = 12;
            meridiano = "a.m";
        } else {

            if (numeroHora < 12) {
                meridiano = "a.m";
            } else {
                if (numeroHora == 12) {
                    meridiano = "p.m";
                } else {
                    numeroHora = numeroHora - 12;
                    meridiano = "p.m";

                }
            }
        }

        horaFormateada = numeroHora + ":" + fechaParticionada[1]
                + " " + meridiano;

        return horaFormateada;
    }

}
