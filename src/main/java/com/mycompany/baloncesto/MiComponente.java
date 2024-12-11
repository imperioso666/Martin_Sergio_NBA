
package com.mycompany.baloncesto;
import javax.swing.*;
import java.awt.*;

public class MiComponente extends JLabel {
    
    public MiComponente() {
        super("Texto");
        configurarEstilo();
    }
    public MiComponente(String texto) {
        super(texto);
        configurarEstilo();
    }
    private void configurarEstilo() {
        // Fondo degradado personalizado
        setForeground(new Color(255, 255, 255)); // Texto blanco
        setBackground(new Color(150, 130, 243)); // morado
        setOpaque(true); // Hacer visible el fondo
        
        // Centrando y aplicando fuente personalizada 
        setHorizontalAlignment(SwingConstants.CENTER); // Centrar texto
        setFont(new Font("Calibri", Font.BOLD | Font.ITALIC, 18)); // Fuente elegante
        setBorder(BorderFactory.createLineBorder(new Color(60, 90, 200), 3)); // Borde colorido
    }

    public void cambiarTamaño(int tamaño) {
        switch (tamaño) {
            case 1 -> this.setFont(new Font("Arial", Font.PLAIN, 12)); // Pequeño
            case 2 -> this.setFont(new Font("Arial", Font.PLAIN, 16)); // Mediano
            case 3 -> this.setFont(new Font("Arial", Font.BOLD, 24)); // Grande
            default -> throw new IllegalArgumentException("Tamaño inválido: " + tamaño);
        }
    }
}
