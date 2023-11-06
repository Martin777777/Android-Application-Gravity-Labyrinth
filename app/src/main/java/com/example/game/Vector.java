package com.example.game;

/**
 *
 * This class represents a vector
 *
 * The Variables in this class are x, y and z which represent the x coordinate, y coordinate and z coordinate of this vector
 *
 * The Methods involved in this class are PlusVector, MinusVector, PLusPoint, byScale, NegateVector, length, Normal, dot, cross
 *
 * @author Ma Zixiao
 *
 *
 */

public class Vector {

	public float x=0;
	public float y=0;

	public Vector()
	{  
		x = 0.0f;
		y = 0.0f;
	}
	 
	public Vector(float x, float y)
	{ 
		this.x = x;
		this.y = y;
	}
	
	//What this method aims to do is to add this vector with an additional vector
	//To realize the functionality, this method returns a new vector whose x, y and z are the sum of this vector's x, y, z and the additional vector's x, y, z
	public Vector PlusVector(Vector Additonal)
	{
		return new Vector(this.x+Additonal.x, this.y+Additonal.y);
	}

	//What this method aims to show the new vector after this vector minuses another vector
	//To realize the functionality, this method returns a new vector whose x, y and z are the difference between this vector's x, y, z and the additional vector's x, y, z
	public Vector MinusVector(Vector Minus)
	{
		return new Vector(this.x-Minus.x, this.y-Minus.y);
	}
	
	//What this method aims to do is to calculate the sum of this vector and an additional point
	//To achieve the functionality, this method returns a new vector whose x, y and z are the sum of this vector's x, y, z and the additional point's x, y, z
	public Point PlusPoint(Point Additonal)
	{
		return new Point(this.x+Additonal.x, this.y+Additonal.y);
	} 
	//Do not implement Vector minus a Point as it is undefined 
	
	//This method is to multiply this vector with a scale
	//To achieve that, this method returns a new vector whose x, y and z are the scale times that of this vector
	public Vector byScalar(float scale )
	{
		 return new Vector(this.x*scale, this.y*scale);
	}

	//This method is to return a vector which is negative to this vector
	//To do that, this method returns a vector whose x, y and z are negative to this vector's x, y and z
	public Vector  NegateVector()
	{
		 return new Vector(-this.x, -this.y);
	}
	
	//This method returns the length of this vector which is (x^2 + y^2 + z^2)^0.5
	public float length()
	{
	   return (float) Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
	}
	
	//This method aims to return the Normal of this vector
	//The x of the Normal vector is the y of this vector / the length of this vector
	//The y of this vector is -x of this vector / the length of this vector
	//Furthermore, by calculating the cross product of this vector and its Normal,
	//The Normal returned by this function will always be the vector which is left to this vector
	public Vector Normal()
	{
		 float xVector = this.y / (float) Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
		 float yVector = -(this.x / (float) Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2)));
		 return new Vector(xVector, yVector);

	}

	public Vector Normalize(){
		return new Vector(x / this.length(), y / this.length());
	}
	
	//This method is to calculate the dot product of this vector and another vector
	//To do that, this method returns a scalar which is the sum of this vector's x * another vector's x, this vector's y * another vector's y, this vector's z * another vector's z
	public float dot(Vector v)
	{
		return ((this.x * v.x) + (this.y * v.y));
	}
	
	//The aim of this method is to calculate the cross product of this vector v1 and another vector v2
	//As we have learned in Linear Algebra, we know that the cross product would be:
	//((v1.y*v2.z)-(v1.z*v2.y)), ((v1.z*v2.x)-(v1.x*v2.z)), ((v1.x*v2.y)-(v1.y*v2.x))

 
}
	 
	   

/*

										MMMM                                        
										MMMMMM                                      
 										MM MMMM                                    
 										MMI  MMMM                                  
 										MMM    MMMM                                
 										MMM      MMMM                              
  										MM        MMMMM                           
  										MMM         MMMMM                         
  										MMM           OMMMM                       
   										MM             .MMMM                     
MMMMMMMMMMMMMMM                        MMM              .MMMM                   
MM   IMMMMMMMMMMMMMMMMMMMMMMMM         MMM                 MMMM                 
MM                  ~MMMMMMMMMMMMMMMMMMMMM                   MMMM               
MM                                  OMMMMM                     MMMMM            
MM                                                               MMMMM          
MM                                                                 MMMMM        
MM                                                                   ~MMMM      
MM                                                                     =MMMM    
MM                                                                        MMMM  
MM                                                                       MMMMMM 
MM                                                                     MMMMMMMM 
MM                                                                  :MMMMMMMM   
MM                                                                MMMMMMMMM     
MM                                                              MMMMMMMMM       
MM                             ,MMMMMMMMMM                    MMMMMMMMM         
MM              IMMMMMMMMMMMMMMMMMMMMMMMMM                  MMMMMMMM            
MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM               ZMMMMMMMM              
MMMMMMMMMMMMMMMMMMMMMMMMMMMMM          MM$             MMMMMMMMM                
MMMMMMMMMMMMMM                       MMM            MMMMMMMMM                  
  									MMM          MMMMMMMM                     
  									MM~       IMMMMMMMM                       
  									MM      DMMMMMMMM                         
 								MMM    MMMMMMMMM                           
 								MMD  MMMMMMMM                              
								MMM MMMMMMMM                                
								MMMMMMMMMM                                  
								MMMMMMMM                                    
  								MMMM                                      
  								MM                                        
                             GlassGiant.com */