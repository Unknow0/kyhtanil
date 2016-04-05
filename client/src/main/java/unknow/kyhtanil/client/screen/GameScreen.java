package unknow.kyhtanil.client.screen;

import org.slf4j.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.kotcrab.vis.ui.widget.*;

public class GameScreen implements Screen
	{
	private Logger log=LoggerFactory.getLogger(GameScreen.class);
	protected Stage stage;

	public GameScreen()
		{
		stage=new Stage(new ScreenViewport());
		}

	public void render(float delta)
		{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		}

	public void pause()
		{
		}

	public void resume()
		{
		}

	public void hide()
		{
		}

	public void resize(int width, int height)
		{
		stage.getViewport().update(width, height, true);
		}

	public void show()
		{
		Gdx.input.setInputProcessor(stage);
		}

	public void dispose()
		{
		stage.dispose();
		}

	public void showError(Exception e)
		{
		showError(e.getMessage());
		}

	public void showError(String e)
		{
		log.error("", e);
		VisWindow error=new VisWindow("Error");
		error.add(new VisLabel(e));
		error.add(new VisTextButton("Quit", new ChangeListener()
			{
				public void changed(ChangeEvent event, Actor actor)
					{
					System.exit(1);
					}
			}));
		}
	}
