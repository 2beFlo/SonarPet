package com.github.dsh105.echopet.entity.pathfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

public class PetGoalSelector {
	
	private Map<String, PetGoalSelectorItem> goalMap = new HashMap<String, PetGoalSelectorItem>();
	private ArrayList<PetGoalSelectorItem> goalList = new ArrayList<PetGoalSelectorItem>();
	private ArrayList<PetGoalSelectorItem> activeGoalList = new ArrayList<PetGoalSelectorItem>();
	
	public void addGoal(int i, String s, PetGoal goal) {
		PetGoalSelectorItem goalItem = new PetGoalSelectorItem(this, i, goal);
		if (this.goalMap.containsKey(goalItem)) {
			return;
		}
		this.goalMap.put(s, goalItem);
		this.goalList.add(i, goalItem);
	}
	
	public void addGoal(String s, PetGoal goal) {
		PetGoalSelectorItem goalItem = new PetGoalSelectorItem(this, goal);
		if (this.goalMap.containsKey(goalItem)) {
			return;
		}
		this.goalMap.put(s, goalItem);
		this.goalList.add(goalItem);
	}
	
	public void removeGoal(String s) {
		if (this.goalMap.containsKey(s)) {
			PetGoalSelectorItem goalItem = this.goalMap.get(s);
			PetGoal goal = goalItem.a;
			this.goalList.remove(goalItem);
			this.goalMap.remove(goalItem);
			if (this.activeGoalList.contains(goalItem)) {
				goal.finish();
			}
			this.activeGoalList.remove(goalItem);
		}
	}
	
	public PetGoal getGoal(String s) {
		PetGoalSelectorItem goalItem = this.goalMap.get(s);
		if (goalItem != null) {
			return goalItem.a;
		}
		return null;
	}
	
	public PetGoal getGoal(Class<? extends PetGoal> goalClass) {
		for (PetGoalSelectorItem goalItem : this.goalList) {
			PetGoal goal = goalItem.a;
			if (goalClass.isInstance(goal)) {
				return goal;
			}
		}
		return null;
	}
	
	public void removeGoals() {
		this.goalMap.clear();
		this.goalList.clear();
		for (PetGoalSelectorItem goalItem : this.activeGoalList) {
			PetGoal goal = goalItem.a;
			goal.finish();
		}
		this.activeGoalList.clear();
	}
	
	public void run() {
		ListIterator<PetGoalSelectorItem> i = this.goalList.listIterator();
		while (i.hasNext()) {
			PetGoalSelectorItem goalItem = i.next();
			PetGoal goal = goalItem.a;
			if (!this.activeGoalList.contains(goalItem)) {
				if (goal.shouldStart()) {
					this.activeGoalList.add(goalItem);
				}
			}
		}
		
		ListIterator<PetGoalSelectorItem> i2 = this.activeGoalList.listIterator();
		while (i2.hasNext()) {
			PetGoalSelectorItem goalItem = i2.next();
			PetGoal goal = goalItem.a;
			if (goal.shouldFinish()) {
				goal.finish();
				i2.remove();
			}
		}
		
		ListIterator<PetGoalSelectorItem> i3 = this.activeGoalList.listIterator();
		while (i3.hasNext()) {
			PetGoalSelectorItem goalItem = i3.next();
			PetGoal goal = goalItem.a;
			goal.tick();
		}
	}
}