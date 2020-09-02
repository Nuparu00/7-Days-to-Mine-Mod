package com.nuparu.sevendaystomine.events;

import com.nuparu.sevendaystomine.computer.process.ShellProcess;
import com.nuparu.sevendaystomine.tileentity.TileEntityComputer;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HandleCommandEvent extends Event{

	public TileEntityComputer computer;
	public ShellProcess process;
	public String command;
	public String output;
	public boolean override = false;
	public HandleCommandEvent(TileEntityComputer computer, ShellProcess process, String command) {
		this.computer = computer;
		this.command = command;
		this.process = process;
		this.output=this.command;
	}
	
}
