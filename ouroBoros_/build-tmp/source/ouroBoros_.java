import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import processing.pdf.*; 
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ouroBoros_ extends PApplet {




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



public void setup(){
	
	frameRate(60);
	

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

public void controlEvent(CallbackEvent theEvent) {
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
				radicalizer = map(val, -100, 100, 0.0001f, 0.2f);
			}
		}

		if(s == "PROTAGONIST"){
			if(val == 0.0f){
				protagonist = false;
				println("protagonist: OFF");
			}
			if (val == 1.0f) {
			    protagonist = true;
			    println("protagonist: ON");
			}
				
		}
		
		if(s == "POP UP"){
			
			if (val == 1.0f) {
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

public void controlEvent(ControlEvent theEvent) {
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


public void draw() {
	
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

public void keyPressed() {
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







	
class Boro{
	PVector location, velocity, acceleration;
	PVector force;
	PVector noff;
	float mass;
  	int col;
  	//int r,g,b;
  	float maxSpeed_s, maxSpeed;
  	float maxForce_s, maxForce;
  	int bWidth, bHeight;
  	float cwt, swt, awt;
  	//ArrayList<Ouro> ouros;
	
	Boro(float x, float y){
		location = new PVector(x, y);
		velocity = new PVector();
		acceleration = new PVector();
		noff = new PVector(random(100), random(100), random(100));
		col = color(255, 255, 255);

		
		
		mass = map(noise(noff.z), 0, 1, 3, 8);
		bWidth = width - margin/2;
		bHeight = height - margin/2;
	}

	public void update(){
		velocity.add(acceleration);
		location.add(velocity);
		acceleration.mult(0);
	}

	public void applyForce(PVector force){
		PVector f = PVector.div(force, mass);
		acceleration.add(f); 
	}

	public void aim(PVector target, int c){
		PVector desired = PVector.sub(target,location);
		desired.setMag(mass*1.50f);
		pushMatrix();
		translate(location.x, location.y);
		stroke(c);
		strokeWeight(2);
		line(0, 0, desired.x, desired.y); 
		popMatrix();

	}

	public void display(ArrayList<Ouro> ouros){
		noStroke();
		for (Ouro ou:ouros){
			PVector distance = PVector.sub(ou.location, location);
			float d = distance.mag();
			if (d < (ou.radius + fallOff)){
				int from = col;
				int to = color(ou.col);
				float influence = map(d, ou.radius, ou.radius + fallOff, 1.0f, 0.0f);
				int interA = lerpColor(from, to, influence);
				fill(interA, 225);

				if (d < (ou.radius + fallOff)/2){
					mass += 0.25f;
					mass = constrain(mass, 2, 25);
				}

			}
			else /*(d > (ou.radius + fallOff)/2 && col != color(255))*/{
				fill(col, 225);
			}
		}
		ellipse(location.x, location.y, mass, mass);
	}

	public void lilNoise(float increment){
		PVector noi = new PVector(0, 0); 
		noi.x = map(noise(noff.x), 0, 1, -0.01f, 0.01f);
		noi.y = map(noise(noff.y), 0, 1, -0.01f, 0.01f);

		applyForce(noi);
		noff.add(increment, increment);


	}
	
	public void flock(ArrayList<Boro> bo, ArrayList<Ouro> ouros) {
		maxSpeed = map(boInfluence, 0, 100, 1, 2);
		maxForce = map(boInfluence, 0, 100, 0.5f, 1);
	    // Arbitrarily weight these forces
	    //awt = map(oInfluence, 0, 100, -1, 0.2);
	    separate(bo).mult(1);
	    align(bo).mult(2);
	    cohesion(bo).mult(1.5f);
		
	    // Add the force vectors to 
	    for(int i = 0; i < ouros.size(); i++){
	    applyForce(seek(ouros.get(i).location));
	}
	    applyForce(separate(bo));
	    applyForce(align(bo));
	    applyForce(cohesion(bo));
		
	}

	public PVector seek(PVector target) {
		maxSpeed_s = map(influence, 100, 500, 0.5f, 2);
		maxForce_s = map(influence, 100, 500, 0.5f, 2);
		PVector desired = PVector.sub(target,location);  

		desired.setMag(maxSpeed_s);

		PVector steer = PVector.sub(desired,velocity);
		steer.limit(maxForce_s);

		return steer;
	}


	public PVector separate (ArrayList<Boro> bo) {
	    float desiredseparation = mass;
	    PVector sum = new PVector();
	    int count = 0;
	    // For every boid in the system, check if it's too close
	    for (Boro other : bo) {
	      	float d = PVector.dist(location, other.location);
	      	// If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
	      	if ((d > 0) && (d < desiredseparation)) {
	        	// Calculate vector pointing away from neighbor
	        	PVector diff = PVector.sub(location, other.location);
	        	diff.normalize();
	        	diff.div(d);        // Weight by distance
	        	sum.add(diff);
	        	count++;            // Keep track of how many
	      	}
	    }
	    if (count > 0) {
	    	sum.div(count);
	      	// desired vector is the average scaled to maximum speed
	      	sum.setMag(maxSpeed*2.5f);
	      	// Steering = Desired - Velocity
	      	sum.sub(velocity);
	      	sum.limit(maxForce*7.5f);
	    }
	    return sum;
	}

	public PVector align (ArrayList<Boro> bo) {
	    float neighbordist = 50.0f;
	    PVector steer = new PVector();
	    int count = 0;
	    for (Boro other : bo) {
	      	float d = PVector.dist(location,other.location);
	      	if ((d > 0) && (d < neighbordist)) {
	        	steer.add(other.velocity);
	        	count++;
	      	}
	    }
	    if (count > 0) {
	      	steer.div((float)count);
	      	// Steering = Desired - Velocity
	      	steer.normalize();
		    steer.mult(maxSpeed);
		    steer.sub(velocity);
		    steer.limit(maxForce);
	    }
	    return steer;
	}

	public PVector cohesion (ArrayList<Boro> bo) {
	    float neighbordist = 200.0f;
	    PVector sum = new PVector(0,0);   // Start with empty vector to accumulate all locations
	    int count = 0;
	    for (Boro other : bo) {
	      	float d = PVector.dist(location,other.location);
	      	if ((d > 0) && (d < neighbordist)) {
	        	sum.add(other.location); // Add location
	        	count++;
	      	}
	    }
	    if (count > 0) {
	      	sum.div((float)count);
	      	return seek(sum);  // Steer towards the location
	    }
	    return sum;
    	//applyForce(sum);
    }

    public void arrive(ArrayList<Ouro> ouros) {
    	for(Ouro ou:ouros){
			PVector desired = PVector.sub(ou.location,location);  // A vector pointing from the location to the target
			float d = desired.mag();
			// Normalize desired and scale with arbitrary damping within 100 pixels
			desired.normalize();
			if (d < (ou.radius + fallOff)/2) {
				  float m = map(d, 0, (ou.radius + fallOff)/2, 0, maxSpeed);
				  desired.mult(-5*m);
				} else {
				  desired.mult(maxSpeed);
			}
			PVector steer = PVector.sub(desired,velocity);
		    steer.limit(maxForce); 
		    applyForce(steer);
		}
    }


	public void borders(){
		if (location.x > width-(margin+mass)/2) {
			location.x = width-(margin+mass)/2;
			velocity.x *= -1;
		} else if (location.x < (margin+mass)/2) {
			velocity.x *= -1;
			location.x = (margin+mass)/2;
		}

		if (location.y > height-(margin+mass)/2) {
			velocity.y *= -1;
			location.y = height-(margin+mass)/2;
		}
		else if(location.y < (margin+mass)/2) {
			velocity.y *= -1;
			location.y = (margin+mass)/2;
		}
	}

	//checks if boro is eaten by ouro
	public boolean isInside(Ouro ou){
		PVector distance = PVector.sub(ou.location, location);
		float d = distance.mag();
		if (d <= ou.radius/2 + mass/2){
			return true;
		}else{
			return false;
		}
	}


	public PVector drag(Environment env){
		float viscosity = map(tolerance, -100, 100, 0.0001f, 0.001f);
		float speed = velocity.mag();
		float dragMag = sq(speed) * viscosity;
		PVector drag = velocity.get();
		drag.normalize();
		drag.mult(-1);
		drag.mult(dragMag);
		return drag;
	}
}
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


	public void addBoros(){
		for (int i = 0; i < population; i++){
			boros.add(new Boro(random(margin/2, boWidth), random(margin/2, boHeight)));
		}
	}


	public void updatePopulation(int newPopulation){
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

	public void runBoros(Environment env, ArrayList<Ouro> ouros){

      
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

	public void removeBoros(ArrayList<Ouro> ous){
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
class Environment {
	float c;
	int bVisibility;
	int rows, columns, rRows, rCols;
  
  
	Environment (float c_, int m_, int spacing_){
    	c = c_;
		margin = m_;
		spacing = spacing_;
		bVisibility = 255;
		
	}
	public void display(){
		rows = (width - margin) / spacing;
		columns = (height - margin) / spacing;
		rRows = round(rows);
		rCols = round(columns);
		
		fill(map(spacing,10, 40, 5, 30),225);
		rectMode(CENTER);
		pushMatrix();
		translate(width / 2, height / 2);
		noStroke();
		rect(0, 0, width, height);
		strokeWeight(map(bVisibility, 0, 255, 1, 4));
		stroke(225, bVisibility);
		rect(0, 0, width - margin, height - margin);
		popMatrix();

		// griddy stuff
		pushMatrix();
		translate(margin/2, margin/2);
		for (int i = 0; i < rRows-1; i++){
			for (int j = 0; j < rCols-1; j++){
				if(spacing >= 20){
					strokeWeight(1.5f);
				}
				else{
					strokeWeight(1);	
				}
				stroke(255);
				point ((spacing + i*spacing), (spacing + j*spacing));
			}
		}
		popMatrix();

		/*
		if(millis() % 10000 == 0){
			pushMatrix();
			translate(width/2, height/2);
			fill(255, 0, 0);
			rectMode(CENTER);
			rect(0, 0, 100, 100);
			popMatrix();
		}
		*/
	}

  	/*void influence(Boro boro){
 
   	PVector force = PVector.sub(location, boro.location);
    float distance = force.mag();
    distance = constrain(distance, 5.0, 25.0);

    force.normalize();
    float strength = (gConstant * mass * boro.mass) / sq(distance);
    force.mult(strength);
    return force;
  	}
	*/
	public void counter(){
		textSize(9);
		fill(255);
		String  s = millis() + "ms since the start";
		float sw = textWidth(s);
		text(s, width - sw, 10);

	}
}


class Interface{

	ControlP5 cp5;
	Group env, ouro, boros;
	Accordion accordion;
	DropdownList d2;
	int env_H, bo_H, ou_H;
	int sliderPosX,sliderPosY, sliderGap;
	int sliderSizeX, sliderSizeY;
	int groupWidth;
	

	Interface(PApplet this_){
		cp5  = new ControlP5(this_);
		//s1 = new CallbackListener(this);
		sliderPosX = 10;
		sliderPosY = 10;
		sliderGap = 25;
		sliderSizeX = 160;
		sliderSizeY = 10;
		groupWidth = 180;
		env_H = sliderSizeY*2 + sliderGap*1;
		bo_H = sliderSizeY*2 + sliderGap*3;
		ou_H = sliderSizeY*2 + sliderGap*5;

		//sliderNames = new String [] {"TOLERANCE", "BOUNDARY VISIBILITY", "INFLUENCE", "FALLOFF", "QUALITY", "POPULATION", "OBEDIENCE", "INFLUENCE "};
		//groupNames = new String [] {"ENVIRONMENT", "OURO", "BOROS"};
	}

	public void build(){
		cp5.addCallback();
		int gFg = color(200, 200, 200, 120);
		int gLabel = color(255);
		int gBg = color(187, 255, 0, 120);
		int sFg = color(120,120);
		int sAct = color(50);
		int sBg = color(100,120);
		int rBg = sBg;
		int rFg = sFg;
		int rAct = gBg;

		String env_slider1 = "TOLERANCE";
		String env_slider2 = "BOUNDARY DENIAL";
		String ouro_slider1 = "INFLUENCE";
		String ouro_slider2 = "FALLOFF";
		String ouro_slider3 = "PROPERTY";
		String boros_slider1 = "POPULATION";
		String boros_slider2 = "OBEDIENCE";
		String boros_slider3 = "AMBITION"; // added a space to fix the identical name problem!--------

		
		env = cp5.addGroup("ENVIRONMENT")
	                .setPosition(100,50)
	                .setBarHeight(10)
	                .setColorForeground(gFg)
	                .setColorLabel(gLabel)
	                .setColorBackground(gBg)
	                .setColorValue(color(255,126,240))
	                .setBackgroundHeight(env_H)
	                .setBackgroundColor(color(100, 100))
	                ;

	    ouro = cp5.addGroup("OURO")
	                .setPosition(100,150)
	                .setBarHeight(10)
	                .setColorForeground(gFg)
	                .setColorLabel(gLabel)
	                .setColorBackground(gBg)
	                .setBackgroundHeight(ou_H)
	                .setBackgroundColor(color(100, 100))
	                ;

	    boros = cp5.addGroup("BOROS")
	                .setPosition(100,270)
	                .setBarHeight(10)
	                .setColorForeground(gFg)
	                .setColorLabel(gLabel)
	                .setColorBackground(gBg)
	                .setBackgroundHeight(bo_H)
	                .setBackgroundColor(color(100,100))
	                ;

	    // environment sliders
	    cp5.addSlider(env_slider1)
	    .setPosition(sliderPosX,sliderPosY)
	    .setSize(sliderSizeX,sliderSizeY)
	    .setSliderMode(Slider.FLEXIBLE)
	    .setRange(-100, 100)
	    .setColorForeground(sFg)
	    .setColorBackground(sBg)
	    .setColorActive(sAct)
	    .setGroup(env)
	    ;
	     
	    cp5.addSlider(env_slider2)
	    .setPosition(sliderPosX,sliderPosY+sliderGap)
	    .setSize(sliderSizeX,sliderSizeY)
	    .setColorForeground(sFg)
	    .setColorBackground(sBg)
	    .setColorActive(sAct)
	    .setGroup(env)
	    ;

	    // ouro sliders
	    cp5.addSlider(ouro_slider1)
	    .setPosition(sliderPosX,sliderPosY)
	    .setSize(sliderSizeX,sliderSizeY)
	    .setColorForeground(sFg)
	    .setColorBackground(sBg)
	    .setColorActive(sAct)
	    .setGroup(ouro)
	    ;

	    cp5.addSlider(ouro_slider2)
	    .setPosition(sliderPosX,sliderPosY+sliderGap)
	    .setSize(sliderSizeX,sliderSizeY)
	    .setColorForeground(sFg)
	    .setColorBackground(sBg)
	    .setColorActive(sAct)
	    .setGroup(ouro)
	    ;

	    cp5.addSlider(ouro_slider3)
	    .setSliderMode(Slider.FLEXIBLE)
	    .setRange(-100, 100)
	    .setDefaultValue(0)
	    .setHandleSize(20)
	    .setPosition(sliderPosX,sliderPosY+sliderGap*2)
	    .setSize(sliderSizeX,sliderSizeY)
	    .setColorForeground(sFg)
	    .setColorBackground(sBg)
	    .setColorActive(sAct)
	    .setGroup(ouro)
	    ;

	    // CHECKBOX - OURO

	    cp5.addCheckBox("check1")
        .setPosition(10,90)
        .setSize(20,20)
        .setColorActive(rAct)
        .setColorLabel(color(255))
        .setColorForeground(rFg)
        .setColorBackground(rBg)
        .setItemsPerRow(1)
        .setSpacingColumn(60)
        .addItem("POP UP",0)
        .moveTo(ouro)
        ;

        cp5.addCheckBox("check2")
        .setPosition(10,112)
        .setSize(20,20)
        .setColorActive(rAct)
        .setColorLabel(color(255))
        .setColorForeground(rFg)
        .setColorBackground(rBg)
        .setItemsPerRow(1)
        .setSpacingColumn(60)
        .addItem("PROTAGONIST", 0)
        .moveTo(ouro)
        ;

	    //boros sliders
	    cp5.addSlider(boros_slider1)
	    .setPosition(sliderPosX,sliderPosY)
	    .setSize(sliderSizeX,sliderSizeY)
	    .setColorForeground(sFg)
	    .setColorBackground(sBg)
	    .setColorActive(sAct)
	    .setGroup(boros)
	    ;

	    cp5.addSlider(boros_slider2)
	    .setPosition(sliderPosX,sliderPosY+sliderGap)
	    .setSize(sliderSizeX,sliderSizeY)
	    .setColorForeground(sFg)
	    .setColorBackground(sBg)
	    .setColorActive(sAct)
	    .setGroup(boros)
	    ;

	    cp5.addSlider(boros_slider3)
	    .setPosition(sliderPosX,sliderPosY+sliderGap*2)
	    .setSize(sliderSizeX,sliderSizeY)
	    .setColorForeground(sFg)
	    .setColorBackground(sBg)
	    .setColorActive(sAct)
	    .setGroup(boros)
	    ;
	    
	    // caption alignment
	    cp5.getController(env_slider1).getCaptionLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
	    cp5.getController(env_slider2).getCaptionLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
	    cp5.getController(ouro_slider1).getCaptionLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
	    cp5.getController(ouro_slider2).getCaptionLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
	    cp5.getController(ouro_slider3).getCaptionLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
	    cp5.getController(boros_slider1).getCaptionLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
	    cp5.getController(boros_slider2).getCaptionLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
	    cp5.getController(boros_slider3).getCaptionLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);

		
	    accordion = cp5.addAccordion("acc")
	                 .setPosition(0,0)
	                 .setWidth(180)
	                 .addItem(env)
	                 .addItem(ouro)
	                 .addItem(boros)
	                 ;

		accordion.setCollapseMode(Accordion.MULTI);
		// unsuccesful looping efforts ----------------------------------------

		/*
		for(int i = 0; i < groupNames.length; i++){
			group = "g" + i;
			for(int j = 0; j < groupNames.length; j++){
				String groupTitle = groupNames[j];
				gCreate = cp5.addGroup(groupTitle)
			                .setPosition(100,50)
			                .setWidth(groupWidth)
			                .setBackgroundHeight(100)
			                .setBackgroundColor(color(100))
			                ;
			}

		} 
		for(int i = 0; i < sliderNames.length; i++){
			String sliderTitle = sliderNames[i];
						cp5.addSlider(sliderTitle)
					    .setPosition(sliderPosX,sliderPosY)
					    .setSize(sliderSizeX,sliderSizeY)
					    .setGroup(group)
					    ;
					    cp5.getController(sliderTitle).getCaptionLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
				
			}
		*/

	}
}
  

 
class Ouro {
	PVector location, velocity, acceleration;
	float mass;
	float gConstant;
	float maxForce, maxSpeed;
	int col;
	float r, g, b;
	float radius;
	PVector noff;
	float limitVel;
	int increment;
	int eatenBoros;
	
	Ouro(float m, float c, int margin_, int fallOff_){
		location = new PVector(random(margin/2, width-margin/2), random(margin/2, height-margin/2));
		velocity = new PVector(0, 0);
		acceleration = new PVector(0, 0);
		maxSpeed = 3;
		maxForce = 0.3f;
		limitVel = 3;
		margin = margin_;
		gConstant = c;
		mass = m;
		fallOff = fallOff_;
		noff = new PVector (random(1000), random(1000), random(1000));
		eatenBoros = 0;
	}

	public void update(){
		if(!protagonist){ 
			acceleration.x = map(noise(noff.x), 0, 1, -0.1f, 0.1f);
			acceleration.y = map(noise(noff.y), 0, 1, -0.1f, 0.1f);
			//acceleration.z = map(noise(noff.z), 0, 1, -1, 1);
		}

		velocity.add(acceleration);
		velocity.limit(limitVel);
		location.add(velocity);
		acceleration.mult(0);
		noff.add(radicalizer, radicalizer, radicalizer);

		if(eatenBoros % 20 == 0 && mass < 20){
			mass+=0.01f;
		}
	}

	public void applyForce(PVector force){
		PVector f = PVector.div(force, mass);
		acceleration.add(f); 
	}

	public PVector seek(PVector target) {
		PVector desired = PVector.sub(target,location);  
		desired.setMag(maxSpeed*50);

		// Steering = Desired minus Velocity
		PVector steer = PVector.sub(desired,velocity);
		steer.limit(maxForce*50);  
		return steer;
	}
	
	public void checkEdges() {
		float r = mass*8;
		if (location.x > width-(margin+r)/2) {
			location.x = width-(margin+r)/2;
			velocity.x *= -1;
		} else if (location.x < (margin+r)/2) {
			velocity.x *= -1;
			location.x = (margin+r)/2;
		}

		if (location.y > height-(margin+r)/2) {
			velocity.y *= -1;
			location.y = height-(margin+r)/2;
		}
		else if(location.y < (margin+r)/2) {
			velocity.y *= -1;
			location.y = (margin+r)/2;
		}
	}
	

	// gravitational attraction
	public PVector influence(Boro boro, float gConstant_){
	  	PVector force = PVector.sub(location, boro.location);
	  	float distance = force.mag();
	  	distance = constrain(distance, 5.0f, 25.0f);
	  	float strength = (gConstant * mass * boro.mass) / sq(distance);
	  	force.normalize();
	  	force.mult(strength);
	  	return force;
	}

	public void display(){
		noStroke();
		float r = map(noise(noff.x),0,1,0,255);
		float g = map(noise(noff.y),0,1,0,255);
		float b = map(noise(noff.z),0,1,0,255);
		col = color(r, g, b);
		fill(col);
		radius = mass * 8;
		ellipse(location.x, location.y, radius, radius);
		noFill();
		strokeWeight(0.5f);
		stroke(225, 100);
		ellipse(location.x, location.y, radius + fallOff, radius + fallOff);
		fill(0);
	}
}
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

	public void addOuros(){
		ouros.add (new Ouro(8, influence, margin, fallOff));
			
	}

	public void runOuros(){
		
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

	public void popup(){
		if(isPopUpEnabled){
			if(ouros.size() < 3){
				if (!protagonist && ou.eatenBoros > 0 && ou.eatenBoros % 20 == 0){
					ouros.add (new Ouro(8, influence, margin, fallOff));
					ou.eatenBoros = 1;
				}
			}
		}
	}

	public Ouro getOne(){
		for (int  i = ouros.size() - 1; i >= 0; i--){
			ou = (Ouro) ouros.get(i);
		}
		return ou;	
	}

	public void labeling(){
		fill(255);
		for (int  i = ouros.size() - 1; i >= 0; i--){
			ou = (Ouro) ouros.get(i);
			textSize(ou.mass*1.5f);
			textFont(font);
			String s = "T.0" + (i+1);
			text(s, (ou.location.x + ou.mass/2), ou.location.y);
		}
	}
	
	public void counter(){
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
class Outkast{
	PVector location, velocity, acceleration;
	PVector noff;
	float maxSpeed, maxForce;
	int mass;
	float x, y, x1, y1;

	Outkast(int margin){
		int side = PApplet.parseInt(random(0, 3.9f));
	    switch(side) {
	      case 0: 
	        x = random(width);
	        y = random(margin/2/2);
	        break;
	      case 1: 
	        x = random(width);
	        y = random(height-margin/2/2, height);
	        break;
	      case 2: 
	        x = random(margin/2/2);
	        y = random(height);
	        break;
	      case 3: 
	        x = random(width-margin/2/2, width);
	        y = random(height);
	        break;
	    }
		

		location = new PVector(x, y);
		velocity = new PVector();
		acceleration = new PVector(0, 0);
		noff = new PVector(random(1000), random(1000), random(1000));
		maxSpeed = 1;
		maxForce = 1.2f;
	}

	public void run(){
		update();
		display();
	}

	public void update(){
		/*acceleration.x = map(noise(noff.x), 0, 1, -1, 1);
		acceleration.y = map(noise(noff.y), 0, 1, -1, 1);*/

    velocity.add(new PVector(map(noise(noff.x), 0, 1, -1, 1),map(noise(noff.y), 0, 1, -1, 1)).mult(3));
		velocity.add(acceleration);
		velocity.limit(maxSpeed);
		location.add(velocity);

		//acceleration.mult(0);
		noff.add(0.01f, 0.05f, 0.001f);	
	}

	public void applyForce(PVector force) {
    	acceleration.add(force);
  	}

		
	  	/*
	  	PVector separate () {
		    float desiredseparation = mass*1;
		    PVector sum = new PVector();
		    int count = 0;
		    // For every boid in the system, check if it's too close
		    for (Outkast outkast : bo) {
		      	float margin/2 = PVector.dist(location, other.location);
		      	// If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
		      	if ((margin/2 > 0) && (margin/2 < desiredseparation)) {
		        	// Calculate vector pointing away from neighbor
		        	PVector diff = PVector.sub(location, other.location);
		        	diff.normalize();
		        	diff.div(margin/2);        // Weight by distance
		        	sum.add(diff);
		        	count++;            // Keep track of how many
		      	}
		    }
		    // Average -- divide by how many
		    if (count > 0) {
		    	sum.div(count);
		      	// desired vector is the average scaled to maximum speed
		      	sum.normalize();
		      	sum.mult(maxSpeed);
		      	// Steering = Desired - Velocity
		      	sum.sub(velocity);
		      	sum.limit(maxForce);
		    }
		    return sum;
		}
		*/

  	public void boundaries() {
  		
  		PVector desired = null;
	    boolean insideBox = false;
	    boolean xHit = false;
	    
	    if (location.x > margin/2 && location.x < width - margin/2 && location.y > margin/2 && location.y < height-margin/2){
	      insideBox = true;
	      if((location.x > margin/2) || (location.x < width - margin/2))
	        xHit = true;
	      else
	        xHit = false;
	    }
	      
	      
	    else if (location.x < 0 || location.x > width || location.y < 0 || location.y > height){
	      insideBox = true;
	      if(location.x < 0 || location.x > width)
	        xHit = true;
	      else
	        xHit = false;
	    }
	      
	    if(insideBox){
	      if(xHit){
  
	        if(location.x > width || (location.x > margin/2 && location.x < width/2))
	          desired = new PVector(-maxSpeed, velocity.y);
	         if(location.x < 0 || (location.x > width / 2 && location.x < width -margin/2 ))
	          desired = new PVector(maxSpeed, velocity.y);
	      }
	        
	      else{
	        if(location.y > height || (location.y > margin/2 && location.y < height/2))
	          desired = new PVector(velocity.x, - maxSpeed);
	         if(location.y < 0 || (location.y > height / 2 && location.y < height -margin/2 ))
	          desired = new PVector(velocity.x, maxSpeed);
	         
	      }
	        
	    }
	    if (desired != null) {
	      desired.normalize();
	      desired.mult(maxSpeed);
	      PVector steer = PVector.sub(desired, velocity);
	      steer.limit(maxForce);
	      applyForce(steer);
		}

	  //println(xHit+":"+insideBox);
	  
  	}
	    
  	public void display(){
  		noStroke();
  		fill(255, 255);
  		ellipse(location.x, location.y, 5, 5);
  	}
}
  public void settings() { 	size (1280, 720, P2D); 	smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ouroBoros_" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
