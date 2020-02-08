package unknow.game.admin.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

import unknow.kyhtanil.common.maps.MapLayout;
import unknow.kyhtanil.common.maps.MapLayout.MapEntry;
import unknow.scene.builder.SceneBuilder;
import unknow.scene.builder.SceneBuilder.Listener;
import unknow.scene.builder.Wrapper;

public class LayoutEditorScreen extends VisTable {
    public static MapLayout.MapEntry selected = null;

    private MapLayout layout;

    private Edit edit = new Edit();
    private Content content = new Content();

    public LayoutEditorScreen(MapLayout mapLayout) throws Exception {
	this.layout = mapLayout;
	SceneBuilder sceneBuilder = new SceneBuilder();
	sceneBuilder.addActor("save", new ClickListener() {
	});
	sceneBuilder.addActor("new", new ClickListener() {
	    @Override
	    public void clicked(InputEvent event, float x, float y) {
		edit.show();
	    }
	});
	sceneBuilder.addActor("content", content);

	sceneBuilder.addActor("edit", edit);
	sceneBuilder.addActor("edit.save", new ClickListener() {
	    @Override
	    public void clicked(InputEvent event, float x, float y) {
		edit.save();
	    }
	});
	sceneBuilder.addActor("edit.cancel", new ClickListener() {
	    @Override
	    public void clicked(InputEvent event, float x, float y) {
		edit.cancel();
	    }
	});
	sceneBuilder.addListener(edit);
	sceneBuilder.build("layout.xml", this);
    }

    @Override
    protected void setStage(Stage stage) {
	super.setStage(stage);
	content.focus();
    }

    // private void save() throws IOException, SQLException
    // {
    // FileOutputStream fos=new FileOutputStream("data/maps.layout");
    // DataOutputStream dos=new DataOutputStream(fos);
    // layout.save(dos);
    // dos.close();
    // }

    private class Content extends Actor implements EventListener {
	private ShapeRenderer sr = new ShapeRenderer();
	private long lastClic;
	private float x, y, scale = 1f;

	public Content() {
	    addListener(this);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
	    batch.end();
	    this.clipBegin();
	    sr.setProjectionMatrix(batch.getProjectionMatrix());
	    sr.setTransformMatrix(batch.getTransformMatrix());
	    sr.translate(getX() + getWidth() / 2, getY() + getHeight() / 2, 0);
	    sr.translate(x, y, 0);
	    sr.scale(scale, scale, scale);

	    sr.begin(ShapeType.Filled);
	    sr.setColor(Color.LIGHT_GRAY);
	    for (MapLayout.MapEntry e : layout.maps()) {
		if (selected == e)
		    sr.setColor(Color.CHARTREUSE);
		sr.rect(e.x, e.y, e.w, e.h);
		if (selected == e)
		    sr.setColor(Color.LIGHT_GRAY);
	    }
	    sr.end();

	    sr.begin(ShapeType.Line);
	    sr.setColor(Color.WHITE);
	    for (MapLayout.MapEntry e : layout.maps()) {
		if (selected == e)
		    sr.setColor(Color.RED);
		sr.rect(e.x, e.y, e.w, e.h);
		if (selected == e)
		    sr.setColor(Color.WHITE);
	    }
	    sr.end();
	    this.clipEnd();
	    batch.begin();
	}

	@Override
	public boolean handle(Event event) {
	    if (!(event instanceof InputEvent))
		return false;

	    InputEvent e = (InputEvent) event;
	    switch (e.getType()) {
	    case scrolled:
		float f = -e.getScrollAmount() * .1f;
		scale += f;
		if (scale < .1)
		    scale = .1f;
		else {
		    x += x * f;
		    y += y * f;
		}
		return true;
	    case keyDown:
		if (Keys.ESCAPE == e.getKeyCode())
		    selected = null;
		break;
	    case touchDown:
		Vector2 v = new Vector2(e.getStageX(), e.getStageY());
		stageToLocalCoordinates(v);
		v.add(-getWidth() / 2 - x, -getHeight() / 2 - y);
		MapEntry last = selected;
		selected = layout.get((int) v.x, (int) v.y);
		if (selected != null && last == selected) {
		    if (System.currentTimeMillis() - lastClic < 200) {
			edit.show(selected);
			return true;
		    }
		    lastClic = System.currentTimeMillis();
		}
		return true;
	    case touchDragged:
		x += Gdx.input.getDeltaX(e.getPointer());
		y -= Gdx.input.getDeltaY(e.getPointer());
		return true;
	    default:
	    }
	    return false;
	}

	public void focus() {
	    if (getStage() == null)
		return;
	    getStage().setScrollFocus(this);
	    getStage().setKeyboardFocus(this);
	}
    }

    private class Edit extends VisWindow implements Listener {
	private VisTextField name;
	private VisTextField x;
	private VisTextField y;
	private VisTextField w;
	private VisTextField h;

	private MapEntry selected;

	public Edit() {
	    super("Edit");
	}

	public void show(MapEntry selected) {
	    this.selected = selected;
	    name.setText(selected.name);
	    x.setText("" + selected.x);
	    y.setText("" + selected.y);
	    w.setText("" + selected.w);
	    h.setText("" + selected.h);
	    show();
	}

	public void show() {
	    getStage().setKeyboardFocus(this);
	    getStage().setScrollFocus(this);
	    pack();
	    centerWindow();
	    setVisible(true);
	}

	public void cancel() {
	    name.setText("");
	    x.setText("");
	    y.setText("");
	    w.setText("");
	    h.setText("");
	    setVisible(false);
	    content.focus();
	}

	public void save() {
	    int x = Integer.parseInt(this.x.getText());
	    int y = Integer.parseInt(this.y.getText());
	    int w = Integer.parseInt(this.w.getText());
	    int h = Integer.parseInt(this.h.getText());
	    MapEntry e = selected; // TODO
	    if (e == null)
		layout.add(x, y, w, h, name.getText(), null);
	    else
		e.set(x, y, w, h, name.getText()); // update
	    cancel();
	}

	@Override
	public void end(SceneBuilder builder, Wrapper<?> root) {
	    name = builder.getActor("edit.name");
	    x = builder.getActor("edit.x");
	    y = builder.getActor("edit.y");
	    w = builder.getActor("edit.w");
	    h = builder.getActor("edit.h");
	    pack();
	    cancel();
	}
    }
}
