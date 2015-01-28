package com.jbs.swipe.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.jbs.framework.rendering.Graphic;
import com.jbs.framework.rendering.Renderable;
import com.jbs.swipe.Game;
import com.jbs.swipe.shop.ShopFont;

public abstract class CoinWindow implements Renderable {
	
	private static final String
		WINDOW_SOURCE = "assets/GUI/Shop/CoinWindow.png";
	
	private Graphic window;
	private ShopFont font;
	
	/** Create a utility for notifying the user of how many jbs coins he or she has. */
	public CoinWindow(Game game) {
		this.window = new Graphic(new Vector2(), game.getTexture(WINDOW_SOURCE));
		window.setPosition(x(), y());
		
		this.font = new ShopFont();
	}
	
	@Override
	public void renderTo(SpriteBatch batch) {
		window.renderTo(batch);
		
		final String
			text = coins() + "";
		final float
			// The horizontal offset of the text.
			leftMargin = window.width() * 1/10f;
		font.draw(batch, text, x() - widthOf(text)/2 + leftMargin, y() + heightOf(text)/2);
	}
	
	/** Scale the window around it's center. */
	public final void scale(float scalar) {
		window.scale(scalar);
		font.scale(scalar);
	}
	
	/** @return the width of the window. */
	public final float width() {
		return window.width();
	}
	
	/** @return the height of the window. */
	public final float height() {
		return window.height();
	}
	
	/** @return the y-coordinate of the center of the window. */
	public abstract float x();
	/** @return the x-coordinate of the center of the window. */
	public abstract float y();
	
	/** @return the number of jbs coins that the user has. */
	protected abstract int coins();
	
	/** @return the width in pixels of the text when it's rendered with the Window's Font. */
	private float widthOf(String text) {
		return font.getBounds(text).width;
	}
	
	/** @return the height in pixels of the text when it's rendered with the Window's Font. */
	private float heightOf(String text) {
		return font.getBounds(text).height;
	}
}