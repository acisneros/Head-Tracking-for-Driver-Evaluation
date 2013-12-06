import java.util.*;
import java.io.*;
import java.lang.*;
import java.text.DecimalFormat;

public class ExpertModel
{	
	public static LinkedList<Position> base;
	public static LinkedList<Position> compare;
	
	public ExpertModel()
	{
		compare = new LinkedList<Position>();
   		base = new LinkedList<Position>();
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException
    {
    	ExpertModel ExpM = new ExpertModel();
    	ExpM.run(args);
    }
	
	public void run(String[] args) throws FileNotFoundException, IOException
	{
		CreateMoveList(args[0], base);
   		CreateMoveList(args[1], compare);
    	CompareLists(base, compare);
	}

	public void CreateMoveList(String fileName, LinkedList<Position> list) throws FileNotFoundException
	{
		//direction: 0 = straight, 1 = left, 2 = right
		Scanner sc, ls;
   		int numFiles, ndx = 1, lineCount = 0, direction, prevDir = 0;
   		double lineX, lineY, totX = 0, totY = 0, prevY = 0;
   		Position temp;
   		String input = "";
   		
   		
   		/* creation of linked-list of head positions */
   		sc = new Scanner(new File(fileName));
   		temp = new Position();
   			
   		while (sc.hasNextLine())
   		{
   			lineCount++;
            input = sc.nextLine();
            	
            ls = new Scanner(input);
            if (ls.hasNext("Frame#:"))
            {
            	while(!ls.hasNextDouble())	//skip over Frame, Rotation, X =
            	{
            		ls.next();
            	}
            	ls.nextDouble();	//frame count
            	
            	while(!ls.hasNextDouble())	//skip over Y =
            	{
            		ls.next();
            	}
            	lineX = ls.nextDouble();
            	
            	while(!ls.hasNextDouble())	//skip over Y =
            	{
            		ls.next();
            	}
            	lineY = ls.nextDouble();	//y-value
            		
            	if (lineY > 10)
            		direction = 1; 	//left
            	else if (lineY < -10)
            		direction = 2;	//right
            	else
            		direction = 0;	//straight
            		
            	if (prevDir != direction)	//new head position
            	{
            		temp.setX(totX / temp.getfCount());	//avg X value for head position
            		temp.setY(totY / temp.getfCount());	//avg Y value for head position
            		temp.setDir(prevDir);
            		list.add(temp);
            		
            		temp = new Position();
            		totX = 0;
            		totY = 0;
            	} 
            		
            	temp.incCount();
            	totX += lineX;
            	totY += lineY;
            	prevY = lineY;
            	prevDir = direction;
            }
        }
        
        // add the last node before end of file
        temp.setX(totX / temp.getfCount());
        temp.setY(totY / temp.getfCount());
        list.add(temp);
	}
	
	public void CompareLists(LinkedList<Position> list1, LinkedList<Position> list2) throws IOException
	{
		int ndx1 = 0, ndx2 = 0, dirDif = 0, frameShort = 0, frameLong = 0;
		int temp1, temp2, finalScore = 100, missLook, extraLook;
		double loc1, loc2;
		
		while (ndx1 < list1.size() && ndx2 < list2.size())
		{
			if (list1.get(ndx1).getDir() == list2.get(ndx2).getDir())
			{
				//System.out.println("Directions at Position " + ndx1 + ", " + ndx2 + " are the same. Comparing frames.");
				if ((list1.get(ndx1).getfCount() - list2.get(ndx2).getfCount()) > 2)
				{
					//System.out.println("  Did not look long enough.");
					frameShort++;
				}
				else if ((list2.get(ndx2).getfCount() - list1.get(ndx1).getfCount()) > 2)
				{
					//System.out.println("  Looked too long.");
					frameLong++;
				}
				else
				{
					//System.out.println("   Look length correct.");
				}
				ndx1++;
				ndx2++;
			}
			/* if the desired position in list1 is found one spot later in list2, then the 
		   	   driver added an undesired head position. If the next position in list1 matches
		       the current position in list2, then the driver missed a desired head position.
		       Check for both possibilities, and then adjust the comparison accordingly. */
			else if (ndx1 + 1 < list1.size() && ndx2 + 1 < list2.size()) 
			{
				//System.out.println("Directions at Position " + ndx1 + ", " + ndx2 + " are not the same.");
				//System.out.println("   Checking for an undesired added head position...");
				if (list1.get(ndx1).getDir() == list2.get(ndx2 + 1).getDir())
				{
					//System.out.println("   undesired head position identified at ndx2 = " + ndx2 + ".");
					ndx2++;
				}
				else if (list1.get(ndx1 + 1).getDir() == list2.get(ndx2).getDir())
				{
					//System.out.println("   Missing head position identified at ndx1 = " + ndx1 + ".");
					ndx1++;
				}
				else
				{
					//System.out.println("   Unsure of how to continue. Proceeding as if equivalent.");
					ndx1++;
					ndx2++;
				}
				dirDif++;
			}
			else
			{
				System.out.println("Reached end of Expert test file.");
				break;
			}
		}
		System.out.println("Direction differences detected: " + dirDif);
		System.out.println("Short looks detected: " + frameShort);
		System.out.println("Long looks detected: " + frameLong);
		
		if (ndx1 < list1.size())
		{
			missLook = list1.size() - ndx1;
			System.out.println("Missing head movements: " + missLook);
			if (missLook > 3)
				finalScore = finalScore - (missLook * 5);
		}
		else if (ndx2 < list2.size())
		{
			extraLook = list2.size() - ndx2;
			System.out.println("Extra head movements: " + extraLook);
			if (extraLook > 3)
				finalScore = finalScore - (extraLook * 5);
		}
		
		finalScore = finalScore - (dirDif * 5 + frameShort * 3 + frameLong * 3);
		System.out.println("Final Score (out of 100): " + finalScore);
		
		/* if the compared results are close to the expert model, add it to the expert
		   model (the "base" linked list).											*/
		if (dirDif < 3 && frameShort < 3 && frameLong < 3)
		{
			System.out.println("Comparison was close to the expert model. Modifying expert model...");
			ndx1 = 0;
			ndx2 = 0;
			while (ndx1 < list1.size() && ndx2 < list2.size())
			{
				if (list1.get(ndx1).getDir() == list2.get(ndx2).getDir())	//if same direction
				{
					temp1 = list1.get(ndx1).getfCount();
					temp2 = list2.get(ndx2).getfCount();
					list1.get(ndx1).setCount((temp1 + temp2) / 2);
					
					loc1 = list1.get(ndx1).getX();
					loc2 = list2.get(ndx2).getX();
					list1.get(ndx1).setX((loc1 + loc2) / 2);
					
					loc1 = list1.get(ndx1).getY();
					loc2 = list2.get(ndx2).getY();
					list1.get(ndx1).setY((loc1 + loc2) / 2);
					
					ndx1++;
					ndx2++;
				}
				/* if the desired position in list1 is found one spot later in list2, then the 
		   	   	   driver added an undesired head position. If the next position in list1 matches
		       	   the current position in list2, then the driver missed a desired head position.
		       	   Check for both possibilities, and then adjust the comparison accordingly. */
				else
				{
					if (list1.get(ndx1).getDir() == list2.get(ndx2 + 1).getDir())
					{
						ndx2++;
					}
					else if (list1.get(ndx1 + 1).getDir() == list2.get(ndx2).getDir())
					{
						ndx1++;
					}
					else
					{
						ndx1++;
						ndx2++;
					}
				}
			}
			PrintModel(list1);
		}
	}
	
	public void PrintList(LinkedList<Position> list, String fileName) throws IOException
	{
		PrintWriter toFile;
		
		toFile = new PrintWriter(new FileWriter(fileName));
        for (int i = 0; i < list.size(); i++)
        {
        	toFile.println("" + list.get(i).toString());
        }
        toFile.close();
	}
	
	public void PrintModel(LinkedList<Position> list) throws IOException
	{
		PrintWriter toFile;
		int frameCount = 1;
		
		toFile = new PrintWriter(new FileWriter("ExpertModel.txt"));
		for (int i = 0; i < list.size(); i++)
		{
			for (int j = 1; j <= list.get(i).getfCount(); j++)
			{
				toFile.println("Frame#: " + frameCount++ + " Rotation: X = " + 
				 list.get(i).getX() + " Y = " + list.get(i).getY());
			}
		}
		toFile.close();
	}

	private class Position
	{
		private double x;
    	private double y;
    	private int direction;	//0 = straight, 1 = left, 2 = right
    	private int fCount;
    
    	public Position()
    	{
    		x = 0;
    		y = 0;
    		direction = 0;
    		fCount = 0;
    	}
    
    	public Position(double x, double y, int direction, int fCount)
    	{
    		this.x = x;
    		this.y = y;
    		this.direction = direction;
    		this.fCount = fCount;
    	}
    
   	 	public double getX() 
    	{
    		return x;
    	}
    
    	public double getY() 
    	{
    		return y;
    	}
    	
    	public int getDir()
    	{
    		return direction;
    	}
    
    	public int getfCount() 
    	{
    		return fCount;
    	}
    
    	public void setX(double newX) 
    	{
    		this.x = newX;
    	}
    
    	public void setY(double newY) 
    	{
    		this.y = newY;
    	}
    	
    	public void setDir(int newDir)
    	{
    		direction = newDir;
    	}
    
    	public void setCount(int newCount) 
    	{
    		this.fCount = newCount;
    	}
    	
    	public void incCount()
    	{
    		fCount++;
    	}
    	
    	public String toString()
    	{
    		return "avg x: " + x + " avg y: " + y + " frame count: " + fCount;
    	}
	}
}