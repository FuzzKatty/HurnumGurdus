package RadiumVestigiumGurdus;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;

public class GurdusMain extends JPanel{
	//camera position and rotation -- x, y, z, rotation vector {x, y, z}
	double[] CAM = {0,-20,0};
	//cam rotation vector
	double[] CRV = {0,0,1};
	//move speed
	double move_speed = 15;
	//custom colors
	float[] HSBNR = Color.RGBtoHSB(254, 0, 70, null);
	Color NiceRed = Color.getHSBColor(HSBNR[0],HSBNR[1],HSBNR[2]);
	float[] HSBNB = Color.RGBtoHSB(13, 136, 229, null);
	Color NiceBlue = Color.getHSBColor(HSBNB[0], HSBNB[1], HSBNB[2]);
	float[] HSBNG = Color.RGBtoHSB(11, 210, 74, null);
	Color NiceGreen = Color.getHSBColor(HSBNG[0], HSBNG[1], HSBNG[2]);
	float[] HSBNSK = Color.RGBtoHSB(232, 238, 255, null);
	Color NiceSkyColour = Color.getHSBColor(HSBNSK[0], HSBNSK[1], HSBNSK[2]);
	Color Ground = Color.gray;
	Color Sky = Color.decode("#f2f294");
	float[] HSBNY = Color.RGBtoHSB(255, 225, 1, null);
	Color NiceYellow = Color.getHSBColor(HSBNY[0], HSBNY[1], HSBNY[2]);
	float[] HSBNM = Color.RGBtoHSB(134, 228, 190, null);
	Color NiceMint = Color.getHSBColor(HSBNM[0],HSBNM[1],HSBNM[2]);
	Color NicePink = Color.decode("#FFC0CB");
	Color HotPink = Color.decode("#cf0e68");
	Color OrangeOrange = Color.decode("#db900d");
	Color[] colours = {NiceRed,NiceBlue,NiceGreen,NiceSkyColour, 
			NiceYellow, NiceMint,Ground,Sky,NicePink,HotPink,OrangeOrange};
	
	
	
	//how spread out to render
	int pixelation = 5;
	//desired resolution
	int desire = 3;
	boolean upset = false;
	int render_distance = 11500;
	
	//how close does the ray have to be to count as a hit??
	double collision_tolerance = 5;
	
	KeyboardInput keys;
	
	int cooldown = 4; // helps with button press
	
	double tilt = 0;
	
	boolean show_info = false;
	boolean fast = true;
	double stretch = 0.3;//0.9
	int wan = 1;
	public void togglewide() {
		if(angle == 30 && stretch == 0.3) {
			angle = 90;
			//System.out.println("set");
			stretch = 0.9;
		} else {
			//System.out.println("returned");
			angle = 30;
			stretch = 0.3;
		}
	}
	double yc = 0;
	double angle = 30;//90
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(NiceGreen);
		int w = this.getWidth();
		int h = this.getHeight();
		g.fillRect(0, 0, w, h);
		for(int x= 0; x<w;x+=pixelation) {
			double xrtcof = (x)-(this.getWidth()/2);
			//xrtcof /= (w/2);
			xrtcof /= (w*0.6);
			for(int y=0;y<h;y+=pixelation) {
				double yrtcof = (y)-(this.getHeight()/2);
				//yrtcof /= (h/2);
				yrtcof /= (h*0.6);
				//a_a_ is adjusted angle
				//double[] a_a_ = rthdv(CRV[0],CRV[1],CRV[2],-1*xrtcof*30,yrtcof*30);
				double[] a_a_ = rthdv(CRV[0],CRV[1],CRV[2],xrtcof*angle,yrtcof*angle);
				yc = yrtcof*stretch+tilt;
				//if(a_a_[0] < 0) {a_a_[1]*=-1;}
				//System.out.println(xrtcof*90);
				
				g.setColor(trace(
						//CAM[0],CAM[1],CAM[2],a_a_[0],a_a_[1],a_a_[2]
						CAM[0],CAM[1],CAM[2],a_a_[0],yc,a_a_[2]
								));
				g.fillRect(x, y, pixelation, pixelation);
			}
		}
		g.setColor(Color.BLACK);
		//show screen info
		if(show_info == true) {
			g.drawString("camera x: " + CAM[0], 11, 15);
			g.drawString("camera y: " + CAM[1], 11, 25);
			g.drawString("camera z: " + CAM[2], 11, 35);
			g.drawString("rotation: x direction: " + CRV[0], 11, 50);
			g.drawString("y direction: " + /*CRV[1]*/tilt, 11, 60);
			g.drawString("z direction: " + CRV[2], 11, 70);
			g.drawString("key code: " + keys.currentKeyCode, 11, 90);
			g.drawString("lens angle: " + angle, 11, 120);
			g.drawString("vertical warp: "+ stretch, 11, 140);
		} else {
			g.drawString("Q to show numbers", 11, 11);
			g.drawString("WASD to move and E to toggle view", 11, 31);
			g.drawString("ARROW KEYS to rotate camera", 11, 51);
			g.drawString("Z X C and V switch between", 11, 71);
			g.drawString("some pre determined scenes", 11, 81);
		}
	}
	
	public void keyboardControl() {
		int key = keys.currentKeyCode;
		double crx = CRV[0];
		double cry = CRV[1];
		double crz = CRV[2];
		double[] side_movement = {0,0,0};
		//System.out.println("key: " + key);
		if(key == 27) {System.exit(0);}
		if(key == 87) {moveCam(crx*move_speed,cry*move_speed,crz*move_speed);}//forwards
		if(key == 83) {moveCam(-crx*move_speed,-cry*move_speed,-crz*move_speed);}//backwards
		if(key == 32) {CAM[1] -= move_speed;}//up
		if(key == 16) {CAM[1] += move_speed;}//down
		if(key == 65) {side_movement = rthdv(crx,cry,crz,-90,0);}//left
		if(key == 68) {side_movement = rthdv(crx,cry,crz,90,0);}//right
		if(key == 37) {CRV = rthdv(crx, cry, crz, -3, 0);}//rotate left
		if(key == 39) {CRV = rthdv(crx, cry, crz, 3, 0);}//rotate right
		//if(key == 38) {CRV = rthdv(crx, cry, crz, 0, -7);}//rotate up
		//if(key == 40) {CRV = rthdv(crx, cry, crz, 0, 7);}//rotate down
		if(key == 38) {if(tilt>-0.4) {tilt -= 0.025;}}//up arrow
		if(key == 40) {if(tilt<0.4) {tilt += 0.025;}}//down arrow 
		if(key == 81 && cooldown == 0) {show_info = !show_info;cooldown = 11;repaint();}//toggle number visibility
		/*if(key == 79) {angle+=2;} // O makes wider angle lens
		if(key == 80) {angle -= 2;} // P makes narrower angle lens*/
		if(key==79 && cooldown == 0) {}// O toggles scan mode
		if(key == 80 && cooldown == 0) {fast = !fast;cooldown = 11;repaint();}// P toggles between render mode and fast mode
		if(key == 74) {}// J 
		if(key == 70) {objects[0][0] = CAM[0];objects[0][1] = CAM[1]; objects[0][2] = CAM[2];}
		if(key == 69 && cooldown == 0) {togglewide();cooldown = 11;}
		if(key >= 49 && key <= 57) {pixelation = key-48;}
		if(key == 90) {objects = new double[][] {
				{-20,30,-100,50,4,9,1},
				{80,-30,60,20,2,4},
				{0,-30,190,20,1,3},
				{-20,130,-100,50,4,9,1},
				{60,-40,190,2,3,8,800},
				{40,-40,150,2,3,8,800},
				{-120,-30,90,20,2,0},
				{-130,-50,141,3,3,5,500}
		};}
		if(key == 88) {objects = new double[][] {
				{-20,-30,100,200,5,9,20,110},
				{-20,-180,100,70,1,2},
				{-30,100,100,34,4,10,8},
				{0,-10,0,5,3,10,999},
				{0,-10,50,5,3,10,999},
				{0,-10,100,5,3,10,999},
				{0,-10,125,5,3,10,999},
				{0,-10,150,5,3,10,999},
				{-20,-350,100,200,5,0,20,110},
				{-20,350,50,200,4,1,9}
				};}
		if(key == 67) {objects = new double[][] {
				{-20,-15,100,50,3,4,13},
				{-20,-25,100,47.5,3,5,13},
				{-20,-35,100,45,3,8,13},
				{-20,-45,100,42.5,3,9,13},
				{-20,-55,100,40,3,10,13},
				{-20,-65,100,37.5,3,4,13},
				{-20,-75,100,35,3,5,13},
				{-20,-85,100,32.5,3,8,13},
				{-20,-95,100,30,3,9,13},
				{-20,-105,100,27.5,3,10,13},
				{-20,-115,100,25,3,4,13},
				{-20,-125,100,22.5,3,5,13},
				{-20,-135,100,20,3,8,13},
				{-20,-145,100,17.5,3,4,13},
				{-20,-155,100,15,3,5,13},
				{-20,-165,100,12.5,3,8,13},
				{-20,-175,100,10,3,9,13},
				{-20,-185,100,7.5,3,10,13},
				{-20,-195,100,5,3,4,13},
				{-20,-205,100,2.5,3,8,13},
		};}
		if(key == 86) {objects = new double[][] {
			{-10,-50,200,50,1,5},
			{-110,-50,200,45,2,8},
			{-210,-50,200,25,3,9,100},
			{310,64,-200,54,4,10,7},
		};}
		
		moveCam(side_movement[0]*move_speed,side_movement[1]*move_speed,side_movement[2]*move_speed);
	}
	double yp; double xp; double zp; double[] rotprev; double angleprev; double tiltprev;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GurdusMain G = new GurdusMain();
		//System.out.println(G.dtno(-1002, 201, 201));
		G.keys = new KeyboardInput();
		JFrame frame = new JFrame("HURNUM GURDUS 4 -- CAN RUN IN FULLSCREEN BUT FRAMERATE WILL BE LOW");
		frame.setSize((int)(400*1.618),400);
		frame.setVisible(true);
		frame.add(G);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(G.keys);
		frame.setResizable(true);
		//System.out.println(G.rthdv(1, 0, 0, 180, 0)[0]);
		//System.out.println(G.multiply_matrices(new double[][] {{1,1,1},{1,1,1},{1,1,1}},new double[][] {{2,2,2},{2,1,2},{2,2,2}})[1][1]);
		while(true) {
			try {
				if(G.cooldown > 0) {G.cooldown -= 1;}
				if(G.fast == false) {
					if(G.CAM[0]!=G.xp||G.CAM[1]!=G.yp||G.CAM[2]!=G.zp|| G.CRV != G.rotprev || G.angle != G.angleprev || G.tilt != G.tiltprev) {G.upset = true;G.pixelation = 13/*22*/;} else {G.upset = false;}
					if((!G.upset)&&G.pixelation>G.desire) {G.pixelation-=2;}
					G.xp=G.CAM[0];G.yp=G.CAM[1];G.zp=G.CAM[2]; G.rotprev = G.CRV; G.angleprev = G.angle; G.tiltprev = G.tilt;
				} else {G.pixelation = 12;}
				Thread.sleep(1000/32);
				//G.objects[0][0] -= 10; 
				/*System.out.println(G.rthdv(2,2,2,30,0)[0]);
				System.out.println(G.rthdv(2,2,2,30,0)[1]);
				System.out.println(G.rthdv(2,2,2,30,0)[2]);
				*/
				if(G.pixelation>G.desire || G.fast == true) {
					G.repaint();	
					//System.out.println("painted");
				}
				G.keyboardControl();
				//G.repaint();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	//distance between 2 points
	public double db(double x, double y, double z, double xx, double yy, double zz) {
		double xd = Math.abs(x-xx);
		double yd = Math.abs(y-yy);
		double zd = Math.abs(z-zz);
		return Math.sqrt((yd*yd)+(xd*xd)+(zd*zd));
	}
	public double db2d(double x, double y, double xx, double yy) {
		return Math.sqrt( (x-xx)*(x-xx) + (y-yy)*(y-yy) );
	}
	//copied matrix by vector multiplication code from online
	public static double[] multiply(double[][] matrix, double[] vector) {
	    int rows = matrix.length;
	    int columns = matrix[0].length;

	    double[] result = new double[rows];

	    for (int row = 0; row < rows; row++) {
	        double sum = 0;
	        for (int column = 0; column < columns; column++) {
	            sum += matrix[row][column] * vector[column];
	        }
	        result[row] = sum;
	    }
	    return result;
	}
	public double[][] multiply_matrices(double[][] m, double[][] m2) {
		double[][] result = new double[m.length][m[0].length];
		for(int row=0;row<m.length;row++) {
			for(int col=0;col<m[0].length;col++) {
				result[row][col] = m[row][col]*m2[col][row];
			}
		}
		return result;
	}
	//rotate three dimensional vector
	public double[] rthdv(double x, double y, double z, double angh, double angv) {
		double rrh = angh*(Math.PI/180);
		double rrv = 0;// = angv*(Math.PI/180);// <--
		//double rrv = 0;
		double rrt = 0;// = 0; // <--
		//using vector info and horizontal angle change and vertical angle change
		//vertical -- around x axis
		//double[][] rv = {{1,0,0},{0,Math.cos(rrv),-Math.sin(rrv)},{0,Math.sin(rrv),Math.cos(rrv)}};
		//horizontal -- around y axis
		//double[][] rh = {{Math.cos(rrh),0,Math.sin(rrh)},{0,1,0},{-Math.sin(rrh),0,Math.cos(rrh)}};
		//not currently used -- around z axis
		//double[][] rt = {{Math.cos(rrt),-Math.sin(rrt),0},{Math.sin(rrt),Math.cos(rrt),0},{0,0,1}};
		
		double[][] rv = {{Math.cos(rrv),-Math.sin(rrv),0},{Math.sin(rrv),Math.cos(rrv),0},{0,0,1}};
		double[][] rh = {{Math.cos(rrh),0,Math.sin(rrh)},{0,1,0},{-Math.sin(rrh),0,Math.cos(rrh)}};
		double[][] rt = {{1,0,0},{0,Math.cos(rrt),-Math.sin(rrt)},{0,Math.sin(rrt),Math.cos(rrt)}};
		//double[][] total_rotate = multiply_matrices(rt ,multiply_matrices(rv,rh));
		//first call multiply function with array x,y,z and rotation matrix for horizontal
		//then multiply this resulting array by rotation matrix for vertical
		double a = rrv;
		double b = rrh;
		double r = rrt;
		//double w = rrh, o = rrv, k = rrt;
		double[][] total_rotate = {
				{cos(a)*cos(b), (cos(a)*sin(b)*sin(r))-(sin(a)*cos(r)), (cos(a)*sin(b)*cos(r))+(sin(a)+sin(r))},
				{sin(a)*cos(b), (sin(a)*sin(b)*sin(r))+(cos(a)*cos(r)), (sin(a)*sin(b)*cos(r))-(cos(a)*sin(r))},
				{-sin(b), (cos(b)*sin(r)), (cos(b)*cos(r))}
				};
		/*double[][] total_rotate = {
				{cos(k)*cos(o), -sin(k)*cos(o), sin(o)},
				{((cos(k)*sin(w)*sin(o))+ (sin(k)*cos(w))), (cos(k)*cos(w))-(sin(k)*sin(w)*sin(o)), -sin(w)*cos(o)},
				{(sin(k)*sin(w))-(cos(k)*cos(w)*sin(o)), (sin(k)*cos(w)*sin(o))+(cos(k)*sin(w)), cos(w)*cos(o)}
		};*/
		//return multiply(total_rotate,new double[] {x,y,z});
		//rv,rh,rt x -- rh,rv,rt x -- rv,rt,rh x -- rh,rt,rv x -- rt,rv,rh x -- rt,rh,rv x
		return multiply(
				total_rotate, new double[] {x,y,z}
				);
	}
	public double sin(double in) { return Math.sin(in); }
	public double cos(double in) { return Math.cos(in); }
	
	public void moveCam(double x, double y, double z) {
		CAM[0] += x;
		CAM[1] += y;
		CAM[2] += z;
	}
	double skip = 1.9;
	double[][] empty = new double[][] {};
	public Color trace(double x, double y, double z, double xc, double yc, double zc) {
		Color found_color = NiceRed;
		//go x, go y, go z. these values are what changes, bc it might be useful to have the original position stored.
		double gx = x;
		double gy = y;
		double gz = z;
		/*
		double change_speed = (dtno(gx,gy,gz)*0.4)/ db(x,y,z,x+xc,y+yc,z+zc);
		//System.out.println(change_speed);
		gx += xc*change_speed;
		gx += yc*change_speed;
		gx += zc*change_speed;*/
		xc *= skip; yc *= skip; zc *= skip;
		//System.out.println(yc);
		boolean hit = false;
		boolean go_on = true;
		while(hit == false && go_on == true) {
			int hitt = hit_test(gx,gy,gz);
			if(hitt != -1) {
				if(hitt == -2) {
					hit = true;
					//to round to nearest x: Math.floor(num/x)*x
					if((Math.floor(Math.abs(gx)/25)*25)%50==(Math.floor(Math.abs(gz)/25)*25)%50) {found_color = Color.gray;} else {found_color = Color.darkGray;}
					//if((Math.floor(Math.abs(gx)/25)*25)%50==(Math.floor(Math.abs(gz)/25)*25)%50) {found_color = Color.darkGray;} else {found_color = Ground;}
				} else {
					hit = true;
					found_color = colours[(int)(objects[hitt][5])];
				}
			}
			//if farther away than the farthest object, just go really fast and run out of render distance.
			if(db(CAM[0],CAM[1],CAM[2],gx,gy,gz) > dtfo(CAM[0],CAM[1],CAM[2])+1050) {xc*= 6000;zc*=6000;if(yc<0) {yc*=6000;}}
			//if over *render distance* away from camera, don't keep going, set sky color.
			if(db(CAM[0],CAM[1],CAM[2],gx,gy,gz)>render_distance) { 
				found_color = Sky;
				go_on = false;
			}
			gx += xc;
			gy += yc;
			gz += zc;
			//System.out.println(db(gx,gy,gz,xc,yc,zc));
			//System.out.println(dtno(gx,gy,gz)/ db(x,y,z,xc,yc,zc));
			//System.out.println(dtno(gx,gy,gz));
		}
		if(shade(gx,gy,gz)) {found_color = found_color.darker().darker();}
		return found_color;
	}
	public boolean shade(double x, double y, double z) {
		if(db(CAM[0],CAM[1],CAM[2],x,y,z) >= render_distance-20) {return false;}
		boolean shaded = false;
		boolean go = true;
		double ox = x, oy = y, oz = z;
		double xc = 4, yc = -4, zc = -4;
		while(go) {
			if(db(ox,oy,oz,x,y,z)>1800) {
				go = false;
			}
			//xc *= 1.0001; yc *= 1.0001; zc *= 1.0001;
			x += xc;
			y += yc;
			z += zc;
			if(hit_test(x,y,z) != -1) {
				shaded = true; go = false;
			}
		}
		return shaded;
	}
	public boolean above(double x, double y, double z) {
		boolean above = true;
		for(int i=0;i<objects.length;i++) {
			if(objects[i][1]-objects[i][3] <= y) {
				above =false;
			}
		}
		return above;
	}
	//hit_test returns -1 for no hit, else returns index of object which it hit within objects[] list
	public int hit_test(double x, double y, double z) {
		if(db(CAM[0],CAM[1],CAM[2],x,y,z) < 60) {return -1;} 
		else {
		for(int i=0;i<objects.length;i++) {
			if(objects[i][4]  == 1) {
				if(db(x,y,z,objects[i][0],objects[i][1],objects[i][2]) < objects[i][3]+collision_tolerance) {
					return i;
				}
			}
			if(objects[i][4] == 2) {
				if(Math.abs(x-objects[i][0])<objects[i][3]+collision_tolerance &&
					Math.abs(y-objects[i][1])<objects[i][3]+collision_tolerance &&
					Math.abs(z-objects[i][2])<objects[i][3]+collision_tolerance) {
					return i;
				}
			}
			if(objects[i][4] == 3) {
				if(db2d(x,z,objects[i][0],objects[i][2]) < objects[i][3]+collision_tolerance &&
						y > objects[i][1]-objects[i][6]/2 && y < objects[i][1]+objects[i][6]/2) {
					return i;
				}
			}
			if(objects[i][4] == 4) {
				double rx = objects[i][0], ry = objects[i][1], rz = objects[i][2]; rx += x; ry += y; rz += z;int xp=1,yp=1,zp=1; if(rx<0) {xp=-1;} if(ry<0) {yp=-1;} if(rz<0) {zp=-1;}
				double ringmath = Math.sqrt((rx*rx)+(ry*ry));ringmath -= objects[i][3]-collision_tolerance; ringmath *= ringmath; ringmath += (rz*rz);
				if(Math.abs(ringmath-(objects[i][6]*objects[i][6])) < collision_tolerance*20) {return i;}
			}
			if(objects[i][4] == 5) {
				if(Math.abs(x-objects[i][0]) < objects[i][3]/2 && 
					Math.abs(y-objects[i][1]) < objects[i][6]/2 &&
					Math.abs(z-objects[i][2]) < objects[i][7]/2) {return i;}
			}
			
		}
		if(y > -10) {
			//return of -2 means 'ground'
			return -2;
		} else {
			return -1;
		}}
	}
	
	public double[] bounce(double[] ray, double[] off) {
		return new double[] {};
	}
	
	
	public double dtno(double x, double y, double z) {
		double[] n_o_ = objects[0];
		double d_t_n_o_ = db(x,y,z,n_o_[0],n_o_[1],n_o_[2]);
		for(int i = 0; i < objects.length; i++) {
			
			double t_d_ = db(x,y,z,objects[i][0],objects[i][1],objects[i][2]);
			if(t_d_<d_t_n_o_) {
				d_t_n_o_ = t_d_;
				n_o_ = objects[i];
				//System.out.println(d_t_n_o_);
			}
		}
		//if(Math.abs(y+10)<d_t_n_o_) {d_t_n_o_ = Math.abs(y+10);}
		return d_t_n_o_;
		//return 10000;
	}
	public double dtfo(double x, double y, double z) {
		double[] fo = objects[0];
		double dtfo = db(x,y,z,fo[0],fo[1],fo[2]);
		for(int i = 0; i< objects.length; i++) {
			double tf = db(x,y,z,objects[i][0],objects[i][1],objects[i][2]);
			if(tf > dtfo) {
				dtfo = tf;
				fo = objects[i];
			}
		}
		return dtfo;
	}
	//object is formatted as such: {x,y,z,size,type,color (color taken from color list depicted earlier up)}
	//types: 1 is sphere, 2 is cube, 3 is cylinder, 4 is upright ring, 5 is rectangular prism
	//define cylinder: x, y, z, size (radius), type (3), color, height
	//upright ring: x, y, z, size (gap radius),  type (4), color, ring thickness
	//for rings, the coordinates are negative relative to normal
	//define 2 corners of prism: x, y, z, x width, type(5), color, y height, z depth
	/*double[][] objects = {
			{-32.4,-30,200,30,1,8},
			{62,-30,200,30,1,5},
			{40,-40,40,20,2,2},
			{-11.1,-30,-31.1,20,2,5},
			{-10,-82,10,26,1,4},
			{20,-170,30,32.2,1,1},
			{-20,-110,190,20,2,3},
			{-54.3,-55,103.8,20,2,1},
			{-92.9,-215,62.1,22.2,2,3},
			{-130.7,-20,53,30,2,0},
			{-146.5,-140,139,27,1,8}
			};*/
	//4 5 8 9 10 <-- colors for cone
	/*double[][] objects = {
			{-20,-15,100,50,3,4,13},
			{-20,-25,100,47.5,3,5,13},
			{-20,-35,100,45,3,8,13},
			{-20,-45,100,42.5,3,9,13},
			{-20,-55,100,40,3,10,13},
			{-20,-65,100,37.5,3,4,13},
			{-20,-75,100,35,3,5,13},
			{-20,-85,100,32.5,3,8,13},
			{-20,-95,100,30,3,9,13},
			{-20,-105,100,27.5,3,10,13},
			{-20,-115,100,25,3,4,13},
			{-20,-125,100,22.5,3,5,13},
			{-20,-135,100,20,3,8,13},
			{-20,-145,100,17.5,3,4,13},
			{-20,-155,100,15,3,5,13},
			{-20,-165,100,12.5,3,8,13},
			{-20,-175,100,10,3,9,13},
			{-20,-185,100,7.5,3,10,13},
			{-20,-195,100,5,3,4,13},
			{-20,-205,100,2.5,3,8,13},
	};*/
	/*double[][] objects = {
			{-20,-30,100,200,5,9,20,110},
			{-20,-180,100,70,1,2},
			{-30,100,100,34,4,10,8},
			{0,-10,0,5,3,10,999},
			{0,-10,50,5,3,10,999},
			{0,-10,100,5,3,10,999},
			{0,-10,125,5,3,10,999},
			{0,-10,150,5,3,10,999},
			{-20,-350,100,200,5,0,20,110},
			{-20,350,50,200,4,1,9}
			};*/
	/*double[][] objects = {
			{-20,30,-100,50,4,9,1},
			{80,-30,60,20,2,4},
			{0,-30,190,20,1,3},
			{-20,130,-100,50,4,9,1},
			{60,-40,190,2,3,8,800},
			{40,-40,150,2,3,8,800},
			{-120,-30,90,20,2,0},
			{-130,-50,141,3,3,5,500}
	};*/
	/*double[][] objects = {
			{-10,-60,200,110,2,10},
			{-350,-60,200,110,2,10},
			
			{-10,-380,200,110,2,10},
			{-350,-380,200,110,2,10},
	};*/
	double[][] objects = {
			{-10,-50,200,50,1,5},
			{-110,-50,200,45,2,8},
			{-210,-50,200,25,3,9,100},
			{310,64,-200,54,4,10,7},
	};
}