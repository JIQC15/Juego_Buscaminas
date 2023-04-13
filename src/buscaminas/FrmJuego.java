package buscaminas;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class FrmJuego extends javax.swing.JFrame {

    int numFilas = 10;
    int numColumnas = 10;
    int numMinas = 10;

    JButton[][] botonesTablero;

    TableroJuego tableroBuscaminas;

    public FrmJuego() {
        initComponents();
        realizar_Juego_Nuevo();
    }

    void generar_Controles() {
        if (botonesTablero != null) {
            for (int i = 0; i < botonesTablero.length; i++) {
                for (int j = 0; j < botonesTablero[i].length; j++) {
                    if (botonesTablero[i][j] != null) {
                        getContentPane().remove(botonesTablero[i][j]);
                    }
                }
            }
        }
    }

    private void realizar_Juego_Nuevo() {
        JOptionPane.showMessageDialog(rootPane, "REGLAS DEL JUEGO:\n1. Para ganar este juego deberas evitar explotar las minas. \n2.Con click derecho del mouse podras colocar tus banderas. \n3. Las pistas del juego se te darán por medio de los numeros que encuentres. \nDISFRUTA!!!");
        generar_Controles();
        cargarControles();
        crearTableroBuscaminas();
        repaint();
    }

    private void crearTableroBuscaminas() {
        tableroBuscaminas = new TableroJuego(numFilas, numColumnas, numMinas);
        tableroBuscaminas.setEventoPartidaPerdida(new Consumer<List<Casilla>>() {
            @Override
            public void accept(List<Casilla> t) {
                for (Casilla casillaConMina : t) {
                    JButton botonActual = botonesTablero[casillaConMina.getPosicion_Fila()][casillaConMina.getPosicion_Columna()];

                    // Cargar la imagen de la mina desde un archivo
                    ImageIcon iconoMina = new ImageIcon(getClass().getResource("/ImagenBuscaminas/Bomba.jpg"));

                    // Escalar la imagen al tamaño del botón
                    Image imagenOriginal = iconoMina.getImage();
                    int anchoDeseado = botonActual.getWidth();
                    int altoDeseado = botonActual.getHeight();
                    Image imagenEscalada = imagenOriginal.getScaledInstance(anchoDeseado, altoDeseado, Image.SCALE_SMOOTH);

                    // Crear un nuevo objeto ImageIcon utilizando la imagen escalada
                    ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);

                    // Establecer el icono del botón
                    botonActual.setIcon(iconoEscalado);
                }
                ImageIcon iconoFeliz = new ImageIcon(getClass().getResource("/ImagenBuscaminas/Sadface.png"));
                JOptionPane.showMessageDialog(null, "Lo siento, has perdido", "Partida perdida...", JOptionPane.INFORMATION_MESSAGE, iconoFeliz);
            }
        });

        tableroBuscaminas.setEventoPartidaGanada(new Consumer<List<Casilla>>() {
            @Override
            public void accept(List<Casilla> t) {
                for (Casilla casillaConMina : t) {
                    JButton botonActual = botonesTablero[casillaConMina.getPosicion_Fila()][casillaConMina.getPosicion_Columna()];

                    ImageIcon iconoMina = new ImageIcon(getClass().getResource("/ImagenBuscaminas/happyface.png"));

                    Image imagenOriginal = iconoMina.getImage();
                    int anchoDeseado = botonActual.getWidth();
                    int altoDeseado = botonActual.getHeight();
                    Image imagenEscalada = imagenOriginal.getScaledInstance(anchoDeseado, altoDeseado, Image.SCALE_SMOOTH);

                    ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);

                    botonActual.setIcon(iconoEscalado);
                }
                
                
                ImageIcon iconoFeliz = new ImageIcon(getClass().getResource("/ImagenBuscaminas/happyface.png"));

                Image imagenOriginal = iconoFeliz.getImage();
                int anchoDeseado = 100;
                int altoDeseado = 100;
                Image imagenRedimensionada = imagenOriginal.getScaledInstance(anchoDeseado, altoDeseado, Image.SCALE_SMOOTH);

                ImageIcon iconoRedimensionado = new ImageIcon(imagenRedimensionada);

                JOptionPane.showMessageDialog(null, "¡Enhorabuena, has ganado!", "¡Felicidades!", JOptionPane.INFORMATION_MESSAGE, iconoRedimensionado);
            }
        });

        tableroBuscaminas.setEventoCasillaAbierta(new Consumer<Casilla>() {
            @Override
            public void accept(Casilla t) {
                botonesTablero[t.getPosicion_Fila()][t.getPosicion_Columna()].setEnabled(false);
                botonesTablero[t.getPosicion_Fila()][t.getPosicion_Columna()].setIcon(null);
                botonesTablero[t.getPosicion_Fila()][t.getPosicion_Columna()].setText(t.getMinas_Alrededor() == 0 ? "" : t.getMinas_Alrededor() + "");
            }
        });

        tableroBuscaminas.setEventoCasillaBandera(new Consumer<Casilla>() {
            @Override
            public void accept(Casilla t) {
            }
        });
   
        tableroBuscaminas.mostrar_Tablero();
    }

    private void cargarControles() {

        int posXReferencia = 25;
        int posYReferencia = 25;
        int anchoControl = 30;
        int altoControl = 30;

        botonesTablero = new JButton[numFilas][numColumnas];
        for (int i = 0; i < botonesTablero.length; i++) {
            for (int j = 0; j < botonesTablero[i].length; j++) {
                botonesTablero[i][j] = new JButton();
                botonesTablero[i][j].setName(i + "," + j);
                botonesTablero[i][j].setBorder(null);
                if (i == 0 && j == 0) {
                    botonesTablero[i][j].setBounds(posXReferencia, posYReferencia,
                            anchoControl, altoControl);
                } else if (i == 0 && j != 0) {
                    botonesTablero[i][j].setBounds(botonesTablero[i][j - 1].getX() + botonesTablero[i][j - 1].getWidth(), posYReferencia,
                            anchoControl, altoControl);
                } else {
                    botonesTablero[i][j].setBounds(
                            botonesTablero[i - 1][j].getX(),
                            botonesTablero[i - 1][j].getY() + botonesTablero[i - 1][j].getHeight(),
                            anchoControl, altoControl);
                }
                botonesTablero[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton btn = (JButton) e.getSource();
                        btnClick(btn);
                    }
                });
                botonesTablero[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON3) { // Verificar si el botón presionado fue el derecho
                            JButton btn = (JButton) e.getSource();
                            btnClickBandera(btn);
                        }
                    }
                });
                getContentPane().add(botonesTablero[i][j]);
            }
        }
        this.setSize(botonesTablero[numFilas - 1][numColumnas - 1].getX()
                + botonesTablero[numFilas - 1][numColumnas - 1].getWidth() + 70,
                botonesTablero[numFilas - 1][numColumnas - 1].getY()
                + botonesTablero[numFilas - 1][numColumnas - 1].getHeight() + 90
        );
    }

    private int[] analizarCasillas(JButton e) {
        JButton btn = e;
        String[] tempCoordenadas = btn.getName().split(",");
        int coordenadas[] = new int[2];
        coordenadas[0] = Integer.parseInt(tempCoordenadas[0]); // Filas
        coordenadas[1] = Integer.parseInt(tempCoordenadas[1]); // Columnas

        return coordenadas;
    }

    private void btnClick(JButton e) {
        int[] coordenas = analizarCasillas(e);
        int posFila = coordenas[0];
        int posColumna = coordenas[1];

        tableroBuscaminas.seleccionar_Casillas(posFila, posColumna);
    }

    private void btnClickBandera(JButton e) {

        if (tableroBuscaminas.establecerBandera()) { // Evalua que las casillas NO esten bloqueadas

            int[] coordenas = analizarCasillas(e);
            int posFila = coordenas[0];
            int posColumna = coordenas[1];
            
            if (botonesTablero[posFila][posColumna].getIcon() != null) {
                botonesTablero[posFila][posColumna].setIcon(null);
            } else {
                // Cargar la imagen desde un archivo
                ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/ImagenBuscaminas/Bandera.png"));
                Image imagenOriginal = iconoOriginal.getImage();

                // Escalar la imagen al tamaño deseado
                int anchoDeseado = botonesTablero[posFila][posColumna].getWidth();
                int altoDeseado = botonesTablero[posFila][posColumna].getHeight();
                Image imagenEscalada = imagenOriginal.getScaledInstance(anchoDeseado, altoDeseado, Image.SCALE_SMOOTH);

                // Crear un nuevo objeto ImageIcon utilizando la imagen escalada
                ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);

                // Establecer el icono del botón
                botonesTablero[posFila][posColumna].setIcon(iconoEscalado);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jMenu1.setText("Juego");

        jMenuItem1.setText("Nuevo Juego");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Tamaño");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Numero Minas");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Dificultad");

        jMenuItem4.setText("Facil");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem5.setText("Intermedio");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem6.setText("Dificil");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        realizar_Juego_Nuevo();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        int num = Integer.parseInt(JOptionPane.showInputDialog("Digite el numero de la matriz"));
        this.numFilas = num;
        this.numColumnas = num;
        realizar_Juego_Nuevo();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        int num = Integer.parseInt(JOptionPane.showInputDialog("Digite el numero de minas: "));
        this.numMinas = num;
        realizar_Juego_Nuevo();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        numFilas = 16;
        numColumnas = 16;
        numMinas = 40;
        realizar_Juego_Nuevo();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        numFilas = 16;
        numColumnas = 30;
        numMinas = 99;
        realizar_Juego_Nuevo();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        numFilas = 8;
        numColumnas = 8;
        numMinas = 10;
        realizar_Juego_Nuevo();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmJuego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    // End of variables declaration//GEN-END:variables
}
