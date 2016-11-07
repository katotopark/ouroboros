class Ouro {
	PVector location, velocity, acceleration;
	float mass;
	float gConstant;
	float maxForce, maxSpeed;
	color col;
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
		maxForce = 0.3;
		limitVel = 3;
		margin = margin_;
		gConstant = c;
		mass = m;
		fallOff = fallOff_;
		noff = new PVector (random(1000), random(1000), random(1000));
		eatenBoros = 0;
	}

	void update(){
		if(!protagonist){ 
			acceleration.x = map(noise(noff.x), 0, 1, -0.1, 0.1);
			acceleration.y = map(noise(noff.y), 0, 1, -0.1, 0.1);
			//acceleration.z = map(noise(noff.z), 0, 1, -1, 1);
		}

		velocity.add(acceleration);
		velocity.limit(limitVel);
		location.add(velocity);
		acceleration.mult(0);
		noff.add(radicalizer, radicalizer, radicalizer);

		if(eatenBoros % 20 == 0 && mass < 20){
			mass+=0.01;
		}
	}

	void applyForce(PVector force){
		PVector f = PVector.div(force, mass);
		acceleration.add(f); 
	}

	PVector seek(PVector target) {
		PVector desired = PVector.sub(target,location);  
		desired.setMag(maxSpeed*50);

		// Steering = Desired minus Velocity
		PVector steer = PVector.sub(desired,velocity);
		steer.limit(maxForce*50);  
		return steer;
	}
	
	void checkEdges() {
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
	PVector influence(Boro boro, float gConstant_){
	  	PVector force = PVector.sub(location, boro.location);
	  	float distance = force.mag();
	  	distance = constrain(distance, 5.0, 25.0);
	  	float strength = (gConstant * mass * boro.mass) / sq(distance);
	  	force.normalize();
	  	force.mult(strength);
	  	return force;
	}

	void display(){
		noStroke();
		float r = map(noise(noff.x),0,1,0,255);
		float g = map(noise(noff.y),0,1,0,255);
		float b = map(noise(noff.z),0,1,0,255);
		col = color(r, g, b);
		fill(col);
		radius = mass * 8;
		ellipse(location.x, location.y, radius, radius);
		noFill();
		strokeWeight(0.5);
		stroke(225, 100);
		ellipse(location.x, location.y, radius + fallOff, radius + fallOff);
		fill(0);
	}
}