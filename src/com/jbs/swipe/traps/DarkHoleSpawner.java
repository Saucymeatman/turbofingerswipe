package com.jbs.swipe.traps;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jbs.framework.io.InputProxy;
import com.jbs.framework.rendering.Renderable;
import com.jbs.framework.util.Updatable;
import com.jbs.swipe.Game;
import com.jbs.swipe.levels.LevelState;

public class DarkHoleSpawner extends TrapSpawner implements Renderable, Updatable {
	
	private final ArrayList<DarkHole> darkHoles;

	public DarkHoleSpawner(Game game, LevelState level, int position) {
		super(game, level, position, new DarkHole(game));
		this.darkHoles = new ArrayList<DarkHole>(5);
	}
	
	@Override
	public void spawnTrap() {
		DarkHole darkHole = new DarkHole(game);
		darkHole.setPosition(100, 100);
		darkHole.setTargets(level);
		darkHole.activate();
		darkHoles.add(darkHole);
	}
	
	@Override
	public void updateWith(InputProxy input) {
		super.updateWith(input);
		for (DarkHole darkHole : darkHoles)
			if (darkHole != null)
				darkHole.updateWith(input);
		clearExpiredFrom(darkHoles);
	}
	
	@Override
	public void renderTo(SpriteBatch batch) {
		super.renderTo(batch);
		for (DarkHole darkHole : darkHoles)
			if (darkHole != null)
				darkHole.renderTo(batch);
	}
	
	/** Clear all the expired DarkHoles from the List. */
	protected void clearExpiredFrom(List<DarkHole> list) {
		for (int i = list.size() - 1; i >= 0; i --)
			if (list.get(i) != null && list.get(i).expired())
				list.remove(i);
	}
}