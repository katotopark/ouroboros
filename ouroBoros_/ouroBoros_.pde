import controlP5.*;
import processing.pdf.*;

boolean recording;
PGraphicsPDF pdf;

BoroSystem boroSystem;
OuroSystem ouroSystem;
Environment environment;
Interface gui;
ArrayList<Outkast> outkasts;

CallbackListener cb;

//CONTROLS
int margin;
int population;
int boWidth, boHeight;
float tolerance;
int influence, boInfluence;
int fallOff;
boolean protagonist;
int spacing;
float radicalizer;
float val;
String s;

// put the listener here



void setup(){
	size (1280, 720, P2D);
	frameRate(60);
	smooth();

	pdf = (PGraphicsPDF) createGraphics(width, height, PDF, "ouroBoros_2.pdf");

	boWidth = width - margin;
	boHeight = height - margin;
	  
	gui  = new Interface(this);
	gui.build();

  outkasts = new ArrayList<Outkast>();
  for (int i = 0; i < 100; i++){
    outkasts.add(new Outkast(margin));
  }
  
	environment = new Environment (tolerance, margin, spacing);
	ouroSystem = new OuroSystem(influence, fallOff, protagonist);
	ouroSystem.addOuros();
	boroSystem = new BoroSystem(population, boWidth, boHeight);
	boroSystem.addBoros();

	
}

void controlEvent(CallbackEvent theEvent) {
		String s = theEvent.getController().getName();
		float val = theEvent.getController().getValue();
		
		//println(s, val);


		//environment controls
		if(s == "TOLERANCE"){
			tolerance = map(val, -100, 100, -1000, 0);
			margin = (int)map(val, -100, 100, 300, 50);
			spacing = (int)map(val, -100, 100, 10, 40);
			//println(tolerance);
		}
		
		if(s == "BOUNDARY DENIAL"){
			if (theEvent.getAction() == ControlP5.ACTION_BROADCAST){
				environment.bVisibility = (int)map(val, 0, 100, 255, 0);
			}
		}

		//ouro controls
		if(s == "FALLOFF"){
			if (theEvent.getAction() == ControlP5.ACTION_BROADCAST){
				fallOff = (int)map(val, 0, 100, 100, 400);
			}
		}

		if(s == "PROPERTY"){
			if (theEvent.getAction() == ControlP5.ACTION_BROADCAST){
				radicalizer = map(val, -100, 100, 0.0001, 0.2);
			}
		}

		if(s == "PROTAGONIST"){
			if(val == 0.0){
				protagonist = false;
				println("protagonist: OFF");
			}
			if (val == 1.0) {
			    protagonist = true;
			    println("protagonist: ON");
			}
				
		}
		
		if(s == "POP UP"){
			
			if (val == 1.0) {
			    println("pop up: ON");
			    ouroSystem.isPopUpEnabled = true;
			}
			else
				ouroSystem.isPopUpEnabled = false;   
			}
		
			//println(theEvent.getAction());
			
				
		

		//boro controls
		if(s == "POPULATION"){
			if (theEvent.getAction() == ControlP5.ACTION_BROADCAST){
				boroSystem.updatePopulation((int)map(val, 0, 100, 100, 500));
				
			}
		}
		
		if(s == "OBEDIENCE"){
			if (theEvent.getAction() == ControlP5.ACTION_BROADCAST){
				influence = (int)map(val, 0, 100, 100, 500);
			}
			
		}
		if(s == "AMBITION"){
			if (theEvent.getAction() == ControlP5.ACTION_BROADCAST){
				boInfluence = (int)val;
			}
		}
	
}

void controlEvent(ControlEvent theEvent) {
	if(theEvent.isGroup()) {
	    println("got an event from group "
	            +theEvent.getGroup().getName()
	            +", isOpen? "+theEvent.getGroup().isOpen()
	            );
	  		            
	} else if (theEvent.isController()){
	  		    println("got something from a controller "
	            +theEvent.getController().getName()
	            + ", the new value is: "
	            +theEvent.getController().getValue()
	            );
	}
}


void draw() {
	
	for (Outkast ka: outkasts){
		fill(255);
		ka.boundaries();
		ka.run();
	}
	
	environment.display();
	//environment.counter();
	
	boroSystem.runBoros(environment, ouroSystem.ouros);
	boroSystem.removeBoros(ouroSystem.ouros);
	ouroSystem.runOuros();
	ouroSystem.popup();
	//ouroSystem.counter();
	//ouroSystem.labeling();
}

void keyPressed() {
  	if (key == 'r') {
	    if (recording) {
	      endRecord();
	      println("Recording stopped.");
	      recording = false;
	    } else {
	      beginRecord(pdf);
	      println("Recording started.");
	      recording = true;
	    }
	} else if (key == 'q') {
	    if (recording) {
	      endRecord();
	    }
	    exit();
	}  
}







	