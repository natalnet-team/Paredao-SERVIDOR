package tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Conexao extends Thread {
	ControleSL controleSL;
	ObjectOutputStream saida;
    ObjectInputStream entrada;
    Socket conexao;
    String mensagem = "";
    String ip;
    Arduino connArduino;

	
	public Conexao(Socket _conexao, String _ip, ControleSL _cont, Arduino _cA){
		conexao = _conexao;
		ip = _ip;
		controleSL = _cont;
		connArduino = _cA;
	}
	
	public void run(){
		//obtendo os fluxos de entrada e de saida
		try {
			saida = new ObjectOutputStream(conexao.getOutputStream());
			entrada = new ObjectInputStream(conexao.getInputStream());
			System.out.println("Conexao " + ip + " >> " + "Conexao estabelecida com sucesso...");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		

        do {//fica aqui ate' o cliente enviar a mensagem FIM
        	 //obtendo a mensagem enviada pelo cliente
        	try {
        		if(entrada.readObject() ==  true || entrada.readObject() == false){ //se a entrada for um valor Boolean
        			ligar = (Boolean) entrada.readObject();
        				connArduino.enviaDados(ligar); //enviando valor Boolean para arduino
				}
        		else{
                	mensagem = (String) entrada.readObject();
                	char letra = mensagem.charAt(0);
                	if (letra >= '0' && letra <= '9'){
	            	    double x = Double.parseDouble(mensagem);
	            	    //Limitador:
	                
	            	    if(x > 50) x = 50;
	            	    else if(x < 0) x = 0;
	                
	            	    controleSL.addValor(x);
                	}else{
                		connArduino.enviaDados(mensagem);
                	}
                
            	}
        	} catch (IOException | ClassNotFoundException iOException) {
                break;
            }
        } while (true);

        System.out.println("Conexao encerrada pelo cliente");
        
		try {
			saida.close();
			entrada.close();
			conexao.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
