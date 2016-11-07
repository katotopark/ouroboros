class BoroSystem{
	ArrayList<Boro> boros;
  ArrayList<Ouro> ouros;

	float x, y;
	//Ouro ou;

	BoroSystem(int population_, int boWidth_, int boHeight_){
		boros = new ArrayList<Boro>();

		population = population_;
		boWidth = boWidth_;
		boHeight = boHeight_;
		population = 100;
		//ou = ouroSystem.getOne();
	}


	void addBoros(){
		for (int i = 0; i < population; i++){
			boros.add(new Boro(random(margin/2, boWidth), random(margin/2, boHeight)));
		}
	}


	void updatePopulation(int newPopulation){
		int tempBoro = newPopulation - population;

		if(tempBoro > 0){
			for (int i = 0; i < newPopulation - population; i++){
				boros.add(new Boro(random(margin/2, boWidth), random(margin/2, boHeight)));
			}
		}
		else {
			for (int  i = boros.size() - newPopulation; i >= 0; i--){
				boros.remove(i);
			}
		}
		population = newPopulation;
	}

	void runBoros(Environment env, ArrayList<Ouro> ouros){

      
		for (int  i = boros.size() - 1; i >= 0; i--){
    		Boro bo = (Boro) boros.get(i);
	        bo.applyForce(bo.drag(env));
        for(Ouro ou:ouros){
	        bo.aim(ou.location, ou.col);
        }
    		bo.flock(boros, ouros);
    		bo.applyForce(bo.drag(env));
		    bo.update();
		    bo.lilNoise(400);
		    bo.arrive(ouros);
		    bo.borders();
		    bo.display(ouros);
		}
	}

	void removeBoros(ArrayList<Ouro> ous){
		for (int  i = boros.size() - 1; i >= 0; i--){
    		Boro bo = (Boro) boros.get(i);
		    for (Ouro ou:ous){
					if(bo.isInside(ou)){
						boros.remove(i);
						boros.add(new Boro(random(margin/2, boWidth), random(margin/2, boHeight)));
						ou.eatenBoros++;
	        		}
			}
		}
	}
}