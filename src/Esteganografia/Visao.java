/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Esteganografia;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Andressa
 */
public class Visao extends JFrame {

    // dimensões da tela
    private static int WIDTH = 500;
    private static int HEIGHT = 400;

    private JTextArea input;
    private JScrollBar scroll, scroll2;
    private JButton codificaButton, decodificaButton;
    private JLabel image_input;

    private JMenu file;
    private JMenuItem codifica;
    private JMenuItem decodifica;
    private JMenuItem exit;

    public Visao(String name) {
        super(name);

        // Menubar
        JMenuBar menu = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic('F');
        codifica = new JMenuItem("codifica");
        codifica.setMnemonic('E');
        file.add(codifica);
        decodifica = new JMenuItem("decodifica");
        decodifica.setMnemonic('D');
        file.add(decodifica);
        file.addSeparator();
        exit = new JMenuItem("Exit");
        exit.setMnemonic('x');
        file.add(exit);

        menu.add(file);
        setJMenuBar(menu);

        setResizable(true); // permite que janela seja redimensionada
        setBackground(Color.lightGray);
        setLocation(100, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
    }

    public JMenuItem getCodifica() {
        return codifica;
    }

    public JMenuItem getDecodifica() {
        return decodifica;
    }

    public JMenuItem getExit() {
        return exit;
    }

    public JTextArea getText() {
        return input;
    }

    public JLabel getImageInput() {
        return image_input;
    }

    public JPanel getTextPanel() {
        return new Text_Panel();
    }

    public JPanel getImagePanel() {
        return new Image_Panel();
    }

    public JButton getEButton() {
        return codificaButton;
    }

    public JButton getDButton() {
        return decodificaButton;
    }

    private class Text_Panel extends JPanel {

        public Text_Panel() {
            GridBagLayout layout = new GridBagLayout();
            GridBagConstraints layoutConstraints = new GridBagConstraints();
            setLayout(layout);

            input = new JTextArea();
            layoutConstraints.gridx = 0;
            layoutConstraints.gridy = 0;
            layoutConstraints.gridwidth = 1;
            layoutConstraints.gridheight = 1;
            layoutConstraints.fill = GridBagConstraints.BOTH;
            layoutConstraints.insets = new Insets(0, 0, 0, 0);
            layoutConstraints.anchor = GridBagConstraints.CENTER;
            layoutConstraints.weightx = 1.0;
            layoutConstraints.weighty = 50.0;
            JScrollPane scroll = new JScrollPane(input, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            layout.setConstraints(scroll, layoutConstraints);
            scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            add(scroll);

            codificaButton = new JButton("Codificar");
            layoutConstraints.gridx = 0;
            layoutConstraints.gridy = 1;
            layoutConstraints.gridwidth = 1;
            layoutConstraints.gridheight = 1;
            layoutConstraints.fill = GridBagConstraints.BOTH;
            layoutConstraints.insets = new Insets(0, -5, -5, -5);
            layoutConstraints.anchor = GridBagConstraints.CENTER;
            layoutConstraints.weightx = 1.0;
            layoutConstraints.weighty = 1.0;
            layout.setConstraints(codificaButton, layoutConstraints);
            add(codificaButton);

            setBackground(Color.lightGray);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }
    }

    private class Image_Panel extends JPanel {

        public Image_Panel() {
            GridBagLayout layout = new GridBagLayout();
            GridBagConstraints layoutConstraints = new GridBagConstraints();
            setLayout(layout);

            image_input = new JLabel();
            layoutConstraints.gridx = 0;
            layoutConstraints.gridy = 0;
            layoutConstraints.gridwidth = 1;
            layoutConstraints.gridheight = 1;
            layoutConstraints.fill = GridBagConstraints.BOTH;
            layoutConstraints.insets = new Insets(0, 0, 0, 0);
            layoutConstraints.anchor = GridBagConstraints.CENTER;
            layoutConstraints.weightx = 1.0;
            layoutConstraints.weighty = 50.0;
            JScrollPane scroll2 = new JScrollPane(image_input, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            layout.setConstraints(scroll2, layoutConstraints);
            scroll2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            image_input.setHorizontalAlignment(JLabel.CENTER);
            add(scroll2);

            decodificaButton = new JButton("Decodificar");
            layoutConstraints.gridx = 0;
            layoutConstraints.gridy = 1;
            layoutConstraints.gridwidth = 1;
            layoutConstraints.gridheight = 1;
            layoutConstraints.fill = GridBagConstraints.BOTH;
            layoutConstraints.insets = new Insets(0, -5, -5, -5);
            layoutConstraints.anchor = GridBagConstraints.CENTER;
            layoutConstraints.weightx = 1.0;
            layoutConstraints.weighty = 1.0;
            layout.setConstraints(decodificaButton, layoutConstraints);
            add(decodificaButton);

            setBackground(Color.lightGray);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }
    }

    public static void main(String args[]) {
        new Visao("Esteganografia");
    }

}
