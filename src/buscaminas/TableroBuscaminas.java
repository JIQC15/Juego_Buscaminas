package buscaminas;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JOptionPane;

public class TableroBuscaminas {

    Casilla[][] casillas;

    int numFilas;
    int numColumas;
    int numMinas;

    int numCasillasAbiertas;
    boolean juegoTerminado;

    private Consumer<List<Casilla>> eventoPartidaPerdida;
    private Consumer<List<Casilla>> eventoPartidaGanada;

    private Consumer<Casilla> eventoCasillaAbierta;
    private Consumer<Casilla> eventoCasillaBandera;

    public TableroBuscaminas(int numFilas, int numColumas, int numMinas) {
        this.numFilas = numFilas;
        this.numColumas = numColumas;
        this.numMinas = numMinas;
        this.inicializarCasillas();
    }

    public void inicializarCasillas() {
        casillas = new Casilla[this.numFilas][this.numColumas];//Instancias

        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                casillas[i][j] = new Casilla(i, j);//Instancia
            }
        }
        generarMinas();
    }

    private void generarMinas() {
        int minasGeneradas = 0;//Variable temporal
        while (minasGeneradas != numMinas) {
            int posicionTempFila = (int) (Math.random() * casillas.length); // 0.72 * 7 = 5
            int posicionTempColm = (int) (Math.random() * casillas[0].length);
            if (!(casillas[posicionTempFila][posicionTempColm].getEstado() == Estados.MINA)) {
                casillas[posicionTempFila][posicionTempColm].setMina();
                minasGeneradas++;//Minas generadas
            }
        }
        actualizarNumeroMinasAlrededor();
    }

    public void imprimirTablero() {
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                System.out.print(casillas[i][j].getEstado() == Estados.MINA ? "*" : "0");//Ternario
            }
            System.out.println("");
        }
    }

    private void imprimirPistas() {
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                System.out.print(casillas[i][j].getNumMinasAlrededor());
            }
            System.out.println("");
        }
    }

    private void actualizarNumeroMinasAlrededor() {
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                if (casillas[i][j].getEstado() == Estados.MINA) {
                    List<Casilla> casillasAlrededor = obtenerCasillasAlrededor(i, j);
                    casillasAlrededor.forEach((c) -> c.incrementarNumeroMinasAlrededor());
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

    List<Casilla> obtenerCasillasConMinas() {
        List<Casilla> casillasConMinas = new LinkedList<>();
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                if (casillas[i][j].getEstado() == Estados.MINA) {
                    casillasConMinas.add(casillas[i][j]);
                }
            }
        }
        return casillasConMinas;
    }

//    public void seleccionarCasilla(int posFila, int posColumna) {
//        eventoCasillaAbierta.accept(this.casillas[posFila][posColumna]);
//        if (this.casillas[posFila][posColumna].getEstado() == Estados.MINA) {
//            eventoPartidaPerdida.accept(obtenerCasillasConMinas());
//        } else if (this.casillas[posFila][posColumna].getNumMinasAlrededor() == 0) {
//            marcarCasillaAbierta(posFila, posColumna);
//            List<Casilla> casillasAlrededor = obtenerCasillasAlrededor(posFila, posColumna);
//            for (Casilla casilla : casillasAlrededor) {
//                if (!(casilla.getEstado() == Estados.ABIERTO)) {
//                    seleccionarCasilla(casilla.getPosFila(), casilla.getPosColum());
//                }
//            }
//        } else {
//            marcarCasillaAbierta(posFila, posColumna);
//        }
//        if (partidaGanada()) {
//            eventoPartidaGanada.accept(obtenerCasillasConMinas());
//        }
//    }
    
    public boolean establecerBandera(){
        if(casillas[0][0].getEstado() == Estados.BLOQUEADO){
            return false;
        }
        return true;  
    }
    
    public void seleccionarCasilla(int posFila, int posColumna) {
        
        if(casillas[0][0].getEstado() == Estados.BLOQUEADO){
            return;
        }
        Casilla casillaSeleccionada = this.casillas[posFila][posColumna];
        eventoCasillaAbierta.accept(casillaSeleccionada);

        if (casillaSeleccionada.getEstado() == Estados.MINA) {
            eventoPartidaPerdida.accept(obtenerCasillasConMinas());
            bloquearCasillas();
            return;
        }

        if (casillaSeleccionada.getNumMinasAlrededor() == 0) {
            marcarCasillaAbierta(posFila, posColumna);
            List<Casilla> casillasAlrededor = obtenerCasillasAlrededor(posFila, posColumna);

            for (Casilla casilla : casillasAlrededor) {
                if (casilla.getEstado() != Estados.ABIERTO) {
                    seleccionarCasilla(casilla.getPosFila(), casilla.getPosColum());
                }
            }
        } else {
            marcarCasillaAbierta(posFila, posColumna);
        }

        if (partidaGanada()) {
            eventoPartidaGanada.accept(obtenerCasillasConMinas());
        }
    }

    void marcarCasillaAbierta(int posFila, int posColumna) {
        if (!(this.casillas[posFila][posColumna].getEstado() == Estados.ABIERTO)) {
            numCasillasAbiertas++;
            this.casillas[posFila][posColumna].setAbierta();
        }
    }

    boolean partidaGanada() {
        return numCasillasAbiertas >= (numFilas * numColumas) - numMinas;
    }

    private void bloquearCasillas() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.casillas[i][j].setBloqueado();
            }
        }
    }

    public static void main(String[] args) {
        TableroBuscaminas tablero = new TableroBuscaminas(5, 5, 6);
        tablero.imprimirTablero();
        System.out.println("-----");
        tablero.imprimirPistas();
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
