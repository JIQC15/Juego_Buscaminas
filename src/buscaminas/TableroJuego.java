package buscaminas;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class TableroJuego {

    Casilla[][] casillas;

    int numero_Filas;
    int numero_Columnas;
    int numero_Minas;

    int numero_CasillasAbiertas;

    private Consumer<List<Casilla>> eventoPartidaPerdida;
    private Consumer<List<Casilla>> eventoPartidaGanada;

    private Consumer<Casilla> eventoCasillaAbierta;
    private Consumer<Casilla> eventoCasillaBandera;

    public TableroJuego(int numFilas, int numColumas, int numMinas) {
        this.numero_Filas = numFilas;
        this.numero_Columnas = numColumas;
        this.numero_Minas = numMinas;
        this.crear_Casillas();
    }

    public void crear_Casillas() {
        casillas = new Casilla[this.numero_Filas][this.numero_Columnas];//Instancias

        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                casillas[i][j] = new Casilla(i, j);//Instancia
            }
        }
        crear_Minas();
    }

    private void crear_Minas() {
        int minasGeneradas = 0;//Variable temporal
        while (minasGeneradas != numero_Minas) {
            int posicionTempFila = (int) (Math.random() * casillas.length); // 0.72 * 7 = 5
            int posicionTempColm = (int) (Math.random() * casillas[0].length);
            if (!(casillas[posicionTempFila][posicionTempColm].getEstado_Generado() == Estados.MINA)) {
                casillas[posicionTempFila][posicionTempColm].setMina();
                minasGeneradas++;//Minas generadas
            }
        }
        actualizar_MinasGeneradas();
    }

    public void mostrar_Tablero() {
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                System.out.print(casillas[i][j].getEstado_Generado() == Estados.MINA ? "*" : "0");//Ternario
            }
            System.out.println("");
        }
    }

    private void mostrar_Pistas() {
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                System.out.print(casillas[i][j].getMinas_Alrededor());
            }
            System.out.println("");
        }
    }

    private void actualizar_MinasGeneradas() {
        for (Casilla[] filaCasillas : casillas) {
            for (Casilla casilla : filaCasillas) {
                if (casilla.getEstado_Generado() == Estados.MINA) {
                    obtenerCasillasAlrededor(casilla.getPosicion_Fila(), casilla.getPosicion_Columna())
                            .forEach(Casilla::incrementarNumeroMinasAlrededor);
                }
            }
        }
    }

    private List<Casilla> obtenerCasillasAlrededor(int posFila, int posColumna) {
        List<Casilla> listaCasilla = new LinkedList<>();
        int[] filas = {-1, -1, 0, 1, 1, 1, 0, -1};
        int[] columnas = {0, 1, 1, 1, 0, -1, -1, -1};
        for (int i = 0; i < filas.length; i++) {
            int tmpPosFila = posFila + filas[i];
            int tmpPosColm = posColumna + columnas[i];
            if (tmpPosFila >= 0 && tmpPosFila < this.casillas.length
                    && tmpPosColm >= 0 && tmpPosColm < this.casillas[0].length) {
                listaCasilla.add(this.casillas[tmpPosFila][tmpPosColm]);
            }
        }
        return listaCasilla;
    }

    List<Casilla> casillas_Mina() {
        List<Casilla> casillasConMinas = new LinkedList<>();
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                if (casillas[i][j].getEstado_Generado() == Estados.MINA) {
                    casillasConMinas.add(casillas[i][j]);
                }
            }
        }
        return casillasConMinas;
    }

    public boolean establecerBandera() {
        if (casillas[0][0].getEstado_Generado() == Estados.BLOQUEADO) {
            return false;
        }
        return true;
    }

    public void seleccionar_Casillas(int posFila, int posColumna) {

        if (casillas[0][0].getEstado_Generado() == Estados.BLOQUEADO) {
            return;
        }
        Casilla casillaSeleccionada = this.casillas[posFila][posColumna];
        eventoCasillaAbierta.accept(casillaSeleccionada);

        if (casillaSeleccionada.getEstado_Generado() == Estados.MINA) {
            eventoPartidaPerdida.accept(casillas_Mina());
            bloquearCasillas();
            return;
        }

        if (casillaSeleccionada.getMinas_Alrededor() == 0) {
            marcar_Casillas_Abiertas(posFila, posColumna);
            List<Casilla> casillasAlrededor = obtenerCasillasAlrededor(posFila, posColumna);

            for (Casilla casilla : casillasAlrededor) {
                if (casilla.getEstado_Generado() != Estados.ABIERTO) {
                    seleccionar_Casillas(casilla.getPosicion_Fila(), casilla.getPosicion_Columna());
                }
            }
        } else {
            marcar_Casillas_Abiertas(posFila, posColumna);
        }

        if (partidaGanada()) {
            eventoPartidaGanada.accept(casillas_Mina());
        }
    }

    void marcar_Casillas_Abiertas(int posFila, int posColumna) {
        if (!(this.casillas[posFila][posColumna].getEstado_Generado() == Estados.ABIERTO)) {
            numero_CasillasAbiertas++;
            this.casillas[posFila][posColumna].setAbierta();
        }
    }

    boolean partidaGanada() {
        return numero_CasillasAbiertas >= (numero_Filas * numero_Columnas) - numero_Minas;
    }

    private void bloquearCasillas() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.casillas[i][j].setBloqueado();
            }
        }
    }

    public static void main(String[] args) {
        TableroJuego tablero = new TableroJuego(5, 5, 6);
        tablero.mostrar_Tablero();
        System.out.println("-----");
        tablero.mostrar_Pistas();
    }

    public void setEventoPartidaPerdida(Consumer<List<Casilla>> eventoPartidaPerdida) {
        this.eventoPartidaPerdida = eventoPartidaPerdida;
    }

    public void setEventoCasillaAbierta(Consumer<Casilla> eventoCasillaAbierta) {
        this.eventoCasillaAbierta = eventoCasillaAbierta;
    }

    public void setEventoPartidaGanada(Consumer<List<Casilla>> eventoPartidaGanada) {
        this.eventoPartidaGanada = eventoPartidaGanada;
    }

    public Consumer<Casilla> getEventoCasillaBandera() {
        return eventoCasillaBandera;
    }

    public void setEventoCasillaBandera(Consumer<Casilla> eventoCasillaBandera) {
        this.eventoCasillaBandera = eventoCasillaBandera;
    }

}
