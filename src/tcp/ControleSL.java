package tcp;

import br.ufrn.musica.bse.player.Player;
import br.ufrn.musica.bse.tools.Composer;
import br.ufrn.musica.bse.tools.RandomGenerator;

public class ControleSL extends Thread{
	Arduino connArduino;
	Composer composer;
    Player player;
	public double somatorio = 0;
	public int cont = 1;
	public int cont2 = 0;
	
	RandomGenerator generator;
	
	public ControleSL(Arduino _ar, Composer _com, Player _pla){
		connArduino = _ar;
		composer = _com;
		player = _pla;
		generator = new RandomGenerator();
	    generator.start();
	}
	
	public void run(){
		
		while(connArduino.isIniciado()){
			
			try {
				Thread.sleep(70);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			int valorUpdate = (int)(somatorio/cont);
			valorUpdate *= 7;
			if(valorUpdate > 50){
				valorUpdate = 50;
			}
			if(cont2 > 2){
				cont2 = 0;
				connArduino.enviaDados("0" + String.valueOf(valorUpdate) + ";");
			}else{
				cont2 += 1;
			}
			
			
			composer.update(valorUpdate);
			
//		 	int value = generator.getValue();
//		 	composer.update(value);
			
			somatorio = 2;
			cont = 1;
			
		}
		
		System.err.println("ControleSL >> Finalizado");
		
	}
	
	public void addValor(double _vl){
		somatorio += _vl;
		cont += 1;
	}
	
}
