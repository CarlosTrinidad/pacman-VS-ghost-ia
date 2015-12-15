package game.entries.ghosts;

import game.controllers.GhostController;
import game.core.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getActions() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.ghosts.mypackage).
 */
public class MyGhosts implements GhostController
{
	

	public boolean Debugging = false;
	public static final int distancia_agrupamiento=20;
	public static final int distancia_maxPacman=14;
    public static final int distancia_puntoPoder=20;

	
	public MyGhosts(boolean debugging){
		
		Debugging = debugging;
	}
	
	public int[] GhostState = {} ;

	
	//Place your game logic here to play the game as the ghosts
	public int[] getActions(Game game,long timeDue)
	{	
		//Se crea un vector de direciones dependiendo del numero de fantasmas en el juego
		// 	NUM_GHOST se puede modificar en Game.java
        int[] acciones = new int[Game.NUM_GHOSTS];

        
        //Obtenemos la posicion actual de Ms PacMan con la funcion getCurPacManLoc()
		int msPosicion=game.getCurPacManLoc();
		
		//Apartir de aqui se definen las acciones para cada fantasma
		 for(int i=0;i<acciones.length;i++)      
	        {
			 //Se efectua un if caso de que el fantasma no este disponible y no requiera que se le asigne una accion
	        	if(game.ghostRequiresAction(i))
	        	{
	        		//En primer lugar se verifica si los fantasmas estan agrupados y si ademas estos se encuentran lejos del objetivo
	        		if(estanAgrupados(game) && lejosDePacMan(game,game.getCurGhostLoc(i))){
	        			//Al entrar en este estado se decide ir donde estan colocadas los puntos de poder o powerups 
	        			
	        			acciones[i]=getModoDefensa(game,i);                          	
	        		}else if(game.getEdibleTime(i)>0 || cercaPuntoPoder(game)){
	        			
	        			//Si pacman nos puede comer o si esta cerca de un punto de poder, nos alejaremos de pacman
	        			acciones[i]=game.getNextGhostDir(i,msPosicion,false,Game.DM.PATH);

	        			//Si ninguna de estas se cumple  entonces nos dirigimos hacia pacman
	        		}else{        		
	        			//asignamos las acciones del fantasma i y lo mandamos hacia la ubicacion de pacman
	        			acciones[i]=game.getNextGhostDir(i,msPosicion,true,Game.DM.PATH);       			
	        		}
	        	}
	        }
	        
		 // Retornamos el conjuto de acciones definidas
	        return acciones;

	}	
    private boolean cercaPuntoPoder(Game game)
    {
    	//En esta funcion se determina si pacman se encuentra lo suficientemente cerca del punto de poder
    	int msPosicion=game.getCurPacManLoc();
    	int[] puntosPoder=game.getPowerPillIndicesActive();
    	
    	for(int i=0;i<puntosPoder.length;i++){
    		if(game.getPathDistance(puntosPoder[i],msPosicion)<distancia_puntoPoder){
    			return true;
    		}
    	}
        return false;
    }

    private boolean lejosDePacMan(Game game,int location)
    {
    	//En esta funcion determinamos si nos encontramos lejos de pacman
    	if(game.getPathDistance(game.getCurPacManLoc(),location)>distancia_maxPacman){
    		return false;
    	}
    	return true;
    }

    private boolean estanAgrupados(Game game)
    {
    	//En esta funcion determinamos si los fantasmas estan mas cerca del minimo determinado
    	
    	
        float distancia=0;
        //Calculamos la distancia de cada fantasma respecto a los otros fantasmas
        for (int i=0;i<Game.NUM_GHOSTS-1;i++){
            for(int j=i+1;j<Game.NUM_GHOSTS;j++){
                distancia= distancia + game.getPathDistance(game.getCurGhostLoc(i),game.getCurGhostLoc(j));
            }
        }
        
        //Al tener la distancia total dividimos entre el numero de combinaciones posibles y verificamos
        //si este resultado es menor al permitido.
        if((distancia/6)<distancia_agrupamiento){
        	return true;
        }else{
        	return false;
        }
        
    }

    private int getModoDefensa(Game game,int index)
    {
    	//En esta funcion se determina si el fantasma debe ir hacia el pacaman o ir hacia los puntos de poder
        if(game.getEdibleTime(index)==0 && game.getPathDistance(game.getCurGhostLoc(index),game.getCurPacManLoc())<distancia_maxPacman){
        	//si el fantasma no esta azul y esta cerca de pacman entonces se dirige al pacman
            return game.getNextGhostDir(index,game.getCurPacManLoc(),true,Game.DM.MANHATTEN);
        }
        else{
        	//de lo contrario se dirige a los puntos de poder
            return game.getNextGhostDir(index,game.getPowerPillIndices()[index],true,Game.DM.MANHATTEN);
        }
    }

	
}