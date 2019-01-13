/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Esteganografia;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Andressa
 */
public class Principal {

    // Variáveis de programa
    private Visao view;
    private Esteganografia model;

    private JPanel decodificar_panel;
    private JPanel codificar_panel;
    private JTextArea input;
    private JButton codificarButton, decodificarButton;
    private JLabel image_input;

    // Variáveis de menu
    private JMenuItem codificar;
    private JMenuItem decodificar;
    private JMenuItem exit;

    private Codificar enc;
    private Decodificar dec;
    private CodificarButton encButton;
    private DecodificarButton decButton;

    private String stat_path = "";
    private String stat_name = "";

    public Principal(Visao aView, Esteganografia aModel) {

        view = aView;
        model = aModel;

        // 2 views
        codificar_panel = view.getTextPanel();
        decodificar_panel = view.getImagePanel();

        input = view.getText();
        image_input = view.getImageInput();

        // 2 botões
        codificarButton = view.getEButton();
        decodificarButton = view.getDButton();

        // menu
        codificar = view.getCodifica();
        decodificar = view.getDecodifica();
        exit = view.getExit();

        enc = new Codificar();
        codificar.addActionListener(enc);
        dec = new Decodificar();
        decodificar.addActionListener(dec);
        exit.addActionListener(new Exit());
        encButton = new CodificarButton();
        codificarButton.addActionListener(encButton);
        decButton = new DecodificarButton();
        decodificarButton.addActionListener(decButton);

        codificar_view();
    }

    private void codificar_view() {
        update();
        view.setContentPane(codificar_panel);
        view.setVisible(true);
    }

    private void decodificar_view() {
        update();
        view.setContentPane(decodificar_panel);
        view.setVisible(true);
    }

    private class Codificar implements ActionListener {

        // evento click do mouse
        @Override
        public void actionPerformed(ActionEvent e) {
            codificar_view();
        }
    }

    private class Decodificar implements ActionListener {

        // evento click do mouse
        @Override
        public void actionPerformed(ActionEvent e) {
            decodificar_view();
            JFileChooser chooser = new JFileChooser("./");
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setFileFilter(new Filtro());
            int returnVal = chooser.showOpenDialog(view);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File directory = chooser.getSelectedFile();
                try {
                    String image = directory.getPath();
                    stat_name = directory.getName();
                    stat_path = directory.getPath();
                    stat_path = stat_path.substring(0, stat_path.length() - stat_name.length() - 1);
                    stat_name = stat_name.substring(0, stat_name.length() - 4);
                    image_input.setIcon(new ImageIcon(ImageIO.read(new File(image))));
                } catch (Exception except) {
                    JOptionPane.showMessageDialog(view, "Arquivo não pode ser aberto.",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private class Exit implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private class CodificarButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser("./");
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setFileFilter(new Filtro());
            int returnVal = chooser.showOpenDialog(view);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File directory = chooser.getSelectedFile();
                try {
                    String text = input.getText();
                    String ext = Filtro.getExtension(directory);
                    String name = directory.getName();
                    String path = directory.getPath();
                    path = path.substring(0, path.length() - name.length() - 1);
                    name = name.substring(0, name.length() - 4);

                    String stegan = JOptionPane.showInputDialog(view,
                            "Nome do arquivo de saída:", "Nome do arquivo",
                            JOptionPane.PLAIN_MESSAGE);

                    if (model.codificar(path, name, ext, stegan, text)) {
                        JOptionPane.showMessageDialog(view, "Imagem codificada com sucesso.",
                                "Success!", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(view, "A imagem não pode ser codificada.",
                                "Error!", JOptionPane.INFORMATION_MESSAGE);
                    }
                    decodificar_view();
                    image_input.setIcon(new ImageIcon(ImageIO.read(new File(path + "/" + stegan + ".png"))));
                } catch (Exception except) {
                    JOptionPane.showMessageDialog(view, "Arquivo não pôde ser aberto.",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

    }

    private class DecodificarButton implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String message = model.decodificar(stat_path, stat_name);
            System.out.println(stat_path + ", " + stat_name);
            if (message != "") {
                codificar_view();
                JOptionPane.showMessageDialog(view, "Imagem decodificada com sucesso.",
                        "Success!", JOptionPane.INFORMATION_MESSAGE);
                input.setText(message);
            } else {
                JOptionPane.showMessageDialog(view, "A imagem não pôde ser decodificada",
                        "Error!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void update() {
        input.setText("");			// clear area de texto
        image_input.setIcon(null);	// clear imagem
        stat_path = "";				// clear path
        stat_name = "";				// clear name
    }

    public static void main(String args[]) {
        new Principal(
                new Visao("Esteganografia"),
                new Esteganografia()
        );
    }
}
