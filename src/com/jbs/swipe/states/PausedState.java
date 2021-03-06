package com.jbs.swipe.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.jbs.framework.control.Application;
import com.jbs.framework.rendering.Renderable;
import com.jbs.framework.rendering.ui.Button;
import com.jbs.swipe.Game;
import com.jbs.swipe.gui.buttons.HomeButton;
import com.jbs.swipe.gui.buttons.RestartButton;
import com.jbs.swipe.gui.buttons.ResumeButton;
import com.jbs.swipe.levels.LevelState;
import com.jbs.swipe.tiles.SwipeTile;

public class PausedState extends OverlayState {
	
	/* The horizontal spacing between each of the buttons in pixels */
	private static final int BUTTON_MARGIN = 10;
	
	private Button
		homeButton,
		restartButton,
		resumeButton;
	
	private LevelState levelState;
	
	private long pauseStartTime;
	
	public PausedState(Game game, LevelState levelState) {
		super(game);
		this.levelState = levelState;
	}
	
	@Override
	public void enterState() {
		System.out.println("Entering PausedState.");
		
		pauseStartTime = System.currentTimeMillis();
		
		// Stop the background music.
		game.stopBackgroundMusic();
		
		// If the buttons are not initialized,
		if (!buttonsInitialized())
			createButtons();
		
		levelState.tweenBarOffset(new Vector2(0,-150), 300f);
	}
	
	@Override
	public void exitState() {
		System.out.println("Exiting PausedState.");
		game.playBackgroundMusic(true);
	}
	
	@Override
	public void updateApplication(Application app) {
		resumeButton.updateWith(app.input);
		restartButton.updateWith(app.input);
		homeButton.updateWith(app.input);
	}
	
	/** Resumes the Level that is in the Paused State. */
	public void unpause() {
		for (SwipeTile tile : levelState.tiles())
			if (tile != null)
				tile.addPausedTime(timePaused());
		game.setState(levelState);
	}
	
	/* @return true if the State's Buttons have been created. */
	private boolean buttonsInitialized() {
		// Return false if any of the buttons are null.
		return
				restartButton != null &&
				resumeButton != null &&
				homeButton != null;
	}
	
	private final void createButtons() {
		// Define the center of the screen as the center of the Game's Screen object.
		final Vector2 SCREEN_CENTER = game.screenCenter();
		
		// Initialize all the Buttons at the center of the screen.
		homeButton = new HomeButton(game, SCREEN_CENTER);
		resumeButton = new ResumeButton(game, SCREEN_CENTER) {
			@Override
			public void resume() {
				unpause();
			}
		};
		restartButton = new RestartButton(game, SCREEN_CENTER) {
			@Override
			public void resetLevel() {
				levelState.restart();
			}
			@Override
			public void resumeLevel() {
				game.setState(levelState);
			}
		};
		
		// Translate the home-button right by it's width to make it not intersect the play-button
		homeButton.translate(homeButton.width(), 0);
		// Translate the restart-button left by it's width to make it not intersect the play-button.
		restartButton.translate(-restartButton.width(), 0);
		
		// Translate the home-button right by the desired button margin.
		homeButton.translate(BUTTON_MARGIN, 0);
		// Translate the restart-button left by the desired button margin.
		restartButton.translate(-BUTTON_MARGIN, 0);
	}

	@Override
	protected Renderable superScreen() {
		return new Renderable() {
			@Override
			public void renderTo(SpriteBatch batch) {
				game.beginIODChange(batch, 3);
					homeButton.renderTo(batch);
					restartButton.renderTo(batch);
					resumeButton.renderTo(batch);
				game.endIODChange(batch, 3);
			}
		};
	}
	
	@Override
	protected Renderable subScreen() {
		return levelState;
	}
	
	protected final long timePaused() {
		return System.currentTimeMillis() - pauseStartTime;
	}
}