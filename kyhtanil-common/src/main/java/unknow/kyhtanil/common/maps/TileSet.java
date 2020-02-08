package unknow.kyhtanil.common.maps;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileSet {
	/** full texture */
	private Texture texture;

	private TextureRegion[] tiles;
	private int width;
	private int height;

	public TileSet(FileHandle tex, int width, int height) {
		texture = new Texture(tex);

		this.width = texture.getWidth() / width;
		this.height = texture.getHeight() / height;
		tiles = new TextureRegion[this.height * width];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < this.height; y++)
				tiles[x + y * width] = new TextureRegion(texture, x * width, y * height, width, height);
		}
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public TextureRegion get(int i) {
		return tiles[i];
	}

	public int tileCount() {
		return tiles.length;
	}

	public void dispose() {
		tiles = null;
		texture.dispose();
		texture = null;
	}
}
