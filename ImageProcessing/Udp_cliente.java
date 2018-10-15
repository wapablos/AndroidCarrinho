package udp_cliente;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.net.Socket;
import javax.imageio.ImageIO;

/**
 *
 * @author Camila Novaes
 */

// Salva apenas uma foto e fecha a conexão
// Apenas para teste
public class Udp_cliente {
    public static void main(String[] args) {
        
        try {
            Socket cliente = new Socket("192.168.0.20",9191); // (SERVER, PORTA)
            DataInputStream is = new DataInputStream(cliente.getInputStream());
            /*
            TOKENS PARA CONTROLE DAS IMAGENS ENVIADAS
            O SERVIDOR COMEÇA ENVIANDO UM TOKEN (INT TOKEN = 4),
            POSTERIORMENTE ENVIA OUTRO TOKEN (TOKEN UTF = '#@@#') PARA 
            EM SEGUIDA ENVIAR O TAMANHO DO ARRAY (INT LENGTH) 
            DA IMAGEM QUE SERÁ ENVIADA
            E POR FIM ENVIA UM OUTRO TOKEN (TOCKEN UTF = '-@@-') PARA 
            INDICAR QUE O PRÓXIMO BYTE É O
            COMEÇO DO ARRAY DE IMAGEM.
            */
            int token = is.readInt(); 
            if (token == 4){
                if (is.readUTF().equals("#@@#")){
                    int imgLength = is.readInt();
                    System.out.println("Lenght:" + imgLength);
                    System.out.println("Tocken 02:" + is.readUTF());
            
                    byte[] buffer = new byte[imgLength];
                    int len = 0;
            
                while (len < imgLength) {
                    len += is.read(buffer, len, imgLength - len);
                }
            
                System.out.println("Buffer:" + buffer);
                BufferedImage image = ImageIO.read( new ByteArrayInputStream(buffer ));
                ImageIO.write(image, "BMP", new File("image.bmp"));
            }
        }
            is.close();
            System.out.println("Conexão encerrada");
        }
        
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }    
}
