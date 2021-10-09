/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.swing.JProgressBar;

/**
 *
 * @author IsraelCM
 */
public class cargarR extends Thread {

    JProgressBar progreso;
    public static boolean detener = false;

    public cargarR(JProgressBar progreso) {
        super();
        this.progreso = progreso;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            if(detener){
                detener = false;
                stop();               
            }
            System.out.println(i);
            progreso.setValue(i);
            pausa(150);
        }
    }

    public void pausa(int mlSeg) {
        try {
            Thread.sleep(mlSeg);
        } catch (Exception e) {
        }
    }
}
