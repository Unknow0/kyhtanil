package unknow.kyhtanil.client.screen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

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

	public void showError(String msg, Exception e)
		{
		log.error(msg, e);
		VisWindow error=new VisWindow("Error");
		error.add(new VisLabel(msg));
		error.add(new VisLabel(e.getMessage()));
		error.add(new VisTextButton("Quit", new ChangeListener()
			{
			public void changed(ChangeEvent event, Actor actor)
				{
				System.exit(1);
				}
			}));
		}
	}
