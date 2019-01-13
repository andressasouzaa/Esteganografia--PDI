/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Esteganografia;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
 
/**
 *
 * @author Andressa
 */
public class Esteganografia {
    /*
     *Esteganografia Empty Constructor
     */

    public Esteganografia() {
    }

    /* Codifica uma imagem com texto
     * arquivo de saída .png
     */
    public boolean codificar(String path, String original, String ext1, String stegan, String message) {
        String file_name = image_path(path, original, ext1);
        BufferedImage image_orig = getImage(file_name);
        BufferedImage image = user_space(image_orig);
        image = add_text(image, message);

        return (setImage(image, new File(image_path(path, stegan, "png")), "png"));
    }

    public String decodificar(String path, String name) {
        byte[] decodificar;
        try {
            BufferedImage image = user_space(getImage(image_path(path, name, "png")));
            decodificar = decodificar_text(get_byte_data(image));
            return (new String(decodificar));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Não há mensagem escondida nessa imagem.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }

    /* Retorna uma string na forma: caminho/nome.extensao */
    private String image_path(String path, String name, String ext) {
        return path + "/" + name + "." + ext;
    }

    /* Método get para imagem selecionada, retorna imagem se possível */
    private BufferedImage getImage(String f) {
        BufferedImage image = null;
        File file = new File(f);

        try {
            image = ImageIO.read(file);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Imagem é possível abrir essa imagem.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return image;
    }

    /* Método set para salvar imagem */
    private boolean setImage(BufferedImage image, File file, String ext) {
        try {
            file.delete();
            ImageIO.write(image, ext, file);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Arquivo não salvo.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /* Adicionar texto em imagem
     * get nos bytes da imagem e nos bytes do texto
     * envia para funcao codificar_text enviando a mensagem e seu tamanho     
     */
    private BufferedImage add_text(BufferedImage image, String text) {
        //converte todos os atributos em bytes: image, message, message length
        byte img[] = get_byte_data(image);
        byte msg[] = text.getBytes();
        byte len[] = bit_conversion(msg.length);
        try {
            codificar_text(img, len, 0); //0 primeira posicao
            codificar_text(img, msg, 32); //4 bytes de espaco para o tamanho: 4bytes*8bit = 32 bits
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Arquivo selecionado não suporta mensagem.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return image;
    }

    /* recria imagem com texto inserido no espaço (aux) de usuário
     * nova imagem será criada com as mesmas dimensões da original
     * uma vez que a imagem esteja no espaço de usuário podemos modifica-la
     */
    private BufferedImage user_space(BufferedImage image) {
        BufferedImage new_img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = new_img.createGraphics();
        graphics.drawRenderedImage(image, null);
        graphics.dispose();
        return new_img;
    }

    /* Método get para array de bytes da imagem */
    private byte[] get_byte_data(BufferedImage image) {
        WritableRaster raster = image.getRaster();
        DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
        return buffer.getData();
    }

    /* Método para conversao de bits 
     * valor máx de um byte 127. Altera a partir de 8 para cima
     * removerá os bits mais a esquerda substituindo por zero
     * FF = 11111111, 0000000011111111 = 255 or 0x00FF
     */
    private byte[] bit_conversion(int i) {
        //utilizando 4 bytes
        byte byte3 = (byte) ((i & 0xFF000000) >>> 24); //0
        byte byte2 = (byte) ((i & 0x00FF0000) >>> 16); //0
        byte byte1 = (byte) ((i & 0x0000FF00) >>> 8); //0
        byte byte0 = (byte) ((i & 0x000000FF));
        return (new byte[]{byte3, byte2, byte1, byte0});
    }

    /*
     * Uniao do array do texto com o array da imagem
     * image = array que representa dados da imagem
     * addition = array de dados para adicionar aos dados do array da imagem     
     * retorna array com dados da imagem e texto 'mesclados' na variavel image
     */
    private byte[] codificar_text(byte[] image, byte[] addition, int offset) {
        if (addition.length + offset > image.length) {
            throw new IllegalArgumentException("Arquivo muito grande.");
        }
        // laço para cada byte adicionado
        for (int i = 0; i < addition.length; ++i) {
            // laço para cada 8 bits de um byte
            int add = addition[i];  // byte atual
            /* >>> 7, >>> 6, >>> 5, >>> 4...
             * e aplicando op AND, b = 0000 & 0001 = 0 = b
             * b será 0 ou 1 
             */
            for (int bit = 7; bit >= 0; --bit, ++offset) {
                int b = (add >>> bit) & 1;
                /* 0xFE = 11111110. Bit menos significativo igual a 0
                 * fazemos op OR com 'b' que será 00000000 ou 00000001
                 * codificar_text avanca em 'offset' conforme o loop continua
                 * os 8 bits de 1 byte são alterados por seus 8 bits menos significativos
                 * de 8 bytes sequenciais da imagem
                 */
                image[offset] = (byte) ((image[offset] & 0xFE) | b);
            }
        }
        return image;
    }

    /* Retorna texto escondido da imagem */
    private byte[] decodificar_text(byte[] image) {
        int length = 0;
        int offset = 32;
        for (int i = 0; i < 32; ++i) {
            /* movemos os bits da esquerda para 1 e então
             * fazemos a op OR com o resultado do bit menos significante do byte
             * '& 1' limpará todos os bits, exceto o último bit  
             */
            length = (length << 1) | (image[i] & 1);
        }

        byte[] result = new byte[length];
        /* result = array de byte com os bits
         * com ele, faz-se o laço pelos bytes da imagem
         */
        for (int b = 0; b < result.length; ++b) {
            //laço para cada bit de um byte de texto
            for (int i = 0; i < 8; ++i, ++offset) {
                // result = vetor de bytes feito pelo bit menos significativo de cada byte
                result[b] = (byte) ((result[b] << 1) | (image[offset] & 1));
            }
        }
        return result;
    }

}
