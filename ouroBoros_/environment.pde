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
	void display(){
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
					strokeWeight(1.5);
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
	void counter(){
		textSize(9);
		fill(255);
		String  s = millis() + "ms since the start";
		float sw = textWidth(s);
		text(s, width - sw, 10);

	}
}