package unknow.kyhtanil.client.graphics;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.common.*;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.tabbedpane.*;

public class Stats extends VisWindow
	{
	private BodyStat body;
	private Cell<?> container;

	public Stats()
		{
		super("Char stats");

		body=new BodyStat();

		TabbedPane tabbedPane=new TabbedPane();
		tabbedPane.add(body);
		add(tabbedPane.getTable()).expandX().fillX();
		row();
		container=add(body.getContentTable()).expand().fill();
		this.debug();

		tabbedPane.addListener(new TabbedPaneAdapter()
			{
				@Override
				public void switchedTab(Tab tab)
					{
					System.out.println("switch");
					container.setActor(tab.getContentTable());
					}
			});

		setSize(300, 300);
		setMovable(true);
		setVisible(true);
		}

	public void update()
		{
		body.update();
		}

	private static class BodyStat extends Tab
		{
		private VisLabel hp=new VisLabel("0/0");
		private VisLabel mp=new VisLabel("0/0");
		private VisLabel constitution=new VisLabel("0");
		private VisLabel strength=new VisLabel("0");
		private VisLabel concentration=new VisLabel("0");
		private VisLabel intelligence=new VisLabel("0");
		private VisLabel dexterity=new VisLabel("0");

		private VisTable content;

		public BodyStat()
			{
			super(false, false);

			content=new VisTable();
			content.add(new VisLabel("Hp"));
			content.add(hp);
			content.row();
			content.add(new VisLabel("mp"));
			content.add(mp);
			content.row();
			content.add(new VisLabel("constitution"));
			content.add(constitution);
			content.row();
			content.add(new VisLabel("strength"));
			content.add(strength);
			content.row();
			content.add(new VisLabel("concentration"));
			content.add(concentration);
			content.row();
			content.add(new VisLabel("intelligence"));
			content.add(intelligence);
			content.row();
			content.add(new VisLabel("dexterity"));
			content.add(dexterity);
			content.row();
			}

		@Override
		public Table getContentTable()
			{
			return content;
			}

		@Override
		public String getTabTitle()
			{
			return "Body";
			}

		public void update()
			{
			PjInfo pj=State.pj;

			hp.setText(String.format("%d / %d", pj.hp, pj.total.maxHp));
			mp.setText(String.format("%d / %d", pj.mp, pj.total.maxMp));

			constitution.setText(String.format("%d", pj.total.constitution));
			strength.setText(String.format("%d", pj.total.strength));
			concentration.setText(String.format("%d", pj.total.concentration));
			intelligence.setText(String.format("%d", pj.total.intelligence));
			dexterity.setText(String.format("%d", pj.total.dexterity));
			}
		}
	}
