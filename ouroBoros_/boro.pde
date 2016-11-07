class Boro{
	PVector location, velocity, acceleration;
	PVector force;
	PVector noff;
	float mass;
  	color col;
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

	void update(){
		velocity.add(acceleration);
		location.add(velocity);
		acceleration.mult(0);
	}

	void applyForce(PVector force){
		PVector f = PVector.div(force, mass);
		acceleration.add(f); 
	}

	void aim(PVector target, color c){
		PVector desired = PVector.sub(target,location);
		desired.setMag(mass*1.50);
		pushMatrix();
		translate(location.x, location.y);
		stroke(c);
		strokeWeight(2);
		line(0, 0, desired.x, desired.y); 
		popMatrix();

	}

	void display(ArrayList<Ouro> ouros){
		noStroke();
		for (Ouro ou:ouros){
			PVector distance = PVector.sub(ou.location, location);
			float d = distance.mag();
			if (d < (ou.radius + fallOff)){
				color from = col;
				color to = color(ou.col);
				float influence = map(d, ou.radius, ou.radius + fallOff, 1.0, 0.0);
				color interA = lerpColor(from, to, influence);
				fill(interA, 225);

				if (d < (ou.radius + fallOff)/2){
					mass += 0.25;
					mass = constrain(mass, 2, 25);
				}

			}
			else /*(d > (ou.radius + fallOff)/2 && col != color(255))*/{
				fill(col, 225);
			}
		}
		ellipse(location.x, location.y, mass, mass);
	}

	void lilNoise(float increment){
		PVector noi = new PVector(0, 0); 
		noi.x = map(noise(noff.x), 0, 1, -0.01, 0.01);
		noi.y = map(noise(noff.y), 0, 1, -0.01, 0.01);

		applyForce(noi);
		noff.add(increment, increment);


	}
	
	void flock(ArrayList<Boro> bo, ArrayList<Ouro> ouros) {
		maxSpeed = map(boInfluence, 0, 100, 1, 2);
		maxForce = map(boInfluence, 0, 100, 0.5, 1);
	    // Arbitrarily weight these forces
	    //awt = map(oInfluence, 0, 100, -1, 0.2);
	    separate(bo).mult(1);
	    align(bo).mult(2);
	    cohesion(bo).mult(1.5);
		
	    // Add the force vectors to 
	    for(int i = 0; i < ouros.size(); i++){
	    applyForce(seek(ouros.get(i).location));
	}
	    applyForce(separate(bo));
	    applyForce(align(bo));
	    applyForce(cohesion(bo));
		
	}

	PVector seek(PVector target) {
		maxSpeed_s = map(influence, 100, 500, 0.5, 2);
		maxForce_s = map(influence, 100, 500, 0.5, 2);
		PVector desired = PVector.sub(target,location);  

		desired.setMag(maxSpeed_s);

		PVector steer = PVector.sub(desired,velocity);
		steer.limit(maxForce_s);

		return steer;
	}


	PVector separate (ArrayList<Boro> bo) {
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
	      	sum.setMag(maxSpeed*2.5);
	      	// Steering = Desired - Velocity
	      	sum.sub(velocity);
	      	sum.limit(maxForce*7.5);
	    }
	    return sum;
	}

	PVector align (ArrayList<Boro> bo) {
	    float neighbordist = 50.0;
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

	PVector cohesion (ArrayList<Boro> bo) {
	    float neighbordist = 200.0;
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

    void arrive(ArrayList<Ouro> ouros) {
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


	void borders(){
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
	boolean isInside(Ouro ou){
		PVector distance = PVector.sub(ou.location, location);
		float d = distance.mag();
		if (d <= ou.radius/2 + mass/2){
			return true;
		}else{
			return false;
		}
	}


	PVector drag(Environment env){
		float viscosity = map(tolerance, -100, 100, 0.0001, 0.001);
		float speed = velocity.mag();
		float dragMag = sq(speed) * viscosity;
		PVector drag = velocity.get();
		drag.normalize();
		drag.mult(-1);
		drag.mult(dragMag);
		return drag;
	}
}