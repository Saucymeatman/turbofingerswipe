package com.jbs.swipe.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.jbs.framework.control.Application;
import com.jbs.framework.rendering.Graphic;
import com.jbs.swipe.Assets;
import com.jbs.swipe.Game;
import com.jbs.swipe.effects.Animator;
import com.jbs.swipe.gui.buttons.ShopButton;
import com.jbs.swipe.gui.buttons.SpinningButton;
import com.jbs.swipe.gui.buttons.StartButton;
import com.jbs.swipe.tiles.Direction;

public abstract class MainMenuState extends GameState {
	final float SLIDE_DURATION = 500;
	
	final String TITLE_SOURCE = "MainMenu/Logo";
	
	public final float
		START_BUTTON_SCALE = 1f;
	
	public final int
		START_BUTTON_BOT_MARGIN = 150,
		TITLE_TOP_MARGIN = 15;

	protected SpinningButton startButton, shopButton, exitButton;

	protected Graphic title;
	
	public MainMenuState(Game game, int width, int height) {
		super(game);
	}
	
	@Override
	public void enterState() {
		System.out.println("Entering MainMenuState.");
		
		initialize();
		super.enterState();
	}
	
	@Override
	public void exitState() {
		System.out.println("Exiting MainMenuState.");
	}
	
	@Override
	public void renderTo(SpriteBatch batch) {
		super.renderTo(batch);
		
		game.beginIODChange(batch, 3f);
			startButton.renderTo(batch);
			shopButton.renderTo(batch);
			exitButton.renderTo(batch);
		game.endIODChange(batch, 3f);
		
		game.beginIODChange(batch, 5f);
			title.renderTo(batch);
		game.endIODChange(batch, 5f);
	}
	
	@Override
	public void updateApplication(Application app) {
		startButton.updateWith(app.input);
		shopButton.updateWith(app.input);
		exitButton.updateWith(app.input);
	}
	
	protected abstract void exitMainMenu();
	
	/** Initialize the MainMenu's components. */
	protected void initialize() {
		game.playBackgroundMusic(true); // True because the background Music should play looped.
		initializeTitle();
		initializeStartButton();
		initializeShopButton();
	}
	
	/** @return the Texture of the MainMenu's background. */
	public final TextureRegion backgroundTexture() {
		return Assets.getAtlasRegion(BACKGROUND_SOURCE);
	}
	
	/** @return the Texture of the title logo. */
	public final TextureRegion titleTexture() {
		return Assets.getAtlasRegion(TITLE_SOURCE);
	}
	
	/** @return the top center of the MainMenu. */
	protected final Vector2 menuTopCenter() {
		return new Vector2(menuCenter.x, menuSize.y);
	}
	
	/** Create and position the MainMenu's title. */
	private void initializeTitle() {
		// Create the title graphic at its position with the title's texture.
		title = new Graphic(new Vector2(), titleTexture());
		title.scale(0.5f);
		title.setPosition(initialTitlePosition());
		
		new Animator(game).tweenGraphicTo(title, 
				menuTopCenter().sub(new Vector2(0,title.height()/2+TITLE_TOP_MARGIN)), SLIDE_DURATION);
	}
	
	/** Create and position the MainMenu's StartButton. */
	private void initializeStartButton() {
		// Create the StartButton at the position of the Title with the specified scale.
		startButton = new StartButton(game, new Vector2(game.screenWidth()/2, START_BUTTON_BOT_MARGIN), START_BUTTON_SCALE) {
			@Override
			public void onTrigger() {
				exitMainMenu();
			}
			@Override
			public void onPress() {
				new Animator(game)
					.slideGraphicOffscreen(SLIDE_DURATION, Direction.LEFT, title);
			}
		};
	}
	
	private void initializeShopButton() {
		shopButton = new ShopButton(game, Vector2.Zero);
		// Scale the ShopButton down.
		shopButton.scale(0.7f);
		Vector2 p = initialShopButtonPosition();
		shopButton.setPosition(p.x, p.y);
		
		
		exitButton = new ShopButton(game, Vector2.Zero, "ExitButton"){
			@Override
			public void onTrigger() {
				Gdx.app.exit();
			}
		};
		// Scale the ShopButton down.
		exitButton.scale(0.7f);
		p = initialExitButtonPosition();
		exitButton.setPosition(p.x, p.y);
		
	}
	
	private Vector2 initialTitlePosition() {
		return new Vector2(game.screenWidth()+title.width(),game.screenHeight()).add(0,-title.height()/2-TITLE_TOP_MARGIN);
	}
	
	private Vector2 initialStartButtonPosition() {
		return new Vector2(game.screenWidth()/2, startButton.height()/2+START_BUTTON_BOT_MARGIN);
	}
	
	private Vector2 initialShopButtonPosition() {
		return initialStartButtonPosition()
				// Translate the ShopButton right to get out of the StartButton's way.
				.add(startButton.width()/2 + shopButton.width()/2+50, -startButton.height()+shopButton.height()/2);
	}
	private Vector2 initialExitButtonPosition() {
		return initialStartButtonPosition()
				// Translate the ShopButton right to get out of the StartButton's way.
				.add(-startButton.width()/2 - shopButton.width()/2-50, -startButton.height()+shopButton.height()/2);
	}

}