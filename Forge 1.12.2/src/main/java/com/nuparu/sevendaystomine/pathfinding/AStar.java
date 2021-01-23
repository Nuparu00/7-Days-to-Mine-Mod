package com.nuparu.sevendaystomine.pathfinding;
import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


//Forge translation of https://github.com/domisum/Pathfinding/blob/master/src/main/java/pathfinding/AStar.java
public class AStar
{
	public World world;
	private BlockPos startBlockPos;
	private BlockPos endBlockPos;
	
	private Node startNode;
	private Node endNode;
	
	private boolean pathFound = false;
	private ArrayList<Node> checkedNodes = new ArrayList<Node>();
	private ArrayList<Node> uncheckedNodes = new ArrayList<Node>();
	
	private int maxNodeTests;
	private boolean canClimbLadders;
	private double maxFallDistance;
	
	
	
	// ---
	// CONSTRUCTORS
	// ---
	
	public AStar(World world,BlockPos start, BlockPos end, int maxNodeTests, boolean canClimbLadders, double maxFallDistance)
	{
		this.startBlockPos = start;
		this.endBlockPos = end;
		
		this.world = world;
		startNode = new Node(world,startBlockPos, 0, null);
		endNode = new Node(world,endBlockPos, 0, null);
		
		this.maxNodeTests = maxNodeTests;
		this.canClimbLadders = canClimbLadders;
		this.maxFallDistance = maxFallDistance;
	}
	
	public AStar(World world,BlockPos start, BlockPos end)
	{
		this(world,start, end, 16982, false, 1);
	}
	
	// ---
	// PATHFINDING
	// ---
	
	public BlockPos[] getPath()
	{
		// check if player could stand at start and endpoint, if not return empty path
		if(!(canStandAt(startBlockPos) && canStandAt(endBlockPos)))
			return new BlockPos[0];
		
		// time for benchmark
		long nsStart = System.nanoTime();
		
		uncheckedNodes.add(startNode);
		
		// cycle through untested nodes until a exit condition is fulfilled
		while(checkedNodes.size() < maxNodeTests && pathFound == false && uncheckedNodes.size() > 0)
		{
			Node n = uncheckedNodes.get(0);
			for(Node nt : uncheckedNodes)
				if(nt.getEstimatedFinalExpense() < n.getEstimatedFinalExpense())
					n = nt;
			
			if(n.estimatedExpenseLeft < 1)
			{
				pathFound = true;
				endNode = n;
				
				// print information about last node
				System.out.println(uncheckedNodes.size() + "uc " + checkedNodes.size() + "c " + round(n.expense) + "cne " + round(n.getEstimatedFinalExpense()) + "cnee ");
				
				break;
			}
			
			n.getReachableBlockPoss();
			uncheckedNodes.remove(n);
			checkedNodes.add(n);
		}
		
		// returning if no path has been found
		if(!pathFound)
		{
			float duration = (System.nanoTime() - nsStart) / 1000000f;
			System.out.println("A* took "  + duration + "ms" + " to not find a path.");
			
			return new BlockPos[0];
		}
		
		// get length of path to create array, 1 because of start
		int length = 1;
		Node n = endNode;
		while(n.origin != null)
		{
			n = n.origin;
			length++;
		}
		
		BlockPos[] BlockPoss = new BlockPos[length];
		
		//fill Array
		n = endNode;
		for(int i = length - 1; i > 0; i --)
		{
			BlockPoss[i] = n.getBlockPos();
			n = n.origin;
		}
		
		BlockPoss[0] = startNode.getBlockPos();
		
		// outputting benchmark result
		float duration = (System.nanoTime() - nsStart) / 1000000f;
		System.out.println("A* took " + duration + "ms"  + " to find a path.");
		
		return BlockPoss;
	}
	
	private Node getNode(BlockPos loc)
	{
		Node test = new Node(world,loc, 0, null);
		
		for(Node n : checkedNodes)
			if(n.id == test.id)
				return n;
		
		return test;
	}
	
	// ---
	// NODE
	// ---
	
	public class Node
	{
		private BlockPos blockPos;
		public World world;
		public double id;
		
		public Node origin;
		
		public double expense;
		private double estimatedExpenseLeft = -1;
		
		// ---
		// CONSTRUCTORS
		// ---
		
		public Node(World world,BlockPos loc, double expense, Node origin)
		{
			this.world = world;
			blockPos = loc;
			id = loc.getX() + 30000000d * loc.getY() + 30000000d * 30000000d * loc.getZ();
			
			this.origin = origin;
			
			this.expense = expense;
		}
		
		// ---
		// GETTERS
		// ---
		
		public BlockPos getBlockPos()
		{
			return blockPos;
		}
		
		public double getEstimatedFinalExpense()
		{
			if(estimatedExpenseLeft == -1)
				estimatedExpenseLeft = distanceTo(blockPos, endBlockPos);
			
			return expense + 1.5 * estimatedExpenseLeft;
		}
		
		// ---
		// PATHFINDING
		// ---
		
		public void getReachableBlockPoss()
		{
			//trying to get all possibly walkable blocks
			for(int x = -1; x <= 1; x++)
				for(int z = -1; z <= 1; z++)
					if(!(x == 0 && z == 0) && x * z == 0)
					{
						BlockPos loc = new BlockPos(blockPos.getX() + x, blockPos.getY(), blockPos.getZ() + z);
						
						// usual unchanged y
						if(canStandAt(loc))
							reachNode(loc, expense + 1);
						
						// one block up
						if(!isObstructed(loc.add(-x, 2, -z))) // block above current tile, thats why subtracting x and z
						{
							BlockPos nLoc = loc.add(0, 1, 0);
							if(canStandAt(nLoc))
								reachNode(nLoc, expense + 1.4142);
						}
						
						// one block down or falling multiple blocks down
						if(!isObstructed(loc.add(0, 1, 0))) // block above possible new tile
						{
							BlockPos nLoc = loc.add(0, -1, 0);
							if(canStandAt(nLoc)) // one block down
								reachNode(nLoc, expense + 1.4142);
							else if(!isObstructed(nLoc) && !isObstructed(nLoc.add(0, 1, 0))) // fall
							{
								int drop = 1;
								while(drop <= maxFallDistance && !isObstructed(loc.add(0, -drop, 0)))
								{
									BlockPos locF = loc.add(0, -drop, 0);
									if(canStandAt(locF))
									{
										Node fallNode = addFallNode(loc,  expense + 1);
										fallNode.reachNode(locF, expense + drop * 2);
									}
									
									drop ++;
								}
							}
						}
						
						//ladder
						if(canClimbLadders) {
							BlockPos nLoc = loc.add(-x, 0, -z);
							if(world.getBlockState(nLoc).getBlock() == Blocks.LADDER)
							{
								
								int up = 1;
								while(world.getBlockState(nLoc.add(0, up, 0)).getBlock() == Blocks.LADDER)
									up++;
								
								reachNode(nLoc.add(0, up, 0), expense + up * 2);
							}
						}
					}
		}
		
		public void reachNode(BlockPos locThere, double expenseThere)
		{
			Node nt = getNode(locThere);
			
			if(nt.origin == null && nt != startNode) // new node
			{
				nt.expense = expenseThere;
				nt.origin = this;
				
				uncheckedNodes.add(nt);
				
				return;
			}
			
			// no new node
			if(nt.expense > expenseThere) // this way is faster to go there
			{
				nt.expense = expenseThere;
				nt.origin = this;
			}
		}
		
		public Node addFallNode(BlockPos loc, double expense)
		{
			Node n = new Node(world,loc, expense, this);
			
			return n;
		}
		
	}
	
	// ---
	// CHECKS
	// ---
	
	public boolean isObstructed(BlockPos loc)
	{
		return world.getBlockState(loc).getMaterial().isSolid();
	}
	
	public boolean canStandAt(BlockPos loc)
	{
		return !(isObstructed(loc) || isObstructed(loc.add(0, 1, 0)) || !isObstructed(loc.add(0, -1, 0)));
	}
	
	// ---
	// UTIL
	// ---
	
	public double distanceTo(BlockPos loc1, BlockPos loc2)
	{
		
		double deltaX = Math.abs(loc1.getX() - loc2.getX());
		double deltaY = Math.abs(loc1.getY() - loc2.getY());
		double deltaZ = Math.abs(loc1.getZ() - loc2.getZ());
		
		// euclidean distance
		double distance2d = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
		double distance3d = Math.sqrt(distance2d * distance2d + deltaY * deltaY);
		
		return distance3d;
		
		// manhattan distance
		//return deltaX + deltaY + deltaZ;
	}
	
	public double round(double d)
	{
		return ((int) (d * 100)) / 100d;
	}
	
}