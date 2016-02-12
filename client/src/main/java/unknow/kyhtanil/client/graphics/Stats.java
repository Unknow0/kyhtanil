package unknow.kyhtanil.client.graphics;

import com.kotcrab.vis.ui.widget.*;

public class Stats extends VisWindow
	{
	private VisLabel hp=new VisLabel("0/0");
	private VisLabel mp=new VisLabel("0/0");
	private VisLabel constitution=new VisLabel("0");
	private VisLabel strength=new VisLabel("0");
	private VisLabel concentration=new VisLabel("0");
	private VisLabel intelligence=new VisLabel("0");
	private VisLabel dexterity=new VisLabel("0");

	public Stats()
		{
		super("Char stats");

		add(new VisLabel("Hp"));
		add(hp);
		row();
		add(new VisLabel("mp"));
		add(mp);
		row();
		add(new VisLabel("constitution"));
		add(constitution);
		row();
		add(new VisLabel("strength"));
		add(strength);
		row();
		add(new VisLabel("concentration"));
		add(concentration);
		row();
		add(new VisLabel("intelligence"));
		add(intelligence);
		row();
		add(new VisLabel("dexterity"));
		add(dexterity);
		row();

		pack();

		setVisible(true);
		}

	public void update()
		{
		}
	}
