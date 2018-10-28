package tcp;
 
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import br.ufrn.musica.bse.player.Player;
import br.ufrn.musica.bse.tools.Composer;

public class Servidor {
	final static int porta = 12000;
	ServerSocket servidor;
	static Arduino connArduino;
	static ControleSL controleSL;
	static Composer composer;
    static Player player;
	
    public void iniciar() {
        try {
            // criando um socket para ouvir na porta e com uma fila de tamanho 20
            servidor = new ServerSocket(porta, 20);
            
            while (true) {
                System.out.println("Servidor >> Ouvindo na porta: " + porta);
 
                //ficara bloqueado aqui ate' alguem cliente se conectar
                Socket conexao = servidor.accept();
                // Abre uma thread conexao a cada novo cliente que se conecta
                Conexao connThread = new Conexao(conexao, conexao.getInetAddress().getHostAddress(), controleSL, connArduino);
                connThread.start();
                
            }
        } catch (Exception e) {
            try {
				servidor.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
        
        try {
			servidor.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
 
    public static void main(String[] args) {
 
    	//Lidar com a comunicacao com Arduino
        connArduino = new Arduino("COM5", 9600);
        connArduino.start();
        
        Composer composer = new Composer();
        Player player = new Player();
        composer.addPlayerListener(player);
        player.start();
        
        //Objeto ControleSL é para controle do dados recebidos das varias conexoes e da comunicacao com Arduino para controle de iluminacao
        controleSL = new ControleSL(connArduino, composer, player);
        controleSL.start();
        
        Servidor s = new Servidor();
        s.iniciar();
    }
}