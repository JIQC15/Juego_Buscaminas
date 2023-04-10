
package buscaminas;

public class Casilla {
    private int posFila;
    private int posColum;
    private int numMinasAlrededor;
    private Estados estado;

    public Casilla(int posFila, int posColum) {
        this.posFila = posFila;
        this.posColum = posColum;
    }

    public int getPosFila() {
        return posFila;
    }

    public void setPosFila(int posFila) {
        this.posFila = posFila;
    }

    public int getPosColum() {
        return posColum;
    }

    public void setPosColum(int posColum) {
        this.posColum = posColum;
    }

    public int getNumMinasAlrededor() {
        return numMinasAlrededor;
    }

    public void setNumMinasAlrededor(int numMinasAlrededor) {
        this.numMinasAlrededor = numMinasAlrededor;
    }
    
    public void incrementarNumeroMinasAlrededor(){
        this.numMinasAlrededor++;
    }
    
    public void setMina() {
        estado = Estados.MINA;
    }

    public void setAbierta(){
        estado = Estados.ABIERTO;
    }
    
    public void setCerrado(){
        estado = Estados.CERRADO;
    }
    
    public void setBandera(){
        estado = Estados.BANDERA;
    }
    
    public void setBloqueado(){
        estado = Estados.BLOQUEADO;
    }
    
    public Estados getEstado() {
        return estado;
    }    
    

    
}
