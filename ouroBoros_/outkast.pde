class Outkast{
	PVector location, velocity, acceleration;
	PVector noff;
	float maxSpeed, maxForce;
	int mass;
	float x, y, x1, y1;

	Outkast(int margin){
		int side = int(random(0, 3.9));
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
		maxForce = 1.2;
	}

	void run(){
		update();
		display();
	}

	void update(){
		/*acceleration.x = map(noise(noff.x), 0, 1, -1, 1);
		acceleration.y = map(noise(noff.y), 0, 1, -1, 1);*/

    velocity.add(new PVector(map(noise(noff.x), 0, 1, -1, 1),map(noise(noff.y), 0, 1, -1, 1)).mult(3));
		velocity.add(acceleration);
		velocity.limit(maxSpeed);
		location.add(velocity);

		//acceleration.mult(0);
		noff.add(0.01, 0.05, 0.001);	
	}

	void applyForce(PVector force) {
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

  	void boundaries() {
  		
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
	    
  	void display(){
  		noStroke();
  		fill(255, 255);
  		ellipse(location.x, location.y, 5, 5);
  	}
}