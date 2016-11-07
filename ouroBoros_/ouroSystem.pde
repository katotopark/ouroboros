class OuroSystem{
	ArrayList<Ouro> ouros;

	int influence;
	Ouro ou;
	PVector mouse;
	PFont font;
	boolean isPopUpEnabled = false;

	OuroSystem(int influence_, int fallOff_, boolean protagonist_){
		ouros = new ArrayList<Ouro>();

		influence = influence_;
		fallOff = fallOff_;
		protagonist = protagonist_;
		font = createFont("CoreSansD45Medium", 12);
	}

	void addOuros(){
		ouros.add (new Ouro(8, influence, margin, fallOff));
			
	}

	void runOuros(){
		
		for (Ouro ou:ouros){
			ou.update();
			ou.checkEdges();
			ou.display();
			
			
			if(protagonist && mousePressed == true){
				mouse = new PVector(pmouseX, pmouseY);
				ou.applyForce(ou.seek(mouse));
			}
      	labeling();
			/*
			for (Boro bo:boros){
				ou.applyForce(ou.influence(bo, influence));
			}*/
		}
	}

	void popup(){
		if(isPopUpEnabled){
			if(ouros.size() < 3){
				if (!protagonist && ou.eatenBoros > 0 && ou.eatenBoros % 20 == 0){
					ouros.add (new Ouro(8, influence, margin, fallOff));
					ou.eatenBoros = 1;
				}
			}
		}
	}

	Ouro getOne(){
		for (int  i = ouros.size() - 1; i >= 0; i--){
			ou = (Ouro) ouros.get(i);
		}
		return ou;	
	}

	void labeling(){
		fill(255);
		for (int  i = ouros.size() - 1; i >= 0; i--){
			ou = (Ouro) ouros.get(i);
			textSize(ou.mass*1.5);
			textFont(font);
			String s = "T.0" + (i+1);
			text(s, (ou.location.x + ou.mass/2), ou.location.y);
		}
	}
	
	void counter(){
		textSize(9);
		fill(255);
		String  s1 = "# of trends being set: " 
					+ ouros.size();
		float s1_w = textWidth(s1);
		text(s1, width - s1_w, 20);

		String  s2 = "# of successful trend adoptions: " 
					+ ou.eatenBoros;
		float s2_w = textWidth(s2);
		text(s2, width - s2_w, 30);
	}
}