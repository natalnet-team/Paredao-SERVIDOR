package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

public class Arduino extends Thread{
	private int taxa;
	private String porta;
	private boolean iniciado;
	private CommPortIdentifier portId;
	private SerialPort port;
	OutputStream saida;
	OutputStreamWriter escrevedor;
	InputStream entrada;
	
	
	
	public Arduino(String _porta, int _taxa){
		porta = _porta;
		taxa = _taxa;
	}
	
	public void run(){
		iniciado = true;
		try {
			portId = CommPortIdentifier.getPortIdentifier(porta);
		} catch (NoSuchPortException e) {
			iniciado = false;
			e.printStackTrace();
		}
		if(iniciado)
		try {
			port = (SerialPort) portId.open("Comunicação serial", taxa);
		} catch (PortInUseException e) {
			iniciado = false;
			e.printStackTrace();
		}
		if(iniciado)
		try {
			
			saida = port.getOutputStream();
			entrada = port.getInputStream();
					
			port.setSerialPortParams(this.taxa, //taxa de transferência da porta serial 
                    SerialPort.DATABITS_8, //taxa de 10 bits 8 (envio)
                    SerialPort.STOPBITS_1, //taxa de 10 bits 1 (recebimento)
                    SerialPort.PARITY_NONE); //receber e enviar dados
			
		} catch (Exception e) {
			iniciado = false;
			e.printStackTrace();
		}
		
//		System.out.println("Arduino >> Conexao USB realizada com sucesso ..");
		
		while(iniciado){
			
			//TODO Leitura Arduino (não é nescessária para o projeto)
			
//			try {
//				entrada.read(b);
//				
//				System.out.println(b.toString());
//				
//				System.err.println("Ok 04");
//			} catch (IOException e) {
//				System.err.println("Erro 04");
//				e.printStackTrace();
//			}
		}
		
		System.out.println("Arduino >> Encerrada conexao");
	}
	
	public void close() {
	    if(iniciado)
		try {
	        saida.close();
	        entrada.close();
	        iniciado = false;
	    }catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	public void enviaDados(String opcao){
		System.out.println("Arduino >> Enviando: " + opcao);
	    if(iniciado)
		try {
//			byte[] bytes = opcao.getBytes();
//		    for (int i=0; i<bytes.length; i++)
//		        saida.write(bytes[i]);
			saida.write(opcao.getBytes());
		    
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	iniciado = false;
	    }
	} 
	
	public boolean isIniciado(){
		return iniciado;
	}
}
