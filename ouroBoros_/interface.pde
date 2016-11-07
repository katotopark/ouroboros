import controlP5.*;

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

	void build(){
		cp5.addCallback();
		color gFg = color(200, 200, 200, 120);
		color gLabel = color(255);
		color gBg = color(187, 255, 0, 120);
		color sFg = color(120,120);
		color sAct = color(50);
		color sBg = color(100,120);
		color rBg = sBg;
		color rFg = sFg;
		color rAct = gBg;

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
  

 