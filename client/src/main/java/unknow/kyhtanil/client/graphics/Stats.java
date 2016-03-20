package unknow.kyhtanil.client.graphics;

import unknow.kyhtanil.client.*;
import unknow.kyhtanil.common.*;

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
