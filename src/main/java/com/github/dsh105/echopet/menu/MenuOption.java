package com.github.dsh105.echopet.menu;


public class MenuOption {
	
	protected MenuItem item;
	protected int position;
	
	public MenuOption(int pos, MenuItem item) {
		this.item = item;
		this.position = pos;
	}
}