//import static org.lwjgl.opengl.GL11.glPopMatrix;
//import static org.lwjgl.opengl.GL11.glPushMatrix;
//import static org.lwjgl.opengl.GL11.glTranslatef;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Scanner;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;



/**
 * Maze represents the maze used by MazeRunner.
 * <p>
 * The Maze is defined as an array of integers, where 0 equals nothing and 1 equals a wall.
 * Note that the array is square and that MAZE_SIZE contains the exact length of one side.
 * This is to perform various checks to ensure that there will be no ArrayOutOfBounds 
 * exceptions and to perform the calculations needed by not only the display(GL) function, 
 * but also by functions in the MazeRunner class itself.<br />
 * Therefore it is of the utmost importance that MAZE_SIZE is correct.
 * <p>
 * SQUARE_SIZE is used by both MazeRunner and Maze itself for calculations of the 
 * display(GL) method and other functions. The larger this value, the larger the world of
 * MazeRunner will be.
 * <p>
 * This class implements VisibleObject to force the developer to implement the display(GL)
 * method, so the Maze can be displayed.
 * 
 * @author Bruno Scheele, revised by Mattijs Driel
 *
 */
public class Maze implements VisibleObject{
	
	Texture Texfloor;
	
	public final double SQUARE_SIZE = 5;

	private int[][] maze = 
	{	{  1,  1,  1,  1,  1,  1,  1,  1,  1,  1 },
		{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
		{  1,  0,  0,  0,  0,  0,  1,  1,  1,  1 },
		{  1,  0,  1,  0,  0,  0,  1,  0,  0,  1 },
		{  1,  0,  1,  0,  1,  0,  1,  0,  0,  1 },
		{  1,  0,  1,  0,  1,  0,  1,  0,  0,  1 },
		{  1,  0,  0,  0,  1,  0,  1,  0,  0,  1 },
		{  1,  0,  0,  0,  1,  1,  1,  0,  0,  1 },
		{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
		{  1,  1,  1,  1,  1,  1,  1,  1,  1,  1 }	};
	
//	private int[][] maze = 
//	{	{  1,  1,  1,  1,  1,  1,  1,  1,  1,  1 },
//		{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
//		{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
//		{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
//		{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
//		{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
//		{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
//		{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
//		{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
//		{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
//		{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
//		{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
//		{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
//		{  1,  0,  0,  0,  0,  0,  0,  0,  0,  1 },
//		{  1,  1,  1,  1,  1,  1,  1,  1,  1,  1 }
//		};
	
	public double MAZE_SIZE_X = maze.length;
	public double MAZE_SIZE_Z = maze[0].length;
	
	public Maze(){
		/*
		 * Load floor texture
		 */
		try {
			Texfloor = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/Wooden_floor_original.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Texture File not found or corrupt!");
		}
		
		/*
		 * Load Maze
		 */
		try {
			maze = importcompMaze("res/EAS",true);
			MAZE_SIZE_X = maze.length;
			MAZE_SIZE_Z = maze[0].length;
		} catch (IOException e) {
			
			e.printStackTrace();
			System.err.println("Maze not found or corrupt, standard maze loaded!");
		}
	}
	
	/**
	 * isWall(int x, int z) checks for a wall.
	 * <p>
	 * It returns whether maze[x][z] contains a 1.
	 * 
	 * @param x		the x-coordinate of the location to check
	 * @param z		the z-coordinate of the location to check
	 * @return		whether there is a wall at maze[x][z]
	 */
	public boolean isWall( int x, int z )
	{
		
		if( x >= 0 && x < MAZE_SIZE_X && z >= 0 && z < MAZE_SIZE_Z )
			return maze[x][z] == 1;
		else
			return false;
	}
	
	/**
	 * isWall(double x, double z) checks for a wall by converting the double values to integer coordinates.
	 * <p>
	 * This method first converts the x and z to values that correspond with the grid 
	 * defined by maze[][]. Then it calls upon isWall(int, int) to check for a wall.
	 * 
	 * @param x		the x-coordinate of the location to check
	 * @param z		the z-coordinate of the location to check
	 * @return		whether there is a wall at maze[x][z]
	 */
	public boolean isWall( double x, double z )
	{
		int gX = convertToGridX( x );
		int gZ = convertToGridZ( z );
		return isWall( gX, gZ );
	}
	 
	/**
	 * Converts the double x-coordinate to its correspondent integer coordinate.
	 * @param x		the double x-coordinate
	 * @return		the integer x-coordinate
	 */
	private int convertToGridX( double x )
	{
		return (int)Math.floor( x / SQUARE_SIZE );

	}

	/**
	 * Converts the double z-coordinate to its correspondent integer coordinate.
	 * @param z		the double z-coordinate
	 * @return		the integer z-coordinate
	 */
	private int convertToGridZ( double z )
	{
	
		return (int)Math.floor( z / SQUARE_SIZE );
	}
	
	public void display() {
		

        // Setting the wall colour and material.
		
		//FloatBuffer wallColour = BufferUtils.createFloatBuffer(4).put(new float[] { 0.5f, 0.0f, 0.7f ,1.0f});	// The walls are purple.    	
		FloatBuffer wallColour = (FloatBuffer) BufferUtils.createFloatBuffer(4).put(0.5f).put(0.0f).put(0.7f).put(1.0f).flip();	// The walls are purple.    		
        GL11.glMaterial( GL11.GL_FRONT, GL11.GL_DIFFUSE, wallColour);	// Set the materials used by the wall.
        
        
        // draw the grid with the current material
		for( int i = 0; i < MAZE_SIZE_X; i++ )
		{
	        for( int j = 0; j < MAZE_SIZE_Z; j++ )
			{
	        	GL11.glPushMatrix();
	        	GL11.glTranslated( i * SQUARE_SIZE + SQUARE_SIZE / 2, SQUARE_SIZE / 2, j * SQUARE_SIZE + SQUARE_SIZE / 2 );
				if ( isWall(i, j) ){
					
					renderCube((float)SQUARE_SIZE/2);
					
				}
					
				GL11.glPopMatrix();
			}
		}
		paintSingleFloorTile(  MAZE_SIZE_X*SQUARE_SIZE,MAZE_SIZE_Z * SQUARE_SIZE );			// Paint the floor.
	}
	
	/**
	 * paintSingleFloorTile(GL, double) paints a single floor tile, to represent the floor of the entire maze.
	 * 
	 * @param gl	the GL context in which should be drawn
	 * @param size	the size of the tile
	 */
	private void paintSingleFloorTile( double size_X, double size_Z)
	{
        // Setting the floor color and material.
		FloatBuffer wallColour = (FloatBuffer) BufferUtils.createFloatBuffer(4).put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();// The floor is blue.

        // Set texture to repeat
	
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Texfloor.getTextureID());
//        Texfloor.bind();
		
        GL11.glMaterial( GL11.GL_FRONT, GL11.GL_DIFFUSE, wallColour);	// Set the materials used by the floor.
        
        
        GL11.glNormal3d(0, 1, 0);
        
        /*
         * The floor 
         */
        GL11.glBegin(GL11.GL_QUADS);
        
        GL11.glVertex3d(0, 0, 0);
        GL11.glTexCoord2f(0,0);
                
        GL11.glVertex3d(0, 0, size_Z);
        GL11.glTexCoord2d(0, MAZE_SIZE_Z);
        
        GL11.glVertex3d(size_X, 0, size_Z);
        GL11.glTexCoord2d(MAZE_SIZE_X, MAZE_SIZE_Z);
        
        GL11.glVertex3d(size_X, 0, 0);	
        GL11.glTexCoord2d(MAZE_SIZE_X, 0);
        	
        
        GL11.glEnd();	

	}
	/**
	 * Draws a sphere at (0,0,0)
	 * @param radius
	 */
	private void renderSphere(float radius) {

	      Sphere s = new Sphere();
	      s.draw(radius, 16, 16);
	      s.setTextureFlag(true);

	  }
	/**
	 * Draws a cube without top centered at (0,0,0)
	 * @param size
	 */
	private void renderCube(float size){
		
		
	      GL11.glBegin(GL11.GL_QUAD_STRIP);
	        
	        GL11.glVertex3d(-size, size, size);	 	        
	        GL11.glVertex3d(-size, -size, size);	
	        
	        GL11.glVertex3d(size, size, size);	      	     
	        GL11.glVertex3d(size, -size, size);		     
	        	        
	        GL11.glVertex3d(size, size, -size);	      	     
	        GL11.glVertex3d(size, -size, -size);	
	        	       	        
	        GL11.glVertex3d(-size, size, -size);	      	     
	        GL11.glVertex3d(-size, -size, -size);		        
	        	        
	        GL11.glVertex3d(-size, size, size);	 	        
	        GL11.glVertex3d(-size, -size, size);		        
	        
	        GL11.glEnd();	
	        
	        GL11.glBegin(GL11.GL_QUADS);
	        GL11.glVertex3d(-size, size, size);	 
	        GL11.glVertex3d(size, size, size);
	        GL11.glVertex3d(size, size, -size);
	        GL11.glVertex3d(-size, size, -size);	        
	        GL11.glEnd();
	}
	/**
	 * Imports a maze in the format specified by 1 is a route and 0 is a wall
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private int[][] importcompMaze(String file, boolean surround) throws IOException{
		Scanner scanner = new Scanner(new File(file));
		
		int width = scanner.nextInt();
		int height = scanner.nextInt();
		int[][] maze = null;
		byte omheining = 0;
		
		if(surround) omheining=1;
		
		maze = new int[height+2*omheining][width+2*omheining];
		
		/*
		 * Surround the maze with solid blocks
		 */
		for(int i = 0 ; i < width+2*omheining;i++){
			maze[0][i]=1;
			maze[height+2*omheining-1][i] =1;
		}
		
		for(int j = 0; j <height+2*omheining;j++){
			maze[j][0]=1;
			maze[j][width+2*omheining-1]=1;
		}
		
		/*
		 * Maze itself
		 */
		for(int j=0; j<height; j++){
			for(int i = 0; i<width; i++){
				maze[j+omheining][i+omheining] = Math.abs(scanner.nextInt()-1);
			}
		}
		scanner.close();
		return maze;
	}
}
