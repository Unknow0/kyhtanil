package unknow.kyhtanil.client.graphics;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.spinner.Spinner.SpinnerStyle;

/**
 * The stat value selector (allow to increment value if points is available)
 * 
 * @author unknow
 */
public class StatSelector extends VisTable {
	private ButtonRepeatTask buttonRepeatTask = new ButtonRepeatTask();
	private float buttonRepeatInitialTime = 0.4f;
	private float buttonRepeatTime = 0.08f;

	private int current;
	private int min = Integer.MIN_VALUE;
	private int max = Integer.MAX_VALUE;

	private Text text = new Text();

	/**
	 * create new StatSelector
	 */
	public StatSelector() {
		SpinnerStyle style = VisUI.getSkin().get("default", SpinnerStyle.class);
		Sizes sizes = VisUI.getSizes();

		text.setProgrammaticChangeEvents(false);
		text.setText("0");
		add(text).fillX().expandX();

		VisTable buttonsTable = new VisTable();
		VisImageButton upButton = new VisImageButton(style.up);
		VisImageButton downButton = new VisImageButton(style.down);

		buttonsTable.add(upButton).height(sizes.spinnerButtonHeight).row();
		buttonsTable.add(downButton).height(sizes.spinnerButtonHeight);
		add(buttonsTable);

		upButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				increment();
				event.cancel();
			}
		});

		downButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				decrement();
				event.cancel();
			}
		});

		upButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (buttonRepeatTask.isScheduled() == false) {
					buttonRepeatTask.increment = true;
					buttonRepeatTask.cancel();
					Timer.schedule(buttonRepeatTask, buttonRepeatInitialTime, buttonRepeatTime);
					event.cancel();
				}

				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				buttonRepeatTask.cancel();
			}
		});

		downButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (buttonRepeatTask.isScheduled() == false) {
					buttonRepeatTask.increment = false;
					buttonRepeatTask.cancel();
					Timer.schedule(buttonRepeatTask, buttonRepeatInitialTime, buttonRepeatTime);
					event.cancel();
				}

				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				buttonRepeatTask.cancel();
			}
		});
	}

	/**
	 * increment the value
	 */
	public void increment() {
		setValue(current + 1);
	}

	/**
	 * decrement the vlaue
	 */
	public void decrement() {
		setValue(current - 1);
	}

	/**
	 * set the value (truncate to max if &gt; max, and to min if &lt; min)
	 * 
	 * @param v the new value
	 */
	public void setValue(int v) {
		int old = current;
		current = v;
		if (current < min)
			current = min;
		if (current > max)
			current = max;

		if (current != old) {
			text.setText(Integer.toString(current));
			fire(new Event(old, current));
		}
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return current;
	}

	/**
	 * @param max the new max
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * @param min the new min
	 */
	public void setMin(int min) {
		this.min = min;
	}

	private class ButtonRepeatTask extends Task {
		boolean increment;

		@Override
		public void run() {
			if (increment) {
				increment();
			} else {
				decrement();
			}
		}
	}

	/**
	 * the Event fired when the value change
	 * 
	 * @author unknow
	 */
	public static class Event extends ChangeEvent {
		/** old value */
		public int old;
		/** the new value */
		public int current;

		/**
		 * create new Event
		 * 
		 * @param old
		 * @param current
		 */
		public Event(int old, int current) {
			this.old = old;
			this.current = current;
		}
	}

	private static class Text extends VisTextField {
		@Override
		protected void initialize() {
			super.initialize();
			clearListeners();
		}
	}
}