
package buscaminas;

public class Casilla {
    private int posicion_Fila;
    private int posicion_Columna;
    private int minas_Alrededor;
    private Estados estado_Generado;

    public Casilla(int posFila, int posColum) {
        this.posicion_Fila = posFila;
        this.posicion_Columna = posColum;
    }

    public int getPosicion_Fila() {
        return posicion_Fila;
    }

    public void setPosicion_Fila(int posicion_Fila) {
        this.posicion_Fila = posicion_Fila;
    }

    public int getPosicion_Columna() {
        return posicion_Columna;
    }

    public void setPosicion_Columna(int posicion_Columna) {
        this.posicion_Columna = posicion_Columna;
    }

    public int getMinas_Alrededor() {
        return minas_Alrededor;
    }

    public void setMinas_Alrededor(int minas_Alrededor) {
        this.minas_Alrededor = minas_Alrededor;
    }
    
    public void incrementarNumeroMinasAlrededor(){
        this.minas_Alrededor++;
    }
    
    public void setMina() {
        estado_Generado = Estados.MINA;
    }

    public void setAbierta(){
        estado_Generado = Estados.ABIERTO;
    }
    
    public void setCerrado(){
        estado_Generado = Estados.CERRADO;
    }
    
    public void setBandera(){
        estado_Generado = Estados.BANDERA;
    }
    
    public void setBloqueado(){
        estado_Generado = Estados.BLOQUEADO;
    }
    
    public Estados getEstado_Generado() {
        return estado_Generado;
    }    
    

    
}
