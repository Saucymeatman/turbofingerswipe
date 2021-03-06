package com.jbs.swipe.states;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.jbs.framework.control.Application;
import com.jbs.framework.rendering.Graphic;
import com.jbs.framework.rendering.ui.Button;
import com.jbs.swipe.Assets;
import com.jbs.swipe.Game;
import com.jbs.swipe.effects.Animator;
import com.jbs.swipe.gui.GraphicAccessor;
import com.jbs.swipe.gui.buttons.BackButton;
import com.jbs.swipe.levels.arcade.ArcadeModeEasy;
import com.jbs.swipe.levels.formation.FormationMode;

public final class GameModeSelectionState extends GameState {
	
	private static final String
		NORMAL_TEXT = "GameModeSelect/Normal",
		NORMAL_CIRCLE = "GameModeSelect/NormalCircle",
		ADVANCED_TEXT = "GameModeSelect/Advanced",
		ADVANCED_CIRCLE = "GameModeSelect/AdvancedCircle",
		PUZZLE_TEXT = "GameModeSelect/Puzzle",
		PUZZLE_CIRCLE = "GameModeSelect/PuzzleCircle";
	
	private final float
		buttonMargin = 50, // The empty space in-between each button.
		rotationAnimationDuration = 4500f,
		translationAnimationDuration = 1000f;
	
	/** The Buttons used to select the game-mode. */
	private Button advancedButton, puzzleButton, normalButton;
	private Graphic advancedText, puzzleText, normalText;
	BackButton backButton;
	
	private final Vector2 center, offscreen;
	
	public GameModeSelectionState(Game game) {
		super(game);
		this.center = game.screenCenter();
		this.offscreen = center.cpy().add(game.screenWidth(), 0);
	}
	
	@Override
	public void renderTo(SpriteBatch batch) {
		super.renderTo(batch);
		//game.background().renderTo(batch);
		
		game.beginIODChange(batch, 3);
			backButton.renderTo(batch);
			advancedButton.renderTo(batch);
			normalButton.renderTo(batch);
//			puzzleButton.renderTo(batch);
			
			advancedText.renderTo(batch);
			normalText.renderTo(batch);
//			puzzleText.renderTo(batch);
		game.endIODChange(batch, 3);
	}
	
	@Override
	public void updateApplication(Application app) {
		backButton.updateWith(app.input);
		advancedButton.updateWith(app.input);
		normalButton.updateWith(app.input);
//		puzzleButton.updateWith(app.input);
	}
	
	@Override
	public void enterState() {
		System.out.println("Entering GameModeSelectionState.");
		
		initialize();
		animateIn();
		super.enterState();
	}
	
	@Override
	public void exitState() {
		System.out.println("Exiting GameModeSelectionState.");
	}
	
	/**
	 * Animate the SelectionState's Graphics into position.
	 */
	protected final void animateIn() {
		final int buttonsToUse = 2;
		
		final Button[] buttons = allButtons();
		
		float targetX = // The position at which to animate our current Button to.
				center.x - ((buttonsToUse - 1) * buttonWidth())/2; // Start at the leftmost target-position.
		
		for (int i = 0; i != buttonsToUse; i ++) {
			// Tween the current button to the target position.
			Tween.to(buttons[i], GraphicAccessor.POSITION_TWEEN, translationAnimationDuration)
					.target(targetX + ((i - (buttonsToUse/2)) * buttonMargin), center.y)
					.ease(Quad.OUT)
					.start(game.tweenManager());
			
			// Move the target position over by the width of the Button.
			targetX += buttonWidth();
		}
	}
	
	/**
	 * Animate the SelectionState's Graphics off screen.
	 */
	protected final void animateOut() {
		
	}
	
	/**
	 * Initialize all the State's components.
	 */
	private void initialize() {
		final float ROTATION_ANIMATION_DURATION = 100f;
		initializeBackground();
		
		this.backButton = new BackButton(game, new Vector2()) {
			final float
				horizontalMargin = 20,
				verticalMargin = 5;
			@Override
			public float x() {
				return width()/2 + horizontalMargin;
			}
			
			@Override
			public float y() {
				return height()/2 + verticalMargin;
			}
		};
		
		// Initialize the Advanced GameMode Button.
		advancedButton = new Button(offscreen.cpy(), Assets.getAtlasRegion(ADVANCED_CIRCLE)) {
			// When the Button is pressed, rotate it very quickly indefinitely.
			public void onPress() {
				new Animator(game)
					.rotateGraphicIndefinitely(this, ROTATION_ANIMATION_DURATION);
			}
			// When the Button is released, bind the "Advanced" GameMode (Which is really just the Arcade GameMode).
			public void onRelease() {
				game.setState(new FormationMode(game));
			}
		};
		// Initialize the Normal GameMode Button.
		normalButton = new Button(offscreen.cpy(), Assets.getAtlasRegion(NORMAL_CIRCLE)) {
			// When the Button is pressed, rotate it very quickly indefinitely.
			public void onPress() {
				new Animator(game)
					.rotateGraphicIndefinitely(this, ROTATION_ANIMATION_DURATION);
			}
			// When the Button is released, bind the Normal GameMode.
			public void onRelease() {
				game.setState(new ArcadeModeEasy(game));
			}
		};
//		// Initialize the Puzzle GameMode Button.
//		puzzleButton = new Button(offscreen.cpy(), game.getTexture(PUZZLE_CIRCLE)) {
//			// When the Button is pressed, rotate it very quickly indefinitely.
//			public void onPress() {
//				new Animator(game)
//					.rotateGraphicIndefinitely(this, ROTATION_ANIMATION_DURATION);
//			}
//			// When the Button is released, bind the Normal GameMode.
//			public void onRelease() {
//				game.setState(new PuzzleMode(game) {
//					@Override
//					protected boolean hasBoughtPuzzleMode() {
//						// For now, always assume that the User has bought PuzzleMode.
//						return true;
//					}
//				});
//			}
//		};
		
		advancedText = new Graphic(offscreen.cpy(), Assets.getAtlasRegion(ADVANCED_TEXT)) {
			public float x() { return advancedButton.x(); }
			public float y() { return advancedButton.y(); }
		};
		normalText = new Graphic(offscreen.cpy(), Assets.getAtlasRegion(NORMAL_TEXT)) {
			public float x() { return normalButton.x(); }
			public float y() { return normalButton.y(); }
		};
//		puzzleText = new Graphic(offscreen.cpy(), game.getTexture(PUZZLE_TEXT)) {
//			public float x() { return puzzleButton.x(); }
//			public float y() { return puzzleButton.y(); }
//		};
		
		new Animator(game)
			.rotateGraphicIndefinitely(advancedText, rotationAnimationDuration + game.random().nextInt(1500))
			.rotateGraphicIndefinitely(normalText, rotationAnimationDuration + game.random().nextInt(1500));
//			.rotateGraphicIndefinitely(puzzleText, rotationAnimationDuration + game.random().nextInt(1500));
	}
	
	/**
	 * @return a new Array containing all the Buttons in the SelectionScreen.
	 */
	private Button[] allButtons() {
		return new Button[] {
			advancedButton, /*puzzleButton,*/ normalButton
		};
	}
	
	/**
	 * @return The width of the average Button and it's text.
	 */
	private float buttonWidth() {
		final float
			textWidth = normalButton.width() - normalText.width();
		
		return normalButton.width() + textWidth*2;
	}
}