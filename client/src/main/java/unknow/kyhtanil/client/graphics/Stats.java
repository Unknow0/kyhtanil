package unknow.kyhtanil.client.graphics;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

public class Stats extends VisTable
	{
	private TabbedPane tabbedPane=new TabbedPane();
	private BodyStat body;
	private Cell<?> container;

	public Stats()
		{
		body=new BodyStat();

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
				container.setActor(tab.getContentTable());
				}
			});

		setSize(300, 300);
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
//			CalculatedComp stat=State.stat;
//
//			hp.setText(String.format("%d / %d", stat.hp, stat.maxHp));
//			mp.setText(String.format("%d / %d", stat.mp, stat.maxMp));
//
//			constitution.setText(String.format("%d", stat.constitution));
//			strength.setText(String.format("%d", stat.strength));
//			concentration.setText(String.format("%d", stat.concentration));
//			intelligence.setText(String.format("%d", stat.intelligence));
//			dexterity.setText(String.format("%d", stat.dexterity));
			}
		}
	}
